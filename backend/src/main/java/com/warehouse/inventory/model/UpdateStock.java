package com.warehouse.inventory.model;

public class UpdateStock {
    private String locationName;
    private long qty;

    public UpdateStock(String locationName, long qty) {
        this.locationName = locationName;
        this.qty = qty;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }
}
