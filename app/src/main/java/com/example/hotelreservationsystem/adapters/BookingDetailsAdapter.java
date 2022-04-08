package com.example.hotelreservationsystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.R;

import java.util.ArrayList;

public class BookingDetailsAdapter extends RecyclerView.Adapter<BookingDetailsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Integer noOfGuests;
    private String userName;
    private ArrayList<ViewHolder> holders;

    public BookingDetailsAdapter(Context context, Integer guests, String userName) {
        this.layoutInflater = LayoutInflater.from(context);
        this.noOfGuests = guests;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.booking_guest_details_layout, parent, false);
        if(holders == null) { holders = new ArrayList<>(this.noOfGuests); }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0) {
            holder.guestName.setText(userName);
        }

        holder.guestNameTextView.setText("Guest "+(position+1));
        holders.add(position, holder);
    }

    @Override
    public int getItemCount() {
        return noOfGuests;
    }

    public ArrayList<ViewHolder> getHolders() {
        return holders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView guestNameTextView, guestNameErr, genderErr;
        EditText guestName;
        RadioButton maleRadio, femaleRadio;
        RadioGroup gender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            guestNameTextView = itemView.findViewById(R.id.name_guest_details_text_view);
            guestNameErr = itemView.findViewById(R.id.name_guest_details_error);
            genderErr = itemView.findViewById(R.id.gender_guest_details_error);
            guestName = itemView.findViewById(R.id.name_guest_details_edit_text);
            maleRadio = itemView.findViewById(R.id.male_guest_details_radio_button);
            femaleRadio = itemView.findViewById(R.id.female_guest_details_radio_button);
            gender = itemView.findViewById(R.id.gender_guest_details_radio_group);
        }

        public TextView getGuestNameErr() {
            return guestNameErr;
        }

        public TextView getGenderErr() {
            return genderErr;
        }

        public EditText getGuestName() {
            return guestName;
        }

        public RadioGroup getGender() {
            return gender;
        }
    }
}
