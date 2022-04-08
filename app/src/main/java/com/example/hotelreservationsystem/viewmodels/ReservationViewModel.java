package com.example.hotelreservationsystem.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hotelreservationsystem.models.ReservationData;
import com.example.hotelreservationsystem.repositories.DB;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends ViewModel {
    private MutableLiveData<String> confirmationNumber;

    public ReservationViewModel() {
        this.confirmationNumber = new MutableLiveData<>();
    }

    public MutableLiveData<String> getConfirmationNumberObserver() {
        return confirmationNumber;
    }

    public void resetConfirmationNumber() {
        this.confirmationNumber = new MutableLiveData<>();
    }

    public void createReservation(ReservationData reservationData) {
        Call<JsonObject> call = DB.getReservationRepository().createReservation(reservationData);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                confirmationNumber.postValue(response.body().get("confirmation_number").toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                confirmationNumber.postValue(null);
            }
        });
    }
}
