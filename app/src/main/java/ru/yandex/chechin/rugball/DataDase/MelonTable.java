package ru.yandex.chechin.rugball.DataDase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MelonTable {

        public static final String TABLE_MELON = "Melon";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TEXTBANK = "bank";
        public static final String COLUMN_IDMSGSZ = "idmagazin";
        public static final String COLUMN_DATA = "dayI";
        public static final String COLUMN_SUMMA = "summa";
        public static final String COLUMN_BALANS = "balance";
        public static final String COLUMN_SMSTEXT = "smsText";

        public static final String DATABASE_CREATE = "create table "
                + TABLE_MELON
                + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_DATA + " integer not null,"
                + COLUMN_TEXTBANK + " text not null, "
                + COLUMN_IDMSGSZ + " integer not null, "
                + COLUMN_SUMMA + " integer not null, "
                + COLUMN_BALANS + " integer not null,"
                + COLUMN_SMSTEXT + " text not null "
                + ");";

        public static void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
            Log.e("PROFL", "MelonTable");
        }


        public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_MELON);
            onCreate(database);
        }
    }

