package com.ggls.covid19;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Objects;

public class CRUD {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private static final String[] columns = {
            UserDataBase.id,
            UserDataBase.name,
            UserDataBase.status,
            UserDataBase.password
    };

    public CRUD(@Nullable Context context,
                @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory,
                int version) {
        dbHelper = new UserDataBase(context, name, factory, version);
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
        contentValues.put(UserDataBase.status, user.getStatusAsString());
        contentValues.put(UserDataBase.password, user.getPassword());

        long insertID = db.insert(UserDataBase.tableName, null, contentValues);
        user.setId(insertID);
        return user;
    }

    public void remove(User user) {
        db.delete(UserDataBase.tableName,
                UserDataBase.id + "=" + user.getUserID(),
                new String[]{String.valueOf(user.getId())}
        );
    }

    public int update(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDataBase.name, user.getName());
        contentValues.put(UserDataBase.status, user.getStatusAsString());
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

        if (status.equals("Red")) {
            s = Status.RED;
        } else if (status.equals("Yellow")) {
            s = Status.YELLOW;
        } else if (status.equals("Green")) {
            s = Status.GREEN;
        }

        if (s == null) {
            // TODO: error handler
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
