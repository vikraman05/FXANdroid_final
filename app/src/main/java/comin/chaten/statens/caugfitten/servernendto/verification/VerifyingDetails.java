package comin.chaten.statens.caugfitten.servernendto.verification;

import org.fejoa.library.Client;
import org.fejoa.library.crypto.CryptoException;
import org.fejoa.library.support.LooperThread;
import org.fejoa.library.support.Task;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import comin.chaten.statens.caugfitten.servernendto.changesdw.Userdetails;
import comin.chaten.statens.caugfitten.servernendto.changesdw.JustNote;
import comin.chaten.statens.caugfitten.servernendto.changesdw.AddDetails;
import comin.chaten.statens.caugfitten.errorfinder.Writer;

public class VerifyingDetails {

    private static final int DEFAULT_PORT = 8180;
    private static final int LOOPER_THREAD_CAPACITY = 100;
    private static final String TEST_DIR = "jettyTest";
    private static final String SERVER_URL = "http://10.0.2.2:" + DEFAULT_PORT + "/";

    private static VerifyingDetails instance;

    private Executor contextExecutor;
    private Executor startScheduler;
    private Executor observableScheduler;

    private Client client;
    private String username;

    private VerifyingDetails(Executor contextExecutor, Executor startScheduler, Executor observableScheduler) {
        this.contextExecutor = contextExecutor;
        this.startScheduler = startScheduler;
        this.observableScheduler = observableScheduler;
    }

    public static VerifyingDetails getInstance() {
        if (instance != null) return instance;

        LooperThread looperThread = new LooperThread(LOOPER_THREAD_CAPACITY);
        Executor contextExecutor = new Task.LooperThreadScheduler(looperThread);
        Executor startScheduler = new Task.CurrentThreadScheduler();
        Executor observableScheduler = new Task.LooperThreadScheduler(looperThread);

        instance = new VerifyingDetails(contextExecutor, startScheduler, observableScheduler);
        return instance;
    }

    public void login(String username, String password, LoginCallback loginCallback, File dir) {
        if (loginCallback == null) {
            return;
        }
        try {
            File userFile = new File(dir + username);
            if (!userFile.exists()) {
                loginCallback.onFailure(username + ": user doesn't exist");
                return;
            } else
                Writer.debug("user file exists, now logging in");
            client = Client.open(userFile, observableScheduler, password);
            client.getConnectionManager().setStartScheduler(new Task.NewThreadScheduler());
            client.getConnectionManager().setObserverScheduler(observableScheduler);
            Writer.debug("LOGIN SUCCESS");
            loginCallback.onSuccess();
            this.username = username;
        } catch (IOException | CryptoException | JSONException e) {
            e.printStackTrace();
            loginCallback.onFailure(e.toString());
        }
    }

    public void signUp(String username, String password, SignUpCallback signUpCallback, File dir) {
        if (signUpCallback == null) return;

        final Userdetails userdetails = new Userdetails(username, SERVER_URL);
        try {
            File userFile = new File(dir + username);
            userFile.mkdirs();
            client = Client.create(userFile, contextExecutor, userdetails.name,
                    userdetails.server, password);
            client.commit();
            client.getConnectionManager().setStartScheduler(startScheduler);
            client.getConnectionManager().setObserverScheduler(observableScheduler);
            JustNote justNote = new JustNote(new AddDetails(client, userdetails));
            client.createAccount(userdetails.remote, password, justNote);
            Writer.info("Sign up success");
            signUpCallback.onSuccess();
            this.username = username;
        } catch (IOException | CryptoException | JSONException e) {
            e.printStackTrace();
            Writer.error(e.toString());
            Writer.error(e.getMessage());
            signUpCallback.onFailure("Internal error! Please try again later.");
            client = null;
        }


    }

    public Client getClient() {
        return client;
    }

    public String getUsername() {
        if (username == null) return "someUsername";
        return username;
    }

    public void logout() {
        client = null;
        username = null;
    }

    public interface LoginCallback {
        void onSuccess();

        void onFailure(String errMessage);
    }

    public interface SignUpCallback {
        void onSuccess();

        void onFailure(String errMessage);
    }


}
