package com.example.hotelreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hotelreservationsystem.R;
import com.example.hotelreservationsystem.viewmodels.HotelResultsViewModel;
import com.example.hotelreservationsystem.viewmodels.ReservationViewModel;

public class ReservationConfirmationFragment extends Fragment {
    View view;
    TextView reservationTextView, hotelNameTextView, guestsTextView, nightsTextView, checkInTextView;
    ImageView hotelNameImg, guestsImg, nightsImg, checkInImg, resultImg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservation_confirmation_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        reservationTextView = view.findViewById(R.id.reservation_confirmation_number_text_view);
        guestsTextView = view.findViewById(R.id.reservation_guests_text_view);
        checkInTextView = view.findViewById(R.id.reservation_check_in_text_view);
        nightsTextView = view.findViewById(R.id.reservation_nights_text_view);
        hotelNameTextView = view.findViewById(R.id.reservation_hotel_name_text_view);

        hotelNameImg = view.findViewById(R.id.ic_hotel_name);
        guestsImg = view.findViewById(R.id.ic_nights_date);
        nightsImg = view.findViewById(R.id.ic_guests_date);
        checkInImg = view.findViewById(R.id.ic_check_in_date);
        resultImg = view.findViewById(R.id.ic_result);

        if(getArguments().getString("confirmationNumberError") != null){
            reservationTextView.setText("ERROR\n" + getArguments().getString("confirmationNumberError"));
            hotelNameTextView.setVisibility(View.GONE);
            guestsTextView.setVisibility(View.GONE);
            nightsTextView.setVisibility(View.GONE);
            checkInTextView.setVisibility(View.GONE);

            hotelNameImg.setVisibility(View.GONE);
            guestsImg.setVisibility(View.GONE);
            nightsImg.setVisibility(View.GONE);
            checkInImg.setVisibility(View.GONE);
        }
        else {
            reservationTextView.setText(reservationTextView.getText().toString() + getArguments().getString("confirmationNumber"));
            hotelNameTextView.setText(getArguments().getString("hotelName"));
            guestsTextView.setText(getArguments().getString("noOfGuests") + " Guests");
            nightsTextView.setText("2 Nights");
            checkInTextView.setText(getArguments().getString("checkInDate"));

            resultImg.setImageResource(R.drawable.ic_check);
        }
    }
}
