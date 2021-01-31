package com.radiostations.radio_recent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favoritelist")
public class Recent_Radio_Items {
    @PrimaryKey
    @NonNull
    public String stationName;
    @ColumnInfo(name = "detail")
    public String stationDetail;
    @ColumnInfo(name = "image")
    public String stationimage;
    @ColumnInfo(name = "link")
    public String stationLink;
    @ColumnInfo(name = "location")
    public String stationLocation;

    @NonNull
    public String getStationName() {
        return stationName;
    }

    public void setStationName(@NonNull String stationName) {
        this.stationName = stationName;
    }

    public String getStationDetail() {
        return stationDetail;
    }

    public void setStationDetail(String stationDetail) {
        this.stationDetail = stationDetail;
    }

    public String getStationimage() {
        return stationimage;
    }

    public void setStationimage(String stationimage) {
        this.stationimage = stationimage;
    }

    public String getStationLink() {
        return stationLink;
    }

    public void setStationLink(String stationLink) {
        this.stationLink = stationLink;
    }

    public String getStationLocation() {
        return stationLocation;
    }

    public void setStationLocation(String stationLocation) {
        this.stationLocation = stationLocation;
    }
}