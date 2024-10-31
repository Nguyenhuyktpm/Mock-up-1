package org.example.servicetest;


import org.example.utils.CommonController;
import org.example.utils.FileReadingUtils;

import org.example.factory.filehandilng.imp.ProductFileHandling;
import org.example.model.Product;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.mock;

public class ProductServiceTest {
    private ProductService productService;
    private ProductRepository mockProductRepository;
    private OrderRepository mockOrderRepository;
    private ProductFileHandling mockProductFileHandling;
    private FileReadingUtils mockReadFileToCRUD;
    private CommonController mockCommonController;


    @BeforeEach
    public void setUp() {
        productService = new ProductService();
        mockProductRepository = mock(ProductRepository.class);
        mockOrderRepository = mock(OrderRepository.class);
        mockProductFileHandling = mock(ProductFileHandling.class);
        mockReadFileToCRUD = mock(FileReadingUtils.class);
        mockCommonController = mock(CommonController.class);


        productService.productRepository = mockProductRepository;
        productService.orderRepository = mockOrderRepository;
        productService.salesManager = mockProductFileHandling;
        productService.readFileToCRUD = mockReadFileToCRUD;
        productService.commonController = mockCommonController;

    }

    @Test
    public void testReadProductFromFile() {

        doAnswer(invocation -> {
            List<Product> products = invocation.getArgument(0);
            products.forEach(mockProductRepository::addElement);
            return null;
        }).when(mockProductFileHandling).addToRepository(anyList());


        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product("P0001", "Product1", 10.0, 100));

        when(mockProductFileHandling.readFile(anyString())).thenReturn(mockProducts);
//        doNothing().when(mockProductRepository).addElement(any(Product.class));


        List<Product> products = productService.readProductFromFile("Data");


        assertEquals(mockProducts, products);
        verify(mockProductRepository, times(1)).addElement(any(Product.class));
    }

    @Test
    public void testEditProductFromFile() {

        List<Product> existingProducts = new ArrayList<>();
        existingProducts.add(new Product("1", "Product1", 10.0, 100));
        when(mockProductRepository.getList()).thenReturn(existingProducts);

        List<Product> updatedProduct = new ArrayList<>();
        updatedProduct.add(new Product("1", "UpdateProduct", 20.0, 120));

        when(mockProductFileHandling.readFile(anyString())).thenReturn(updatedProduct);

        productService.editProductFromFile("Data");

        verify(mockProductRepository, times(1)).editProduct(updatedProduct.get(0));
    }

    @Test
    public void testDeleteProductFromFile() throws FileNotFoundException {
        List<Product> existingProducts = new ArrayList<>();
        existingProducts.add(new Product("1", "Product1", 10.0, 100));
        when(mockProductRepository.getList()).thenReturn(existingProducts);

        List<String> ids = new ArrayList<>();
        ids.add("1");
        when(mockReadFileToCRUD.readOneColumn(anyString())).thenReturn(ids);

        productService.deleteProductFromFile("Data");

        verify(mockProductRepository, times(1)).removeElement(ids.get(0));
    }


    @Test
    public void testAddProductFromFile(){
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "Product1", 10.0, 100));
        when(mockProductFileHandling.readFile(anyString())).thenReturn(products);

        productService.addProductFormFile("Data");

        verify(mockProductRepository, times(1)).addElement(any(Product.class));
    }

    @Test
    public void testFindTop3HighestOrder(){

    }
}