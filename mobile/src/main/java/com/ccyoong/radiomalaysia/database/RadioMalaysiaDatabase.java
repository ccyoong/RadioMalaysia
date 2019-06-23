package com.ccyoong.radiomalaysia.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ccyoong.radiomalaysia.dao.FavStationDao;
import com.ccyoong.radiomalaysia.data.FavStation;

@Database(entities = {FavStation.class}, version = 1, exportSchema = false)
public abstract class RadioMalaysiaDatabase extends RoomDatabase {

    private static RadioMalaysiaDatabase INSTANCE;

    public static synchronized RadioMalaysiaDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RadioMalaysiaDatabase.class, "RADIO_MALAYSIA_DATABASE").
                    fallbackToDestructiveMigration().allowMainThreadQueries()
//                    .addCallback(new Callback() {
//                        @Override
//                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                            super.onOpen(db);
//                            getInstance(context).stationDao().deleteAll();
//
//                            getInstance(context).stationDao().insert(new Station("988", "988", "友声有色", "https://starrfm.rastream.com/starrfm-988"));
//                            getInstance(context).stationDao().insert(new Station("melody", "Melody", "心聆听, 新体会", "https://astro3.rastream.com/melody"));
//                            getInstance(context).stationDao().insert(new Station("myfm", "My Fm", "MY 好玩!", "https://astro1.rastream.com/myfm"));
//                            getInstance(context).stationDao().insert(new Station("flyfm", "Fly Fm", "Malaysia's Hottest Music", "https://mediaprima.rastream.com/mediaprima-flyfm"));
//                            getInstance(context).stationDao().insert(new Station("hitz", "Hitz Fm", "All the hitz, all the time", "https://astro1.rastream.com/hitz"));
//                            getInstance(context).stationDao().insert(new Station("onefm", "One Fm", "最 Hit!", "https://mediaprima.rastream.com/mediaprima-onefm"));
//
//
//                            token :xrcauzvbhbcbnsb1gbblbgk5vvb6bdiqovc9.xrcotpjbhbclrqeltp8bevcirqjbhcx1gct4hwf2gsblbfo4vvb6fmn2fwd3fwt3fg0.f19phucvtyexoqtsrkawujjaggicvcsnfoa1udxousj
//                            stations.add(new Station("goxuan", "GOXUAN", "GOXUAN 够fun", "https://astro3.rastream.com/astro-goxuan"));
//
//                        }
//                    })
                    .build();

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

//    public abstract StationDao stationDao();

    public abstract FavStationDao favStationDao();

}

