package com.ggls.covid19;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CRUD {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private static final String[] columns = {
            UserDataBase.id,
            UserDataBase.name,
            UserDataBase.travelMapID,
            UserDataBase.status,
            UserDataBase.userID,
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

}
