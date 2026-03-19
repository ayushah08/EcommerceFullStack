package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payLoad.ProductDTO;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/{categoryId}/product")
    private ResponseEntity<ProductDTO> addProduct(@RequestBody Product product,
                                                  @RequestParam Long categoryId) {
        ProductDTO savedProduct = productService.addProduct(product, categoryId);

        return new ResponseEntity(savedProduct, HttpStatus.CREATED);


    }
}
