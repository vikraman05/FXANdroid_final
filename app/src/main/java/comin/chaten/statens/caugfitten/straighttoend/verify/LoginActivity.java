package comin.chaten.statens.caugfitten.straighttoend.verify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.verification.VerifyingDetails;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;
import comin.chaten.statens.caugfitten.straighttoend.its.MainActivity;

public class LoginActivity extends BaseActivity implements VerifyingDetails.LoginCallback {

    @BindView(R.id.login_username)
    protected EditText loginUsernameEditText;

    @BindView(R.id.login_password)
    protected EditText loginPasswordEditText;

    @BindView(R.id.login_progress_bar)
    protected ProgressBar progressBar;

    @BindView(R.id.login_button)
    protected Button loginButton;

    SQLiteDatabase sqld;

    private VerifyingDetails verifyingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        sqld = openOrCreateDatabase("Chaters",MODE_PRIVATE , null);

        sqld.execSQL("create table if not exists signupers(name varchar,  password varchar)");

        Intent i = getIntent();

        String stk = i.getStringExtra("nameofuser");

        loginUsernameEditText.setText(stk);

        verifyingDetails = VerifyingDetails.getInstance();
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void loginButtonClicked(View view) {
        String loginUsername = loginUsernameEditText.getText().toString();
        String loginPassword = loginPasswordEditText.getText().toString();

//        // For debugging only
//        if (loginUsername.isEmpty()) loginUsername = "someUsername";
//        if (loginPassword.isEmpty()) loginPassword = "somePassword";
//
//        loginButton.setEnabled(false);
//        progressBar.setVisibility(View.VISIBLE);
//        (new Thread(new LoginRunnable(loginUsername, loginPassword))).start();

        try
        {

            Cursor c = sqld.rawQuery("select * from signupers where name = '"+loginUsername+"' and password = '"+loginPassword+"'", null);

            c.moveToFirst();

            if(c.getCount() > 0)
            {
                Intent i = new Intent(LoginActivity.this , MainActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
            }
        }
        catch(NullPointerException e)
        {
            Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_LONG).show();
        }




    }

    public void forgottenPassword(View view) {
//        showSnackbar("Feature not yet implemented...", Snackbar.LENGTH_LONG);
    }

    public void createAccountClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    @Override
    public void onSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                showSnackbar("Login successful.", Snackbar.LENGTH_LONG);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onFailure(final String errMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginButton.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
//                showSnackbar(errMessage, Snackbar.LENGTH_LONG);
            }
        });

    }

    public void skipButtonClicked(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class LoginRunnable implements Runnable {

        private final String username;
        private final String password;

        private LoginRunnable(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void run() {
            verifyingDetails.login(username, password, LoginActivity.this, getFilesDir());
        }
    }

}
