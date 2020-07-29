package com.warehouse.inventory.service;

import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.model.UpdateStock;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StockService {
    List<Stock> storeStockDataByCSV(MultipartFile file);
    List<Stock> getStockByProductCode(String productCode);
    List<Stock> updateStock(String productCode, List<UpdateStock> updateStockList);
}
