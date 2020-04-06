package ru.yandex.chechin.rugball.Service;


import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import ru.yandex.chechin.rugball.DataDase.MagazTable;
import ru.yandex.chechin.rugball.DataDase.MelonTable;
import ru.yandex.chechin.rugball.DataDase.SmsDatabaseHelper;

import static ru.yandex.chechin.rugball.DataDase.MagazTable.COLUMN_ID;
import static ru.yandex.chechin.rugball.DataDase.MagazTable.TABLE_MAGAZ;
import static ru.yandex.chechin.rugball.DataDase.MelonTable.COLUMN_IDMSGSZ;
import static ru.yandex.chechin.rugball.DataDase.MelonTable.TABLE_MELON;

public class SmsContentProvider extends ContentProvider {
    private SmsDatabaseHelper database;

    private static final int MESSAGES = 10;
    private static final int MESSAGE_ID = 20;
    private static final int COLUMN_MAGAZ_ID = 30;
    private static final int MAGAZ_DATA = 40;
    public static final String AUTHORITY = "ru.yandex.chechin.rugball.Service";

    public static final String BASE_PATH = "sms";
    public static final String BASE_MAGAZ = "magsz";
    public static final String SMS_MAGAZ = "sms_magsz";
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/messages";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/message";
/*
* разбирает запрос */
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MESSAGES);
        sURIMatcher.addURI(AUTHORITY,  BASE_PATH+ "/#", MESSAGE_ID);
        sURIMatcher.addURI(AUTHORITY,  BASE_MAGAZ+ "/*", COLUMN_MAGAZ_ID);
        sURIMatcher.addURI(AUTHORITY, SMS_MAGAZ + "/#", MAGAZ_DATA);
    }

    @Override
    public boolean onCreate() {
        database = new SmsDatabaseHelper(getContext());
        return false;
    }
/*
* занимается созданием определенного курсора для определеного запроса */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = database.getWritableDatabase();
        String sQL;
        Boolean brefresh = false;
        String veri = " ";
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MESSAGES:
                sQL = "SELECT a."+ COLUMN_ID +" , "+ MelonTable.COLUMN_DATA +" , "+ MelonTable.COLUMN_TEXTBANK+" , " + MelonTable.COLUMN_SUMMA +" , " +MelonTable.COLUMN_SMSTEXT+" ,b.krmagaz " +
                        " FROM " +TABLE_MELON+" a,"+TABLE_MAGAZ+" b " +
                        " where "+COLUMN_IDMSGSZ+" = b."+COLUMN_ID+"  "+veri+";";
                brefresh = true ;
                break;
            case MESSAGE_ID:
                sQL = "SELECT a."+ COLUMN_ID +" , "+ MelonTable.COLUMN_DATA +" , "+ MelonTable.COLUMN_TEXTBANK+" , " + MelonTable.COLUMN_SUMMA +" ,b.krmagaz " +" , " +MelonTable.COLUMN_SMSTEXT+
                        " FROM " +TABLE_MELON+" a,"+TABLE_MAGAZ+" b " +
                        " where "+COLUMN_IDMSGSZ+" = b."+COLUMN_ID+"  "+veri+"  and a." +MelonTable.COLUMN_ID + "=" + uri.getLastPathSegment();
                break;
            case COLUMN_MAGAZ_ID:
                sQL = "SELECT _id FROM rita where magaz  ='" + uri.getLastPathSegment() + "';";
                int magszin = 0;
                Cursor query9 = db.rawQuery("SELECT _id FROM rita where magaz  ='" + uri.getLastPathSegment() + "';", null);
                if (query9.moveToFirst()) {
                    magszin = query9.getInt(0);



                } else {
                    db.execSQL("INSERT INTO rita (krmagaz , magaz ) VALUES ('" + uri.getLastPathSegment() + "','" + uri.getLastPathSegment() + "');");
                    Cursor query8 = db.rawQuery("SELECT _id FROM rita where magaz  ='" + uri.getLastPathSegment() + "';", null);
                    if (query8.moveToFirst()) {
                        magszin = query8.getInt(0);
                    }

                }
                break;
           case MAGAZ_DATA:
                sQL = " SELECT _id , bank,summa,dayI FROM Melon WHERE idmagazin = " +uri.getLastPathSegment() ;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        Cursor query3 = db.rawQuery(sQL, null);
        if (brefresh)query3.setNotificationUri(getContext().getContentResolver(), uri);
        return query3;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MESSAGES:
                id = sqlDB.insert(TABLE_MELON, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = {

                COLUMN_ID,
                MagazTable.COLUMN_MAGAZKR,
                MagazTable.COLUMN_MAGAZ,
                MelonTable.COLUMN_TEXTBANK,
                COLUMN_IDMSGSZ,
                MelonTable.COLUMN_DATA,
                MelonTable.COLUMN_SUMMA,
                MelonTable.COLUMN_BALANS,
                MelonTable.COLUMN_SMSTEXT
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}

