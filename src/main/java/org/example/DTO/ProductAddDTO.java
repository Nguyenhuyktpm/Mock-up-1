package org.example.DTO;

import java.time.LocalDate;
import java.util.List;

public class ProductAddDTO {
    String customerId;
    List<String> productIds;
    String productName;
    int quantity;
    String orderDate;
}
