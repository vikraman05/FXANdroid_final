package comin.chaten.statens.caugfitten.servernendto.message;

import java.util.ArrayList;

/**
 * Created by User on 5/05/2017.
 */

public class FindAlert {

    private static FindAlert instance;
    private ArrayList<NoteDetails> dialogMessagesList;

    public FindAlert(ArrayList<NoteDetails> dialogMessagesList) {
        this.dialogMessagesList = dialogMessagesList;
    }

    public static FindAlert getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new FindAlert(createDialogMsgList());
        return instance;
    }

    public static ArrayList<NoteDetails> createDialogMsgList() {
        ArrayList<NoteDetails> noteDetailses = new ArrayList<>();
        noteDetailses.add(new NoteDetails("Aaron", "Good Morning!"));
        noteDetailses.add(new NoteDetails("Adarsh", "How s health"));
        noteDetailses.add(new NoteDetails("Buz", "Goog"));
        noteDetailses.add(new NoteDetails("Ceaser", "Bye"));

        return noteDetailses;
    }

    public ArrayList<NoteDetails> getDialogMessagesList() {
        return dialogMessagesList;
    }
}
