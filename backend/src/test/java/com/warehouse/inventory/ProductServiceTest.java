package com.warehouse.inventory;

import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.exception.ProductNotFoundException;
import com.warehouse.inventory.repository.ProductRepository;
import com.warehouse.inventory.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;

    @BeforeEach
    public void init() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        createdDateTime = LocalDateTime.of(currentDate, currentTime);
        updatedDateTime = LocalDateTime.of(currentDate, currentTime);
    }

    @Nested
    @DisplayName("Get product by product code")
    public class getProductByCode{
        @Test
        @DisplayName("Normal")
        public void getProductByCodeNormal(){
            Location location = new Location(1, "TKO", createdDateTime, updatedDateTime);
            List<Stock> stockList = new ArrayList<Stock>();
            Stock stock = new Stock(1, location, 5, createdDateTime, updatedDateTime);
            stockList.add(stock);
            Product product = new Product(1, "face mask", "FM", "FM-HKTV01", 100, createdDateTime, updatedDateTime, stockList);
            Mockito.when(productRepository.findByCode("FM-HKTV01")).thenReturn(Optional.of(product));
            Product actualProduct = productService.getProductByCode("FM-HKTV01");

            Mockito.verify(productRepository, Mockito.times(1)).findByCode("FM-HKTV01");
            assertEquals(1, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals(1, actualProduct.getStockList().size());
            assertEquals("TKO", actualProduct.getStockList().get(0).getLocation().getName());
            assertEquals(5, actualProduct.getStockList().get(0).getQuantity());
        }

        @Test
        @DisplayName("Wrong Product Code - Can't find Product")
        public void getProductByCodeCannotFind(){
            Mockito.when(productRepository.findByCode("FM-HKTV01")).thenReturn(Optional.empty());
            assertThrows(ProductNotFoundException.class, ()-> productService.getProductByCode("FM-HKTV01"));
        }
    }

    @Nested
    @DisplayName("Get all products")
    public class getAllProducts{
        @Test
        @DisplayName("Normal")
        public void getAllProductsNormal(){
            Location location = new Location(1, "TKO", createdDateTime, updatedDateTime);
            List<Stock> stockList = new ArrayList<Stock>();
            List<Stock> stockList2 = new ArrayList<Stock>();
            Stock stock = new Stock(1, location, 5, createdDateTime, updatedDateTime);
            stockList.add(stock);
            Stock stock2 = new Stock(2, location, 20, createdDateTime, updatedDateTime);
            stockList2.add(stock2);
            Product product = new Product(1, "face mask", "FM", "FM-HKTV01", 100, createdDateTime, updatedDateTime, stockList);
            Product product2 = new Product(2, "apple", "A", "A-HKTV01", 50, createdDateTime, updatedDateTime, stockList2);
            List<Product> productList = new ArrayList<Product>();
            productList.add(product);
            productList.add(product2);
            Mockito.when(productRepository.findAll()).thenReturn(productList);
            List<Product> actualProductList = productService.getAllProducts();

            Mockito.verify(productRepository, Mockito.times(1)).findAll();
            assertEquals(1, actualProductList.get(0).getId());
            assertEquals(100, actualProductList.get(0).getWeight());
            assertEquals(1, actualProductList.get(0).getStockList().size());
            assertEquals("TKO", actualProductList.get(0).getStockList().get(0).getLocation().getName());
            assertEquals(5, actualProductList.get(0).getStockList().get(0).getQuantity());

            assertEquals(2, actualProductList.get(1).getId());
            assertEquals(50, actualProductList.get(1).getWeight());
            assertEquals(1, actualProductList.get(1).getStockList().size());
            assertEquals("TKO", actualProductList.get(1).getStockList().get(0).getLocation().getName());
            assertEquals(20, actualProductList.get(1).getStockList().get(0).getQuantity());
        }

        @Test
        @DisplayName("Empty Database")
        public void getProductByCodeEmptyDB(){
            Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<Product>());
            List<Product> actualProductList = productService.getAllProducts();
            Mockito.verify(productRepository, Mockito.times(1)).findAll();
            assertEquals(0, actualProductList.size());
        }
    }

    @Nested
    @DisplayName("Create product")
    public class createProduct{
        @Test
        @DisplayName("Normal - id < 10")
        public void createProductNormal(){
            String productName = "face mask";
            int productWeight = 100;
            String shortName = "FM";
            Mockito.when(productRepository.countShortName("FM")).thenReturn((long) 0);
            Mockito.when(productRepository.saveAndFlush(any(Product.class)))
                    .thenReturn(new Product(1,productName, shortName, "FM-HKTV01", productWeight, createdDateTime, updatedDateTime));

            Product actualProduct = productService.createProduct(productName, productWeight);
            Mockito.verify(productRepository, Mockito.times(1)).countShortName("FM");
            Mockito.verify(productRepository, Mockito.times(1)).saveAndFlush(any(Product.class));

            assertEquals(1, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals("FM-HKTV01", actualProduct.getCode());
        }

        @Test
        @DisplayName("Normal - id >= 10")
        public void createProductNormalLargerThanOrEqualTen(){
            String productName = "face mask";
            int productWeight = 100;
            String shortName = "FM";
            Mockito.when(productRepository.countShortName("FM")).thenReturn((long) 10);
            Mockito.when(productRepository.saveAndFlush(any(Product.class)))
                    .thenReturn(new Product(1,productName, shortName, "FM-HKTV11", productWeight, createdDateTime, updatedDateTime));

            Product actualProduct = productService.createProduct(productName, productWeight);
            Mockito.verify(productRepository, Mockito.times(1)).countShortName("FM");
            Mockito.verify(productRepository, Mockito.times(1)).saveAndFlush(any(Product.class));

            assertEquals(1, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals("FM-HKTV11", actualProduct.getCode());
        }

        @Test
        @DisplayName("Negative Weight")
        public void getProductByCodeNegativeWeight(){
            assertThrows(InvalidNumberException.class, ()->productService.createProduct("face mask", -2));
        }
    }

    @Nested
    @DisplayName("Delete product")
    public class deleteProduct{
        @Test
        @DisplayName("Normal")
        public void deleteProductNormal(){
            String productCode = "FM-HKTV01";
            productService.deleteProduct(productCode);
            Mockito.verify(productRepository, Mockito.times(1)).deleteByCode(productCode);
        }
    }
}
