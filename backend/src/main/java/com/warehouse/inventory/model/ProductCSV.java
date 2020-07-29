package com.warehouse.inventory.model;

public class ProductCSV {
    private String productCode;
    private String productName;
    private String locationName;
    private int weight;
    private long quantity;

    public ProductCSV(String productCode, String productName, String locationName, int weight, long quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.locationName = locationName;
        this.weight = weight;
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
