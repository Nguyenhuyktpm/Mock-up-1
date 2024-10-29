package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.common.CommonController;
import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.example.service.ProductService;

import java.io.FileNotFoundException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        CommonController commonController = new CommonController();

        ProductService productService = new ProductService();
        CustomerService customerService = new CustomerService();
        OrderService orderService = new OrderService();

        if (args.length != 2) {
            System.out.println("Usage: java -jar console-manager.jar <function_code> <folder_path>");
            System.exit(1);
        }

        String functionCode = args[0];
        String folderPath = args[1];

        switch (functionCode) {
            case "1":
                commonController.run(folderPath);
                break;
            case "2.1":

                productService.addProductFormFile(folderPath);

                break;
            case "2.2":
                productService.editProductFromFile(folderPath);
                break;
            case "2.3":
                try {
                    productService.deleteProductFromFile(folderPath);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
                break;
            case "3.1":
                try {
                    customerService.deleteCustomerFromFile(folderPath);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
                break;
            case "3.2":

                customerService.addCustomerFromFile(folderPath);
                break;
            case "3.3":

                customerService.editCustomerFromFile(folderPath);
                break;
            case "4.1":

                try {
                    orderService.addOrderFromFile(folderPath);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
                break;
            case "4.2":
                orderService.editOrderFromFile(folderPath);
                break;
            case "4.3":

                try {
                    orderService.deleteOrderFromFile(folderPath);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
                break;
            case "5.1":

                productService.findTop3HighestOrder(folderPath);
                break;
            case "5.2":
                try {
                    orderService.findOrdersByProductId(folderPath);
                } catch (FileNotFoundException e) {
                    log.error(e.getMessage());
                }
                break;
            default:
                System.out.println("Unknown function code: " + functionCode);
        }
        System.out.println("Program completed !");
        System.out.println("Please check the error file for more information.");
    }
}
