package comin.chaten.statens.caugfitten.servernendto.message;


import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.Date;

public class TalkStraight implements IMessage {

    private String id;
    private String text;
    private Teller teller;
    private Date createdAt;

    public TalkStraight(String id, String text, Teller teller, Date createdAt) {
        this.id = id;
        this.text = text;
        this.teller = teller;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Teller getUser() {
        return teller;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTeller(Teller teller) {
        this.teller = teller;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}