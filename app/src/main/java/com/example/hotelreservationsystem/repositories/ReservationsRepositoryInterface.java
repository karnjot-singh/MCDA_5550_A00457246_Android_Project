package com.example.hotelreservationsystem.repositories;

import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.models.ReservationData;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReservationsRepositoryInterface {

    @POST("/api/makereservation")
    Call<JsonObject> createReservation(@Body ReservationData reservationData);
}
