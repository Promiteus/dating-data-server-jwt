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
    private String url = "";

    public FileStatus(boolean saved, String fileName, String error) {
        this.error = error;
        this.fileName = fileName;
        this.saved = saved;
        this.url = "";
    }
}
