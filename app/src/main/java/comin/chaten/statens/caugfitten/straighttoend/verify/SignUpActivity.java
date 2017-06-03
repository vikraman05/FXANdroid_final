package comin.chaten.statens.caugfitten.straighttoend.verify;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.verification.VerifyingDetails;
import comin.chaten.statens.caugfitten.servernendto.verification.CheckCredentials;
import comin.chaten.statens.caugfitten.errorfinder.Writer;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;
import comin.chaten.statens.caugfitten.straighttoend.its.MainActivity;

public class SignUpActivity extends BaseActivity implements VerifyingDetails.SignUpCallback {

    @BindView(R.id.su_usernameEditText)
    protected EditText signUpUsernameEditText;

    @BindView(R.id.su_passwordEditText)
    protected EditText signUpPasswordEditText;

    @BindView(R.id.su_confirmpwEditText)
    protected EditText signUpConfirmPassworkEditText;

    SQLiteDatabase sqld = null;

    @BindView(R.id.su_pwerrorTextView)
    protected TextView signUpPasswordErrorTextView;
    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String signUpPassword = signUpPasswordEditText.getText().toString();
            String signUpConfirmPassword = signUpConfirmPassworkEditText.getText().toString();

            if (signUpConfirmPassword.length() == 0) {
                signUpPasswordErrorTextView.setVisibility(View.GONE);
            } else {
                if (signUpConfirmPassword.equals(signUpPassword)) {
                    signUpPasswordErrorTextView.setVisibility(View.GONE);
                } else {
                    signUpPasswordErrorTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    protected Button signUpButton;
    @BindView(R.id.signup_progress_bar)
    protected ProgressBar progressBar;
    //Sign up logic in the authentication manager
    private VerifyingDetails verifyingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        sqld = openOrCreateDatabase("Chaters",MODE_PRIVATE , null);

        sqld.execSQL("create table if not exists signupers(name varchar,  password varchar)");

        //Hide error label
        signUpPasswordErrorTextView.setVisibility(View.GONE);
        //Add text watcher to confirm password edit text
        signUpConfirmPassworkEditText.addTextChangedListener(passwordWatcher);
        signUpPasswordEditText.addTextChangedListener(passwordWatcher);

        signUpButton = (Button) findViewById(R.id.su_button);
        progressBar = (ProgressBar) findViewById(R.id.signup_progress_bar);

        progressBar.setVisibility(View.INVISIBLE);
        verifyingDetails = VerifyingDetails.getInstance();
    }

    public void registerButtonClicked(View view) {
        String signUpUsername = signUpUsernameEditText.getText().toString();
        String signUpPassword = signUpPasswordEditText.getText().toString();
        String signUpConfirmPassword = signUpConfirmPassworkEditText.getText().toString();

        // For debugging only
//        if (signUpUsername.isEmpty()) signUpUsername = "someUsername";
//        if (signUpConfirmPassword.isEmpty()) signUpConfirmPassword = "somePassword";
//        if (signUpPassword.isEmpty()) signUpPassword = "somePassword";

        if(CheckCredentials.validPassword(signUpPassword) && signUpUsername.length() > 0){
            if(signUpPassword.equals(signUpConfirmPassword)){
                signUpButton.setEnabled(false);
                signUpUsernameEditText.setEnabled(false);
                signUpPasswordEditText.setEnabled(false);
                signUpConfirmPassworkEditText.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                sqld.execSQL("insert into signupers values('"+signUpUsername+"' , '"+signUpPassword+"' );");
                sqld.close();

                showSnackbar("This may take upto 5 minutes...", Snackbar.LENGTH_LONG);
                (new Thread(new SignUpRunnable(signUpUsername, signUpPassword))).start();
            } else {
                showSnackbar("Error: passwords do not match", Snackbar.LENGTH_LONG);
            }
        } else if(signUpUsername.length() == 0) {
            showSnackbar("Error: No username entered", Snackbar.LENGTH_LONG);
        } else {
            showSnackbar("Error: password must have 5 or more characters", Snackbar.LENGTH_LONG);
        }
    }

    public void existingAccountClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onSuccess() {
        Writer.info("OnSuccess called");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSnackbar("Sign up successful.", Snackbar.LENGTH_LONG);

                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onFailure(final String errMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                signUpButton.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                showSnackbar(errMessage, Snackbar.LENGTH_LONG);
            }
        });
    }

    private class SignUpRunnable implements Runnable {

        private final String signUpUsername;
        private final String signUpPassword;

        public SignUpRunnable(String signUpUsername, String signUpPassword) {
            this.signUpUsername = signUpUsername;
            this.signUpPassword = signUpPassword;
        }

        @Override
        public void run() {
            verifyingDetails.signUp(signUpUsername, signUpPassword, SignUpActivity.this, getFilesDir());
        }
    }
}
