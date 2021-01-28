package com.radiostations.radio_favorites;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert
    void addData(Favorites_Radio_Items favoriteList);

    @Query("select * from favoritelist")
    List<Favorites_Radio_Items> getFavoriteData();

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE stationName =:id)")
    int isFavorite(String id);

    @Delete
    void delete(Favorites_Radio_Items favoriteList);


}
