package com.ccyoong.radiomalaysia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ccyoong.radiomalaysia.data.Station;

import java.util.List;

@Dao
public interface StationDao {


    @Query("SELECT * FROM STATION")
    List<Station> findAll();

    @Insert
    void insert(Station station);

    @Query("DELETE FROM STATION")
    void deleteAll();


}
