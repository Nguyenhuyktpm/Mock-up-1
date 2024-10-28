package org.example.repository;

import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static ProductRepository instance = null;
    private final List<Product> list;

    private ProductRepository() {
        list = new ArrayList<>();
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public List<Product> getList() {
        return list;
    }

    public void addElement(Product element) {

        list.add(element);
    }

    public Product getById(String id) {
        return list.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }


    public void editProduct(Product element) {
        list.stream()
                .filter(product -> product.getId().equals(element.getId()))
                .findFirst()
                .ifPresent(product -> {
                    product.setName(element.getName());
                    product.setPrice(element.getPrice());
                    product.setStockAvailable(element.getStockAvailable());
                });
    }

    public Product findById(String id) {
        return list.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);  // Trả về null nếu không tìm thấy sản phẩm
    }

    public void removeElement(String id) {
        Product product = this.getById(id);
        if (product != null) {
            list.remove(product);
        }
    }

    public boolean isProductIdExist(String id) {
        return list.stream().anyMatch(product -> product.getId().equals(id));
    }

}
