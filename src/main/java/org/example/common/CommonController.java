package org.example.common;

import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.example.service.ProductService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonController {
    static ProductService productService = new ProductService();
    static CustomerService customerService = new CustomerService();
    static OrderService orderService = new OrderService();

    public  void run(String folderPath) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable productTask = () -> productService.readProductFromFile(folderPath);
        Runnable customerTask = () -> customerService.readCustomerFromFile(folderPath);
        executor.submit(productTask);
        executor.submit(customerTask);

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        orderService.readOrderFromFile(folderPath);
    }
}
