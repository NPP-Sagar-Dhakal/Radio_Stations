package com.radiostations.radio_recent;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecentDao {
    @Insert
    void addData(Recent_Radio_Items favoriteList);

    @Query("select * from favoritelist")
    List<Recent_Radio_Items> getFavoriteData();

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE stationName =:id)")
    int isFavorite(String id);

    @Delete
    void delete(Recent_Radio_Items favoriteList);


}
