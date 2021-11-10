package com.romanm.jwtservicedata.models.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locality {
    private String name;
    private double lat = 0;
    private double lon = 0;

    public Locality(String name) {
        this.name = name;
    }
}
