package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ProductController {
    private List<Product> products;


    public ProductController() {
        products = List.of(
                new Product("Кресло"),
                new Product("Стиральная машина"),
                new Product("Варочная панель"),
                new Product("Велосипед")
        );
    }

    @GetMapping(value = "/products")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(products);
    }
}
