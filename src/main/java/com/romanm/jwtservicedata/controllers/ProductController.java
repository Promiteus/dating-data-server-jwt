package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = Api.API_PREFIX)
public class ProductController {
    private Flux<Product> products;


    public ProductController() {
        products = Flux.just(
                new Product("Кресло"),
                new Product("Стиральная машина"),
                new Product("Варочная панель"),
                new Product("Велосипед")
        );
    }

    @GetMapping(value = "/products")
    public ResponseEntity<Flux<Product>> getAll() {
        return ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(products);
    }
}
