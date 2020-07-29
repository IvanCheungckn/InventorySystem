package com.warehouse.inventory;

import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.helper.CSVHelper;
import com.warehouse.inventory.model.ProductCSV;
import com.warehouse.inventory.model.UpdateStock;
import com.warehouse.inventory.repository.LocationRepository;
import com.warehouse.inventory.repository.ProductRepository;
import com.warehouse.inventory.repository.StockRepository;
import com.warehouse.inventory.service.StockServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVHelper.class)
public class StockServiceTest {

    @Mock
    StockRepository stockRepository;

    @Mock
    LocationRepository locationRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    StockServiceImpl stockService;

    LocalDateTime createdDateTime;
    LocalDateTime updatedDateTime;

        @Before
        public void init() {
            PowerMockito.mockStatic(CSVHelper.class);
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            createdDateTime = LocalDateTime.of(currentDate, currentTime);
            updatedDateTime = LocalDateTime.of(currentDate, currentTime);
        }
        @Test
        @DisplayName(value = "store stock data by csv - normal")
        public void storeStockDataByCSVNormal() throws Exception{

            String initialString = "text";
            InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
            MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

            Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
            Location locationCWB = new Location(2, "CWB", createdDateTime, updatedDateTime);

            Product firstProduct = new Product(1, "face mask", "FM", "FM-HKTV01", 100 , createdDateTime, updatedDateTime);
            Product secondProduct = new Product(6, "lemon juice", "LJ", "LJ-HKTV01", 100, createdDateTime, updatedDateTime);

            //1st stock
            Stock firstCallStock = new Stock(1, locationTKO, firstProduct, 150, createdDateTime, updatedDateTime);
            Mockito.when(stockRepository.findByLocation_NameAndProduct_Code("TKO", "FM-HKTV01")).
                    thenReturn(Optional.of(firstCallStock));

            //2nd stock
            Product expectedSecondProductResult = new Product(6, "lemon juice", "LJ", "LJ-HKTV01", 200, createdDateTime, updatedDateTime);
            Mockito.when(stockRepository.findByLocation_NameAndProduct_Code(locationCWB.getName(), secondProduct.getCode())).
                    thenReturn(Optional.empty());
            Mockito.when(locationRepository.findByName("CWB")).
                    thenReturn(Optional.of(locationCWB));
            Mockito.when(productRepository.findByCode("LJ-HKTV01")).
                    thenReturn(Optional.of(secondProduct));

            List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
            mockProductCSVList.add(new ProductCSV( "FM-HKTV01", "face mask","TKO", 100, 200));
            mockProductCSVList.add(new ProductCSV( "LJ-HKTV01", "lemon juice","CWB", 200, 400));
            Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
            List<Stock> savedStocks = stockService.storeStockDataByCSV(file);
            Mockito.verify(stockRepository, Mockito.times(1)).findByLocation_NameAndProduct_Code("TKO", "FM-HKTV01");
            Mockito.verify(stockRepository, Mockito.times(1)).findByLocation_NameAndProduct_Code("CWB", "LJ-HKTV01");

            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.size() == 2));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(0).getProduct().getCode() == "FM-HKTV01"));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(0).getProduct().getId() == 1));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(0).getQuantity() == 200));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(0).getProduct().getWeight() == 100));

            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(1).getProduct().getCode() == "LJ-HKTV01"));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(1).getProduct().getId() == 6));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(1).getQuantity() == 400));
            Mockito.verify(stockRepository).saveAll(Mockito.argThat((List<Stock> stockList) -> stockList.get(1).getProduct().getWeight() == 200));
        }

    @Test
    @DisplayName(value = "store stock data by csv - product not exist")
    public void storeStockDataByCSVProductNotExist() throws Exception{
        String initialString = "text";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

        Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
        Location locationCWB = new Location(2, "CWB", createdDateTime, updatedDateTime);

        Product firstProduct = new Product(1, "face mask", "FM", "FM-HKTV01", 100, createdDateTime, updatedDateTime);
        Product secondProduct = new Product(6, "lemon juice", "LJ", "LJ-HKTV01", 100, createdDateTime, updatedDateTime);

        //1st stock
        Stock firstCallStock = new Stock(1, locationTKO, firstProduct, 150, createdDateTime, updatedDateTime);
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code("TKO", "FM-HKTV01")).
                thenReturn(Optional.of(firstCallStock));

        //2nd stock
        Product expectedSecondProductResult = new Product(6, "LJ", "lemon juice", "LJ-HKTV01", 200, createdDateTime, updatedDateTime);
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code(locationCWB.getName(), secondProduct.getCode())).
                thenReturn(Optional.empty());
        Mockito.when(locationRepository.findByName("CWB")).
                thenReturn(Optional.of(locationCWB));
        Mockito.when(productRepository.findByCode("LJ-HKTV01")).
                thenReturn(Optional.of(secondProduct));

        //3rd stock
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code(locationCWB.getName(), "W-HKTV01")).
                thenReturn(Optional.empty());
        Mockito.when(locationRepository.findByName("CWB")).
                thenReturn(Optional.of(locationCWB));
        Product expectedThirdProductResult = new Product("water", "W-HKTV01", 300);
        Mockito.when(productRepository.findByCode("W-HKTV01")).
                thenReturn(Optional.empty());

        List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
        mockProductCSVList.add(new ProductCSV( "FM-HKTV01", "face mask","TKO", 100, 200));
        mockProductCSVList.add(new ProductCSV( "LJ-HKTV01", "lemon juice","CWB", 200, 400));
        mockProductCSVList.add(new ProductCSV( "W-HKTV01", "water","CWB", 300, 300));
        Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
        assertThrows(RuntimeException.class, ()-> {
            stockService.storeStockDataByCSV(file);
        });
    }

        @Test
        @DisplayName(value = "store stock data by csv - empty csv")
        public void storeStockDataByCSVEmptyCSV() throws Exception{
            String initialString = "text";
            InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
            MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

            List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
            Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
            assertThrows(RuntimeException.class, () -> {
                stockService.storeStockDataByCSV(file);
            });
        }

        @Test
        @DisplayName(value = "store stock data by csv - quantity < 0")
        public void storeStockDataByCSVQuantitySmallerThanZero() throws Exception{
            String initialString = "text";
            InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
            MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

            List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
            mockProductCSVList.add(new ProductCSV( "FM-HKTV01", "face mask","TKO", 100, -200));
            Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
            assertThrows(RuntimeException.class, () -> {
                stockService.storeStockDataByCSV(file);
            });
        }

        @Test
        @DisplayName(value = "store stock data by csv - weight < 0")
        public void storeStockDataByCSVWeightSmallerThanZero() throws Exception{
            String initialString = "text";
            InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
            MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

            List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
            mockProductCSVList.add(new ProductCSV( "FM-HKTV01", "face mask","TKO", -100, 200));
            Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
            assertThrows(RuntimeException.class, () -> {
                stockService.storeStockDataByCSV(file);
            });
        }

        @Test
        @DisplayName(value = "store stock data by csv - weight = 0")
        public void storeStockDataByCSVWeightEqualZero() throws Exception{
            String initialString = "text";
            InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
            MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", targetStream);

            List<ProductCSV> mockProductCSVList = new ArrayList<ProductCSV>();
            mockProductCSVList.add(new ProductCSV( "FM-HKTV01", "face mask","TKO", 0, 200));
            Mockito.when(CSVHelper.csvToProducts(any(InputStream.class))).thenReturn(mockProductCSVList);
            assertThrows(RuntimeException.class, () -> {
                stockService.storeStockDataByCSV(file);
            });
        }
//    }

    @Test
    @DisplayName(value = "getStockByProductCode - Normal")
    public void getStockByProductCodeNormal(){
        Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
        Location locationCWB = new Location(2, "CWB", createdDateTime, updatedDateTime);
        Stock stockA = new Stock(1, locationTKO,10, createdDateTime, updatedDateTime);
        Stock stockB = new Stock(2, locationCWB, 20, createdDateTime, updatedDateTime);
        List<Stock> mockStockList = new ArrayList<Stock>();
        mockStockList.add(stockA);
        mockStockList.add(stockB);
        Mockito.when(stockRepository.findByProduct_Code("FM-HKTV01"))
                .thenReturn(Optional.of(mockStockList));
        List<Stock> stockList = stockService.getStockByProductCode("FM-HKTV01");
        Mockito.verify(stockRepository, Mockito.times(1)).findByProduct_Code("FM-HKTV01");

        assertEquals(2, stockList.size());
        assertEquals(1, stockList.get(0).getId());
        assertEquals(10, stockList.get(0).getQuantity());
        assertEquals("TKO", stockList.get(0).getLocation().getName());
        assertEquals(createdDateTime, stockList.get(0).getLocation().getCreatedAt());

        assertEquals(2, stockList.get(1).getId());
        assertEquals(20, stockList.get(1).getQuantity());
        assertEquals("CWB", stockList.get(1).getLocation().getName());
        assertEquals(createdDateTime, stockList.get(1).getCreatedAt());

    }

    @Test
    @DisplayName(value = "getStockByProductCode - No Stock Found")
    public void getStockByProductCodeNoStockFound(){
        List<Stock> mockStockList = new ArrayList<Stock>();
        Mockito.when(stockRepository.findByProduct_Code("FM-HKTV01"))
                .thenReturn(Optional.of(mockStockList));

        List<Stock> stockList = stockService.getStockByProductCode("FM-HKTV01");
        assertEquals(0, stockList.size());
    }

    @Test
    @DisplayName(value = "updateStock - Normal")
    public void updateStockNormal(){
        List<UpdateStock> updateStockList = new ArrayList<UpdateStock>();
        UpdateStock updateStock = new UpdateStock("TKO", 20);
        updateStockList.add(updateStock);
        Product product = new Product(1, "face mask", "FM", "FM-HKTV01", 100, createdDateTime, updatedDateTime);
        Location locationTKO = new Location(1, "TKO");
        Stock stock = new Stock(1, locationTKO, product, 10, createdDateTime, updatedDateTime);
        Stock savedStock = new Stock(1, locationTKO, product, 20, createdDateTime, updatedDateTime);
        List<Stock> savedStockList = new ArrayList<Stock>();
        savedStockList.add(savedStock);
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code("TKO", "FM-HKTV01"))
                .thenReturn(Optional.of(stock));
        Mockito.when(stockRepository.saveAll(anyList()))
                .thenReturn(savedStockList);
        List<Stock> actualStockList = stockService.updateStock("FM-HKTV01", updateStockList);
        assertEquals(20, actualStockList.get(0).getQuantity());
        assertEquals(1, actualStockList.get(0).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(0).getProduct().getCode());
        assertEquals("TKO", actualStockList.get(0).getLocation().getName());
    }

    @Test
    @DisplayName(value = "updateStock - Normal update multiple location")
    public void updateStockNormalMultipleLocation(){
        List<UpdateStock> updateStockList = new ArrayList<UpdateStock>();
        UpdateStock updateStock = new UpdateStock("TKO", 20);
        UpdateStock updateStock2 = new UpdateStock("CWB", 100);
        updateStockList.add(updateStock);
        updateStockList.add(updateStock2);
        Product product = new Product(1, "face mask", "FM", "FM-HKTV01", 100, createdDateTime, updatedDateTime);
        Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
        Location locationCWB = new Location(1, "CWB", createdDateTime, updatedDateTime);
        Stock stock = new Stock(1, locationTKO, product, 10, createdDateTime, updatedDateTime);
        Stock stock2 = new Stock(2, locationCWB, product, 20, createdDateTime, updatedDateTime);
        Stock savedStock = new Stock(1, locationTKO, product, 20, createdDateTime, updatedDateTime);
        Stock savedStock2 = new Stock(1, locationCWB, product, 100, createdDateTime, updatedDateTime);
        List<Stock> savedStockList = new ArrayList<Stock>();
        savedStockList.add(savedStock);
        savedStockList.add(savedStock2);
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code("TKO", "FM-HKTV01"))
                .thenReturn(Optional.of(stock));
        Mockito.when(stockRepository.findByLocation_NameAndProduct_Code("CWB", "FM-HKTV01"))
                .thenReturn(Optional.of(stock2));
        Mockito.when(stockRepository.saveAll(anyList()))
                .thenReturn(savedStockList);
        List<Stock> actualStockList = stockService.updateStock("FM-HKTV01", updateStockList);
        assertEquals(20, actualStockList.get(0).getQuantity());
        assertEquals(1, actualStockList.get(0).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(0).getProduct().getCode());
        assertEquals("TKO", actualStockList.get(0).getLocation().getName());

        assertEquals(100, actualStockList.get(1).getQuantity());
        assertEquals(1, actualStockList.get(1).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(1).getProduct().getCode());
        assertEquals("CWB", actualStockList.get(1).getLocation().getName());
    }

    @Test
    @DisplayName(value = "updateStock - Quantity < 0")
    public void updateStockQuantityLessThanZero(){
        List<UpdateStock> updateStockList = new ArrayList<UpdateStock>();
        UpdateStock updateStock = new UpdateStock("TKO", -1);
        updateStockList.add(updateStock);
        assertThrows(InvalidNumberException.class, () -> {
            stockService.updateStock("FM-HKTV01", updateStockList);
        });
    }
}
