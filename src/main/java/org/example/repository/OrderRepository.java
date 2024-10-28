package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class OrderRepository {
    private static OrderRepository instance = null;
    private final List<Order> list;

    private OrderRepository() {
        list = new ArrayList<>();
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public List<Order> getList() {
        return list;
    }

    public void addElement(Order element) {
        list.add(element);
    }

    public void updateTotalAmount(Double totalAmount) {

    }

    public boolean isCustomerIdExisted(String customerId) {
        CustomerRepository customerRepository = CustomerRepository.getInstance();
        return customerRepository.getList().stream().anyMatch(customer -> customer.getId().equals(customerId));
    }

    public boolean isProductIdExisted(String productId) {
        ProductRepository productRepository = ProductRepository.getInstance();
        return productRepository.getList().stream().anyMatch(product -> product.getId().equals(productId));
    }

    public boolean isProductIdExisted(Set<String> productId) {
        ProductRepository productRepository = ProductRepository.getInstance();
        return productId.stream().allMatch(this::isProductIdExisted);
    }

    public void addOrders(Order order) {

        String lastId = list.get(list.size() - 1).getId();

        int numberInId = Integer.parseInt(lastId.substring(3));
        if (this.isProductIdExisted(order.getProductQuantities().keySet())
                && this.isCustomerIdExisted(order.getCustomerId())) {
            String orderId = String.format("ORD%07d", numberInId + 1);
            order.setId(orderId);
            this.addElement(order);
        } else
            log.error("Validate productId and CustomerId failed!");
    }


    public void editOrder(Order updateOrder) {
        list.stream().filter(order -> order.getId().equals(updateOrder.getId()))
                .findFirst()
                .ifPresent(order -> {
                    if (updateOrder.getCustomerId() != null) {
                        order.setCustomerId(updateOrder.getCustomerId());
                    }
                    order.setProductQuantities(updateOrder.getProductQuantities());
                    if (updateOrder.getOrderDate() != null) {
                        order.setOrderDate(updateOrder.getOrderDate());
                    }
                    order.setId(updateOrder.getId());
                    order.calculateTotalAmount(ProductRepository.getInstance());
                });
    }

    public void deleteOrder(String orderId) {
        list.removeIf(order -> order.getId().equals(orderId));
    }

    public List<Order> findByProductId(String productId) {
        return list.stream().filter(order -> order.getProductQuantities().containsKey(productId))
                .toList();
    }

}
