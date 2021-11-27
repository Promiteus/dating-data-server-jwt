package com.romanm.jwtservicedata.models.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Locality {
    @Id
    @NotNull
    private String id;
    @Indexed(name = "localityName")
    private String localityName;
    private double lat = 0;
    private double lon = 0;
    @Indexed(name = "locationId")
    private String locationId;

    public Locality(String localityName, String locationId, double lat, double lon) {
        this.localityName = localityName;
        this.locationId = locationId;
        this.lat = lat;
        this.lon = lon;
    }
}
