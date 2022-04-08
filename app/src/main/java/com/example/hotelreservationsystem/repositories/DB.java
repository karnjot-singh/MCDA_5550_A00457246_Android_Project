package com.example.hotelreservationsystem.repositories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DB {
    private static Retrofit retrofit;
    private static String BASE_URL = "http://192.168.2.15:8000/";

    public static Retrofit getClient() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static HotelsRepositoryInterface getHotelsRepository() {
        HotelsRepositoryInterface hotelsRepository = DB.getClient().create(HotelsRepositoryInterface.class);
        return hotelsRepository;
    }

    public static ReservationsRepositoryInterface getReservationRepository() {
        ReservationsRepositoryInterface repositoryInterface = DB.getClient().create(ReservationsRepositoryInterface.class);
        return repositoryInterface;
    }
}
