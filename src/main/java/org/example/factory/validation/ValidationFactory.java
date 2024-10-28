package org.example.factory.validation;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.DataType;
import org.example.factory.validation.iml.CustomerValidation;
import org.example.factory.validation.iml.OrderValidation;
import org.example.factory.validation.iml.ProductValidation;

@Slf4j
public class ValidationFactory {
    public static ValidationManager getValidationManager(DataType dataType) {
        switch (dataType) {
            case ORDER -> {
                return new OrderValidation();
            }
            case CUSTOMER -> {
                return new CustomerValidation();
            }
            case PRODUCT -> {
                return new ProductValidation();
            }

            default -> {
                log.error("Unknown data type: {}", dataType);
                throw new IllegalArgumentException("Unknown data type: " + dataType);
            }
        }
    }
}
