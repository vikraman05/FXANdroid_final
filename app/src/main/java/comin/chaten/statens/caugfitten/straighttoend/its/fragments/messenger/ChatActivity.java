package comin.chaten.statens.caugfitten.straighttoend.its.fragments.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.servernendto.message.NoteDetails;
import comin.chaten.statens.caugfitten.servernendto.verification.VerifyingDetails;
import comin.chaten.statens.caugfitten.servernendto.message.Teller;
import comin.chaten.statens.caugfitten.servernendto.message.TalkStraight;
import comin.chaten.statens.caugfitten.servernendto.message.FindAlert;
import comin.chaten.statens.caugfitten.straighttoend.BaseActivity;
import comin.chaten.statens.caugfitten.straighttoend.its.MainActivity;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.messagesList)
    protected MessagesList messagesList;

    @BindView(R.id.input)
    protected MessageInput messageInput;

    protected Teller user;

    protected Teller bot;

    protected int messageIdCount = 1;

    protected MessagesListAdapter<TalkStraight> adapter;

    protected String lastBotMessage;

    @Override
    public void onBackPressed() {
        int msgIndex = getIndexByname(bot.getName());
        FindAlert.getInstance().getDialogMessagesList().get(msgIndex).setLastMsg(this.lastBotMessage);
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        adapter = new MessagesListAdapter<>(VerifyingDetails.getInstance().getUsername(), null);
        messagesList.setAdapter(adapter);

        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                showSnackbar("Feature not yet implemented.", Snackbar.LENGTH_SHORT);
            }
        });

        user = new Teller();
        user.setAvatar(null);
        user.setId(VerifyingDetails.getInstance().getUsername());
        user.setName(VerifyingDetails.getInstance().getUsername());

        bot = new Teller();
        bot.setAvatar(null);
        bot.setId(getIntent().getExtras().getString(FRIEND_NAME));
        bot.setName(getIntent().getExtras().getString(FRIEND_NAME));

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                TalkStraight userTalkStraight = buildMessage(input.toString(), user);
                adapter.addToStart(userTalkStraight, true);
                enqueueBotMessageReverse(input);
                return true;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getIntent().getExtras().getString(FRIEND_NAME));
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        enqueueBotMessageForward(getIntent().getExtras().getString(LAST_MESSAGE));
    }

    private void enqueueBotMessageForward(CharSequence input) {
        if (input == null) return;
        enqueueBotMessage(input.toString());
    }

    private void enqueueBotMessageReverse(CharSequence input) {
        if (input == null) return;
        String text = new StringBuilder(input.toString()).reverse().toString();
        enqueueBotMessage(text);
    }

    private void enqueueBotMessage(String input) {
        if (input == null || input.isEmpty()) return;
        this.lastBotMessage = input;
        final TalkStraight talkStraight = buildMessage(input, bot);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addToStart(talkStraight, true);
            }
        }, 1000);
    }

    private TalkStraight buildMessage(String text, Teller teller) {
        TalkStraight talkStraight = new TalkStraight(messageIdCount + "", text, teller, new Date());
        messageIdCount++;
        return talkStraight;
    }

    public static final String FRIEND_NAME = "friend_name";
    public static final String LAST_MESSAGE = "lastmessage";


}
