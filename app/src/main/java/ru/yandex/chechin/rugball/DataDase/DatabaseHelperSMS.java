package ru.yandex.chechin.rugball.DataDase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperSMS extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BankiN.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "BankiN"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "telifon";

    public DatabaseHelperSMS(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE IF NOT EXISTS users ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,bank TEXT, idmagazin INTEGER, dayI TEXT, summa INTEGER, balance INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS msgazin ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,krmagazin TEXT, magazin TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS test1 ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,test2 TEXT, test3 TEXT)");

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT, " + COLUMN_YEAR + " INTEGER);");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ", " + COLUMN_YEAR  + ") VALUES ('Мама', 79163435607);");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ", " + COLUMN_YEAR  + ") VALUES ('Я', 79152774474);");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ", " + COLUMN_YEAR  + ") VALUES ('СберБанк', 900);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
