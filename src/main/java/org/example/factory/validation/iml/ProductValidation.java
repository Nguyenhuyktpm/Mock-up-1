package org.example.factory.validation.iml;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.ProductValidationDTO;
import org.example.enums.ColumnEnum;
import org.example.factory.validation.ValidationManager;
import org.example.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
            log.error("Product empty name");
            return true;
        }
        return false;
    }

    private boolean isPriceValid(ProductValidationDTO productDTO) {
        try {
            if (productDTO.getProduct().getPrice() < 0) {
                log.error("Lỗi: Price phải lớn hơn hoặc bằng 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            log.error("Lỗi: Giá trị Price không hợp lệ. Phải là số.");
            return false;
        }
        return true;
    }

    private boolean isStockValid(ProductValidationDTO productDTO) {
        if (productDTO.getProduct().getStockAvailable() < 0) {
            log.error("Lỗi: StockAvailable phải là số nguyên lớn hơn hoặc bằng 0.");
            return false;
        }
        return true;
    }



    @Override
    public boolean validate(ProductValidationDTO product) {
        List<Boolean> valids = new ArrayList<>();
        valids.add(!isIdExisted(product));
        valids.add(isPriceValid(product));
        valids.add(!isNameEmpty(product));
        valids.add(isStockValid(product));
        return valids.stream().noneMatch(valid -> valid.equals(false));
    }
}
