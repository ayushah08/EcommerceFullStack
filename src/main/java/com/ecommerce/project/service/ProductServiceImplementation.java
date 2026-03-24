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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {

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
        }
        else {
            throw new ApiException("Product Alreasy Exists !!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {


        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOS = productPage.stream().map(productDub -> modelMapper.map(productDub, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getPageable().getPageNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(Boolean.valueOf(productPage.isLast() ? "true" : "false"));
        productResponse.setTotalElements((int) productPage.getTotalElements());


        return productResponse;


    }


    @Override
    public ProductResponse getByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.getByCategoryOrderByPriceAsc(category, pageable);

        List<ProductDTO> productDTOS = productPage.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        if(productDTOS.size() > 0) {

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getPageable().getPageNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(Boolean.valueOf(productPage.isLast() ? "true" : "false"));
        productResponse.setTotalElements((int) productPage.getTotalElements());

        return productResponse;}
        else {
            throw new ApiException("Product with this category "+ category.getCategoryName() + " dosen't Exists !!");
        }
    }

    @Override
    public ProductResponse getByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%",pageable);

        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();


if(products.size() > 0) {
    ProductResponse productResponse = new ProductResponse();
    productResponse.setContent(productDTOS);
    productResponse.setPageNumber(productPage.getPageable().getPageNumber());
    productResponse.setPageSize(productPage.getSize());
    productResponse.setTotalPages(productPage.getTotalPages());
    productResponse.setLastPage(Boolean.valueOf(productPage.isLast() ? "true" : "false"));
    productResponse.setTotalElements((int) productPage.getTotalElements());
    return productResponse;

}
else {
    throw new ApiException("Product dosen't Exists with" +keyword+" !!");
}

    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product product = modelMapper.map(productDTO, Product.class);

        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductTd", productId));

        foundProduct.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .specialPrice(product.getSpecialPrice())
                .build();

        Product savedProduct = productRepository.save(foundProduct);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductTd", productId));
        productRepository.delete(product);

        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductDTO UpdateProductImage(Long productId, MultipartFile file) throws IOException {
        Product foundProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "ProductTd", productId));

        //FileName for the uploaded image

        //Upload the image in db
        String fileName = fileService.uploadImage(path, file);

        //save the image and set it frist
        foundProduct.setImage(fileName);

        Product updatedProduct = productRepository.save(foundProduct);

        return modelMapper.map(updatedProduct, ProductDTO.class);

    }


}
