package ru.yandex.chechin.rugball.DataDase;

import android.database.sqlite.SQLiteDatabase;

public class MagazTable {
    public static final String TABLE_MAGAZ = "rita";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MAGAZKR = "krmagaz";
    public static final String COLUMN_MAGAZ = "magaz";

    public static final String DATABASE_MAGAZIN = "create table "
            + TABLE_MAGAZ
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MAGAZKR + " text not null, "
            + COLUMN_MAGAZ + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_MAGAZIN);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MAGAZ);
        onCreate(database);
    }

    public class DATABASE_CREATE {
    }
}
