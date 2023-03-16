package algonquin.cst2335.yang0212;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.yang0212.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.yang0212.databinding.ReceiveMessageBinding;
import algonquin.cst2335.yang0212.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel;
    RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages;
    ChatMessageDAO mDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //open a database
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        // initialize the onCreate function
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        //retrieve the ArrayList<> that it is storing
        messages = chatModel.messages.getValue();

        if (messages == null) {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
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
            ChatMessage newMessage = new ChatMessage(message, currentDateandTime, true);

            // add the string to the messages ArrayList
            messages.add(newMessage);

            Executor thread_send = Executors.newSingleThreadExecutor();
            thread_send.execute(() ->{
                //store id of the new inserted message to variable id
                long id = mDAO.insertMessage(newMessage);
                //set the id to the value stored in id variable
                newMessage.id = (int)id;
            });
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
            ChatMessage newMessage = new ChatMessage(message, currentDateandTime, false);

            messages.add(newMessage);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->{
               //mDAO.insertMessage(newMessage);
                long id = mDAO.insertMessage(newMessage);
                newMessage.id = (int)id;
            });

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
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    // load the view from receive_message.xml
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }

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
            public int getItemViewType(int position) {

                ChatMessage obj = messages.get(position);
                if (obj.isSentButton()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;


        public MyRowHolder(@NonNull View itemView) {

            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();

                Executor thread = Executors.newSingleThreadExecutor();

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);

                builder.setMessage("Do you want to delete this message: " + messageText.getText()) //set the message on the alert window
                        .setTitle("Question: ") //set the title of the alert dialog
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(position);

                            //ChatMessage newMessage = new ChatMessage(message, currentDateandTime, false);

                            thread.execute(() ->
                            {mDAO.deleteMessage(m);
                            });
                            ChatMessage removedMessage = messages.remove(position);
                            myAdapter.notifyItemRemoved(position);




                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo",click -> {
                                thread.execute(() ->
                                {mDAO.insertMessage(m);
                                });
                                messages.add(position,removedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                                    .show();

                        })
                        .setNegativeButton("No", (dialog, cl) -> {
                        })

                        .create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}

