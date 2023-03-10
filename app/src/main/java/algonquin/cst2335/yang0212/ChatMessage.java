package algonquin.cst2335.yang0212;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity //mark this as something that can go into a database
public class ChatMessage {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id") //specify that this variable will go into a database column named id
    public int id;

    @ColumnInfo(name="message")
    protected String message;

    @ColumnInfo(name="TimeSent")
    protected String timeSent;

    @ColumnInfo(name="SendOrReceive")
    protected boolean isSentButton;


    public ChatMessage() {}

    public ChatMessage(String m, String t, boolean sent) {
        this.message = m;
        this.timeSent = t;
        this.isSentButton = sent;
    }
    // getter for message
    public String getMessage() {
        return message;
    }
    // getter for time
    public String getTimeSent(){
        return timeSent;
    }
    // getter for isSentButton
    public boolean isSentButton() {
        return isSentButton;
    }


}



