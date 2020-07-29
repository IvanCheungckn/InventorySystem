package com.warehouse.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product", uniqueConstraints = { @UniqueConstraint( columnNames = { "name", "weight" } )})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Stock> stockList;

    public Product(long id, String name, String code, int weight) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.weight = weight;
    }

    public Product(long id, String name, String shortName, String code, int weight) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.weight = weight;
    }

    public Product(String name, String code, int weight) {
        this.name = name;
        this.code = code;
        this.weight = weight;
    }

    public Product(String name, String shortName, String code, int weight) {
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.weight = weight;
    }

    public Product(long id, String name, String shortName, String code, int weight, LocalDateTime createdAt, LocalDateTime updatedAt, List<Stock> stockList) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.weight = weight;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stockList = stockList;
    }

    public Product(long id, String name, String shortName, String code, int weight, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.weight = weight;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Product() {
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", weight=" + weight +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
