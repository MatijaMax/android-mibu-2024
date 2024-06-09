package com.example.ma02mibu.model;

import java.util.ArrayList;
import java.util.List;

public class GuestListDTO {

    private List<Guest> guests;

    public GuestListDTO(List<Guest> guests) {
        this.guests = guests;
    }

    public GuestListDTO() {

    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }
}
