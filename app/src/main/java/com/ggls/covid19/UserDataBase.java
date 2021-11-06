package com.ggls.covid19;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDataBase extends SQLiteOpenHelper {
    public static final String tableName = "users";
    public static final String id = "_id";
    public static final String name = "user_name";
    public static final String travelMapID = "travel_map_id";
    public static final String status = "user_status";
    public static final String password = "password";

    public UserDataBase(@Nullable Context context,
                        @Nullable String name,
                        @Nullable SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tableName
                + "("
                + id + " SERIAL PRIMARY KEY NOT NULL, "
                + name + " VARCHAR(30), "
                + travelMapID + " INT, "
                + status + " VARCHAR(10), "
                + password + " VARCHAR(50) "
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
