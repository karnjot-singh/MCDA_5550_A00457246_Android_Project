package com.example.hotelreservationsystem.repositories;

import retrofit.RestAdapter;

public class DB {
    private static RestAdapter adapter;

    public static RestAdapter getClient() {
        if(adapter==null) {
            adapter = new RestAdapter.Builder()
                    .setEndpoint("http://192.168.2.15:8000/")
                    .build();
        }
        return adapter;
    }

    public static HotelsRepositoryInterface getHotelsRepository() {
        HotelsRepositoryInterface hotelsRepository = adapter.create(HotelsRepositoryInterface.class);
        return hotelsRepository;
    }
}
