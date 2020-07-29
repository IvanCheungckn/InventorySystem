package com.warehouse.inventory.controller;

import com.warehouse.inventory.entity.Product;
import com.warehouse.inventory.model.Response;
import com.warehouse.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/product")
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createProduct(@RequestPart("productName") String productName,
                                        @RequestPart("productWeight") String productWeight) {

        Product savedProduct = productService.createProduct(productName, Integer.parseInt(productWeight));
        return new ResponseEntity(new Response(true, savedProduct), HttpStatus.OK);
    }

    @GetMapping("{productCode}")
    public ResponseEntity getProducts(@PathVariable("productCode") String productCode) {
        Product product = productService.getProductByCode(productCode);
        return new ResponseEntity(new Response(true, product), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        return new ResponseEntity(new Response(true, productList), HttpStatus.OK);
    }

    @DeleteMapping("{productCode}")
    public ResponseEntity deleteProduct(@PathVariable("productCode") String productCode){
        productService.deleteProduct(productCode);
        return new ResponseEntity(new Response(true, "successfully deleted"), HttpStatus.OK);
    }

}
