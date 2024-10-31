package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.DataCommon;
import org.example.utils.FileReadingUtils;
import org.example.enums.DataType;
import org.example.enums.FilePathEnum;
import org.example.factory.filehandilng.SaleManagerFactory;
import org.example.factory.filehandilng.SalesManager;
import org.example.factory.validation.ValidationFactory;
import org.example.factory.validation.ValidationManager;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;

import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
public class CustomerService {
    public CustomerRepository customerRepository = CustomerRepository.getInstance();
    public FileReadingUtils readFileToCRUD = new FileReadingUtils();
    DataType dataType = DataType.CUSTOMER;
    public SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);
    public ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    public static void main(String[] args) throws FileNotFoundException {
        CustomerService customerService = new CustomerService();
        customerService.readCustomerFromFile("Data");
//        customerService.editCustomerFromFile("Data");
//        customerService.deleteCustomerFromFile("Data");
        customerService.addCustomerFromFile("Data");
    }

    public List<Customer> readCustomerFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.CustomerInputPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.CustomerOutputPath.getPath();

        List<Customer> customers = (List<Customer>) salesManager.readFile(filePathInput);
        salesManager.addToRepository(customers);
        salesManager.writeFile(customerRepository.getList(), filePathOutput);


        return customers;
    }

    public void editCustomerFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.CustomerUpdatePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.CustomerOutputPath.getPath();
        DataCommon.addCustomerToRepo(folderPath);


        List<Customer> customers = (List<Customer>) salesManager.readFile(filePathInput);
        customers.forEach(customerRepository::editCustomer);
        salesManager.writeFile(customerRepository.getList(), filePathOutput);
    }

    public void deleteCustomerFromFile(String folderPath) throws FileNotFoundException {
        String filePathInput = folderPath + FilePathEnum.CustomerDeletePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.CustomerOutputPath.getPath();
        DataCommon.addCustomerToRepo(folderPath);


        try {
            List<String> phoneNumber = readFileToCRUD.readOneColumn(filePathInput);
            if (phoneNumber.isEmpty()) {
                log.error("No phone numbers found to delete.");
                return;
            }
            phoneNumber.forEach(customerRepository::deleteCustomer);
            salesManager.writeFile(customerRepository.getList(), filePathOutput);
        } catch (FileNotFoundException e) {
            log.error("File not found {}", e.getMessage());
        }
    }

    public void addCustomerFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.CustomerAddPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.CustomerOutputPath.getPath();
        DataCommon.addCustomerToRepo(folderPath);

        List<Customer> customers = (List<Customer>) salesManager.readFile(filePathInput);
        customers.forEach(customerRepository::addCustomer);

        salesManager.writeFile(customerRepository.getList(), filePathOutput);
    }
}