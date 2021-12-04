package com.romanm.jwtservicedata.models.responses.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStatus implements Serializable {
    private boolean saved = false;
    private String fileName;
    private String error = "";
}
