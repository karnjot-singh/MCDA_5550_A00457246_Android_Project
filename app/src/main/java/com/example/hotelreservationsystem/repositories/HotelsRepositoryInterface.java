package com.example.hotelreservationsystem.repositories;

import com.example.hotelreservationsystem.models.HotelData;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface HotelsRepositoryInterface {

    @GET("/api/hotel")
    public void getHotelsList(Callback<List<HotelData>> callback);
}
