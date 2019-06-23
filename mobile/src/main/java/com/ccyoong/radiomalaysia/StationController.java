package com.ccyoong.radiomalaysia;

import com.ccyoong.radiomalaysia.application.MyApplication;
import com.ccyoong.radiomalaysia.data.FavStation;
import com.ccyoong.radiomalaysia.data.Station;
import com.ccyoong.radiomalaysia.database.RadioMalaysiaDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class StationController {


    private static Map<String, Station> stationMap = new HashMap<>();
    private static List<Station> stations = new ArrayList<>();
    private static List<FavStation> favStations;

    static {

        stations.add(new Station("988", "988", "友声有色", "https://starrfm.rastream.com/starrfm-988"));
        stations.add(new Station("melody", "Melody", "心聆听, 新体会", "https://astro3.rastream.com/melody"));
        stations.add(new Station("myfm", "My Fm", "MY 好玩!", "https://astro1.rastream.com/myfm"));
        stations.add(new Station("flyfm", "Fly Fm", "Malaysia's Hottest Music", "https://mediaprima.rastream.com/mediaprima-flyfm"));
        stations.add(new Station("hitz", "Hitz Fm", "All the hitz, all the time", "https://astro1.rastream.com/hitz"));
        stations.add(new Station("onefm", "One Fm", "最 Hit!", "https://mediaprima.rastream.com/mediaprima-onefm"));

        //token :xrCauZvbHbCBNsB1GbBlBgK5vVB6BdiQOVC9.xrCotPJbHbClRQeltP8bEVCiRQJbHcX1GcT4HWF2GsBlBfO4vVB6FMN2FWd3FWT3Fg0.f19phuCvTYExOqTsrKaWUJJaggiCVcSnFOa1UDXousJ
        //stations.add(new Station("goxuan", "GOXUAN", "GOXUAN 够fun", "https://astro3.rastream.com/astro-goxuan"));

        for (Station station : stations) {
            stationMap.put(station.getId(), station);
        }
    }

    public static Station getStationById(String id) {
        return stationMap.get(id);
    }

    public static List<Station> getStations() {
        return stations;
    }

    public static List<FavStation> getFavStations() {
        if (favStations == null) {
            favStations = RadioMalaysiaDatabase.getInstance(MyApplication.getAppContext()).favStationDao().findAll();
        }
        return favStations;
    }

    public static boolean isFavStations(String id) {
        boolean isFav = false;
        favStations = getFavStations();

        for (FavStation favStation : favStations) {
            if (favStation.getStationId().equals(id)) {
                isFav = true;
                break;
            }
        }
        return isFav;
    }

    public static void addFavStation(String id) {
        FavStation newFav = new FavStation();
        newFav.setStationId(id);
        RadioMalaysiaDatabase.getInstance(MyApplication.getAppContext()).favStationDao().insert(newFav);
        favStations.add(newFav);
    }

    public static void deleteFavStations(String id) {

        List<FavStation> favStationList = RadioMalaysiaDatabase.getInstance(MyApplication.getAppContext()).favStationDao().findByStationId(id);
        if (favStationList != null && favStationList.size() > 0) {
            for (FavStation fav : favStationList) {
                RadioMalaysiaDatabase.getInstance(MyApplication.getAppContext()).favStationDao().delete(fav);
                Iterator<FavStation> itr = favStations.iterator();
                while (itr.hasNext()) {
                    FavStation favStation = itr.next();
                    if (favStation.getStationId().equals(id)) {
                        itr.remove();
                    }
                }
            }
        }
    }


    public static Station getNextStation(String id) {
        Station station = stationMap.get(id);
        Timber.i(station.getName());
        int i = stations.indexOf(station);
        Timber.i(i + "");
        if (++i == stations.size()) {
            i = 0;
        }
        Timber.i(i + "");
        return stations.get(i);
    }

    public static Station getPreviousStation(String id) {
        Station station = stationMap.get(id);
        Timber.i(station.getName());
        int i = stations.indexOf(station);
        Timber.i(i + "");
        if (--i < 0) {
            i = stations.size() - 1;
        }
        Timber.i(i + "");
        return stations.get(i);
    }
}
