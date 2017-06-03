package comin.chaten.statens.caugfitten.straighttoend.its.fragments.migration;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;

public class MigrationFragment extends Fragment {

    @BindView(R.id.m_confirmBtn)
    protected Button migrateBtn;

    @BindView(R.id.m_destEditText)
    protected EditText destEditText;

    @BindView(R.id.m_pwEditText)
    protected EditText pwEditText;

    public MigrationFragment() {
        // required empty constructor
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_migration, container, false);
        ButterKnife.bind(this, fragmentView);

        migrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destServer = destEditText.getText().toString();
                String userPw = pwEditText.getText().toString();

                if (destServer.isEmpty() || userPw.isEmpty()) {
                    ((BaseActivity) getActivity()).showSnackbar("Please enter a destination or password", Snackbar.LENGTH_LONG);
                } else {
                    showMigrationConfirmation();
                }
            }
        });

        return fragmentView;
    }

    public void showMigrationConfirmation() {
        MigrationConfirmFragment dialog = MigrationConfirmFragment.newInstance();
        dialog.show(getFragmentManager(), "fragmentDialog");
    }
}
