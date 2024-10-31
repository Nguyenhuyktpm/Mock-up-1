package org.example.servicetest;

import org.example.utils.CommonController;
import org.example.utils.ReadFileToAddOrder;
import org.example.utils.FileReadingUtils;
import org.example.factory.filehandilng.imp.OrderFileHandling;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private OrderService orderService;
    private OrderFileHandling mockOrderFileHandling;
    private FileReadingUtils mockReadFileToCRUD;
    private OrderRepository mockOrderRepository;
    private ProductRepository mockProductRepository;
    private CustomerRepository mockCustomerRepository;
    private ReadFileToAddOrder mockReadFileToAddOrder;
    private CommonController mockCommonController;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
        mockOrderFileHandling = mock(OrderFileHandling.class);
        mockReadFileToCRUD = mock(FileReadingUtils.class);
        mockProductRepository = mock(ProductRepository.class);
        mockCustomerRepository = mock(CustomerRepository.class);
        mockOrderRepository = mock(OrderRepository.class);
        mockReadFileToAddOrder = mock(ReadFileToAddOrder.class);
        mockCommonController = mock(CommonController.class);

        orderService.salesManager = mockOrderFileHandling;
        orderService.orderRepository = mockOrderRepository;
        orderService.productRepository = mockProductRepository;
        orderService.readFileToCRUD = mockReadFileToCRUD;
        orderService.readFileToAddOrder = mockReadFileToAddOrder;
        orderService.commonController = mockCommonController;

    }

    @Test
    public void testReadOrderFromFile() {

        doAnswer(invocation -> {
            List<Order> orders = invocation.getArgument(0);
            orders.forEach(mockOrderRepository::addElement);
            return null;
        }).when(mockOrderFileHandling).addToRepository(anyList());

        Map<String, Integer> productQuantities = new HashMap<>();
        productQuantities.put("P0001", 2);
        productQuantities.put("P0002", 1);

        List<Order> mockOrders = new ArrayList<>();
        mockOrders.add(new Order("ORD0001", "CUS0001", productQuantities, "2024-05-13T10:17:08.055107+07:07"));

        List<Product> products = new ArrayList<>();
        products.add(new Product("P0001", "Máy ảnh", 10.0, 100));
        products.add(new Product("P0002", "Máy ảnh", 10.0, 100));

        when(mockProductRepository.getList()).thenReturn(products);

        when(mockOrderFileHandling.readFile(anyString())).thenReturn(mockOrders);


        List<Order> orders = orderService.readOrderFromFile("Data");


        assertEquals(orders, mockOrders);
        verify(mockOrderRepository, times(1)).addElement(any(Order.class));
    }

    @Test
    public void testAddOrderFromFile() throws FileNotFoundException, InterruptedException {
        List<Order> mockOrders = new ArrayList<>();
        Map<String, Integer> productQuantities = new HashMap<>();
        productQuantities.put("P0001", 2);
        productQuantities.put("P0002", 1);
        mockOrders.add(new Order("ORD0001", "CUS0001", productQuantities, "2024-05-13T10:17:08.055107+07:07"));

        when(mockReadFileToAddOrder.readFile(anyString())).thenReturn(mockOrders);

        orderService.addOrderFromFile("Data");

        verify(mockOrderRepository, times(1)).addOrders(any(Order.class));
    }

    @Test
    public void testEditOrderFromFile() throws InterruptedException {
        List<Order> mockOrders = new ArrayList<>();
        Map<String, Integer> productQuantities = new HashMap<>();
        productQuantities.put("P0001", 2);
        productQuantities.put("P0002", 1);
        mockOrders.add(new Order("ORD0001", "CUS0001", productQuantities, "2024-05-13T10:17:08.055107+07:07"));

        when(mockOrderFileHandling.readFile(anyString())).thenReturn(mockOrders);

        orderService.editOrderFromFile("Data");

        verify(mockOrderRepository, times(1)).editOrder(any(Order.class));

    }

    @Test
    public void testDeleteOrderFromFile() throws FileNotFoundException, InterruptedException {

        List<String> ids = new ArrayList<>();
        ids.add("ORD0001");

        when(mockReadFileToCRUD.readOneColumn(anyString())).thenReturn(ids);

        orderService.deleteOrderFromFile("Data");

        verify(mockOrderRepository, times(1)).deleteOrder(anyString());
    }
}
