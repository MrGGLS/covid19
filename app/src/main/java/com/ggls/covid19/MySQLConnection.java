package com.ggls.covid19;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
    private String driver = "";
    private String dbURL = "";
    private String user = "";
    private String password = "";

    private static MySQLConnection connection = null;

    private MySQLConnection() throws Exception {
        driver = "com.mysql.jdbc.Driver";
        dbURL = "jdbc:mysql://" +
                "rm-2vccl279itesf75k33o.mysql.cn-chengdu.rds.aliyuncs.com" + "/" +
                "covid_19_users";
        user = "android_app_db";
        password = "covid_19";
    }

    public static Connection getConnection() {
        Connection conn;
        if (connection == null) {
            try {
                connection = new MySQLConnection();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            Class.forName(connection.driver);
            conn = DriverManager.getConnection(connection.dbURL, connection.user, connection.password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return conn;
    }
}
