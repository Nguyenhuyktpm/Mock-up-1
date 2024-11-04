package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.CommonController;
import org.example.utils.ReadFileToAddOrder;
import org.example.utils.FileReadingUtils;
import org.example.enums.DataType;
import org.example.enums.FilePathEnum;
import org.example.factory.filehandilng.SaleManagerFactory;
import org.example.factory.filehandilng.SalesManager;
import org.example.model.Order;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class OrderService {
    public OrderRepository orderRepository = OrderRepository.getInstance();
    public ProductRepository productRepository = ProductRepository.getInstance();
    public FileReadingUtils readFileToCRUD = new FileReadingUtils();
    public ReadFileToAddOrder readFileToAddOrder = new ReadFileToAddOrder();
    public CommonController commonController = new CommonController();
    public CommonController controller = new CommonController();
    DataType dataType = DataType.ORDER;
    public SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);

    public List<Order> readOrderFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.OrderInputPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.OrderOutputPath.getPath();
        List<Order> orders = (List<Order>) salesManager.readFile(filePathInput);

        salesManager.addToRepository(orders);
        orders.forEach(order -> {
            order.calculateTotalAmount(productRepository);
        });
        salesManager.writeFile(orders, filePathOutput);
        return orders;
    }

    public void addOrderFromFile(String folderPath) throws FileNotFoundException, InterruptedException {
        String filePathInput = folderPath + FilePathEnum.OrderAddPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.OrderOutputPath.getPath();

        commonController.run(folderPath);
        try {
            List<Order> orders = readFileToAddOrder.readFile(filePathInput);
            orders.forEach(order -> order.calculateTotalAmount(productRepository));
            System.out.println(orders);
            orders.forEach(orderRepository::addOrders);
        } catch (Exception e) {
            log.error("File not found: " + filePathInput);
        }

        salesManager.writeFile(orderRepository.getList(), filePathOutput);
    }

    public void editOrderFromFile(String folderPath) throws InterruptedException {
        String filePathInput = folderPath + FilePathEnum.OrderUpdatePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.OrderOutputPath.getPath();

        commonController.run(folderPath);
        List<Order> orders = (List<Order>) salesManager.readFile(filePathInput);
        orders.forEach(orderRepository::editOrder);
        salesManager.writeFile(orderRepository.getList(), filePathOutput);
    }

    public void deleteOrderFromFile(String folderPath) throws FileNotFoundException, InterruptedException {
        String filePathInput = folderPath + FilePathEnum.OrderDeletePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.OrderOutputPath.getPath();

        commonController.run(folderPath);
        try {
            List<String> orderIds = readFileToCRUD.readOneColumn(filePathInput);

            orderIds.forEach(orderRepository::deleteOrder);
        } catch (Exception e) {
            log.error("File not found: " + filePathInput);
        }
        salesManager.writeFile(orderRepository.getList(), filePathOutput);
    }

    public void findOrdersByProductId(String folderPath) throws FileNotFoundException, InterruptedException {

        String filePathInput = folderPath + FilePathEnum.OrderSearchingInputPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.OrderOutputPath.getPath();

        commonController.run(folderPath);
        List<String> productIds = readFileToCRUD.readOneColumn(filePathInput);

        List<Order> orders = productIds.stream()
                .map(orderRepository::findByProductId)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .toList();
        salesManager.writeFile(orders, filePathOutput);
    }
}