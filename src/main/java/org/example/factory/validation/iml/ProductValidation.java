package org.example.factory.validation.iml;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.ProductValidationDTO;
import org.example.factory.validation.ValidationManager;

@Slf4j
public class ProductValidation implements ValidationManager<ProductValidationDTO> {

    private boolean isIdExisted(ProductValidationDTO productDTO) {
        boolean result = productDTO.getProducts().stream().
                anyMatch(product -> product.getId().equals(productDTO.getProduct().getId()));
        if (result) {
            log.error("Product with id {} already exists", productDTO.getProduct().getId());
        }
        return false;
    }

    private boolean isNameEmpty(ProductValidationDTO productDTO) {
        String name = productDTO.getProduct().getName();
        boolean result = name == null || name.trim().isEmpty();
        if (result) {
            log.error("Product with id: {} empty name",productDTO.getProduct().getId());
            return true;
        }
        return false;
    }

    private boolean isPriceValid(ProductValidationDTO productDTO) {
        try {
            if (productDTO.getProduct().getPrice() < 0) {
                log.error("Error: Price for product with ID '{}' must be greater than or equal to 0.", productDTO.getProduct().getId());
                return false;
            }
        } catch (NumberFormatException e) {
            log.error("Error: Invalid Price value for product with ID '{}'. Must be a numeric value.", productDTO.getProduct().getId());
            return false;
        }
        return true;
    }

    private boolean isStockValid(ProductValidationDTO productDTO) {
        if (productDTO.getProduct().getStockAvailable() < 0) {
            log.error("Error: StockAvailable for product with ID '{}' must be a non-negative integer.", productDTO.getProduct().getId());

            return false;
        }
        return true;
    }

    @Override
    public boolean validate(ProductValidationDTO product) {
        return !isIdExisted(product) &&
                isPriceValid(product) &&
                !isNameEmpty(product) &&
                isStockValid(product);
    }
}
