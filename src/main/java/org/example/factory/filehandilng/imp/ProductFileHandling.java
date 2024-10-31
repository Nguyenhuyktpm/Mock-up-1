package org.example.factory.filehandilng.imp;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.ProductValidationDTO;
import org.example.enums.ColumnEnum;
import org.example.enums.ColumnNameEnum;
import org.example.enums.DataType;
import org.example.factory.filehandilng.SalesManager;
import org.example.factory.validation.ValidationFactory;
import org.example.factory.validation.ValidationManager;
import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductFileHandling implements SalesManager {
    DataType dataType = DataType.PRODUCT;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Product> readFile(String filePath) {
        List<Product> products = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(filePath), StandardCharsets.UTF_8);
            lines.stream().skip(1).forEach(line -> {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    try {
                        String id = values[ColumnEnum.Column1.getCode()].trim();
                        String name = values[ColumnEnum.Column2.getCode()].trim();
                        double price = Double.parseDouble(values[ColumnEnum.Column3.getCode()].trim());
                        int stock = Integer.parseInt(values[ColumnEnum.Column4.getCode()].trim());

                        Product product = Product.builder()
                                .id(id)
                                .name(name)
                                .price(price)
                                .stockAvailable(stock)
                                .build();

                        if (validationManager.validate(ProductValidationDTO.builder()
                                .product(product)
                                .products(products)
                                .build())) {
                            products.add(product);
                        }
                    } catch (NumberFormatException e) {
                        log.error("Invalid data format in line: {}", line, e);
                    }
                } else {
                    log.error("Invalid data format in line: {}", line);
                }
            });
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
        }
        return products;
    }

    @Override
    public <T> void addToRepository(List<T> elements) {
        ProductRepository productRepository = ProductRepository.getInstance();
        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Product) {
            List<Product> products = (List<Product>) elements;
            products.forEach(productRepository::addElement);
        } else {
            log.error("Add product to repository failed");
        }
    }

    @Override
    public <T> void writeFile(List<T> elements, String filePath) {


        if (elements != null && !elements.isEmpty() && elements.get(0) instanceof Product) {
            List<Product> products = (List<Product>) elements;
            StringBuilder content = new StringBuilder(ColumnNameEnum.ProductNameEnum.getColumnName()).append("\n");

            products.forEach(product -> {
                content.append(product.getId()).append(",")
                        .append(product.getName()).append(",")
                        .append(product.getPrice()).append(",")
                        .append(product.getStockAvailable()).append("\n");
            });

            try {
                Files.writeString(Path.of(filePath),
                        content.toString(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE);
            } catch (IOException e) {
                log.error("Error writing Product file: {}", e.getMessage());
            }
        }
    }
}


