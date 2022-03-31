package com.natanduarte.listadecompras.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.natanduarte.listadecompras.database.dao.ProductDAO;
import com.natanduarte.listadecompras.model.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {

    public abstract ProductDAO getRoomProductDAO();

    public static ProductDatabase getInstance(Context context) {
        String DATABASE_NAME = "products.db";
        return Room
                .databaseBuilder(
                        context,
                        ProductDatabase.class,
                        DATABASE_NAME
                )
                .allowMainThreadQueries()
                .build();
    }
}
