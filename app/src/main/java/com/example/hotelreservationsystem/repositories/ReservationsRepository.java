package com.example.hotelreservationsystem.repositories;

public class ReservationsRepository {
    public static ReservationsRepositoryInterface createRepository() {
        ReservationsRepositoryInterface reservationsRepository = DB.getClient().create(ReservationsRepositoryInterface.class);
        return reservationsRepository;
    }
}
