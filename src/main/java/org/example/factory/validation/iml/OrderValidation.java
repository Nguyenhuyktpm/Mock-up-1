package org.example.factory.validation.iml;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.OrderValidationDTO;
import org.example.enums.ColumnEnum;
import org.example.factory.validation.ValidationManager;
import org.example.model.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderValidation implements ValidationManager<OrderValidationDTO> {


    @Override
    public boolean validate(OrderValidationDTO orderValidationDTO) {
        List<Boolean> valids = new ArrayList<>();
        valids.add(!isIdExisted(orderValidationDTO));
        valids.add(isCustomerIdExisted(orderValidationDTO));
        valids.add(isOrderDateFormated(orderValidationDTO));
        valids.add(isProductQuantitiesValid(orderValidationDTO));
        return valids.stream().noneMatch(valid -> valid.equals(false));
    }


    private boolean isIdExisted(OrderValidationDTO orderValidationDTO) {

        boolean result = orderValidationDTO.getOrderList().stream()
                .anyMatch(order -> order.getId().equals(orderValidationDTO.getOrder().getId()));

        if (result) {
            log.error("Order{} is existed!",orderValidationDTO.getOrder().getId());
            return true;
        }
        return false;
    }

    private boolean isCustomerIdExisted(OrderValidationDTO orderValidationDTO) {
        boolean result = orderValidationDTO.getCustomerList().stream()
                .anyMatch(customer -> customer.getId().equals(orderValidationDTO.getOrder().getCustomerId()));
        if (!result) {
            log.error("Customer{} is not existed!",orderValidationDTO.getOrder().getCustomerId());
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

                log.error("Product quantities are not valid!");
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
            log.error("Lỗi: Định dạng thời gian của OrderDate không hợp lệ.");
            return false;
        }

        return true;
    }
}
