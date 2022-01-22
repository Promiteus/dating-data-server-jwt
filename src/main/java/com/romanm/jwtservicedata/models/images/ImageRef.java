package com.romanm.jwtservicedata.models.images;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRef implements Serializable {
    private String src = "";
    private String alt = "";
}
