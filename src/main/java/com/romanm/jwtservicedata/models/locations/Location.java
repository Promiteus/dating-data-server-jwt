package com.romanm.jwtservicedata.models.locations;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Location {
    @Id
    @NotNull
    private String id;
    @NotBlank
    private String country;
    @NotBlank
    private String state;

    private List<Locality> localityList;

    public Location(String country, String state, List<Locality> localityList) {
        this.country = country;
        this.state = state;
        this.localityList = localityList;
    }
}
