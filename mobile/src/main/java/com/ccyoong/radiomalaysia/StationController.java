package com.ccyoong.radiomalaysia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationController {


    private static Map<String, Station> stationMap = new HashMap<>();
    private static List<Station> stations = new ArrayList<>();

    static {
        stations.add(new Station("988", "988", "友声有色", "https://starrfm.rastream.com/starrfm-988"));
        stations.add(new Station("melody", "Melody", "心聆听, 新体会", "https://astro3.rastream.com/melody"));
        stations.add(new Station("myfm", "My Fm", "MY 好玩!", "https://astro1.rastream.com/myfm"));
        stations.add(new Station("hitz", "Hitz Fm", "All the hitz, all the time", "https://astro1.rastream.com/hitz"));
        stations.add(new Station("flyfm", "Fly Fm", "Malaysia's Hottest Music", "https://mediaprima.rastream.com/mediaprima-flyfm"));
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

    public static Station getNextStation(String id) {
        Station station = stationMap.get(id);
        int i = stations.indexOf(station);
        if (++i == stations.size()) {
            i = 0;
        }
        return stations.get(i);
    }

    public static Station getPreviousStation(String id) {
        Station station = stationMap.get(id);
        int i = stations.indexOf(station);
        if (--i < 0) {
            i = stations.size() - 1;
        }
        return stations.get(i);
    }
}
