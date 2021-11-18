package com.ggls.covid19;

import android.database.SQLException;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TravelMapDataBase {
    private static final String TABLE_NAME = "travel_map";
    private static final String ID = "id";
    private static final String USER_NAME = "user_name";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private static final String TAG = "MAP_DB";

    class MapDBItem {
        public String province;
        public String city;
        public double latitude;
        public double longitude;
        public Status status;

        public MapDBItem(String province, String city, double latitude, double longitude, Status status) {
            this.province = province;
            this.city = city;
            this.latitude = latitude;
            this.longitude = longitude;
            this.status = status;
        }
    }

    private static ArrayList<MapDBItem> msg;
    private static TravelMap input_travel_map;

    public void addLocation(TravelMap travelMap) {
        input_travel_map = travelMap;
        AddLocationThread th = new AddLocationThread();
        th.start();
//        th.join();
    }

    public ArrayList<MapDBItem> getStatusList() {
        GetStatusListThread th = new GetStatusListThread();
        th.start();
//        th.join();
        return msg;
    }


    class AddLocationThread extends Thread {
        @Override
        public void run() {
            try {
                Connection conn = MySQLConnection.getConnection();
                if (conn == null) {
                    throw new SQLException();
                }
                Log.i(TAG, "数据库连接成功");
                Statement stat = conn.createStatement();
                stat.execute(
                        "INSERT INTO "
                                + TravelMapDataBase.TABLE_NAME
                                + " VALUES(null, "
                                + "'" + UserDataBase.currentUser.getName() + "', "
                                + "'" + input_travel_map.getProvince() + "', "
                                + "'" + input_travel_map.getCity() + "', "
                                + "'" + input_travel_map.getLatitude() + "', "
                                + "'" + input_travel_map.getLongitude() + "'"
                                + ");"
                );
                ResultSet res = stat.executeQuery(
                        "SELECT status FROM color_map WHERE color = "
                                + "'" + input_travel_map.getProvince() + "'"
                                + ";"
                );
                // size of res must be 1.
                while (res.next()) {
                    String color = res.getString("color");
                    if (color.equals("Red")) {
                        UserDataBase.currentUser.setStatus(Status.RED);
                    } else if (color.equals("Yellow") &&
                            UserDataBase.currentUser.getStatus() == Status.GREEN) {
                        UserDataBase.currentUser.setStatus(Status.YELLOW);
                    }
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    class GetStatusListThread extends Thread {
        @Override
        public void run() {
            try {
                Connection conn = MySQLConnection.getConnection();
                if (conn == null) {
                    throw new SQLException();
                }
                Log.i(TAG, "数据库连接成功");
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(
                        "SELECT * FROM "
                                + TravelMapDataBase.TABLE_NAME
                                + "WHERE "
                                + TravelMapDataBase.USER_NAME
                                + " = "
                                + "'" + UserDataBase.currentUser.getName() + "'"
                                + ";"
                );
                while (res.next()) {
                    ResultSet status = stat.executeQuery(
                            "SELECT status FROM color_map "
                                    + "WHERE province = "
                                    + "'" + res.getString(TravelMapDataBase.PROVINCE) + "'"
                                    + ";"
                    );
                    Status retStatus = null;
                    switch (status.getString("color")) {
                        case "Green":
                            retStatus = Status.GREEN;
                            break;
                        case "Red":
                            retStatus = Status.RED;
                            break;
                        case "Yellow":
                            retStatus = Status.YELLOW;
                            break;
                    }
                    msg.add(new MapDBItem(
                            res.getString(TravelMapDataBase.PROVINCE),
                            res.getString(TravelMapDataBase.CITY),
                            res.getDouble(TravelMapDataBase.LATITUDE),
                            res.getDouble(TravelMapDataBase.LONGITUDE),
                            retStatus
                    ));
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }
    }
}

