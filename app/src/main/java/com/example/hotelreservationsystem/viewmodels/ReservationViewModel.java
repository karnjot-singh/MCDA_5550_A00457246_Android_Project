package com.example.hotelreservationsystem.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.models.ReservationData;
import com.example.hotelreservationsystem.repositories.HotelsRepository;
import com.example.hotelreservationsystem.repositories.ReservationsRepository;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReservationViewModel extends ViewModel {
    private MutableLiveData<String> confirmationNumber = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFetched = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public String createReservation(ReservationData reservationData) {
        isError.setValue(false);
        isFetched.setValue(false);

        ReservationsRepository.createRepository().createReservation(reservationData, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject confirmation, Response response) {
                confirmationNumber.setValue(confirmation.get("confirmation_number").toString());
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
        return confirmationNumber.getValue();
    }

    public MutableLiveData<String> getConfirmationNumber() {
        return confirmationNumber;
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
