package com.ggls.covid19;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDataBase {
    public static final String tableName = "users";
    public static final String id = "_id";
    public static final String name = "user_name";
    public static final String status = "user_status";
    public static final String password = "password";

    private User currentUser;


    // 数据库查找用户
    public ArrayList<User> getUser(String username) throws SQLException {
        Connection con = MySQLConnection.getConnection();
        if (con == null) {
            throw new SQLException();
        }
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM " + tableName
                        + "WHERE " + name + " = " + "'" + username + "'"
                        + ";"
        );

        ArrayList<User> ret = new ArrayList<>();
        while (resultSet.next()) {
            User cur = new User();
            try {
                cur.setId(resultSet.getLong(UserDataBase.id));
                cur.setName(resultSet.getString(UserDataBase.name));
                cur.setStatusWithString(resultSet.getString(UserDataBase.status));
                cur.setPassword(resultSet.getString(UserDataBase.password));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ret.add(cur);
        }

        return ret;
    }

    // 比对用户名和密码与数据库信息是否一致
    public Boolean userLogin(String username, String password) throws SQLException {
        ArrayList<User> users = getUser(username);
        for (User user : users) {
            if (user.getPassword().equals(password)) {
                // 密码验证正确，登陆成功
                this.currentUser = user;
                return true;
            }
        }
        currentUser = null;
        return false;
    }


    // 数据库增加用户
    public void addUser(User newUser) throws SQLException {
        Connection con = MySQLConnection.getConnection();
        if (con == null) {
            throw new SQLException();
        }
        Statement statement = con.createStatement();
        statement.execute(
                "INSERT INTO " + tableName
                        + "VALUES ('" + newUser.getName() + "', "
                        + "'" + newUser.getStatus().toString() + "', "
                        + "'" + newUser.getPassword() + "'"
                        + ");"
        );
    }

    // 用户注册
    public void userSignUp(String username, String password) throws SQLException {
        User newUser = new User(username, password);
        addUser(newUser);
        this.currentUser = newUser;
    }
}
