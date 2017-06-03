package comin.chaten.statens.caugfitten.servernendto.details;

/**
 * Created by Siregel on 27/04/2017.
 */

public class Details {
    private String userName;
    private Boolean isContact;

    public Details() {}

    public Details(String userName, Boolean isContact) {
        this.userName = userName;
        this.isContact = isContact;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsContact() {
        return isContact;
    }

    public void setIsContact(Boolean isContact) {
        this.isContact = isContact;
    }

}
