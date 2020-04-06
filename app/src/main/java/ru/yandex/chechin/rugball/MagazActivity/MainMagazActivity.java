package ru.yandex.chechin.rugball.MagazActivity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.yandex.chechin.rugball.DataDase.MelonTable;
import ru.yandex.chechin.rugball.R;
import ru.yandex.chechin.rugball.Service.SmsContentProvider;


public class MainMagazActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;
    int idMagazina;
    String data_magaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_magaz);
        TextView textView = (TextView) findViewById(R.id.empty1);
        Bundle arguments = getIntent().getExtras();
          idMagazina = arguments.getInt("position");

          data_magaz = arguments.get("dateMagaz").toString();
         String nameMagaz = arguments.get("nameMagaz").toString();
        textView.setText(nameMagaz);
        fillData();





}
/*процедура по выведению данных на экран*/
    private void fillData() {
        String[] from = new String[]{MelonTable.COLUMN_TEXTBANK,MelonTable.COLUMN_SUMMA, MelonTable.COLUMN_DATA};
        int[] to = new int[]{R.id.kk1, R.id.kk,R.id.kk2};

        getLoaderManager().initLoader(0, null,  this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item, null, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == cursor.getColumnIndex(MelonTable.COLUMN_DATA)) {
                    Date d = new Date(cursor.getLong(columnIndex));
                    String formatted = new SimpleDateFormat("dd.MM.yy HH:mm").format(d);
                    ((TextView) view).setText(formatted);
                    return true;
                }
                if (columnIndex == cursor.getColumnIndex(MelonTable.COLUMN_SUMMA)){
                    double r = cursor.getLong(columnIndex) / 100.0;
                    ((TextView) view).setText(String.valueOf(r));
                    return true;
                }
                return false;
            }
        });
        setListAdapter(adapter);
    }
/*
* процедура по отправки данных и запроса в провайдер для создания курсора */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection1 = {MelonTable.COLUMN_TEXTBANK,MelonTable.COLUMN_SUMMA, MelonTable.COLUMN_DATA};
        //Uri messageUri = Uri.parse(SmsContentProvider.CONTENT_URI+  "/" + id);
        Uri messageUri = Uri.parse("content://"
                + SmsContentProvider.AUTHORITY +  "/" + SmsContentProvider.SMS_MAGAZ + "/" + idMagazina);
        CursorLoader cursorLoader1 = new CursorLoader(this, messageUri,
                projection1, null, null, "" );
        return cursorLoader1;
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
