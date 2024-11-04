package org.example.factory.validation.iml;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.OrderValidationDTO;
import org.example.factory.validation.ValidationManager;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Slf4j
public class OrderValidation implements ValidationManager<OrderValidationDTO> {


    @Override
    public boolean validate(OrderValidationDTO orderValidationDTO) {
        return !isIdExisted(orderValidationDTO) &&
                isCustomerIdExisted(orderValidationDTO) &&
                isOrderDateFormated(orderValidationDTO) &&
                isProductQuantitiesValid(orderValidationDTO);
    }


    private boolean isIdExisted(OrderValidationDTO orderValidationDTO) {

        boolean result = orderValidationDTO.getOrderList().stream()
                .anyMatch(order -> order.getId().equals(orderValidationDTO.getOrder().getId()));

        if (result) {
            log.error("Order{} is existed!", orderValidationDTO.getOrder().getId());
            return true;
        }
        return false;
    }

    private boolean isCustomerIdExisted(OrderValidationDTO orderValidationDTO) {
        boolean result = orderValidationDTO.getCustomerList().stream()
                .anyMatch(customer -> customer.getId().equals(orderValidationDTO.getOrder().getCustomerId()));
        if (!result) {
            log.error("Customer{} is not existed!", orderValidationDTO.getOrder().getCustomerId());
            return false;
        }
        return true;
    }

    private boolean isProductQuantitiesValid(OrderValidationDTO orderValidationDTO) {
        Map<String, Integer> productQuantities = orderValidationDTO.getOrder().getProductQuantities();

        for (String key : productQuantities.keySet()) {

            boolean productIdExisted = orderValidationDTO.getProductList().stream()
                    .anyMatch(product -> product.getId().equals(key));
            if (!productIdExisted && productQuantities.get(key) != null && productQuantities.get(key) > 0) {

                log.error("Product quantities are invalid for Order ID: {}", orderValidationDTO.getOrder().getId());
                return false;
            }
        }
        return true;
    }

    private boolean isOrderDateFormated(OrderValidationDTO orderValidationDTO) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
            OffsetDateTime.parse(orderValidationDTO.getOrder().getOrderDate(), formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format for OrderDate in Order ID: {}", orderValidationDTO.getOrder().getId());
            return false;
        }
        return true;
    }
}
