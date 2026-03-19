package com.ecommerce.project.payLoad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApiResponse {
    public String message ;
    public Boolean status;
}
