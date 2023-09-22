package com.example.lpginventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CreditPersonAdapter extends ArrayAdapter<CreditPerson> {
    private Context context;
    private List<CreditPerson> creditPersonsList;
    private CreditPersonListener creditPersonListener;

    public List<CreditPerson> getCreditPersonsList() {
        return creditPersonsList;
    }

    public interface CreditPersonListener {
        void onCreditPaid(double amount);
    }

    public CreditPersonAdapter(Context context, List<CreditPerson> creditPersonsList, CreditPersonListener creditPersonListener) {
        super(context, 0, creditPersonsList);
        this.context = context;
        this.creditPersonsList = creditPersonsList;
        this.creditPersonListener = creditPersonListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_credit_person, parent, false);
        }

        final CreditPerson creditPerson = creditPersonsList.get(position);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewAmount = convertView.findViewById(R.id.textViewAmount);
        Button buttonPaid = convertView.findViewById(R.id.buttonPaid);

        textViewName.setText(creditPerson.getName());
        textViewAmount.setText("Amount: " + creditPerson.getAmount());

        buttonPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Paid" button click here
                double creditsPaid = creditPerson.getAmount();

                // Notify the CreditPersonListener (which is the CreditsActivity) about the paid credit amount
                if (creditPersonListener != null) {
                    creditPersonListener.onCreditPaid(creditsPaid);
                }
                // Remove the credit person from the list and update the ListView
                creditPersonsList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}

