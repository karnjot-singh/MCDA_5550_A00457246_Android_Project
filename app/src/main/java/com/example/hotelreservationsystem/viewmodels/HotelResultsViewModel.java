package com.example.hotelreservationsystem.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.repositories.DB;
import com.example.hotelreservationsystem.repositories.HotelsRepository;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HotelResultsViewModel extends ViewModel {
    private MutableLiveData<List<HotelData>> hotelData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFetched = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public HotelResultsViewModel() {
        isError.setValue(false);
        isFetched.setValue(false);
        HotelsRepository.getHotelsRepository().getHotelsList(new Callback<List<HotelData>>() {
            @Override
            public void success(List<HotelData> hotelDataList, Response response) {
                hotelData.setValue(hotelDataList);
                isError.setValue(false);
                isFetched.setValue(true);
            }

            @Override
            public void failure(RetrofitError err) {
                isError.setValue(true);
                error.setValue(err.toString());
                isFetched.setValue(true);
            }
        });
    }

    public MutableLiveData<List<HotelData>> getHotelData() {
        return hotelData;
    }

    public MutableLiveData<Boolean> getIsError() {
        return isError;
    }

    public MutableLiveData<Boolean> getIsFetched() {
        return isFetched;
    }

    public MutableLiveData<String> getError() {
        return error;
    }
}
