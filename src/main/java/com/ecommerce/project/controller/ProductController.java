package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payLoad.ProductDTO;
import com.ecommerce.project.payLoad.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProduct = productService.addProduct(productDTO, categoryId);

        return new ResponseEntity(savedProduct, HttpStatus.CREATED);


    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.DIR) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.DIR) String sortOrder) {

        ProductResponse productResponse = productService.getByCategory(categoryId,pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
                                                               @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.DIR) String sortOrder) {
        ProductResponse productResponse = productService.getByKeyword(keyword,pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId) {

        ProductDTO updatedProduct = productService.updateProduct(productDTO, productId);

        return new ResponseEntity(updatedProduct, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId) {

        ProductResponse product = productService.deleteProduct(productId);
        return new ResponseEntity(product, HttpStatus.OK);

    }

    @PutMapping("/admin/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImages(@PathVariable Long productId,
                                                          @RequestParam("image") MultipartFile file) throws IOException {

        ProductDTO productImage = productService.UpdateProductImage(productId, file);
        return new ResponseEntity(productImage, HttpStatus.OK);
    }

}
