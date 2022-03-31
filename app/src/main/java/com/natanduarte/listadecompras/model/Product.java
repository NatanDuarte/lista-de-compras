package com.natanduarte.listadecompras.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String name;
    private int quantity;

    public Product() {}

    @NonNull
    @Override
    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("%d --- %s", quantity, name);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0)
            this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasValidId() {
        return this.id > 0;
    }
}