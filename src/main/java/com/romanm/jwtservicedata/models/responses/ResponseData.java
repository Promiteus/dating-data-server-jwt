package com.romanm.jwtservicedata.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Getter
@NoArgsConstructor
public class ResponseData<T> implements Serializable {
    private List<T> data;
    private int page;
    private int size;
    private int total;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseData(int page, int size, List<T> data) {
        this.page = page;
        this.size = size;
        this.data = data;
        this.total = 0;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseData(int page, int size, List<T> data, int total) {
        this.page = page;
        this.size = size;
        this.data = data;
        this.total = total;
        this.timestamp = LocalDateTime.now();
    }
}
