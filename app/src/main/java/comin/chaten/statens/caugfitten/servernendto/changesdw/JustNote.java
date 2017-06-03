package comin.chaten.statens.caugfitten.servernendto.changesdw;

import org.fejoa.library.remote.Errors;
import org.fejoa.library.remote.RemoteJob;
import org.fejoa.library.support.Task;

import comin.chaten.statens.caugfitten.errorfinder.Writer;


public class JustNote<T extends RemoteJob.Result> implements Task.IObserver<Void, T> {
    final private Runnable onSuccess;

    public JustNote(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @Override
    public void onProgress(Void aVoid) {
        // do nothing
    }

    @Override
    public void onResult(T result) {
        if (result.status != Errors.DONE) {
            Writer.error("Internal error!");
            return;
        }

        Writer.debug("onNext: " + result.message);
        onSuccess.run();
    }

    @Override
    public void onException(Exception exception) {
        Writer.error(exception.getMessage());
    }
}
