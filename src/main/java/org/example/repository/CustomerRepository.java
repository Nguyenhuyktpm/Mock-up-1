package org.example.repository;

import org.example.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository {
    private static CustomerRepository instance = null;
    private final Map<String, Customer> list;

    private CustomerRepository() {
        list = new HashMap<>();
    }

    public static CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    public void editCustomer(Customer element) {
        Customer customer = list.get(element.getPhoneNumber());
        if (customer != null) {
            customer.setName(element.getName());
            customer.setEmail(element.getEmail());
            customer.setId(element.getId());
        }
    }

    public void deleteCustomer(String phoneNumber) {
        list.remove(phoneNumber);
    }

    public List<Customer> getList() {
        return new ArrayList<>(list.values());
    }

    public void addCustomer(Customer element) {
        list.put(element.getPhoneNumber(), element);
    }

    public boolean isCustomerIdExisted(String id) {
        return list.values().stream().anyMatch(element -> element.getId().equals(id));
    }

    public void addElement(Customer element) {
        list.put(element.getPhoneNumber(), element);
    }
}
