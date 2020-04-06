package ru.yandex.chechin.rugball.RelizActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ru.yandex.chechin.rugball.Activity.MainActivity;
import ru.yandex.chechin.rugball.MagazActivity.MainMagazActivity;
import ru.yandex.chechin.rugball.R;
import ru.yandex.chechin.rugball.DataDase.SmsDatabaseHelper;

public class MainRelizActivity extends AppCompatActivity /*Для адаптера*/ implements MyRecyclerViewAdapter.ItemClickListener, CustomArrayAdapter.ItemClickListener1 {
     List<MessageArrayAdapter> listMagaz = new ArrayList<>(); /*Список из String с сартировкой по магазину и дате с выведением магазина и суммы затрат в нем*/
    private List<MessageArrayAdapter> phones;
    private Set<String> dataList = new HashSet<>();
    SQLiteDatabase db ;/*База данных*/
    private MyRecyclerViewAdapter adapterListDate;
    private CustomArrayAdapter adapterListMagaz;
    public Date dateMagaz;
    RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reliz);
        /*разметка для данной активности*/
        db = new SmsDatabaseHelper(this.getApplicationContext()).getWritableDatabase();
        /*создание базы данных*/
        Button knopka = (Button) findViewById(R.id.vanActivityTy);
        /*привязка id кнопки в xml к назвнию в данной активности*/
        knopka.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainRelizActivity.this, MainActivity.class);
                /*Реализация для кнопки перехода в другую активности*/
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.statistika_magazin);
        /*привязка id списка в xml к назвнию в данной активности*/
        recyclerView.invalidate();
        listMagaz = Updatc(new Date());
        /*добавление в лис даты*/
        adapterListMagaz = new CustomArrayAdapter(this, listMagaz);
        /*привязка листа к адаптору*/
        adapterListMagaz.setClickListener1(this);
        /*клик по стаке в списке*/
        recyclerView.setAdapter(adapterListMagaz);
        /*переходник для адаптора к разметке*/
        adapterListMagaz.notifyDataSetChanged();
        /*извещение одаптора об изменении данных для выведения данных*/

        RecyclerView recyclerView1 = findViewById(R.id.month);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainRelizActivity.this, LinearLayoutManager.HORIZONTAL, false);

        Cursor query4 = db.rawQuery(" SELECT dayI from Melon ;", null);/*курсор даствть все строки dayI из таблицы Melon*/
        dataList = new HashSet<>();
        if (query4.moveToFirst()) {
            do { Long name = query4.getLong(0) ;
                DateAkim i = new DateAkim (new Date(name));/*преоброзование лонга в дату*/
                DateAkim begining = new DateAkim (i.begining());/*новый формата для даты*/
                dataList.add(begining.printInFormat());
            } while (query4.moveToNext());
        }
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.addAll(dataList);
        ArrayList<String> viewColors = new ArrayList<>();
        viewColors.addAll(dataList);

        recyclerView1.setLayoutManager(horizontalLayoutManager);
        adapterListDate = new MyRecyclerViewAdapter(this, viewColors, animalNames);
        adapterListDate.setClickListener(this);
        recyclerView1.setAdapter(adapterListDate);

    }
/*
* класс выполнеяет обновление списка для адаптора CustomArray
* в нем прописан курсор для данного адптора */
    List<MessageArrayAdapter> Updatc(Date date) {
        listMagaz.clear();/*очистить список*/
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        DateAkim i = new DateAkim (date);
        DateAkim begining = new DateAkim (i.begining());
        calendar.setTime(date);
        DateAkim i1 = new DateAkim (date);
        DateAkim end = new DateAkim (i1.end());
        dateMagaz = begining;
        Cursor query3 = db.rawQuery("SELECT * from (SELECT Sum(summa) as summa,rita.krmagaz,idmagazin FROM Melon,rita " +
                "where idmagazin  = rita._id and dayI between " + begining.getTime() + " and " + end.getTime() +
                " group by rita.krmagaz,idmagazin)  rt order by rt.summa DESC;", null);
        listMagaz = new ArrayList<>();
        if (query3.moveToFirst()) {
            do {
                double r = query3.getInt(0) / 100.0;
                String name = query3.getString(1) + " /  " + r;

                listMagaz.add(new MessageArrayAdapter (name,query3.getInt(2),query3.getString(1)));
            } while (query3.moveToNext());
        }

     return listMagaz;
    }
    @Override
    /*
    * выполнение клика по списку RecyclerView и обробаткук в адапторе MyRecyclerView */
    public void onItemClick(View view, int position) {
        listMagaz.clear();
        DateFormat format = new SimpleDateFormat("yyyy.MM", Locale.ENGLISH);
        try {
            Date date = format.parse(adapterListDate.getItem(position));
            dateMagaz = date;
            listMagaz = Updatc(date);
            CustomArrayAdapter adapter = new CustomArrayAdapter(this, listMagaz);
            adapter.setClickListener1(this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }catch (Exception e){}


        Toast.makeText(this, "МЕСЯЦ " + adapterListDate.getItem(position) + " НОМЕР " + position, Toast.LENGTH_SHORT).show();
    }
    @Override
    /*
     * выполнение клика по списку RecyclerView и обробаткук в адапторе CustomArray */
    public void  onItemClick1(View view , int position, String nameMagaz) {
        Intent intent1 = new Intent(MainRelizActivity.this, MainMagazActivity.class);/*перехлд в другой плас по клику*/
        intent1.putExtra("position", position);/*передача обекта*/
        intent1.putExtra("dateMagaz", dateMagaz.getTime());
        intent1.putExtra("nameMagaz", nameMagaz);
        startActivity(intent1);



    }

}