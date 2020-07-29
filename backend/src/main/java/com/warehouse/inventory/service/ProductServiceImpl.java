package com.warehouse.inventory.service;

import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.exception.InvalidNumberException;
import com.warehouse.inventory.exception.ProductDuplicatedException;
import com.warehouse.inventory.exception.ProductNotFoundException;
import com.warehouse.inventory.repository.LocationRepository;
import com.warehouse.inventory.repository.ProductRepository;
import com.warehouse.inventory.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    ProductRepository productRepository;
    StockRepository stockRepository;
    LocationRepository locationRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, StockRepository stockRepository, LocationRepository locationRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    @Transactional
    public Product getProductByCode(String productCode) {
        try{
            Product product = productRepository.findByCode(productCode).orElseThrow(()->new ProductNotFoundException("Product not found."));
            return product;
        }   catch (Exception e){
            System.out.println("[ProductServiceError] getProductByCode");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try{
            List<Product> productList = productRepository.findAll();
            return productList;
        }   catch (Exception e){
            System.out.println("[ProductServiceError] getAllProducts");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public Product createProduct(String productName, int productWeight) {
        try{
            if (!(productWeight > 0)) throw new InvalidNumberException("Weight can't smaller or equal to zero.");
            String[] splited = productName.split("\\s+");
            StringBuilder shortName = new StringBuilder();
            for (String word:splited){
                shortName.append(word.substring(0,1).toUpperCase());
            }
            long count = productRepository.countShortName(shortName.toString());
            StringBuilder code = new StringBuilder(shortName);
            if (count + 1 < 10){
                code.append("-HKTV0" + (count+1));
            }else{
                code.append("-HKTV" + (count+1));
            }
            Product savedProduct = productRepository.saveAndFlush(new Product(productName, shortName.toString(), code.toString(), productWeight));
            return savedProduct;

        }   catch (DataIntegrityViolationException e){
                System.out.println("[ProductServiceError] createProduct");
                throw new ProductDuplicatedException("Product Name and Product Weight combination already exits.");
        }   catch (Exception e){
                System.out.println("[ProductServiceError] createProduct");
                System.out.println(e.getMessage());
                throw e;
        }
    }

    @Override
    @Transactional
    public void deleteProduct(String productCode) {
        try{
            productRepository.deleteByCode(productCode);
        }   catch (Exception e){
            System.out.println("[ProductServiceError] deleteProduct");
            System.out.println(e.getMessage());
            throw e;
        }

    }
}
