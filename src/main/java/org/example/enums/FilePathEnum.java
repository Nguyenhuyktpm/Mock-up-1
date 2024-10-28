package org.example.enums;

public enum FilePathEnum {
    CustomerInputPath("/InputFolder/customers.origin.csv"),
    OrderInputPath("/InputFolder/orders.origin.csv"),
    ProductInputPath("/InputFolder/products.origin.csv"),
    CustomerOutputPath("/OutputFolder/customers.output.csv"),
    OrderOutputPath("/OutputFolder/orders.output.csv"),
    ProductOutputPath("/OutputFolder/products.output.csv"),
    ProductAddPath("/InputFolder/products.new.csv"),
    ProductUpdatePath("/InputFolder/products.edit.csv"),
    ProductDeletePath("/InputFolder/products.delete.csv"),
    CustomerAddPath("/InputFolder/customers.new.csv"),
    CustomerUpdatePath("/InputFolder/customers.edit.csv"),
    CustomerDeletePath("/InputFolder/customers.delete.csv"),
    OrderAddPath("/InputFolder/orders.new.csv"),
    OrderUpdatePath("/InputFolder/orders.edit.csv"),
    OrderDeletePath("/InputFolder/orders.delete.csv"),
    OrderSearchingInputPath("/InputFolder/productIds.search.csv"),
    OrderSearchingOutputPath("/SearchingFolder/orders.output.csv"),
    ;

    private final String path;

    FilePathEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}

