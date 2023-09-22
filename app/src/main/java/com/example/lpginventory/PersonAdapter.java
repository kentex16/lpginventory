package com.example.lpginventory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private static List<Person> persons = new ArrayList<>();
    private CylinderReturnedListener listener;

    private static OnReturnedButtonClickListener onReturnedButtonClickListener;
    public interface OnReturnedButtonClickListener {
        void onCylinderReturned(Person person);
    }
    public void setPersons(List<Person> persons) {
        this.persons = persons;
        notifyDataSetChanged();
    }
    public void setOnReturnedButtonClickListener(OnReturnedButtonClickListener listener) {
        this.onReturnedButtonClickListener = listener;
    }


    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        final Person person = persons.get(position);
        holder.textViewName.setText(person.getName());
        holder.textViewStatus.setText(person.isContainerReturned() ? "Container Returned" : "Not Returned");

        // Add other fields to display, such as lpgWeight, amount, etc., if needed

        // Set a click listener for the "Returned" button
        holder.buttonReturned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Returned" button click here
                // Perform any necessary actions, such as updating the database and removing the person from the list
                if (!person.isContainerReturned()) {
                    // If the container is not returned, mark it as returned and update the database
                    person.setContainerReturned(true);
                    // You may need to call a method to update the database here

                    // Remove the person from the list
                    persons.remove(person);
                    // Notify the adapter that the data has changed so the UI can be updated
                    notifyDataSetChanged();

                    // Notify the DailySalesActivity that the cylinder is returned
                    // You can use an interface to communicate this information to the activity
                    if (listener != null) {
                        listener.onCylinderReturned(person);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewStatus; // Add this TextView for displaying Container Returned status
        Button buttonReturned;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus); // Initialize textViewStatus
            buttonReturned = itemView.findViewById(R.id.buttonReturned); // Initialize the "Returned" button

            // Set the click listener for the "Returned" button
            buttonReturned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onReturnedButtonClickListener != null) {
                        onReturnedButtonClickListener.onCylinderReturned(persons.get(position));
                    }
                }
            });
        }
    }



    public void setListener(CylinderReturnedListener listener) {
        this.listener = listener;
    }

    // Define an interface for communicating the cylinder returned event to the activity
    public interface CylinderReturnedListener {
        void onCylinderReturned(Person person);
    }
}

