package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.CommonController;
import org.example.utils.DataCommon;
import org.example.utils.FileReadingUtils;
import org.example.enums.DataType;
import org.example.enums.FilePathEnum;
import org.example.factory.filehandilng.SaleManagerFactory;
import org.example.factory.filehandilng.SalesManager;
import org.example.model.Order;
import org.example.model.Product;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductService {
    public ProductRepository productRepository = ProductRepository.getInstance();
    public OrderRepository orderRepository = OrderRepository.getInstance();
    public FileReadingUtils readFileToCRUD = new FileReadingUtils();
    public CommonController commonController = new CommonController();
    DataType dataType = DataType.PRODUCT;
    public SalesManager salesManager = SaleManagerFactory.getSalesManager(dataType);

    public List<Product> readProductFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.ProductInputPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.ProductOutputPath.getPath();

        List<Product> products = (List<Product>) salesManager.readFile(filePathInput);

        salesManager.addToRepository(products);
        salesManager.writeFile(products, filePathOutput);

        return products;
    }

    public void editProductFromFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.ProductUpdatePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.ProductOutputPath.getPath();
        DataCommon.addProductToRepo(folderPath);

        List<Product> products = (List<Product>) salesManager.readFile(filePathInput);

        products.forEach(productRepository::editProduct);
        salesManager.writeFile(productRepository.getList(), filePathOutput);
    }

    public void deleteProductFromFile(String folderPath) throws FileNotFoundException {
        String filePathInput = folderPath + FilePathEnum.ProductDeletePath.getPath();
        String filePathOutput = folderPath + FilePathEnum.ProductOutputPath.getPath();
        DataCommon.addProductToRepo(folderPath);

        try {
            List<String> productIds = readFileToCRUD.readOneColumn(filePathInput);

            productIds.forEach(productRepository::removeElement);
        } catch (FileNotFoundException e) {
            log.error("file not found" + e.getMessage());
        }
        salesManager.writeFile(productRepository.getList(), filePathOutput);
    }

    public void addProductFormFile(String folderPath) {
        String filePathInput = folderPath + FilePathEnum.ProductAddPath.getPath();
        String filePathOutput = folderPath + FilePathEnum.ProductOutputPath.getPath();

        DataCommon.addProductToRepo(folderPath);

        List<Product> products = (List<Product>) salesManager.readFile(filePathInput);
        products.forEach(productRepository::addElement);
        salesManager.writeFile(productRepository.getList(), filePathOutput);
    }

    public void findTop3HighestOrder(String folderPath) throws InterruptedException {

        String filePathOutput = folderPath + FilePathEnum.ProductOutputPath.getPath();
        Map<String, Integer> productOrderCount = new HashMap<>();
        commonController.run(folderPath);

        for (Order order : orderRepository.getList()) {
            Map<String, Integer> productQuantities = order.getProductQuantities();
            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                productOrderCount.put(productId, productOrderCount.getOrDefault(productId, 0) + quantity);
            }
        }

        List<Product> products = productRepository.getList().stream()
                .filter(product -> productOrderCount.containsKey(product.getId()))
                .sorted((p1, p2) -> productOrderCount.get(p2.getId()) - productOrderCount.get(p1.getId()))
                .limit(3)
                .toList();


        salesManager.writeFile(products, filePathOutput);
    }
}
