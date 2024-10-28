package org.example.repository;

import org.example.model.Customer;
import org.example.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustomerRepository {
    private static CustomerRepository instance = null;
    private final List<Customer> list;

    private CustomerRepository() {
        list = new ArrayList<>();
    }

    public static CustomerRepository getInstance() {
        if (instance == null) {
            instance = new CustomerRepository();
        }
        return instance;
    }

    public void editCustomer(Customer element) {
        list.stream().filter(customer -> Objects.equals(customer.getPhoneNumber(), element.getPhoneNumber()))
                .findFirst()
                .ifPresent(customer -> {
                    customer.setName(element.getName());
                    customer.setEmail(element.getEmail());
                    customer.setId(element.getId());
                });
    }

    public void deleteCustomer(String phoneNumber) {
        list.removeIf(customer -> customer.getPhoneNumber().equals(phoneNumber));
    }

    public List<Customer> getList() {
        return list;
    }

    public void addCustomer(Customer element) {
        Optional<Customer> existingCustomer = list.stream()
                .filter(customer -> customer.getPhoneNumber().equals(element.getPhoneNumber()))
                .findFirst();

        if (existingCustomer.isPresent()) {
            Customer customerToUpdate = existingCustomer.get();
            customerToUpdate.setName(element.getName());
            customerToUpdate.setEmail(element.getEmail());

        } else {
            list.add(element);
        }
    }
    public boolean isCustomerIdExisted(String id) {
        return list.stream().anyMatch(element -> element.getId().equals(id));
    }

    public void addElement(Customer element) {
        list.add(element);
    }

}
