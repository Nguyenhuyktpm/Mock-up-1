package org.example.DTO;

import lombok.Builder;
import lombok.Data;
import org.example.model.Product;

import java.util.List;

@Data
@Builder
public class ProductValidationDTO {
    List<Product> products;
    Product product;
}
