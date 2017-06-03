package comin.chaten.statens.caugfitten.servernendto.message;

/**
 * Created by User on 4/05/2017.
 */

public class NoteDetails {
    private String contactName;
    private String lastMsg;

    public NoteDetails() {
    }

    public NoteDetails(String contactName, String lastMsg) {
        this.contactName = contactName;
        this.lastMsg = lastMsg;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
