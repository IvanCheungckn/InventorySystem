package com.warehouse.inventory.controller;

import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidFileTypeException;
import com.warehouse.inventory.helper.CSVHelper;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.model.UpdateStock;
import com.warehouse.inventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/stock")
public class StockController {

    StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping(value = "", consumes = { "multipart/form-data"})
    public ResponseEntity storeStockDataByCSV(@RequestPart("file") MultipartFile file){
        if (!CSVHelper.hasCSVFormat(file)) throw new InvalidFileTypeException("Uploaded file is not csv");
        List<Stock> stockList = stockService.storeStockDataByCSV(file);
        return new ResponseEntity(new Response(true, stockList), HttpStatus.OK);
    }

    @GetMapping("{productCode}")
    public ResponseEntity getStockByProductCode(@PathVariable("productCode") String productCode){
        List<Stock> stockList = stockService.getStockByProductCode(productCode);
        return new ResponseEntity(new Response(true, stockList), HttpStatus.OK);
    }


    @PutMapping(value = "{productCode}" , consumes="application/json")
    public ResponseEntity updateStock(@RequestBody List<UpdateStock> body,
                                      @PathVariable("productCode") String productCode) {
        List<Stock> updatedStocks = stockService.updateStock(productCode, body);
        return new ResponseEntity(new Response(true, updatedStocks), HttpStatus.OK);
    }

}
