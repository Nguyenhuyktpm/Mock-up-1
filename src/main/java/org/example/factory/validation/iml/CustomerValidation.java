package org.example.factory.validation.iml;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.CustomerValidateDTO;
import org.example.enums.ColumnEnum;
import org.example.factory.validation.ValidationManager;
import org.example.model.Customer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j

public class CustomerValidation implements ValidationManager<CustomerValidateDTO> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,7}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,11}$");

    private static boolean isIdExist(CustomerValidateDTO customerValidateDTO) {
        boolean result = customerValidateDTO.getCustomers().stream()
                .anyMatch(customer -> customer.getId().equals(customerValidateDTO.getCustomer().getId()));

        if (result) {
            log.error("Customer with id {} already exists", customerValidateDTO.getCustomer().getId());
            return true;
        }
        return false;
    }

    private static boolean isEmailExist(CustomerValidateDTO customerValidateDTO) {
        boolean result = customerValidateDTO.getCustomers().stream()
                .anyMatch(customer -> customer.getEmail().equals(customerValidateDTO.getCustomer().getEmail()));
        if (result) {
            log.error("Customer with email {} already exists", customerValidateDTO.getCustomer().getEmail());
            return true;
        }
        return false;
    }

    private static boolean isEmailFormat(Customer customer) {

        if (!EMAIL_PATTERN.matcher(customer.getEmail()).matches()) {
            log.error("Customer with email {} is not a valid email", customer.getEmail());
            return false;

        }
        return true;
    }

    private static boolean isPhoneExist(CustomerValidateDTO customerValidateDTO) {
        boolean result = customerValidateDTO.getCustomers().stream()
                .anyMatch(customer -> customer.getPhoneNumber().equals(customerValidateDTO.getCustomer().getPhoneNumber()));
        if (result) {
            log.error("Customer with phone {} already exists", customerValidateDTO.getCustomer().getPhoneNumber());
            return true;
        }
        return false;
    }

    private static boolean isPhoneFormat(Customer customer) {
        if (!PHONE_PATTERN.matcher(customer.getPhoneNumber()).matches()) {
            log.error("Customer with phone {} is not a valid phone number", customer.getPhoneNumber());
            return false;
        }
        return true;
    }

    private static boolean isNameEmpty(Customer customer) {


        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            log.error("Customer with name {} is empty", customer.getName());
            return true;
        }
        return false;
    }

    @Override
    public <T2> boolean isElementInFile(T2 element, String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Customer customer = (Customer) element;

        boolean result = lines.stream()
                .anyMatch(line -> {
                    String[] parts = line.split(",");
                    return parts[ColumnEnum.Column1.getCode()].equals(customer.getId());
                });
        if (result) {
            log.error("Customer with id {} exists in output file", customer.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean validate(CustomerValidateDTO customerValidateDTO) {
        return !isIdExist(customerValidateDTO) &&
                !isEmailExist(customerValidateDTO) &&
                !isPhoneExist(customerValidateDTO) &&
                !isNameEmpty(customerValidateDTO.getCustomer()) &&
                isPhoneFormat(customerValidateDTO.getCustomer()) &&
                isEmailFormat(customerValidateDTO.getCustomer());
    }


}
