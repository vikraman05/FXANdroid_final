package comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.message.FindAlert;

public class MessengerFragment extends Fragment {

    private FloatingActionButton fab;
    //private ArrayList<NoteDetails> messages;
    private MessagesAdapter messagesAdapter;

    public MessengerFragment() {
        // required empty constructor
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_messenger, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.messages_list);

        //Initialise fake messages
        FindAlert.getInstance().getDialogMessagesList();

        //Create adapter passing in dummy message data
        messagesAdapter = new MessagesAdapter(this, FindAlert.getInstance().getDialogMessagesList());
        recyclerView.setAdapter(messagesAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);


        fab = (FloatingActionButton)fragmentView.findViewById(R.id.messages_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageContactActivity.class);
                startActivity(intent);
            }
        });

        //Hide fab when scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if ((dy > 0 ||dy < 0) && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return fragmentView;
    }

}
