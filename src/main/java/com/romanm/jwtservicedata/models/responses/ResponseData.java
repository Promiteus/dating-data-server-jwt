package com.romanm.jwtservicedata.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ResponseData<T> implements Serializable {
    private Flux<T> data;
    private int page;
    private int size;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseData(int page, int size, Flux<T> data) {
        this.page = page;
        this.size = size;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
