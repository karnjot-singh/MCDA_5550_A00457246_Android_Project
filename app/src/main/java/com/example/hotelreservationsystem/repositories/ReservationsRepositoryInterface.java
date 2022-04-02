package com.example.hotelreservationsystem.repositories;

import com.example.hotelreservationsystem.models.HotelData;
import com.example.hotelreservationsystem.models.ReservationData;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ReservationsRepositoryInterface {

    @POST("/api/makereservation")
    void createReservation(@Body ReservationData reservationData, Callback<JsonObject> callback);
}
