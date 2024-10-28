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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderFileHandling implements SalesManager {
    DataType dataType = DataType.ORDER;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Order> readFile(String filePath) {

        List<Order> orders = new ArrayList<>();
        ProductRepository productRepository = ProductRepository.getInstance();
        CustomerRepository customerRepository = CustomerRepository.getInstance();


        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
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
                    String[] pairs = String.valueOf(values[ColumnEnum.Column3.getCode()].trim()).split(";");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if (keyValue.length == 2) {
                            String productId = keyValue[0].trim();
                            int quantity;
                            try {
                                quantity = Integer.parseInt(keyValue[1].trim());
                            } catch (NumberFormatException e) {
                                log.error("Invalid number format in quantity: {}", keyValue[1], e);
                                continue;
                            }
                            productQuantityMap.put(productId, quantity);
                        }
                    }
                    String date = String.valueOf(values[ColumnEnum.Column4.getCode()].trim());
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
                            .build()))
                        orders.add(order);
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


        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Order) {
            List<Order> orders = (List<Order>) elements;

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

                bw.write(ColumnNameEnum.OrderNameEnum.getColumnName());
                bw.newLine();

                for (Order order : orders) {
                    if (!validationManager.isElementInFile(order, filePath)) {
                        StringBuilder result = new StringBuilder();
                        for (Map.Entry<String, Integer> entry : order.getProductQuantities().entrySet()) {
                            result.append(entry.getKey())
                                    .append(":")
                                    .append(entry.getValue())
                                    .append(";");
                        }
                        if (!result.isEmpty()) result.deleteCharAt(result.length() - 1);
                        String line = order.getId() + "," + order.getCustomerId() + "," + result + "," + order.getOrderDate() + "," + order.getTotalAmount();

                        bw.write(line);
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
               log.error("Error while writing Order file: {}", filePath, e);
            }
        }
    }
}
