package com.ccyoong.radiomalaysia.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FAV_STATION")
public class FavStation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "FAV_STATION_ID")
    private Long favStationId;

    @ColumnInfo(name = "STATION_ID")
    private String stationId;


    public Long getFavStationId() {
        return favStationId;
    }

    public void setFavStationId(Long favStationId) {
        this.favStationId = favStationId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
