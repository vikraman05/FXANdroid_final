package comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.details.Details;
import comin.chaten.statens.caugfitten.servernendto.details.ManageDetails;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;

public class ContactsFragment extends Fragment {

    private ArrayList<Details> detailses;
    private ArrayList<Details> contactsWithAlphabet = new ArrayList<>();
    private FloatingActionButton fab;
    private ContactsAdapter contactsAdapter;

    public ContactsFragment() {
        // required empty constructor
    }

    private View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_contacts, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.contacts_list);

        //Initialise fake detailses
        detailses = ManageDetails.getInstance().getContactsList();
        sortContacts(detailses);
        addAlphabet(detailses);

        //Create adapter passing in dummy user data
        contactsAdapter = new ContactsAdapter(contactsWithAlphabet);
        recyclerView.setAdapter(contactsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        //Find fab
        fab = (FloatingActionButton)fragmentView.findViewById(R.id.contacts_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
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

    //Helper methods
    //Sorts detailses in alphabetical order
    private void sortContacts(ArrayList<Details> detailses){
        Collections.sort(detailses, new Comparator<Details>() {
            @Override
            public int compare(Details details1, Details details2) {
                return details1.getUserName().compareTo(details2.getUserName());
            }
        });
    }

    private void addAlphabet(ArrayList<Details> detailses) {
        this.contactsWithAlphabet.clear();
        //Add first letter of first contact to list
        Details firstAlphabet = new Details();
        firstAlphabet.setUserName(String.valueOf(detailses.get(0).getUserName().charAt(0)));
        firstAlphabet.setIsContact(false);
        this.contactsWithAlphabet.add(firstAlphabet);

        for(int i = 0; i < detailses.size() - 1; i++) {
            char name1 = detailses.get(i).getUserName().charAt(0);
            char name2 = detailses.get(i + 1).getUserName().charAt(0);

            if (name1 != name2) {
                this.contactsWithAlphabet.add(detailses.get(i));
                Details detailsAlphabet = new Details();
                detailsAlphabet.setUserName(String.valueOf(name2));
                detailsAlphabet.setIsContact(false);
                this.contactsWithAlphabet.add(detailsAlphabet);
            } else {
                this.contactsWithAlphabet.add(detailses.get(i));
            }
        }

        this.contactsWithAlphabet.add(detailses.get(detailses.size() - 1));
    }

    // Show number picker pop up
    public void showDialog() {
        AddingFragmentDetails dialog = AddingFragmentDetails.newInstance();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "fragmentDialog");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddingFragmentDetails.ADD_CONTACT_REQUEST_CODE) {
            String username = data.getStringExtra("Username");
            String firstLetter = String.valueOf(username.charAt(0));
            Log.i("DIALOGUE", "Got username=" + username);

            if (!containsUsername(contactsWithAlphabet, firstLetter)) {
                //Add letter if it does not exist
                Details details = new Details(firstLetter, false);
                contactsWithAlphabet.add(details);
            }

            if (!containsUsername(contactsWithAlphabet, username)) {
                //create new details object and add it to the array list
                Details details = new Details(username, true);
                ManageDetails.getInstance().getContactsList().add(details);
                contactsWithAlphabet.add(details);
                sortContacts(contactsWithAlphabet);
                contactsAdapter.notifyDataSetChanged();
            } else {
                ((BaseActivity) getActivity()).showSnackbar(username + " is already a contact", Snackbar.LENGTH_LONG);
            }
        }
    }

    public boolean containsUsername(ArrayList<Details> detailses, String username) {
        for (Details details : detailses) {
            if (details.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
