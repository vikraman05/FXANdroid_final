package comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import comin.chaten.statens.caugfitten.R;

/**
 * Created by User on 30/04/2017.
 */

public class AddingFragmentDetails extends android.support.v4.app.DialogFragment {

    public static int ADD_CONTACT_REQUEST_CODE = 0;
    private EditText acUsernameEditText;
    private AlertDialog addContactDialog;

    //Watcher for password change
    private final TextWatcher usernameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String username = acUsernameEditText.getText().toString();

            if (username.isEmpty()) {
                addContactDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            } else {
                addContactDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }
    };

    public static AddingFragmentDetails newInstance() {
        return new AddingFragmentDetails();
    }

    // Pop up window adding a contact
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View npView = inflater.inflate(R.layout.dialogue_add_contact, null);

        //Reference username edit text UI element
        acUsernameEditText = (EditText) npView.findViewById(R.id.ac_username_editText);
        acUsernameEditText.addTextChangedListener(usernameWatcher);

        // Use the Builder class for convenient dialog construction
        // Sets up buttons, titles, UI stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Details")
                .setView(npView)
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(ADD_CONTACT_REQUEST_CODE);
                    }

                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        addContactDialog = builder.create();
        addContactDialog.show();
        addContactDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        return addContactDialog;
    }

    private void sendResult(int requestCode) {
        Intent intent = new Intent();
        String username = acUsernameEditText.getText().toString();
        String upperUsername = username.substring(0, 1).toUpperCase() + username.substring(1);
        intent.putExtra("Username", upperUsername);
        getTargetFragment().onActivityResult(getTargetRequestCode(), requestCode, intent);
    }
}
