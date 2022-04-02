package com.example.hotelreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.adapters.BookingDetailsAdapter;
import com.example.hotelreservationsystem.models.GuestData;
import com.example.hotelreservationsystem.models.ReservationData;
import com.example.hotelreservationsystem.viewmodels.HotelResultsViewModel;
import com.example.hotelreservationsystem.viewmodels.ReservationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookingDetailsFragment extends Fragment {
    View view;
    EditText hotelName, hotelPrice, checkInDate, checkOutDate;
    String userName;
    Integer noOfGuests;
    Button bookButton;
    BookingDetailsAdapter bookingDetailsAdapter;
    ReservationViewModel reservationViewModel;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.booking_user_details_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        hotelName = view.findViewById(R.id.hotel_name_user_details_edit_text);
        hotelPrice = view.findViewById(R.id.hotel_price_user_details_edit_text);
        checkInDate = view.findViewById(R.id.check_in_user_details_edit_text);
        checkOutDate = view.findViewById(R.id.check_out_user_details_edit_text);
        bookButton = view.findViewById(R.id.user_details_book_button);
        progressBar = view.findViewById(R.id.confirmation_progress_bar);

        Bundle bundle = getArguments();

        hotelName.setText(bundle.getString("hotelName"));
        hotelPrice.setText(bundle.getString("hotelPrice"));
        userName = bundle.getString("userName");
        noOfGuests = bundle.getInt("noOfGuests");
        checkInDate.setText(bundle.getString("checkInDate"));
        checkOutDate.setText(bundle.getString("checkOutDate"));

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<GuestData> guestData = bookingDetailsAdapter.getGuestDataList();

                ReservationData reservationData = new ReservationData();
                reservationData.setHotel_name(hotelName.getText().toString());
                reservationData.setCheckin(checkInDate.getText().toString());
                reservationData.setCheckout(checkOutDate.getText().toString());
                reservationData.setGuests_list(guestData);

                try {
                    reservationData.setCheckin(new SimpleDateFormat("yyyy-MM-dd").format(
                            new SimpleDateFormat("MM/dd/yyyy").parse(bundle.getString("checkInDate"))
                    ));

                    reservationData.setCheckout(new SimpleDateFormat("yyyy-MM-dd").format(
                            new SimpleDateFormat("MM/dd/yyyy").parse(bundle.getString("checkOutDate"))
                    ));

                    reservationViewModel.createReservation(reservationData);
                    progressBar.setVisibility(View.VISIBLE);
                    confirmReservation();
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "INVALID DATE FORMAT", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupRecyclerView();
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.user_details_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingDetailsAdapter = new BookingDetailsAdapter(getActivity(), noOfGuests, userName);
        recyclerView.setAdapter(bookingDetailsAdapter);
    }

    private void confirmReservation() {
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);

        reservationViewModel.getIsFetched().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetched) {
                if(isFetched){
                    updateData();
                }
            }
        });
    }

    private void updateData() {
        Bundle confirmation = new Bundle();

        if(reservationViewModel.getIsError().getValue()) {
            confirmation.putString("confirmationNumber", reservationViewModel.getError().getValue());
        }
        else {
            confirmation.putString("confirmationNumber", reservationViewModel.getConfirmationNumber().getValue());
        }

        ReservationConfirmationFragment confirmationFragment = new ReservationConfirmationFragment();
        confirmationFragment.setArguments(confirmation);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.remove(BookingDetailsFragment.this);
        fragmentTransaction.replace(R.id.main_layout, confirmationFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
