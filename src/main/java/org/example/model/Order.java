package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.ProductRepository;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class Order {
    String id;
    String customerId;
    Map<String, Integer> productQuantities;
    String orderDate;
    Double totalAmount;

    public Order(String id, String cusId, Map<String, Integer> productQuantities, String time) {
        this.id = id;
        this.customerId = cusId;
        this.productQuantities = productQuantities;
        this.orderDate = time;


    }

    public void calculateTotalAmount(ProductRepository productRepository) {

        totalAmount = productQuantities.entrySet().stream()
                .mapToDouble(entry -> {
                    String productId = entry.getKey();
                    Integer quantity = entry.getValue();

                    Product product = productRepository.findById(productId);


                    if (product != null) {
                        return product.getPrice() * quantity;
                    } else {
                        log.error("Product với id {} không tồn tại.", productId);
                        return 0.0;
                    }
                })
                .sum();

    }
}
