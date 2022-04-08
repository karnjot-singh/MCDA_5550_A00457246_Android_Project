package com.example.hotelreservationsystem.repositories;

import com.example.hotelreservationsystem.models.HotelData;

import java.util.List;

import retrofit.Callback;
import retrofit2.Call;
import retrofit2.http.GET;

public interface HotelsRepositoryInterface {

    @GET("/api/hotel")
    Call<List<HotelData>> getHotelsList();
}
