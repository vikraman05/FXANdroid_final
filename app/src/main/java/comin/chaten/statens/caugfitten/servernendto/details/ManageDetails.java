package comin.chaten.statens.caugfitten.servernendto.details;


import java.util.ArrayList;

public class ManageDetails {

    private static ManageDetails instance;
    private ArrayList<Details> contactsList;

    public ManageDetails(ArrayList<Details> contactsList) {
        this.contactsList = contactsList;
    }

    public static ManageDetails getInstance() {
        if (instance != null) return instance;

        instance = new ManageDetails(createContactsList());
        return instance;
    }

    public ArrayList<Details> getContactsList() {
        return contactsList;
    }

    public static ArrayList<Details> createContactsList() {
        ArrayList<Details> detailses = new ArrayList<Details>();
        detailses.add(new Details("Adarsh", true));
        detailses.add(new Details("Aaron", true));
        detailses.add(new Details("Buz", true));
        detailses.add(new Details("Caesar", true));
        return detailses;
    }

}
