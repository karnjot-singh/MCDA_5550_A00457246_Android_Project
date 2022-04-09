package com.example.hotelreservationsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.adapters.HotelResultsAdapter;
import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.viewmodels.HotelResultsViewModel;

import java.util.List;

public class HotelResultsFragment extends Fragment implements OnClickListener {

    View view;
    ProgressBar progressBar;
    TextView userDetailsTextView;

    HotelResultsViewModel hotelsViewModel;
    List<HotelData> hotelData;

    String userName, checkIn, checkOut;
    Integer noOfGuests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotel_search_results_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.search_results_progress_bar);
        userDetailsTextView = view.findViewById(R.id.user_details_search_results);

        Bundle bundle = getArguments();
        userName = bundle.getString("guestName");
        noOfGuests = Integer.parseInt(bundle.getString("guestNumber"));

        checkIn = bundle.getString("checkInDate");
        checkOut = bundle.getString("checkOutDate");

        String userDetailsText = "Hello "+ userName +", showing results for \n"+ noOfGuests +
                " guests from " + checkIn + " → " + checkOut;

        userDetailsTextView.setText(userDetailsText);

        setUpRecyclerView();

    }

    @Override
    public void onClick(HotelData hotel) {
        String hotelName = hotel.getName();
        String price = hotel.getPrice();

        Bundle bundle = new Bundle();
        bundle.putString("hotelName", hotelName);
        bundle.putString("hotelPrice", price);
        bundle.putString("checkInDate", checkIn);
        bundle.putString("checkOutDate", checkOut);
        bundle.putString("userName", userName);
        bundle.putInt("noOfGuests", noOfGuests);

        BookingDetailsFragment bookingDetailsFragment = new BookingDetailsFragment();
        bookingDetailsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.remove(HotelResultsFragment.this);
        fragmentTransaction.replace(R.id.frame_layout, bookingDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setUpRecyclerView() {
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HotelResultsAdapter hotelResultsAdapter = new HotelResultsAdapter(getActivity(), hotelData, this);
        recyclerView.setAdapter(hotelResultsAdapter);

        hotelsViewModel = new ViewModelProvider(this).get(HotelResultsViewModel.class);
        hotelsViewModel.getHotelDataObserver().observe(getViewLifecycleOwner(), new Observer<List<HotelData>>() {
            @Override
            public void onChanged(List<HotelData> hotelDataList) {
                if(hotelDataList != null) {
                    hotelResultsAdapter.setHotelData(hotelDataList);
                }
                else {
                    Toast.makeText(getActivity(), "ENABLE TO FETCH DATA", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        hotelsViewModel.getHotelListData();
    }
}
