package com.ggls.covid19;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class UserDatabaseHandler {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    public static final int DATABASE_VERSION = 1;

    private static final String[] columns = {
            UserDataBase.id,
            UserDataBase.name,
            UserDataBase.travelMapID,
            UserDataBase.status,
            UserDataBase.password
    };

    public UserDatabaseHandler(@Nullable Context context) {
        dbHelper = new UserDataBase(context, "user_database", null, DATABASE_VERSION);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User addUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDataBase.name, user.getName());
        contentValues.put(UserDataBase.status, user.getStatus().toString());
        contentValues.put(UserDataBase.travelMapID, user.getTravelMapID());
        contentValues.put(UserDataBase.password, user.getPassword());

        long insertID = db.insert(UserDataBase.tableName, null, contentValues);
        user.setId(insertID);
        return user;
    }

    public void removeUser(User user) {
        db.delete(UserDataBase.tableName,
                UserDataBase.id + "=" + user.getUserID(),
                new String[]{String.valueOf(user.getId())}
        );
    }

    public int updateUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDataBase.name, user.getName());
        contentValues.put(UserDataBase.status, user.getStatus().toString());
        contentValues.put(UserDataBase.travelMapID, user.getTravelMapID());
        contentValues.put(UserDataBase.password, user.getPassword());

        return db.update(UserDataBase.tableName, contentValues, UserDataBase.id + "=?", null);
    }

    public User getUser(long id) {
        Cursor cursor =
                db.query(UserDataBase.tableName,
                        columns,
                        UserDataBase.id + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null,
                        null
                );

        if (cursor != null)
            cursor.moveToFirst();
        else
            ; // throw exception;

        assert cursor != null;
        String status = cursor.getString(4);
        Status s = null;

        switch (status) {
            case "Red":
                s = Status.RED;
                break;
            case "Yellow":
                s = Status.YELLOW;
                break;
            case "Green":
                s = Status.GREEN;
                break;
            default:
                // throw exception
        }

        User user = new User(cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3),
                s,
                cursor.getString(5),
                cursor.getString(6));

        return user;
    }
}
