package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.ColumnEnum;
import org.example.model.Order;
import org.example.repository.OrderRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Slf4j
public class ReadFileToAddOrder {
    public List<Order> readFile(String filePath) throws FileNotFoundException {
        List<Order> list = new ArrayList<>();
        Random random = new Random();
        OrderRepository orderRepository = OrderRepository.getInstance();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            boolean isFirstLine = true;
            String line;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                Map<String, Integer> productQuantityMap = new HashMap<>();
                String[] values = line.split(",");
                String customerId = values[ColumnEnum.Column1.getCode()].trim();
                String[] pairs = String.valueOf(values[ColumnEnum.Column2.getCode()].trim()).split(";");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        String productId = keyValue[0].trim();
                        int quantity;
                        try {
                            quantity = Integer.parseInt(keyValue[1].trim());
                        } catch (NumberFormatException e) {
                            log.error("Lỗi định dạng số ở quantity: {}", keyValue[1]);
                            continue;
                        }
                        productQuantityMap.put(productId, quantity);
                    }
                }
                String date = values[ColumnEnum.Column3.getCode()].trim();
                Order order = Order.builder()
                        .customerId(customerId)
                        .productQuantities(productQuantityMap)
                        .orderDate(date)
                        .build();
                list.add(order);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
