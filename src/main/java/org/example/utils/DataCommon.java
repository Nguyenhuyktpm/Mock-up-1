package org.example.utils;

import org.example.enums.DataType;
import org.example.enums.FilePathEnum;
import org.example.factory.filehandilng.SaleManagerFactory;
import org.example.factory.filehandilng.SalesManager;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.Product;

import java.util.List;

public class DataCommon {

    public static void addProductToRepo(String folderPath) {
        DataType dataType = DataType.PRODUCT;
        SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);

        String filePathOriginal = folderPath + FilePathEnum.ProductInputPath.getPath();
        List<Product> productsOriginal = (List<Product>) salesManager.readFile(filePathOriginal);
        salesManager.addToRepository(productsOriginal);
    }

    public static void addCustomerToRepo(String folderPath) {
        DataType dataType = DataType.CUSTOMER;
        SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);

        String filePathOriginal = folderPath + FilePathEnum.CustomerInputPath.getPath();
        List<Customer> customersOriginal = (List<Customer>) salesManager.readFile(filePathOriginal);
        salesManager.addToRepository(customersOriginal);
    }

    public static void addOrderToRepo(String folderPath) {
        DataType dataType = DataType.ORDER;
        SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);

        String filePathOriginal = folderPath + FilePathEnum.OrderInputPath.getPath();
        List<Order> ordersOriginal = (List<Order>) salesManager.readFile(filePathOriginal);

        salesManager.addToRepository(ordersOriginal);

    }

    public static void main(String[] args) {
        DataCommon.addOrderToRepo("Data");
    }

}
