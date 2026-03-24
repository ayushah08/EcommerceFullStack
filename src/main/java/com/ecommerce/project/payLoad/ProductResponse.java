package com.ecommerce.project.payLoad;

import com.ecommerce.project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private List<ProductDTO> content;
    private  int totalElements;
    private  int totalPages;
    private Boolean lastPage;
    private int pageNumber;
    private int pageSize;


}
