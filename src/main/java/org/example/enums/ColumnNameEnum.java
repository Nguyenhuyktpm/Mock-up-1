package org.example.enums;

public enum ColumnNameEnum {
    ProductNameEnum("Id,Name,Price,StockAvailable"),
    OrderNameEnum("Id,CustomerId,ProductQuantities,OrderDate,TotalAmount"),
    CustomerNameEnum("Id,Name,Email,PhoneNumber"),
    ;

    private final String ColumnName;

    ColumnNameEnum(String name) {
        this.ColumnName = name;
    }
    public String getColumnName() {
        return ColumnName;
    }
}
