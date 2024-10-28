package org.example.servicetest;

import org.example.common.ReadFileToCRUD;
import org.example.factory.filehandilng.imp.CustomerFileHandling;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    private CustomerService customerService;
    private ReadFileToCRUD mockReadFileToCRUD;
    private CustomerRepository mockCustomerRepository;
    private CustomerFileHandling mockCustomerFileHandling;

    @BeforeEach
    public void setUp() {
        customerService = new CustomerService();
        mockCustomerFileHandling = mock(CustomerFileHandling.class);
        mockReadFileToCRUD = mock(ReadFileToCRUD.class);
        mockCustomerRepository = mock(CustomerRepository.class);

        customerService.customerRepository = mockCustomerRepository;
        customerService.readFileToCRUD = mockReadFileToCRUD;
        customerService.salesManager = mockCustomerFileHandling;
    }

    @Test
    public void testReadCustomerFromFile(){
        List<Customer> mockCustomers = new ArrayList<>();
        mockCustomers.add(new Customer("CUS0001","Nguyễn Đức Hoàng","9gmufwa3@gmail.com","03836429663"));

        when(mockCustomerFileHandling.readFile(anyString())).thenReturn(mockCustomers);


    }
}
