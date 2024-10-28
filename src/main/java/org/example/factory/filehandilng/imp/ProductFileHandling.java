package org.example.factory.filehandilng.imp;

import lombok.extern.slf4j.Slf4j;
import org.example.DTO.ProductValidationDTO;
import org.example.enums.ColumnEnum;
import org.example.enums.ColumnNameEnum;
import org.example.enums.DataType;
import org.example.enums.FilePathEnum;
import org.example.factory.filehandilng.SalesManager;
import org.example.factory.validation.ValidationFactory;
import org.example.factory.validation.ValidationManager;
import org.example.model.Product;
import org.example.repository.ProductRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductFileHandling implements SalesManager {
    DataType dataType = DataType.PRODUCT;
    ValidationManager validationManager = ValidationFactory.getValidationManager(dataType);

    @Override
    public List<Product> readFile(String filePath) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 4) {
                    String id = values[ColumnEnum.Column1.getCode()].trim();
                    String name = values[ColumnEnum.Column2.getCode()].trim();
                    Double price = Double.parseDouble(values[ColumnEnum.Column3.getCode()].trim());
                    Integer stock = Integer.valueOf(values[ColumnEnum.Column4.getCode()].trim());

                    Product product = Product.builder()
                            .id(id)
                            .name(name)
                            .price(price)
                            .stockAvailable(stock)
                            .build();
                    if (validationManager.validate(ProductValidationDTO.builder()
                            .product(product)
                            .products(products)
                            .build()))
                        products.add(product);
                } else
                    log.error("Invalid data format in line: {}", line);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
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


            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
                bw.write(ColumnNameEnum.ProductNameEnum.getColumnName());
                bw.newLine();
                for (Product product : products) {
                    if (!validationManager.isElementInFile(product, filePath)) {
                        String line = product.getId() + ","
                                + product.getName() + ","
                                + product.getPrice() + ","
                                + product.getStockAvailable();
                        bw.write(line);
                        bw.newLine();
                    } else
                        log.error("ProductId {} already exists", product.getId());
                }
            } catch (IOException e) {
                log.error("Error writing Product file: {}", e.getMessage());
            }
        }
    }
}
