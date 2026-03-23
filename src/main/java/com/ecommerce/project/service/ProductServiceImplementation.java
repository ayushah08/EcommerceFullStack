package com.ecommerce.project.service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payLoad.ProductDTO;
import com.ecommerce.project.payLoad.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.path}")
    private String path;

    @Override
    public ProductDTO addProduct( ProductDTO productDTO, Long categoryId) {
        Product product = modelMapper.map(productDTO, Product.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        List<Product> products = category.getProduct();
        Boolean isProductNotPresent = true;

        for (Product value : products)
            if (value.getProductName().equals(product.getProductName())) {
                isProductNotPresent = false;
                break;
            }


        if (isProductNotPresent) {
            product.setImage("default.url");
            product.setCategory(category);

            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();

            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new ApiException("Product Alreasy Exists !!");

        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;


    }

    @Override
    public ProductResponse getByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        List<Product> products = productRepository.getByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");

        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct( ProductDTO productDTO, Long productId) {
        Product product = modelMapper.map(productDTO , Product.class);

        Product foundProduct = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product" ,"ProductTd" , productId ));

        foundProduct.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .specialPrice(product.getSpecialPrice())
                .build();

       Product savedProduct = productRepository.save(foundProduct);

        return modelMapper.map(savedProduct,ProductDTO.class);

    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product" ,"ProductTd" , productId));
        productRepository.delete(product);

        return modelMapper.map(product , ProductResponse.class);
    }

    @Override
    public ProductDTO UpdateProductImage(Long productId, MultipartFile file) throws IOException {
        Product foundProduct = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product" ,"ProductTd" , productId ));

        //FileName for the uploaded image

        //Upload the image in db
        String fileName = fileService.uploadImage(path , file);

        //save the image and set it frist
       foundProduct.setImage(fileName);

        Product updatedProduct = productRepository.save(foundProduct);

        return modelMapper.map(updatedProduct , ProductDTO.class);

    }



}
