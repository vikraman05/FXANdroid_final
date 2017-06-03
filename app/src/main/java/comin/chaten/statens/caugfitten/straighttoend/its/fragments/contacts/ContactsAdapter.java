package comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.details.Details;
import comin.chaten.statens.caugfitten.servernendto.message.FindAlert;
import comin.chaten.statens.caugfitten.servernendto.message.NoteDetails;
import comin.chaten.statens.caugfitten.servernendto.details.ManageDetails;
import comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger.ChatActivity;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by User on 27/04/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Details> detailses;
    private final int typeLetter = 0;
    private final int typeContact = 1;
    private Vibrator vibrator;
    private Context context;

    public ContactsAdapter(List<Details> detailses) {
        this.detailses = detailses;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if(detailses.get(position).getIsContact()) {
            viewType = typeContact;
        } else {
            viewType = typeLetter;
        }
        return viewType;
    }


    //Inflating layout from xml and returning the holder
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder = null;

        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        switch (viewType){
            case typeLetter:
                //Inflate the custom layout
                View alphabetView = inflater.inflate(R.layout.item_alphabet, parent, false);
                //Return a new holder instance
                viewHolder = new ViewHolder(alphabetView);
                break;
            case typeContact:
                //Inflate the custom layout
                View contactView = inflater.inflate(R.layout.item_contact, parent, false);
                //Return a new holder instance
                viewHolder = new ViewHolder(contactView);
                break;
        }

        return viewHolder;
    }

    //Populating data into the item through holder
    @Override
    public void onBindViewHolder(final ContactsAdapter.ViewHolder viewHolder, final int position) {
        //Get data model based on position
        final Details details = this.detailses.get(position);

        switch (viewHolder.getItemViewType()){
            case typeLetter:
                //Set item views based on views and data model
                TextView alphabetTextView = viewHolder.alphabetTextView;
                alphabetTextView.setText(details.getUserName());
                break;
            case typeContact:
                //Set item views based on views and data model
                TextView userTextView = viewHolder.uNameTextView;
                userTextView.setText(details.getUserName());
                userTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#e8e8e8"));
                        new CountDownTimer(100, 5) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#FAFAFA"));
                            }
                        }.start();
                    }
                });
                userTextView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        vibrator.vibrate(50);

                        viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#e8e8e8"));

                        AlertDialog builder = new AlertDialog.Builder(context)
                                .setTitle("Delete " + details.getUserName() + "?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeAt(position, details.getUserName());
                                        viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                        // do nothing
                                    }
                                })
                                .create();
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                viewHolder.ll_contact.setBackgroundColor(Color.parseColor("#FAFAFA"));
                            }
                        });
                        builder.show();

                        return true;
                    }
                });

                Button button = viewHolder.msgButton;
                button.setText("Message");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent().setClass(v.getContext(), ChatActivity.class);;
                        intent.putExtra(ChatActivity.FRIEND_NAME, details.getUserName());

                        String lastMessage = "";
                        if (containsMessage(FindAlert.getInstance().getDialogMessagesList(), details.getUserName())) {
                            int msgIndex = getIndexBynameDialog(details.getUserName());
                            if (msgIndex != -1) {
                                lastMessage = FindAlert.getInstance().getDialogMessagesList().get(msgIndex).getLastMsg();
                            }
                        }

                        intent.putExtra(ChatActivity.LAST_MESSAGE, lastMessage);

                        if(!containsMessage(FindAlert.getInstance().getDialogMessagesList(), details.getUserName())){
                            FindAlert.getInstance().getDialogMessagesList().add(new NoteDetails(details.getUserName(), ""));
                        }

                        v.getContext().startActivity(intent);
                    }
                });

                break;
        }
    }

    private boolean containsMessage(ArrayList<NoteDetails> noteDetailses, String contactName) {
        for (NoteDetails noteDetails : noteDetailses) {
            if (noteDetails.getContactName().equals(contactName)) {
                return true;
            }
        }
        return false;
    }

    //Helper method to find position in array list of current message
    private int getIndexBynameDialog(String contactName)
    {
        for(NoteDetails noteDetails : FindAlert.getInstance().getDialogMessagesList())
        {
            if(noteDetails.getContactName().equals(contactName))
                return FindAlert.getInstance().getDialogMessagesList().indexOf(noteDetails);
        }
        return -1;
    }

    //Helper method to find position in array list of current contact
    private int getIndexBynameContact(String contactName)
    {
        for(Details details : ManageDetails.getInstance().getContactsList())
        {
            if(details.getUserName().equals(contactName))
                return ManageDetails.getInstance().getContactsList().indexOf(details);
        }
        return -1;
    }


    //Returns total count of items in the list
    @Override
    public int getItemCount() {
        return this.detailses.size();
    }

    public void removeAt(int position, String username) {

        if (username == null || username.length() < 1) return;

        String firstLetter = String.valueOf(username.charAt(0));
        String prevContact = detailses.get(position - 1).getUserName();

        if (prevContact.equals(firstLetter)) {
            if (position == detailses.size() - 1) {
                detailses.remove(position - 1);
            } else if (position < detailses.size() - 1) {
                String nextContactFirstLetter = String.valueOf(detailses.get(position + 1).getUserName().charAt(0));
                if (!firstLetter.equals(nextContactFirstLetter)) {
                    detailses.remove(position - 1);
                }
            }
        }

        removeContactBasedOnName(username);
        notifyDataSetChanged();
    }

    private void removeContactBasedOnName(String username) {
        if (username.length() < 2) return;
        int index = -1;
        for (int i = 0; i < detailses.size(); i++) {
            if (detailses.get(i).getUserName().equals(username)) {
                index = i;
                break;
            }
        }

        if (index != -1) detailses.remove(index);

        int indexInSingleton = getIndexBynameContact(username);
        if (indexInSingleton != -1) {
            ManageDetails.getInstance().getContactsList().remove(indexInSingleton);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView alphabetTextView;
        public TextView uNameTextView;
        public Button msgButton;
        public LinearLayout ll_contact;

        public ViewHolder(View itemView) {
            super(itemView);

            alphabetTextView = (TextView) itemView.findViewById(R.id.contact_alphabet);
            uNameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            msgButton = (Button) itemView.findViewById(R.id.message_button);
            ll_contact = (LinearLayout) itemView.findViewById(R.id.ll_contact);
        }
    }
}
