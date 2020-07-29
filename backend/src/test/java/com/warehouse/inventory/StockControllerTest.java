package com.warehouse.inventory;

import com.warehouse.inventory.controller.StockController;
import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.exception.ProductNotFoundException;
import com.warehouse.inventory.helper.CSVHelper;
import com.warehouse.inventory.model.ProductCSV;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.model.UpdateStock;
import static org.junit.jupiter.api.Assertions.*;
import com.warehouse.inventory.service.StockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVHelper.class)
public class StockControllerTest {

    @Mock
    StockService stockService;

    @InjectMocks
    private StockController stockController;

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
    @DisplayName(value = "getStockByProductCode - Normal")
    public void getStockByProductCodeNormal(){
        Location locationTKO = new Location(1, "TKO", createdDateTime, updatedDateTime);
        Location locationCWB = new Location(2, "CWB", createdDateTime, updatedDateTime);
        Stock stockA = new Stock(1, locationTKO,10, createdDateTime, updatedDateTime);
        Stock stockB = new Stock(2, locationCWB, 20, createdDateTime, updatedDateTime);
        List<Stock> mockStockList = new ArrayList<Stock>();
        mockStockList.add(stockA);
        mockStockList.add(stockB);
        Mockito.when(stockService.getStockByProductCode("FM-HKTV01"))
                .thenReturn(mockStockList);
        ResponseEntity<Response> responseEntity = stockController.getStockByProductCode("FM-HKTV01");
        List<Stock> actualStockList = (List<Stock>) responseEntity.getBody().getMessage();
        Mockito.verify(stockService, Mockito.times(1)).getStockByProductCode("FM-HKTV01");

        assertEquals(2, actualStockList.size());
        assertEquals(1, actualStockList.get(0).getId());
        assertEquals(10, actualStockList.get(0).getQuantity());
        assertEquals("TKO", actualStockList.get(0).getLocation().getName());
        assertEquals(createdDateTime, actualStockList.get(0).getLocation().getCreatedAt());

        assertEquals(2, actualStockList.get(1).getId());
        assertEquals(20, actualStockList.get(1).getQuantity());
        assertEquals("CWB", actualStockList.get(1).getLocation().getName());
        assertEquals(createdDateTime, actualStockList.get(1).getCreatedAt());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(true, responseEntity.getBody().isSuccess());
    }

    @Test
    @DisplayName(value = "getStockByProductCode - No Stock Found")
    public void getStockByProductCodeNoStockFound(){
        List<Stock> mockStockList = new ArrayList<Stock>();
        Mockito.when(stockService.getStockByProductCode("FM-HKTV01"))
                .thenReturn(mockStockList);
        ResponseEntity<Response> responseEntity = stockController.getStockByProductCode("FM-HKTV01");
        List<Stock> actualStockList = (List<Stock>) responseEntity.getBody().getMessage();
        assertEquals(0, actualStockList.size());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(true, responseEntity.getBody().isSuccess());
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
        Mockito.when(stockService.updateStock("FM-HKTV01", updateStockList))
                .thenReturn(savedStockList);
        ResponseEntity<Response> responseEntity = stockController.updateStock(updateStockList, "FM-HKTV01");
        List<Stock> actualStockList = (List<Stock>) responseEntity.getBody().getMessage();
        assertEquals(20, actualStockList.get(0).getQuantity());
        assertEquals(1, actualStockList.get(0).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(0).getProduct().getCode());
        assertEquals("TKO", actualStockList.get(0).getLocation().getName());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(true, responseEntity.getBody().isSuccess());
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

        Mockito.when(stockService.updateStock("FM-HKTV01", updateStockList))
                .thenReturn(savedStockList);

        ResponseEntity<Response> responseEntity = stockController.updateStock(updateStockList, "FM-HKTV01");
        List<Stock> actualStockList = (List<Stock>) responseEntity.getBody().getMessage();
        assertEquals(20, actualStockList.get(0).getQuantity());
        assertEquals(1, actualStockList.get(0).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(0).getProduct().getCode());
        assertEquals("TKO", actualStockList.get(0).getLocation().getName());

        assertEquals(100, actualStockList.get(1).getQuantity());
        assertEquals(1, actualStockList.get(1).getProduct().getId());
        assertEquals("FM-HKTV01", actualStockList.get(1).getProduct().getCode());
        assertEquals("CWB", actualStockList.get(1).getLocation().getName());

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(true, responseEntity.getBody().isSuccess());
    }

    @Test
    @DisplayName(value = "updateStock - Quantity < 0")
    public void updateStockQuantityLessThanZero(){
        List<UpdateStock> updateStockList = new ArrayList<UpdateStock>();
        UpdateStock updateStock = new UpdateStock("TKO", -1);
        updateStockList.add(updateStock);
        Mockito.when(stockService.updateStock("FM-HKTV01", updateStockList)).thenThrow(new InvalidNumberException("Quantity can't smaller than 0"));
        assertThrows(InvalidNumberException.class, () -> {
            stockController.updateStock( updateStockList, "FM-HKTV01");
        });
    }
}
