package org.example.common;

import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.example.service.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonController {
    static ProductService productService = new ProductService();
    static CustomerService customerService = new CustomerService();
    static OrderService orderService = new OrderService();

    public  void run(String folderPath) throws InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Callable<Void>> tasks = Arrays.asList(
                () -> { productService.readProductFromFile(folderPath); return null; },
                () -> { customerService.readCustomerFromFile(folderPath); return null; }
        );

        executor.invokeAll(tasks);

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        orderService.readOrderFromFile(folderPath);
    }
}
