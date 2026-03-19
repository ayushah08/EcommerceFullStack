package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payLoad.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);
}
