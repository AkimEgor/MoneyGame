package ru.yandex.chechin.rugball.Service;


import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.CalendarContract.Events;

import ru.yandex.chechin.rugball.Activity.MainActivity;
import ru.yandex.chechin.rugball.DataDase.MelonTable;
import ru.yandex.chechin.rugball.R;
import ru.yandex.chechin.rugball.SMSReading.MelonSMS;
import ru.yandex.chechin.rugball.Service.SmsContentProvider;

public class SmsService extends Service {
    private class SmsData {
        public int hh;
        public int mm;
        public String description;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void showNotification(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("СМС пришло")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.ic_launcher_background, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        showNotification(sms_body);
        saveSms(sms_body);

        SmsData event = processSms(sms_body);
        if (event != null) {
            addEvent(event.hh, event.mm, event.description);
        }
        stopSelfResult(startId);
        return START_STICKY;
    }

    private MelonSMS saveSms(String sms_body) {
        MelonSMS melon = new MelonSMS();


        ContentValues values = new ContentValues();
        values.put(MelonTable.COLUMN_TEXTBANK, melon.bank);
        values.put(MelonTable.COLUMN_IDMSGSZ, melon.magazin);
        values.put(MelonTable.COLUMN_DATA, melon.dayI.getTime());
        values.put(MelonTable.COLUMN_SUMMA, melon.summa);
        values.put(MelonTable.COLUMN_BALANS, melon.balance);

        getContentResolver().insert(SmsContentProvider.CONTENT_URI, values);
        return melon;
    }

    private SmsData processSms(String sms_body) {
        return null;
    }

    private void addEvent(int hh, int mm, String description) {
        long calId = 1;
        long startMillis = 0;
        long endMillis = 0;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.HOUR_OF_DAY, hh);
        beginTime.set(Calendar.MINUTE, mm);
        startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, hh+2);
        endTime.set(Calendar.MINUTE, mm);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Rugball");
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, calId);
        values.put(Events.EVENT_TIMEZONE, "Asia/Yekaterinburg");
        cr.insert(Events.CONTENT_URI, values);
    }
}

