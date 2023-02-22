package algonquin.cst2335.yang0212;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.yang0212.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.yang0212.databinding.ReceiveMessageBinding;
import algonquin.cst2335.yang0212.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel ;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the onCreate function
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        //retrieve the ArrayList<> that it is storing
        messages = chatModel.messages.getValue();

        // verify if the chatModel.messages variable has never been set before.
        // If it is the first time, initialize the ChatModel class
        if(messages == null) {
            chatModel.messages.postValue( messages = new ArrayList<>());
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  a click listener to the send button
        binding.sendButton.setOnClickListener(click -> {

            // string message that was typed that the button was clicked
            String message = binding.textInput.getText().toString();

            // a String representing the time
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            // it was the Sent button that was clicked
            ChatMessage newMessage = new ChatMessage(message,currentDateandTime, true);

            // add the string to the messages ArrayList
            messages.add(newMessage);
            // notify entire ArrayList has changed
            myAdapter.notifyDataSetChanged();
            // notify the Adapter object that something has been inserted
            // tells the Adapter which row has to be redrawn (the back of the list)
            myAdapter.notifyItemInserted(messages.size() - 1);

            //clear the previous text:
            binding.textInput.setText("");

        });

        //  a click listener to the receive button
        binding.receiveButton.setOnClickListener(click -> {

            String message = binding.textInput.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            // it was not the Sent button that was clicked
            ChatMessage newMessage = new ChatMessage(message,currentDateandTime, false);

            messages.add(newMessage);

            myAdapter.notifyDataSetChanged();
            myAdapter.notifyItemInserted(messages.size() - 1);

            binding.textInput.setText("");

        });

        // specify a single column scrolling in a Vertical direction
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));




        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            // creates a ViewHolder object represents a single row in the list
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // Check if viewType is 0 (isSendButton)
                if (viewType == 0) {
                    // load the view from sent_message.xml
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(),parent,false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    // load the view from receive_message.xml
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(),parent,false);
                    return new MyRowHolder(binding.getRoot());
                }

//                int layoutId;
//                if (viewType == 0) {
//                    layoutId = R.layout.sent_message;
//                } else {
//                    layoutId = R.layout.receive_message;
//                }
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
//                return new MyRowHolder(view);
            }

            // set the data for ViewHolder object that will go at row position in the list
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // retrieve the object that goes in row "position" in this list
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            // return the number of rows in the list
            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){

                ChatMessage obj = messages.get(position);
                if (obj.isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }
}

// create an inner class MyRowHolder which will be an object for representing
// everything that goes on a row in the list. It's to maintain variables for
// what you want to set on each row in your list.
class MyRowHolder extends RecyclerView.ViewHolder {
    TextView messageText;
    TextView timeText;

    public MyRowHolder(@NonNull View itemView) {

        super(itemView);
        // Use id to find TextViews for message and time in xml files of sent or receive message as sub-items
        messageText = itemView.findViewById(R.id.message);
        timeText = itemView.findViewById(R.id.time);
    }
}