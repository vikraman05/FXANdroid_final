package comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.details.Details;
import comin.chaten.statens.caugfitten.servernendto.message.FindAlert;
import comin.chaten.statens.caugfitten.servernendto.message.NoteDetails;

public class MessageContactAdapter extends BaseAdapter implements Filterable {

    private MessageContactActivity messageContactActivity;
    private ArrayList<Details> originalDetailses;
    private ArrayList<Details> displayedDetailses; //Contacts to be displayed
    LayoutInflater inflater;

    public MessageContactAdapter(MessageContactActivity messageContactActivity, Context context, ArrayList<Details> detailses) {
        this.messageContactActivity = messageContactActivity;
        this.originalDetailses = detailses;
        this.displayedDetailses = detailses;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return displayedDetailses.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView contactName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_contact_search, null);
            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
            holder.contactName = (TextView)convertView.findViewById(R.id.contact_search_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contactName.setText(displayedDetailses.get(position).getUserName());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageContactActivity, ChatActivity.class);
                intent.putExtra(ChatActivity.FRIEND_NAME, displayedDetailses.get(position).getUserName());

                String lastMessage = "";
                if (containsMessage(FindAlert.getInstance().getDialogMessagesList(), displayedDetailses.get(position).getUserName())) {
                    int msgIndex = getIndexByname(displayedDetailses.get(position).getUserName());
                    lastMessage = FindAlert.getInstance().getDialogMessagesList().get(msgIndex).getLastMsg();
                }

                intent.putExtra(ChatActivity.LAST_MESSAGE, lastMessage);

                if(!containsMessage(FindAlert.getInstance().getDialogMessagesList(), displayedDetailses.get(position).getUserName())){
                    FindAlert.getInstance().getDialogMessagesList().add(new NoteDetails(displayedDetailses.get(position).getUserName(), ""));
                }

                messageContactActivity.startActivity(intent);
                Log.d("MSGCONTACTADAPTER", displayedDetailses.get(position).getUserName());
            }
        });

        return convertView;
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
    private int getIndexByname(String contactName)
    {
        for(NoteDetails noteDetails : FindAlert.getInstance().getDialogMessagesList())
        {
            if(noteDetails.getContactName().equals(contactName))
                return FindAlert.getInstance().getDialogMessagesList().indexOf(noteDetails);
        }
        return -1;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Details> FilterArrayList = new ArrayList<Details>();

                if(originalDetailses == null) {
                    //saves original data in originalDetailses
                    originalDetailses = new ArrayList<Details>(displayedDetailses);
                }


                 //If char sequence that is received is null returns the originalDetailses values
                 // else does the Filtering and returns FilterArrayList
                if (constraint == null || constraint.length() == 0) {
                    //Original result to return
                    results.count = originalDetailses.size();
                    results.values = originalDetailses;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalDetailses.size(); i++) {
                        String data = originalDetailses.get(i).getUserName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilterArrayList.add(new Details(originalDetailses.get(i).getUserName(), originalDetailses.get(i).getIsContact()));
                        }
                    }

                    results.count = FilterArrayList.size();
                    results.values = FilterArrayList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                displayedDetailses = (ArrayList<Details>)results.values; //has the filtered values
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
