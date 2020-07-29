package com.warehouse.inventory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.warehouse.inventory.controller.ProductController;
import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.exception.ProductNotFoundException;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.service.ProductService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    private ProductController productController;

    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;

    @Before
    public void init() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        createdDateTime = LocalDateTime.of(currentDate, currentTime);
        updatedDateTime = LocalDateTime.of(currentDate, currentTime);
    }

    @Nested
    @DisplayName("Get all products")
    public class getAllProducts{
        @Test
        @DisplayName("Normal")
        public void getAllProductsNormal() throws Exception {
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
            Mockito.when(productService.getAllProducts()).thenReturn(productList);
            ResponseEntity<Response> responseEntity = productController.getAllProducts();

            assertEquals(200, responseEntity.getStatusCodeValue());
            Mockito.verify(productService, Mockito.times(1)).getAllProducts();
            List<Product> actualProductList = (List<Product>)responseEntity.getBody().getMessage();
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
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }

        @Test
        @DisplayName("Empty Database")
        public void getProductByCodeEmptyDB(){
            Mockito.when(productService.getAllProducts()).thenReturn(new ArrayList<Product>());
            ResponseEntity<Response> responseEntity = productController.getAllProducts();
            List<Product> actualProductList = (List<Product>)responseEntity.getBody().getMessage();
            Mockito.verify(productService, Mockito.times(1)).getAllProducts();
            assertEquals(0, actualProductList.size());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
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
            Mockito.when(productService.createProduct(productName, productWeight))
                    .thenReturn(new Product(1,productName, shortName, "FM-HKTV01", productWeight));
            ResponseEntity<Response> responseEntity = productController.createProduct(productName, String.valueOf(productWeight));
            Product actualProduct = (Product)responseEntity.getBody().getMessage();
            Mockito.verify(productService, Mockito.times(1)).createProduct(productName, productWeight);

            assertEquals(1, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals("FM-HKTV01", actualProduct.getCode());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }

        @Test
        @DisplayName("Normal - id >= 10")
        public void createProductNormalLargerThanOrEqualTen(){
            String productName = "face mask";
            int productWeight = 100;
            String shortName = "FM";
            Mockito.when(productService.createProduct(productName, productWeight))
                    .thenReturn(new Product(11,productName, shortName, "FM-HKTV11", productWeight));
            ResponseEntity<Response> responseEntity = productController.createProduct(productName, String.valueOf(productWeight));
            Product actualProduct = (Product)responseEntity.getBody().getMessage();
            Mockito.verify(productService, Mockito.times(1)).createProduct(productName, productWeight);

            assertEquals(11, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals("FM-HKTV11", actualProduct.getCode());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }

        @Test
        @DisplayName("Negative Weight")
        public void getProductByCodeNegativeWeight(){
            String productName = "face mask";
            int productWeight = 100;
            Mockito.when(productService.createProduct(productName, productWeight))
                    .thenThrow(new InvalidNumberException("Weight can't smaller or equal to zero."));
            assertThrows(InvalidNumberException.class, ()->productController.createProduct(productName, String.valueOf(productWeight)));
        }
    }
    @Nested
    @DisplayName("Delete product")
    public class deleteProduct{
        @Test
        @DisplayName("Normal")
        public void deleteProductNormal(){
            String productCode = "FM-HKTV01";
            productController.deleteProduct(productCode);
            Mockito.verify(productService, Mockito.times(1)).deleteProduct(productCode);
            ResponseEntity<Response> responseEntity = productController.deleteProduct(productCode);
            String message = (String) responseEntity.getBody().getMessage();
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
            assertEquals("successfully deleted", message);
        }
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
            Mockito.when(productService.getProductByCode("FM-HKTV01")).thenReturn(product);
            ResponseEntity<Response> responseEntity = productController.getProducts("FM-HKTV01");
            Product actualProduct = (Product) responseEntity.getBody().getMessage();

            Mockito.verify(productService, Mockito.times(1)).getProductByCode("FM-HKTV01");
            assertEquals(1, actualProduct.getId());
            assertEquals(100, actualProduct.getWeight());
            assertEquals(1, actualProduct.getStockList().size());
            assertEquals("TKO", actualProduct.getStockList().get(0).getLocation().getName());
            assertEquals(5, actualProduct.getStockList().get(0).getQuantity());
            assertEquals(200, responseEntity.getStatusCodeValue());
            assertEquals(true, responseEntity.getBody().isSuccess());
        }

        @Test
        @DisplayName("Wrong Product Code - Can't find Product")
        public void getProductByCodeCannotFind(){
            Mockito.when(productService.getProductByCode("FM-HKTV01")).thenThrow(new ProductNotFoundException("Product not found."));
            assertThrows(ProductNotFoundException.class, ()-> productService.getProductByCode("FM-HKTV01"));
        }
    }
}
