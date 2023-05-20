package com.example.ridesharing.commonClasses;

/**
 * Класс, отвечающий за создание екземпляра поездки
 */
public class ClassTrip {
    private String trip_id;
    private Boolean status;

    public ClassTrip(String trip_id, Boolean status) {
        this.trip_id = trip_id;
        this.status = status;
    }

    public ClassTrip() {
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
