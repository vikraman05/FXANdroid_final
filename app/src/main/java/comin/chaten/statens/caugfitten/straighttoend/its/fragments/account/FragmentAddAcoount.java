package comin.chaten.statens.caugfitten.straighttoend.its.fragments.account;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.verification.VerifyingDetails;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;
import comin.chaten.statens.caugfitten.straighttoend.verify.LoginActivity;

public class FragmentAddAcoount extends PreferenceFragmentCompat {

    //Fields for changing username
    private EditText unEditText;
    private EditText unConfirmEditText;
    private View unView;
    private ProgressDialog unChangeDialog;
    private TextView unErrorTextView;
    private AlertDialog unDialog;

    //Watcher for username change
    private final TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newUsername = unEditText.getText().toString();
            String newUsernameConfirm = unConfirmEditText.getText().toString();

            if (newUsernameConfirm.length() == 0) {
                unErrorTextView.setVisibility(View.GONE);
            } else {
                if (newUsernameConfirm.equals(newUsername)) {
                    unErrorTextView.setVisibility(View.GONE);
                    unDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    unErrorTextView.setVisibility(View.VISIBLE);
                    unDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        }
    };

    //Fields for changing password
    private EditText oldPwEditText;
    private EditText newPwEditText;
    private EditText newPwConfirmEditText;
    private View pwView;
    private ProgressDialog pwChangeDialog;
    private TextView passwordErrorTextView;
    private AlertDialog pwAlertDialog;

    //Watcher for password change
    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newPassword = newPwEditText.getText().toString();
            String newPasswordConfirm = newPwConfirmEditText.getText().toString();

            if (newPasswordConfirm.length() == 0) {
                passwordErrorTextView.setVisibility(View.GONE);
            } else {
                if (newPasswordConfirm.equals(newPassword)) {
                    passwordErrorTextView.setVisibility(View.GONE);
                    pwAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    passwordErrorTextView.setVisibility(View.VISIBLE);
                    pwAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        }
    };

    public FragmentAddAcoount() {
        // required empty constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Loads preferences from XML resource
        setPreferencesFromResource(R.xml.preferences, null);

        Preference logOutButton = (Preference) getPreferenceManager().findPreference("exitlink");
        if (logOutButton != null) {
            logOutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    logoutClick();
                    return true;
                }
            });
        }

        Preference usernameButton = (Preference) getPreferenceManager().findPreference("pref_username");
        if (usernameButton != null) {
            usernameButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg1) {
                    changeUsername();
                    return true;
                }
            });
        }

        Preference pwButton = (Preference) getPreferenceManager().findPreference("pref_password");
        if (pwButton != null) {
            pwButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg) {
                    changePassword();
                    return true;
                }
            });
        }
    }

    public void changeUsername() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        unView = inflater.inflate(R.layout.change_username, null);

        //Retrieves elements
        unErrorTextView = (TextView) unView.findViewById(R.id.username_error_textView);
        unEditText = (EditText) unView.findViewById(R.id.new_username_editText);
        unConfirmEditText = (EditText) unView.findViewById(R.id.new_username_confirm_editText);

        //Hides error labels and adds text watchers
        unErrorTextView.setVisibility(View.GONE);
        unEditText.addTextChangedListener(usernameWatcher);
        unConfirmEditText.addTextChangedListener(usernameWatcher);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Username").setView(unView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String newUsername = unEditText.getText().toString();
                String confirmNewUsername = unConfirmEditText.getText().toString();

                if (newUsername.isEmpty() || confirmNewUsername.isEmpty()) {
                    ((BaseActivity) getActivity()).showSnackbar("Please fill in all the fields", Snackbar.LENGTH_LONG);
                } else if (!newUsername.equals(confirmNewUsername)) {
                        ((BaseActivity) getActivity()).showSnackbar("The usernames do not match", Snackbar.LENGTH_LONG);
                } else {
                    unChangeDialog = ProgressDialog.show(getContext(), "", "Changing username...", true);
                    Log.d("Change Username", "The previous username is " + VerifyingDetails.getInstance().getUsername());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (unChangeDialog != null) {
                                unChangeDialog.dismiss();
                                unChangeDialog = null;
                            }
                        }
                    }, 1000);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //User cancelled username change
            }
        });

        unDialog = builder.create();
        unDialog.show();
        unDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public void changePassword() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        pwView = inflater.inflate(R.layout.change_password, null);

        //Retrieves the elements
        passwordErrorTextView = (TextView) pwView.findViewById(R.id.new_pw_error_textView);
        oldPwEditText = (EditText) pwView.findViewById(R.id.old_pw_editText);
        newPwEditText = (EditText) pwView.findViewById(R.id.new_pw_editText);
        newPwConfirmEditText = (EditText) pwView.findViewById(R.id.new_pw_confirm_editText);

        //Hides error labels and adds text watchers
        passwordErrorTextView.setVisibility(View.GONE);
        newPwEditText.addTextChangedListener(passwordWatcher);
        newPwConfirmEditText.addTextChangedListener(passwordWatcher);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password").setView(pwView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String oldPw = oldPwEditText.getText().toString();
                String newPw = newPwEditText.getText().toString();
                String confirmNewPw = newPwConfirmEditText.getText().toString();

                if (oldPw.isEmpty() || newPw.isEmpty() || confirmNewPw.isEmpty()) {
                    ((BaseActivity) getActivity()).showSnackbar("Please fill in all the fields", Snackbar.LENGTH_LONG);
                } else if (!newPw.equals(confirmNewPw)) {
                    ((BaseActivity) getActivity()).showSnackbar("The new passwords do not match", Snackbar.LENGTH_LONG);
                } else {
                    pwChangeDialog = ProgressDialog.show(getContext(), "", "Changing password...", true);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pwChangeDialog != null) {
                                pwChangeDialog.dismiss();
                                pwChangeDialog = null;
                            }
                        }
                    }, 1000);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //User cancelled password change
            }
        });

        pwAlertDialog = builder.create();
        pwAlertDialog.show();
        pwAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    public void logoutClick() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View npView = inflater.inflate(R.layout.logout_confirmation, null);

        //Builer class to create alert dialog
        // Sets up buttons, titles, UI
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Log Out").setView(npView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                //Logs the user out
                VerifyingDetails.getInstance().logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //User cancelled before logging out
            }
        });

        builder.show();

    }
}
