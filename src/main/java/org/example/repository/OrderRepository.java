package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Order;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OrderRepository {
    private static OrderRepository instance = null;
    private final Map<String, Order> orders;

    private OrderRepository() {
        orders = new LinkedHashMap<>();
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public List<Order> getList() {
        return List.copyOf(orders.values());
    }

    public void addElement(Order order) {
        orders.put(order.getId(), order);
    }

    public void updateTotalAmount(Double totalAmount) {
        // Implement logic if needed
    }

    public boolean isCustomerIdExisted(String customerId) {
        return CustomerRepository.getInstance().getList().stream()
                .anyMatch(customer -> customer.getId().equals(customerId));
    }

    public boolean isProductIdExisted(String productId) {
        return ProductRepository.getInstance().getList().stream()
                .anyMatch(product -> product.getId().equals(productId));
    }

    public boolean isProductIdExisted(Set<String> productIds) {
        return productIds.stream().allMatch(this::isProductIdExisted);
    }

    public void addOrders(Order order) {
        String lastId = orders.isEmpty() ? "ORD0000000" : orders.keySet().stream()
                .max(String::compareTo).orElse("ORD0000000");

        int numberInId = Integer.parseInt(lastId.substring(3));

        if (this.isProductIdExisted(order.getProductQuantities().keySet())
                && this.isCustomerIdExisted(order.getCustomerId())) {
            String orderId = String.format("ORD%07d", numberInId + 1);
            order.setId(orderId);
            this.addElement(order);
        } else {
            log.error("Validate productId and customerId failed!");
        }
    }

    public void editOrder(Order updateOrder) {
        Order existingOrder = orders.get(updateOrder.getId());

        if (existingOrder != null) {
            if (updateOrder.getCustomerId() != null) {
                existingOrder.setCustomerId(updateOrder.getCustomerId());
            }
            existingOrder.setProductQuantities(updateOrder.getProductQuantities());
            if (updateOrder.getOrderDate() != null) {
                existingOrder.setOrderDate(updateOrder.getOrderDate());
            }
            existingOrder.calculateTotalAmount(ProductRepository.getInstance());
        }
    }

    public void deleteOrder(String orderId) {
        orders.remove(orderId);
    }

    public List<Order> findByProductId(String productId) {
        return orders.values().stream()
                .filter(order -> order.getProductQuantities().containsKey(productId))
                .collect(Collectors.toList());
    }
}