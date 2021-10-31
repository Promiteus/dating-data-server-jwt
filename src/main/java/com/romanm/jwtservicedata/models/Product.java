package com.romanm.jwtservicedata.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@EqualsAndHashCode
public class Product implements Serializable {

    private String id;
    private String name;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Product() {
        this.id = UUID.randomUUID().toString();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public Product(String name) {
        this();
        this.name = name;
    }
}
