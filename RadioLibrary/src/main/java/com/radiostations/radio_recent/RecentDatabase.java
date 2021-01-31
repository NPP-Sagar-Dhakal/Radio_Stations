package com.radiostations.radio_recent;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recent_Radio_Items.class}, version = 1, exportSchema = false)
public abstract class RecentDatabase extends RoomDatabase {

    public abstract RecentDao favoriteDao();


}
