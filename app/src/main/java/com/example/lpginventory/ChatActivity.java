package com.example.lpginventory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private Button sendButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // Set up the listener for receiving real-time updates
        setupMessageListener();
    }

    private void sendMessage() {
        String content = messageInput.getText().toString().trim();
        if (!content.isEmpty()) {
            String sender = "User"; // Replace with actual sender info

            Map<String, Object> data = new HashMap<>();
            data.put("sender", sender);
            data.put("content", content);

            db.collection("messages")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        // Data added successfully
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                    });

            messageInput.setText("");
        }
    }

    private void setupMessageListener() {
        db.collection("messages")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    messageList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Message message = document.toObject(Message.class);
                        messageList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                });
    }
}
