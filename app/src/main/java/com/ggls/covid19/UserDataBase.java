package com.ggls.covid19;


import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDataBase {
    public static final String TABLE_NAME = "users";
    public static final String ID = "id"; /* primary key */
    public static final String USER_NAME = "user_name";
    public static final String USER_STATUS = "status";
    public static final String PASSWORD = "password";

    private static final String TAG = "database";

    // login user
    public static User currentUser;
    private String loginUserName;
    private String loginPassword;


    /**
     * query user information from database
     *
     * @param username username
     * @return result of query, could more than one
     * @throws SQLException
     */
    private ArrayList<User> getUser(String username) throws SQLException {
        Connection con = MySQLConnection.getConnection();
        if (con == null) {
            throw new SQLException();
        }
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM " + TABLE_NAME
                        + "WHERE " + USER_NAME + " = " + "'" + username + "'"
                        + ";"
        );

        ArrayList<User> ret = new ArrayList<>();
        while (resultSet.next()) {
            User cur = new User();
            try {
                cur.setId(resultSet.getInt(UserDataBase.ID));
                cur.setName(resultSet.getString(UserDataBase.USER_NAME));
                cur.setStatusWithString(resultSet.getString(UserDataBase.USER_STATUS));
                cur.setPassword(resultSet.getString(UserDataBase.PASSWORD));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ret.add(cur);
        }

        return ret;
    }

    /**
     * compare input password and real password to check if it is the same inorder to
     * identify the user
     */
    public Boolean userLogin() throws InterruptedException {
        LoginThread loginThread = new LoginThread();
        loginThread.start();
        loginThread.join();
        return currentUser != null;
    }

    // ????????????
    public boolean userSignUp() throws InterruptedException {
        SignUpThread signUpThread = new SignUpThread();
        signUpThread.start();
        signUpThread.join();
        return currentUser != null;
    }

    public String getName() {
        return currentUser.getName();
    }

    public Status getStatus() {
        return currentUser.getStatus();
    }

    public void setStatus(Status status) {
        currentUser.setStatus(status);
    }

    public void preLogin(String userName, String password) {
        this.loginUserName = userName;
        this.loginPassword = password;
    }

    class LoginThread extends Thread {
        @Override
        public void run() {
            try {
                Connection con = MySQLConnection.getConnection();
                if (con == null) {
                    throw new SQLException();
                }
                Log.i(TAG, "?????????????????????");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM "
                                + UserDataBase.TABLE_NAME
                                + " WHERE "
                                + UserDataBase.USER_NAME
                                + " = "
                                + "'" + loginUserName + "'"
                                + ";"
                );
                Log.i(TAG, "????????????");
                ArrayList<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    User cur = new User();
                    try {
                        cur.setName(resultSet.getString(UserDataBase.USER_NAME));
                        cur.setStatusWithString(resultSet.getString(UserDataBase.USER_STATUS));
                        cur.setPassword(resultSet.getString(UserDataBase.PASSWORD));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    users.add(cur);
                }
                for (User user : users) {
                    Log.i("database", "user??? " + user.getName());
                    Log.i(TAG, "password: " + user.getPassword());
                    Log.i(TAG, "login password: " + loginPassword);
                    if (user.getPassword().equals(loginPassword)) {
                        // ?????????????????????????????????
                        currentUser = user;
                        Log.i("database", "??????????????????");
                        return;
                    }
                }
                Log.i("database", "database: error");
                currentUser = null;
                resultSet.close();
                statement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    class SignUpThread extends Thread {
        @Override
        public void run() {
            try {
                Connection con = MySQLConnection.getConnection();
                if (con == null) {
                    throw new SQLException();
                }
                Log.i(TAG, "?????????????????????");
                Statement statement = con.createStatement();
                statement.execute(
                        "INSERT INTO "
                                + UserDataBase.TABLE_NAME
                                + " VALUES("
                                + "'"
                                + loginUserName
                                + "', "
                                + "'"
                                + loginPassword
                                + "', "
                                + "'Green'"
                                + ");"
                );
                Log.i(TAG, "????????????");
                currentUser = new User(loginUserName, loginPassword);
                statement.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
