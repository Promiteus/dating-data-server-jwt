package com.romanm.jwtservicedata.models.responses.files;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     * Вернет текущее расширение файла
     * @return String
     */
    @JsonIgnore
    public String getCurrentFileExtension() {
        String regexpDelimiter = "\\.";
        String[] strParts = this.getFileName().split(regexpDelimiter);
        if (strParts.length > 1) {
            return strParts[strParts.length-1];
        }
        return "";
    }


}
