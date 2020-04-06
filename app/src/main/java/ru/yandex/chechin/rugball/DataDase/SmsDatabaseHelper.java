package ru.yandex.chechin.rugball.DataDase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.yandex.chechin.rugball.DataDase.MagazTable;
import ru.yandex.chechin.rugball.DataDase.MelonTable;

public class SmsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smstable.db";
    private static final int DATABASE_VERSION = 1;

    public SmsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MelonTable.DATABASE_CREATE);
        database.execSQL(MagazTable.DATABASE_MAGAZIN);
        Log.e("PROFL", "SmsDatabaseHelper");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
    }
}

