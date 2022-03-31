package com.natanduarte.listadecompras.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.natanduarte.listadecompras.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {

    @Insert
    void save(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product;")
    List<Product> selectAll();

    @Query("DELETE FROM product;")
    void clear();
}
