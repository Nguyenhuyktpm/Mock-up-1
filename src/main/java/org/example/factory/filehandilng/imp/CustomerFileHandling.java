package org.example.factory.filehandilng.imp;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.CustomerValidateDTO;
import org.example.enums.ColumnEnum;
import org.example.enums.ColumnNameEnum;
import org.example.enums.DataType;
import org.example.factory.filehandilng.SalesManager;
import org.example.factory.validation.ValidationFactory;
import org.example.factory.validation.ValidationManager;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerFileHandling implements SalesManager {
    DataType dataType = DataType.CUSTOMER;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Customer> readFile(String filePath) {
        List<Customer> customers = new ArrayList<>();


        try {
            List<String> lines = Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
            lines.stream().skip(1).forEach(line -> {
                if (!line.trim().isEmpty()) {
                    String[] values = line.split(",");
                    if (values.length >= 4) {
                        try {
                            String id = values[ColumnEnum.Column1.getCode()].trim();
                            String name = values[ColumnEnum.Column2.getCode()].trim();
                            String email = values[ColumnEnum.Column3.getCode()].trim();
                            String phoneNumber = values[ColumnEnum.Column4.getCode()].trim();

                            Customer customer = Customer.builder()
                                    .id(id)
                                    .name(name)
                                    .email(email)
                                    .phoneNumber(phoneNumber)
                                    .build();

                            if (validationManager.validate(CustomerValidateDTO.builder()
                                    .customer(customer)
                                    .customers(customers)
                                    .build())) {
                                customers.add(customer);
                            }
                        } catch (Exception e) {
                            log.error("Invalid data format in line: {}", line, e);
                        }
                    } else {
                        log.error("Invalid data format, missing columns in line: {}", line);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
        }
        return customers;
    }

    @Override
    public <T> void addToRepository(List<T> elements) {
        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Customer) {
            List<Customer> customers = (List<Customer>) elements;
            CustomerRepository customerRepository = CustomerRepository.getInstance();
            customers.forEach(customerRepository::addElement);
        } else
            log.error("Add Customer to repository failed");
    }

    @Override
    public <T> void writeFile(List<T> elements, String filePath) {
        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Customer) {
            List<Customer> customers = (List<Customer>) elements;
            StringBuilder content = new StringBuilder(ColumnNameEnum.CustomerNameEnum.getColumnName()).append("\n");

            customers.forEach(customer -> {


                content.append(customer.getId()).append(",")
                        .append(customer.getName()).append(",")
                        .append(customer.getEmail()).append(",")
                        .append(customer.getPhoneNumber()).append("\n");
            });
            try {
                Files.writeString(Path.of(filePath),
                        content.toString(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                log.error("Error writing Customer to file: {}", e.getMessage());
            }
        } else {
            log.error("Write Customer to file failed");
        }
    }
}
