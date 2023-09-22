package com.example.lpginventory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_OTHERS = 2;

    private String currentUser; // Current user's name
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.currentUser = currentUser;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        if (viewType == VIEW_TYPE_USER) {
            layoutRes = R.layout.item_message_user;
        } else {
            layoutRes = R.layout.item_message_others;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.contentTextView.setText(message.getContent());

        if (getItemViewType(position) == VIEW_TYPE_USER) {
            // Adjust the layout for the user's message
            holder.senderTextView.setVisibility(View.GONE);
            // Set other styles or adjustments specific to the user's messages
        } else {
            // Adjust the layout for others' messages
            holder.senderTextView.setText(message.getSender());
            // Set other styles or adjustments specific to others' messages
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.getSender().equals(currentUser)) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_OTHERS;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}

