package ru.yandex.chechin.rugball.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import ru.yandex.chechin.rugball.SMSReading.SMSReading;
import ru.yandex.chechin.rugball.Service.SmsIntentService;

public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
/*
* принимет смс и раберает ее на текст и нормер телефона */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                Map<String, SMSReading> states = new HashMap<String, SMSReading>();

                    String sms_from = messages[0].getDisplayOriginatingAddress();

                    StringBuilder bodyText = new StringBuilder();
                    for (int g = 0; g < messages.length; g++) {
                        if (messages[g] != null) bodyText.append(messages[g].getMessageBody());
                    }
                    String body = bodyText.toString();
                    Intent mIntent = new Intent(context, SmsIntentService.class);
                    mIntent.putExtra("sms_body", body);
                    mIntent.putExtra("sms_telefon", messages[i].getDisplayOriginatingAddress());
                    Log.e("PROFL", "roadcastReceiver onReceive");
                    SmsIntentService.enqueueWork(context, mIntent);

                }
            }
        }
    }
