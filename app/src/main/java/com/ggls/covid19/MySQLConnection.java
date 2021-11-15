package com.ggls.covid19;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;


public class MySQLConnection {
    private static final String TAG = "tag_of_remote_database";
    private String driver;
    private String dbURL = "";
    private String user = "";
    private String password;
    private static MySQLConnection connection = null;

    private MySQLConnection() throws Exception {
        driver = "com.mysql.cj.jdbc.Driver";
        dbURL = "jdbc:mysql://" +
                "rm-2vccl279itesf75k33o.mysql.cn-chengdu.rds.aliyuncs.com:3306" +
                "/covid_19_users?" +
                "allowPublicKeyRetrieval=true";
        user = "android_app_db";
        password = "covid_19";
    }

    public static Connection getConnection() {
        Connection con = null;
        if (con == null) {
            try {
                connection = new MySQLConnection();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            Class.forName(connection.driver);
            con = DriverManager.getConnection(connection.dbURL, connection.user, connection.password);
            Log.i(TAG, "successfully connect to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

}
