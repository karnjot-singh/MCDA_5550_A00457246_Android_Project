package com.example.hotelreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView reservationTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reservation_confirmation_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationTextView = view.findViewById(R.id.reservation_confirmation_text_view);

        reservationTextView.setText(getArguments().getString("confirmationNumber"));
    }
}
