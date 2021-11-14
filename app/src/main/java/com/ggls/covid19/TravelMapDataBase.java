package com.ggls.covid19;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TravelMapDataBase {
    private static final String TABLE_NAME = "travel_map";
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";

    /**
     * get all city that @user_id has gone
     *
     * @param user_id user
     * @return all location that user has gone
     * @throws SQLException
     */
    public ArrayList<TravelMap> getMap(int user_id) throws SQLException {
        Connection con = MySQLConnection.getConnection();
        if (con == null) {
            throw new SQLException();
        }
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM " + TABLE_NAME
                        + "WHERE " + USER_ID + " = " + user_id
                        + ";"
        );

        ArrayList<TravelMap> ret = new ArrayList<>();
        while (resultSet.next()) {
            TravelMap cur = new TravelMap();
            try {
                cur.setId(resultSet.getInt(TravelMapDataBase.ID));
                cur.setUserId(resultSet.getInt(TravelMapDataBase.USER_ID));
                cur.setProvince(resultSet.getString(TravelMapDataBase.PROVINCE));
                cur.setCity(resultSet.getString(TravelMapDataBase.CITY));
                cur.setCountry(resultSet.getString(TravelMapDataBase.COUNTRY));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ret.add(cur);
        }

        return ret;
    }

    public void addLocation(TravelMap location) throws SQLException {
        Connection con = MySQLConnection.getConnection();
        if (con == null) {
            throw new SQLException();
        }
        Statement statement = con.createStatement();
        statement.execute(
                "INSERT INTO " + TABLE_NAME
                        + "VALUES (" + "null, "
                        + "'" + location.getUserId() + "', "
                        + "'" + location.getProvince() + "', "
                        + "'" + location.getCity() + "', "
                        + "'" + location.getCountry() + "'"
                        + ");"
        );
    }

}
