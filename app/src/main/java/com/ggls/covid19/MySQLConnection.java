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

    private MySQLConnection() {
        driver = "com.mysql.jdbc.Driver";
        dbURL = "";
        user = "";
        password = "";
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            connection = new MySQLConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
