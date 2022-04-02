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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelreservationsystem.adapters.HotelResultsAdapter;
import com.example.hotelreservationsystem.models.GuestData;
import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.repositories.HotelsRepository;
import com.example.hotelreservationsystem.viewmodels.HotelResultsViewModel;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        String userDetailsText = "Hello "+ userName +", showing results for "+ noOfGuests +
                " guests from " + checkIn + " to " + checkOut;

        userDetailsTextView.setText(userDetailsText);

        getHotelData();

    }

    @Override
    public void onClick(View view, int position) {
        HotelData hotel = hotelData.get(position);

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
        fragmentTransaction.replace(R.id.main_layout, bookingDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void getHotelData() {
        progressBar.setVisibility(View.VISIBLE);
        hotelsViewModel = new ViewModelProvider(this).get(HotelResultsViewModel.class);
        hotelsViewModel.getIsFetched().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFetched) {
                if(isFetched){
                    updateData();
                }
            }
        });
    }

    private void updateData() {
        if(hotelsViewModel.getIsError().getValue()) {
            Toast.makeText(getActivity(), hotelsViewModel.getError().getValue(), Toast.LENGTH_LONG).show();
        }
        else {
            hotelData = hotelsViewModel.getHotelData().getValue();
            setUpRecyclerView();
        }
    }

    private void setUpRecyclerView() {
        progressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        HotelResultsAdapter hotelResultsAdapter = new HotelResultsAdapter(getActivity(), hotelData);
        recyclerView.setAdapter(hotelResultsAdapter);

        hotelResultsAdapter.setItemClickListener(this);
    }
}
