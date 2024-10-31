package org.example.repository;

import org.example.model.Product;

import java.util.*;

public class ProductRepository {
    private static ProductRepository instance = null;
    private final Map<String, Product> productMap;

    private ProductRepository() {
        productMap = new LinkedHashMap<>();
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public List<Product> getList() {
        return List.copyOf(productMap.values());
    }

    public void addElement(Product product) {
        productMap.put(product.getId(), product);
    }

    public Product getById(String id) {
        return productMap.get(id);
    }

    public void editProduct(Product product) {
        if (productMap.containsKey(product.getId())) {
            Product existingProduct = productMap.get(product.getId());
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStockAvailable(product.getStockAvailable());
        }
    }

    public Product findById(String id) {
        return productMap.get(id);
    }

    public void removeElement(String id) {
        productMap.remove(id);
    }

    public boolean isProductIdExist(String id) {
        return productMap.containsKey(id);
    }
}