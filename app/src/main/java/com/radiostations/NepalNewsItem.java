package com.radiostations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NepalNewsItem {

    @SerializedName("NepalNews")
    public List<NepalNews> NepalNews;

    public static class NepalNews {
        @SerializedName("NepaliRadios")
        public List<NepaliRadios> NepaliRadios;

        public static class NepaliRadios {
            @SerializedName("stationName")
            public String stationName;
            @SerializedName("stationDetail")
            public String stationDetail;
            @SerializedName("stationimage")
            public String stationimage;
            @SerializedName("stationLink")
            public String stationLink;
            @SerializedName("stationLocation")
            public String stationLocation;
        }
    }
}
