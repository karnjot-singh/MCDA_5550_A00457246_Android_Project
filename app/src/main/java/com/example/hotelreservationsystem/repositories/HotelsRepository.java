package com.example.hotelreservationsystem.repositories;

import com.example.hotelreservationsystem.models.HotelData;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

public class HotelsRepository {
    public static HotelsRepositoryInterface getHotelsRepository() {
        HotelsRepositoryInterface hotelsRepository = DB.getClient().create(HotelsRepositoryInterface.class);
        return hotelsRepository;
    }
}
