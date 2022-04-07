package com.example.hotelreservationsystem.repositories;

import retrofit.RestAdapter;

public class DB {
    private static RestAdapter adapter;

    public static RestAdapter getClient() {
        if(adapter==null) {
            adapter = new RestAdapter.Builder()
                    .setEndpoint("http://10.0.2.2:8000/")
                    .build();
        }

        return adapter;
    }
}
