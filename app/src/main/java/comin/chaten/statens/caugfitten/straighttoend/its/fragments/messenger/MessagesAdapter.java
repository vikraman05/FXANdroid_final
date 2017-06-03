package comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.message.NoteDetails;

/**
 * Created by User on 4/05/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{

    private MessengerFragment messengerFragment;
    private List<NoteDetails> messages;

    public MessagesAdapter(MessengerFragment messengerFragment, List<NoteDetails> messages) {
        this.messengerFragment = messengerFragment;
        this.messages = messages;
    }

    //Inflating layout from xml and returning the holder
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View messageView = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(messageView);
    }

    //Populating data into the item through holder
    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder viewHolder, int position) {
        //Get data model based on position
        final NoteDetails message = this.messages.get(position);

        TextView contactNameView = viewHolder.contactName;
        contactNameView.setText(message.getContactName());

        TextView lastMsgView = viewHolder.lastMsg;
        lastMsgView.setText(message.getLastMsg());

        LinearLayout msgLinearLayout = viewHolder.linearLayout;
        msgLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messengerFragment.getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.FRIEND_NAME, message.getContactName());
                intent.putExtra(ChatActivity.LAST_MESSAGE, message.getLastMsg());
                messengerFragment.getActivity().startActivity(intent);
            }
        });
    }

    //Returns total count of items in the list
    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName;
        public TextView lastMsg;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            contactName = (TextView) itemView.findViewById(R.id.msg_contact_name);
            lastMsg = (TextView) itemView.findViewById(R.id.last_msg);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.msg_linear_layout);
        }
    }
}
