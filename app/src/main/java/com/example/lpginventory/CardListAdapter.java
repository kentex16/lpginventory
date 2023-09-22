package com.example.lpginventory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CardListAdapter extends ArrayAdapter<String> {

    private List<String> resetNames;
    private Context context;

    public CardListAdapter(Context context, List<String> resetNames) {
        super(context, 0, resetNames);
        this.context = context;
        this.resetNames = resetNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_reset_activity, parent, false);
        }

        final String resetName = getItem(position);
        TextView resetTitleTextView = convertView.findViewById(R.id.resetTitleTextView);
        TextView resetDetailsTextView = convertView.findViewById(R.id.resetDetailsTextView);

        resetTitleTextView.setText(resetName);
        resetDetailsTextView.setText("Click to view details");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click here
                Intent resetDetailsIntent = new Intent(context, ResetDetailsActivity.class);
                resetDetailsIntent.putExtra("resetName", resetName);
                context.startActivity(resetDetailsIntent);
            }
        });

        return convertView;
    }
}
