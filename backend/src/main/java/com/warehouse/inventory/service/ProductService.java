package com.warehouse.inventory.service;

import com.warehouse.inventory.entity.Product;
import java.util.List;

public interface ProductService {
    Product getProductByCode(String productCode);
    Product createProduct(String productName, int productWeight);
    List<Product> getAllProducts();
    void deleteProduct(String productCode);
}
