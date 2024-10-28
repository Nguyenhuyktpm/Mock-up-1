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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerFileHandling implements SalesManager {
    DataType dataType = DataType.CUSTOMER;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Customer> readFile(String filePath) {
        List<Customer> customers = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 4) {
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

                            .build()))
                        customers.add(customer);
                    else
                        log.error("Invalid data format, missing columns in line: {}", line);
                }

            }

        } catch (IOException e) {
            log.error(e.getMessage());
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


            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

                bw.write(ColumnNameEnum.CustomerNameEnum.getColumnName());
                bw.newLine();

                for (Customer customer : customers) {
                    if (!validationManager.isElementInFile(customer, filePath)) {
                        String line = customer.getId() + ","
                                + customer.getName() + ","
                                + customer.getEmail() + ","
                                + customer.getPhoneNumber();
                        bw.write(line);
                        bw.newLine();
                    }
                }
            } catch (IOException e) {
                log.error("Error writing Customer to file: {}", e.getMessage());
            }
        } else
            log.error("Write Customer to file failed");
    }
}
