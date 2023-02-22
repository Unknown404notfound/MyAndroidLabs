package algonquin.cst2335.yang0212;

import java.util.ArrayList;

// store the data required for each message sent (message, time sent, and which button was clicked)
public class ChatMessage {

    String message;
    String timeSent;
    boolean isSentButton;

    public ChatMessage(String m, String t, boolean sent) {
        message = m;
        timeSent = t;
        isSentButton = sent;
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
