package comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.details.Details;
import comin.chaten.statens.caugfitten.servernendto.details.ManageDetails;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;

public class MessageContactActivity extends BaseActivity {

    private ArrayList<Details> detailses;
    private MessageContactAdapter messageContactAdapter;

    @BindView(R.id.contact_search)
    protected EditText contactSearchEditText;

    @BindView(R.id.contacts_list_search)
    protected ListView contactSearchListView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_contact_list);

        ButterKnife.bind(this);

        detailses = ManageDetails.getInstance().getContactsList();

        messageContactAdapter = new MessageContactAdapter(this, MessageContactActivity.this, detailses);
        contactSearchListView.setAdapter(messageContactAdapter);

        // Add Text Change Listener to EditText
        contactSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                messageContactAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
