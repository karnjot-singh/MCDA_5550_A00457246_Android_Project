package com.example.hotelreservationsystem.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.OnClickListener;
import com.example.hotelreservationsystem.R;
import com.example.hotelreservationsystem.models.GuestData;
import com.example.hotelreservationsystem.models.ReservationData;

import java.util.ArrayList;
import java.util.List;

public class BookingDetailsAdapter extends RecyclerView.Adapter<BookingDetailsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Integer noOfGuests;
    private String userName;
    private ArrayList<GuestData> guestDataList;
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
        if(guestDataList == null) {
            guestDataList = new ArrayList<>();
            holders = new ArrayList<>(this.noOfGuests);
            for (int i = 0; i < this.noOfGuests; i++) {
                guestDataList.add(new GuestData("", "M"));
            }
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==0) {
            holder.guestName.setText(userName);
            guestDataList.get(0).setGuest_name(userName);
        }
        Log.e("POSITION", String.valueOf(position));
        holder.guestNameTextView.setText("Guest "+(position+1));

        holder.guestName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                guestDataList.get(position).setGuest_name(editable.toString());
            }
        });

        holder.gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==0) {
                    guestDataList.get(position).setGender("M");
                } else {
                    guestDataList.get(position).setGender("F");
                }
            }
        });

        holders.add(position, holder);
    }

    @Override
    public int getItemCount() {
        return noOfGuests;
    }

    public ArrayList<GuestData> getGuestDataList() {
        return guestDataList;
    }

    public ArrayList<ViewHolder> getHolders() {
        return holders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView guestNameTextView, guestNameErr;
        EditText guestName;
        RadioButton maleRadio, femaleRadio;
        RadioGroup gender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            guestNameTextView = itemView.findViewById(R.id.name_guest_details_text_view);
            guestNameErr = itemView.findViewById(R.id.name_guest_details_error);
            guestName = itemView.findViewById(R.id.name_guest_details_edit_text);
            maleRadio = itemView.findViewById(R.id.male_guest_details_radio_button);
            femaleRadio = itemView.findViewById(R.id.female_guest_details_radio_button);
            gender = itemView.findViewById(R.id.gender_guest_details_radio_group);
        }

        public TextView getGuestNameErr() {
            return guestNameErr;
        }

        public EditText getGuestName() {
            return guestName;
        }
    }
}
