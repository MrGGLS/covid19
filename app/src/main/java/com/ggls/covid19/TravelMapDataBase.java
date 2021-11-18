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
        public String status;
    }

    private static ArrayList<MapDBItem> msg;

    public void addLocation(TravelMap travelMap) {

    }

    public ArrayList<MapDBItem> getStatusList() {
        return msg;
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
                                + UserDataBase.currentUser.getName()
                                + ";"
                );
                while (res.next()) {

                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
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
                ResultSet res = stat.executeQuery(
                        "SELECT * FROM "
                                + TravelMapDataBase.TABLE_NAME
                                + "WHERE "
                                + TravelMapDataBase.USER_NAME
                                + " = "
                                + UserDataBase.currentUser.getName()
                                + ";"
                );
                while (res.next()) {
                    TravelMap map = new TravelMap(
//                            res.getString()
                    );
                }

            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

        }
    }
}

