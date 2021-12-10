package com.romanm.jwtservicedata.models.responses.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    public String getCurrentFileExtension() {
        String regexpDelimiter = "\\.";
        String[] strParts = this.getFileName().split(regexpDelimiter);
        if (strParts.length > 1) {
            return strParts[strParts.length-1];
        }
        return "";
    }

    /**
     * Проверить входит ли расширение в представленное множество
     * @param extensions List<String>
     * @param ext String
     * @return boolean
     */
    public boolean isExtensionInSet(List<String> extensions, String ext) {
        return extensions.contains(ext);
    }
}
