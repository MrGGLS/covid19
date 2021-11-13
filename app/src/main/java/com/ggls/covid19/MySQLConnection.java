package com.ggls.covid19;

import android.util.Log;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {
    // 数据库users的相关
//    public static final String tableNameOfUsers = "users";
//    public static final String userID = "_id";
//    public static final String userName = "user_name";
//    public static final String userStatus = "user_status";
//    public static final String userPassword = "password";

    // 数据库map相关

    // 连接数据库使用
    private static final String TAG = "tag_of_remote_database";
    private String driver;
    private String dbURL = "";
    private String user = "";
    private String password;
    private static MySQLConnection connection = null;

    private MySQLConnection() throws Exception {
        driver = "com.mysql.jdbc.Driver";
        dbURL = "jdbc:mysql://" +
                "rm-2vccl279itesf75k33o.mysql.cn-chengdu.rds.aliyuncs.com:3306" +
                "/android_app_db";
        user = "covid_19_users";
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

    public static String[] queryDataBase(String database, String query) throws SQLException {
        String[] ret = new String[0];
        Connection con = getConnection();
        if (con == null) {
            throw new SQLException();
        }

        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            // TODO
        }

        statement.close();
        con.close();
        return ret;
    }

}
