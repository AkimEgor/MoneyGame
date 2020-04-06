package ru.yandex.chechin.rugball.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.HashMap;
import java.util.Map;

import ru.yandex.chechin.rugball.Activity.MainActivity;
import ru.yandex.chechin.rugball.DataDase.MagazTable;
import ru.yandex.chechin.rugball.DataDase.MelonTable;
import ru.yandex.chechin.rugball.DataDase.SmsDatabaseHelper;
import ru.yandex.chechin.rugball.R;
import ru.yandex.chechin.rugball.SMSReading.MelonSMS;
import ru.yandex.chechin.rugball.SMSReading.SMSReading;
import ru.yandex.chechin.rugball.SMSReading.SMSReadingSber;

public class SmsIntentService  extends JobIntentService {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final int JOB_ID = 0x01;
    private SmsDatabaseHelper db;

    public static void enqueueWork(Context context, Intent work) {
        Log.e("PROFL", "SmsIntentService enqueueWork");
        enqueueWork(context, SmsIntentService.class, JOB_ID, work);

    }
/*
* принимет через intent данные из класса по мониторинку смс
* и отпровляет и в класс saveSms*/
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("PROFL", "SmsIntentService onHandleWork");
        String sms_body = intent.getExtras().getString("sms_body");
        String sms_telefon = intent.getExtras().getString("sms_telefon");
        db = new SmsDatabaseHelper(this.getApplicationContext());
        showNotification(sms_body);
        saveSms(sms_body,sms_telefon,db);


    }
/*
* процедура по выведени сообщения в шторку (в не рабочим састоянии)*/
    private void showNotification(String text) {
        Context context = getApplicationContext();
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("СМС пришло")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.ic_launcher_background, notification);
    }
/*
* процедура занимается распределением смс по номеру телефона
* отправлению смс определенному кслссу для рабора смс
* записовает нужные данные в базу */
    private void saveSms(String sms_body, String sms_telefon,SmsDatabaseHelper db) {

        Map<String, SMSReading> states = new HashMap<String, SMSReading>();
        states.put("+79152774474", new SMSReadingSber());
        states.put("900", new SMSReadingSber());
        if (states.containsKey(sms_telefon)) {
            MelonSMS melon = states.get(sms_telefon).onReceiveAll(sms_body, sms_telefon);
            ContentValues values = new ContentValues();
            Uri messageUri = Uri.parse(Uri.parse("content://"
                    + SmsContentProvider.AUTHORITY +  "/" + SmsContentProvider.BASE_MAGAZ) + "/" + melon.ubitok );
            String[] projection = {MagazTable.COLUMN_ID};
            Cursor cursor = getContentResolver().query(messageUri, projection, null, null, null);
            int magszin = 0;
            if (cursor != null) {
                cursor.moveToFirst();
                magszin = cursor.getInt(cursor.getColumnIndexOrThrow(MagazTable.COLUMN_ID));
            }

            values.put(MelonTable.COLUMN_TEXTBANK, melon.bank);
            values.put(MelonTable.COLUMN_IDMSGSZ, magszin);
            values.put(MelonTable.COLUMN_DATA, melon.dayI.getTime());
            values.put(MelonTable.COLUMN_SUMMA, melon.summa);
            values.put(MelonTable.COLUMN_BALANS, melon.balance);
            values.put(MelonTable.COLUMN_SMSTEXT, sms_body);
            getContentResolver().insert(SmsContentProvider.CONTENT_URI, values);

        }
    }



}