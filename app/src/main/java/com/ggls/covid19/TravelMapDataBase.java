package com.ggls.covid19;

import android.database.SQLException;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TravelMapDataBase {
    private static final Map<String, String> map = new HashMap<String, String>() {{
        put("黑龙江", "Yellow");
        put("辽宁", "Yellow");
        put("云南", "Yellow");
        put("台湾", "Red");
        put("陕西", "Green");
        put("广东", "Green");
        // TODO more province is needed here
    }};



    private static final String TABLE_NAME = "travel_map";
    private static final String ID = "id";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";

    private static final String TAG = "MAP_DB";

    private int user_id;

    public void setUserID(int id) {
        user_id = id;
    }

    public void addLocation(TravelMap travelMap) {

    }

    public TravelMap getStatus() {
        return null;
    }



    class MapThread extends Thread {
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
                        + TravelMapDataBase.ID
                        + " = "
                        + user_id
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
