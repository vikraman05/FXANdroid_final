package comin.chaten.statens.caugfitten.straighttoend.its.fragments.migration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;

/**
 * Created by Einolin on 4/05/2017.
 */

public class MigrationConfirmFragment extends DialogFragment {

    private ProgressDialog workingDialog;

    public static MigrationConfirmFragment newInstance() {
        MigrationConfirmFragment fragment = new MigrationConfirmFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View npView = inflater.inflate(R.layout.migration_confirmation, null);
        ButterKnife.bind(this, npView);

        // Using builder class for convenient dialog construction
        // Sets up buttons, titles, UI
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Migration").setView(npView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Log.d("Migration", "it worked!");

                workingDialog = ProgressDialog.show(getContext(), "", "Migrating...", true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (workingDialog != null) {
                            workingDialog.dismiss();
                            workingDialog = null;
                        }
                    }
                }, 3000);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }
}
