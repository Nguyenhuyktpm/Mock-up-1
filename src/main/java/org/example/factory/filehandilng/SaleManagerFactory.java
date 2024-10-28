package org.example.factory.filehandilng;

import org.example.enums.DataType;
import org.example.factory.filehandilng.imp.CustomerFileHandling;
import org.example.factory.filehandilng.imp.OrderFileHandling;
import org.example.factory.filehandilng.imp.ProductFileHandling;

public class SaleManagerFactory {
    public static SalesManager getSalesManager(DataType dataType) {
        switch (dataType){
            case ORDER -> {
                return new OrderFileHandling();
            }
            case CUSTOMER -> {
                return new CustomerFileHandling();
            }
            case PRODUCT -> {
                return new ProductFileHandling();
            }

            default ->  throw new IllegalArgumentException("Unknown data type: " + dataType);
        }
    }
}