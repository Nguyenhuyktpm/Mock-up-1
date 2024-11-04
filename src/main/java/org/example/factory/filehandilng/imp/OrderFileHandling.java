package org.example.factory.filehandilng.imp;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.OrderValidationDTO;
import org.example.enums.ColumnEnum;
import org.example.enums.ColumnNameEnum;
import org.example.enums.DataType;
import org.example.factory.filehandilng.SalesManager;
import org.example.factory.validation.ValidationFactory;
import org.example.factory.validation.ValidationManager;
import org.example.model.Order;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Slf4j
public class OrderFileHandling implements SalesManager {
    DataType dataType = DataType.ORDER;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Order> readFile(String filePath) {

        List<Order> orders = new ArrayList<>();
        ProductRepository productRepository = ProductRepository.getInstance();
        CustomerRepository customerRepository = CustomerRepository.getInstance();


        try {
            List<String> lines = Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
            boolean isFirstLine = true;

            for (String line : lines) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 4) {
                    Map<String, Integer> productQuantityMap = new HashMap<>();
                    String id = values[ColumnEnum.Column1.getCode()].trim();
                    String customerId = values[ColumnEnum.Column2.getCode()].trim();
                    String[] pairs = values[ColumnEnum.Column3.getCode()].trim().split(";");

                    boolean isProductIdExisted = Arrays.stream(pairs)
                            .map(pair -> pair.split(":"))
                            .filter(keyValue -> keyValue.length == 2)
                            .anyMatch(keyValue -> {
                                String productId = keyValue[0].trim();
                                int quantity;


                                try {
                                    quantity = Integer.parseInt(keyValue[1].trim());
                                } catch (NumberFormatException e) {
                                    log.error("Order with id {}: Invalid number format in quantity for productId {}: {}"
                                            ,id, productId, keyValue[1], e);
                                    return true;
                                }
                                if (productQuantityMap.putIfAbsent(productId, quantity) != null) {
                                    log.error("Order with id {} :Duplicate product id: {}",id, productId);
                                    return true;
                                }
                                return false;
                            });

                    if (isProductIdExisted) continue;

                    String date = values[ColumnEnum.Column4.getCode()].trim();
                    Order order = Order.builder()
                            .id(id)
                            .customerId(customerId)
                            .productQuantities(productQuantityMap)
                            .orderDate(date)
                            .build();

                    if (validationManager.validate(OrderValidationDTO.builder()
                            .orderList(orders)
                            .order(order)
                            .customerList(customerRepository.getList())
                            .productList(productRepository.getList())
                            .build())) {
                        orders.add(order);
                    }
                } else {
                    log.error("Invalid data format in line: {}", line);
                }
            }
        } catch (IOException e) {
            log.error("Error while reading file: {}", filePath, e);
        }
        return orders;
    }

    @Override
    public <T> void addToRepository(List<T> elements) {
        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Order) {
            List<Order> orders = (List<Order>) elements;
            OrderRepository orderRepository = OrderRepository.getInstance();
            orders.forEach(orderRepository::addElement);
        }
    }

    @Override
    public <T> void writeFile(List<T> elements, String filePath) {

        if (elements == null || elements.isEmpty() || !(elements.get(0) instanceof Order)) return;

        List<Order> orders = (List<Order>) elements;

        StringBuilder content = new StringBuilder(ColumnNameEnum.OrderNameEnum.getColumnName()).append("\n");

        orders.forEach(order -> {
            StringBuilder result = new StringBuilder();
            order.getProductQuantities().forEach((productId, quantity) ->
                    result.append(productId).append(":").append(quantity).append(";"));
            if (result.length() > 0) result.setLength(result.length() - 1); // Remove last ";"

            content.append(order.getId()).append(",")
                    .append(order.getCustomerId()).append(",")
                    .append(result).append(",")
                    .append(order.getOrderDate()).append(",")
                    .append(order.getTotalAmount()).append("\n");
        });

        try {
            Files.writeString(Path.of(filePath),
                    content.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error("Error while writing Order file: {}", filePath, e);
        }
    }
}

