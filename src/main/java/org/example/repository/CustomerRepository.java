package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Customer;
import org.example.utils.EmailUtils;
import org.example.utils.PhoneNumberUtils;

import java.util.*;

@Slf4j
public class CustomerRepository {
    private static CustomerRepository instance = null;
    private final Map<String, Customer> list;

    private CustomerRepository() {
        list = new TreeMap<>();
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
        List<String> emails, phoneNumbers;
        List<Customer> customers = getList();
        emails = customers.stream()
                .map(Customer::getPhoneNumber)
                .toList();
        phoneNumbers = customers.stream()
                .map(Customer::getEmail)
                .toList();
        if (PhoneNumberUtils.phoneExisted(phoneNumbers, element.getPhoneNumber())
                && EmailUtils.emailExists(emails, element.getEmail())) {
            list.put(element.getPhoneNumber(), element);
        }
        else
            log.error("Customer {} has an existing phone number or email.", element.getId());

    }

    public boolean isCustomerIdExisted(String id) {
        return list.values().stream().anyMatch(element -> element.getId().equals(id));
    }

    public void addElement(Customer element) {
        list.put(element.getPhoneNumber(), element);
    }
}