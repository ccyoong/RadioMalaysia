package com.ccyoong.radiomalaysia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ccyoong.radiomalaysia.data.FavStation;

import java.util.List;

@Dao
public interface FavStationDao {


    @Query("SELECT * FROM FAV_STATION")
    List<FavStation> findAll();


    @Query("SELECT * FROM FAV_STATION WHERE FAV_STATION_ID = :id")
    FavStation findByPk(Long id);


    @Query("SELECT * FROM FAV_STATION WHERE STATION_ID = :stationId")
    List<FavStation> findByStationId(String stationId);

    @Insert
    void insert(FavStation FavStation);

    @Delete
    void delete(FavStation FavStation);

    @Query("DELETE FROM FAV_STATION")
    void deleteAll();

}
