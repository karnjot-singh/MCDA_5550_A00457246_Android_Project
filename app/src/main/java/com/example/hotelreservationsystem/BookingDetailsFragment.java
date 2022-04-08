package com.example.hotelreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.adapters.BookingDetailsAdapter;
import com.example.hotelreservationsystem.models.GuestData;
import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.models.ReservationData;
import com.example.hotelreservationsystem.viewmodels.HotelResultsViewModel;
import com.example.hotelreservationsystem.viewmodels.ReservationViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class BookingDetailsFragment extends Fragment {
    View view;
    EditText hotelName, hotelPrice, checkInDate, checkOutDate, noOfGuestsEditText;
    String userName;
    Integer noOfGuests;
    Button bookButton;
    BookingDetailsAdapter bookingDetailsAdapter;
    ReservationViewModel reservationViewModel;
    ProgressBar progressBar;
    ArrayList<BookingDetailsAdapter.ViewHolder> holders;

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
        noOfGuestsEditText = view.findViewById(R.id.no_of_guests_user_details_edit_text);
        bookButton = view.findViewById(R.id.user_details_book_button);
        progressBar = view.findViewById(R.id.confirmation_progress_bar);

        Bundle bundle = getArguments();

        hotelName.setText(bundle.getString("hotelName"));

        hotelPrice.setText('$' + String.valueOf(bundle.getInt("noOfGuests") * Integer.parseInt(bundle.getString("hotelPrice"))));
        userName = bundle.getString("userName");
        noOfGuests = bundle.getInt("noOfGuests");
        checkInDate.setText(bundle.getString("checkInDate"));
        checkOutDate.setText(bundle.getString("checkOutDate"));
        noOfGuestsEditText.setText(String.valueOf(bundle.getInt("noOfGuests")));

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()) {
                    progressBar.setVisibility(View.VISIBLE);
                    ArrayList<GuestData> guestData = new ArrayList<>();
                    for(BookingDetailsAdapter.ViewHolder holder : holders) {
                        String name = holder.getGuestName().getText().toString();
                        int genderSelected = holder.getGender().getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) holder.itemView.findViewById(genderSelected);
                        String radioText = radioButton.getText().toString().trim().toLowerCase();

                        GuestData guest;

                        if(radioText.equals("female")) {
                            guest = new GuestData(name,"F");
                        } else {
                            guest = new GuestData(name,"M");
                        }

                        guestData.add(guest);
                    }

                    ReservationData reservationData = new ReservationData();
                    reservationData.setHotel_name(hotelName.getText().toString());
                    reservationData.setCheckin(checkInDate.getText().toString());
                    reservationData.setCheckout(checkOutDate.getText().toString());
                    reservationData.setGuests_list(guestData);

                    try {
                        String datePattern = "dd MMM yyyy";
                        reservationData.setCheckin(new SimpleDateFormat("yyyy-MM-dd").format(
                                new SimpleDateFormat(datePattern).parse(bundle.getString("checkInDate"))
                        ));

                        reservationData.setCheckout(new SimpleDateFormat("yyyy-MM-dd").format(
                                new SimpleDateFormat(datePattern).parse(bundle.getString("checkOutDate"))
                        ));
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "INVALID DATE FORMAT", Toast.LENGTH_LONG).show();
                    }

                    makeReservation(reservationData);
                }
            }
        });

        setupRecyclerView();
    }

    public boolean validateData() {
        boolean validated = true;
        holders = bookingDetailsAdapter.getHolders();

        for(int i=0; i < holders.size(); i++) {
            BookingDetailsAdapter.ViewHolder holder = holders.get(i);

            String value = holder.getGuestName().getText().toString();
            boolean isValidName = validateGuestName(value, holder.getGuestNameErr());

            int genderSelected = holder.getGender().getCheckedRadioButtonId();
            boolean isValidGender = validateGender(genderSelected, holder.getGenderErr());

            if(validated){
                validated = isValidName & isValidGender;
            }
        }
        return validated;
    }

    private boolean validateGender(int value, TextView genderErr) {
        genderErr.setText(null);
        genderErr.setVisibility(View.GONE);
        if(value == -1 ) {
            genderErr.setText("Select gender");
            genderErr.setVisibility(View.VISIBLE);
            return false;
        }
        return false;
    }

    private boolean validateGuestName(String text, TextView guestNameErr) {
        guestNameErr.setVisibility(View.GONE);

        if (text == null || text.length() == 0) {
            guestNameErr.setText("Please enter your name");
            guestNameErr.setVisibility(View.VISIBLE);
        } else if (!Pattern.matches("^[a-zA-Z ]+$", text)) {
            guestNameErr.setText("Name can contain only alphabets");
            guestNameErr.setVisibility(View.VISIBLE);
        }else if (text.length() < 4) {
            guestNameErr.setText("Name should have at least 4 characters");
            guestNameErr.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.user_details_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingDetailsAdapter = new BookingDetailsAdapter(getActivity(), noOfGuests, userName);
        recyclerView.setAdapter(bookingDetailsAdapter);
    }

    private void makeReservation(ReservationData reservationData) {
        reservationViewModel.getConfirmationNumberObserver().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressBar.setVisibility(View.GONE);
                if(s != null) {
                    updateData(s, false);
                }
                else {
                    updateData("FAILED TO MAKE RESERVATION", true);
                }
                reservationViewModel.resetConfirmationNumber();
            }
        });
        reservationViewModel.createReservation(reservationData);
    }

    private void updateData(String data, boolean isError) {
        Bundle confirmation = new Bundle();

        if(isError) {
            confirmation.putString("confirmationNumberError", data);
            Toast.makeText(getActivity(), data, Toast.LENGTH_LONG);
        }
        else {
            confirmation.putString("confirmationNumberError", null);
            confirmation.putString("confirmationNumber", data);
            confirmation.putString("hotelName", hotelName.getText().toString());
            confirmation.putString("noOfGuests", String.valueOf(noOfGuests));
            confirmation.putString("checkInDate", checkInDate.getText().toString());
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
