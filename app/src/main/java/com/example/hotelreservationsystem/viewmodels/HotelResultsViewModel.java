package com.example.hotelreservationsystem.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.repositories.DB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelResultsViewModel extends ViewModel {
    private MutableLiveData<List<HotelData>> hotelData;

    public HotelResultsViewModel() {
        hotelData = new MutableLiveData<>();
    }


    public MutableLiveData<List<HotelData>> getHotelDataObserver() {
        return hotelData;
    }

    public void getHotelListData() {
        Call<List<HotelData>> call = DB.getHotelsRepository().getHotelsList();
        call.enqueue(new Callback<List<HotelData>>() {
            @Override
            public void onResponse(Call<List<HotelData>> call, Response<List<HotelData>> response) {
                hotelData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<HotelData>> call, Throwable t) {
                hotelData.postValue(null);
            }
        });
    }
}
