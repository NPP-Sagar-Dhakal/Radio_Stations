package com.radiostations.radio_favorites;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Favorites_Radio_Items.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();


}
