package com.ecommerce.project.service;

import com.ecommerce.project.payLoad.ProductDTO;
import com.ecommerce.project.payLoad.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getByCategory(Long categoryId);

    ProductResponse getByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductResponse deleteProduct(Long productId);
}
