package com.warehouse.inventory.service;

import com.warehouse.inventory.entity.Location;
import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.entity.Stock;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.exception.LocationNotFoundException;
import com.warehouse.inventory.exception.ProductNotFoundException;
import com.warehouse.inventory.exception.StockNotFoundException;
import com.warehouse.inventory.helper.CSVHelper;
import com.warehouse.inventory.model.ProductCSV;
import com.warehouse.inventory.model.UpdateStock;
import com.warehouse.inventory.repository.LocationRepository;
import com.warehouse.inventory.repository.ProductRepository;
import com.warehouse.inventory.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService{

    ProductRepository productRepository;
    LocationRepository locationRepository;
    StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(ProductRepository productRepository, LocationRepository locationRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public List<Stock> storeStockDataByCSV(MultipartFile file) {
        try {
            List<ProductCSV> productCSVs = CSVHelper.csvToProducts(file.getInputStream());
            List<Stock> stockList = new ArrayList<Stock>();
            for (ProductCSV productCSV: productCSVs){
                if (productCSV.getWeight() <= 0 ){
                    throw new RuntimeException("Weight can't less than or equal to 0");
                }
                if (productCSV.getQuantity() < 0 ){
                    throw new RuntimeException("Quantity can't less than 0");
                }
                Optional<Stock> stockOpt = stockRepository.findByLocation_NameAndProduct_Code(productCSV.getLocationName(), productCSV.getProductCode());
                Optional<Product> productOpt = productRepository.findByCode(productCSV.getProductCode());
                if (stockOpt.isPresent()){
                    Stock stock = stockOpt.get();
                    stock.setQuantity(productCSV.getQuantity());
                    stock.getProduct().setWeight(productCSV.getWeight());
                    stock.getProduct().setName(productCSV.getProductName());
                    stockList.add(stock);
                }else if (productOpt.isPresent()){
                    Location location = locationRepository.findByName(productCSV.getLocationName()).orElseThrow(()->new LocationNotFoundException(productCSV.getLocationName() + " is not a valid location"));
                    System.out.println(productCSV.getProductCode());
                    Product product = productOpt.get();
                    product.setName(productCSV.getProductName());
                    product.setWeight(productCSV.getWeight());
                    Stock stock = new Stock(
                            location,product, productCSV.getQuantity()
                    );
                    stockList.add(stock);
                }else{
                    throw new ProductNotFoundException("Please create product first");
                }
            }
            if (stockList.size() == 0) {throw new RuntimeException("Failed to save data");};
            List<Stock> savedStocks = stockRepository.saveAll(stockList);
            return savedStocks;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        } catch (Exception e){
            System.out.println("[StockServiceError] storeStockDataByCSV");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public List<Stock> getStockByProductCode(String productCode) {
        try{
            List<Stock> stocks = stockRepository.findByProduct_Code(productCode).orElseThrow(()->new StockNotFoundException("Stock not found"));
            return stocks;
        }catch (Exception e) {
            System.out.println("[StockServiceError] getStockByProductCode");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public List<Stock> updateStock(String productCode, List<UpdateStock> updateStockList) {
        try{
            List<Stock> stockList = new ArrayList<Stock>();
            for (UpdateStock updateStock: updateStockList){
                if (!(updateStock.getQty() >= 0)) {throw new InvalidNumberException("Quantity can't smaller than 0");}
                Optional<Stock> stockOpt = stockRepository.findByLocation_NameAndProduct_Code(updateStock.getLocationName(), productCode);
                if (stockOpt.isPresent()){
                    Stock stock = stockOpt.get();
                    stock.setQuantity(updateStock.getQty());
                    stockList.add(stock);
                }else{
                    Product product = productRepository.findByCode(productCode).orElseThrow(()-> new ProductNotFoundException("Product not found"));
                    Location location = locationRepository.findByName(updateStock.getLocationName()).orElseThrow(()->new LocationNotFoundException("Location not found"));
                    Stock stock = new Stock(location, product, updateStock.getQty());
                    stockList.add(stock);
                }
            }
            List<Stock> savedStockList = stockRepository.saveAll(stockList);
            return savedStockList;
        }catch (Exception e) {
            System.out.println("[StockServiceError] updateStock");
            System.out.println(e.getMessage());
            throw e;
        }
    }

}
