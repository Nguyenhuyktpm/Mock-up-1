package org.example.DTO;

import lombok.Builder;
import lombok.Data;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;

import java.util.List;

@Builder
@Data
public class OrderValidationDTO {
    List<Order> orderList;
    Order order;
    List<Customer> customerList;
    List<Product> productList;
}
