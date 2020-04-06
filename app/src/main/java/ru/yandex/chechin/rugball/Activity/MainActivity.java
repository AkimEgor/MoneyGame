package ru.yandex.chechin.rugball.Activity;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import ru.yandex.chechin.rugball.DataDase.MagazTable;
import ru.yandex.chechin.rugball.DataDase.MelonTable;
import ru.yandex.chechin.rugball.R;
import ru.yandex.chechin.rugball.Service.SMSMonitor;
import ru.yandex.chechin.rugball.Service.SmsContentProvider;


public class MainActivity extends ListActivity /*ListFragment*/ implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;
    private SMSMonitor smsMonitor;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_list);
        fillData();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            smsMonitor = new SMSMonitor();
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
            this.registerReceiver(smsMonitor, intentFilter);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
// YES!!
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                smsMonitor = new SMSMonitor();
                IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
                this.registerReceiver(smsMonitor, intentFilter);
            }*/
        }
    }

    @Override
    /*
    * закывавет активность */
        public void onDestroy(){
        this.unregisterReceiver(smsMonitor);
        super.onDestroy();

    }
    /*
    * процедура по выведению данных на экран */
    private void fillData() {
        String[] from = new String[] {MelonTable.COLUMN_DATA, MelonTable.COLUMN_TEXTBANK,MelonTable.COLUMN_SUMMA, MagazTable.COLUMN_MAGAZKR};/*группа строк*/
        int[] to = new int[] {R.id.smsDate, R.id.smsMessage,R.id.symma,R.id.magazin};/*группа id*/

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.sms_row, null, from, to, 0);/*привязка строк к id и передача их в лист разметку*/

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            /*
            * процедура по ворматированию данных перед выведением на экран*/
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(MelonTable.COLUMN_DATA)) {/*присвоение фармата к дате*/
                    Date d = new Date(cursor.getLong(columnIndex));
                    String formatted = new SimpleDateFormat("dd.MM.yy HH:mm").format(d);
                    ((TextView) view).setText(formatted);
                    return true;
                }
                if (columnIndex == cursor.getColumnIndex(MelonTable.COLUMN_SUMMA)){/*диление и установка точки в сумме(вычисление копеек)*/
                    double r = cursor.getLong(columnIndex) / 100.0;
                    ((TextView) view).setText(String.valueOf(r));
                    return true;
                }
                return false;
            }
        });

        setListAdapter(adapter);
    }

    @Override

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Uri messageUri = Uri.parse(SmsContentProvider.CONTENT_URI+  "/" + id);
        String[] projection = {MelonTable.COLUMN_DATA, MelonTable.COLUMN_TEXTBANK};
        Cursor cursor = getContentResolver().query(messageUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String message = cursor.getString(cursor.getColumnIndexOrThrow(MelonTable.COLUMN_SMSTEXT));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage( message)
                    .setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();

            cursor.close();
        } else {
            Toast.makeText(this, "Can't extract SMS", Toast.LENGTH_SHORT).show();
        }
    }
    /*
     * процедура занимается отправкой данных и запроса в провайдер для создания курсора */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MelonTable.COLUMN_ID, MelonTable.COLUMN_DATA, MelonTable.COLUMN_TEXTBANK,MelonTable.COLUMN_SUMMA,MagazTable.COLUMN_MAGAZKR};/*инфармация отправыленная в провайдер*/
        CursorLoader cursorLoader = new CursorLoader(this, SmsContentProvider.CONTENT_URI,
                projection, null, null, "" );/*отправка в провайдер*/
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
