package ru.yandex.chechin.rugball.SMSReading;

import java.util.Date;

public class SMSReadingSber implements SMSReading {
    @Override
    public MelonSMS onReceiveAll(String smska, String send){
        MelonSMS melon =   onReceiveSber( smska);
        melon.bank = "Сбер Банк";
        return melon;
    }
    public static MelonSMS onReceiveSber(String smska) {
        MelonSMS melon = new MelonSMS();
        String aCatsL[] = smska.split(" ");  // по пробелу
        String str1 = aCatsL[0];
        if (str1.contains("VISA")) {
            melon = onReceiveViza(smska);
        } else {
            melon = onReceivePerevod(smska);
        }
        return melon;
    }
    public static MelonSMS onReceiveViza(String smska) {
        MelonSMS melon = new MelonSMS();
        melon.dayI = new Date(); // дата
        String aCats[] = smska.split(" "); // разложили на слова
        String testString = aCats[3];

        melon.summa = Integer.parseInt(aCats[3].replaceAll("[\\D]", "")); // затрата
        if(!testString.contains(".")){
            melon.summa = melon.summa*100;
        }
        int n = 0;
        for (String i : aCats) {
            if (i.equalsIgnoreCase("Баланс:")) { // поиск слова баланс
                break;
            }
            n = n + 1;
        }
        String testString1 = aCats[n + 1];
        melon.balance = Integer.parseInt(aCats[n + 1].replaceAll("[\\D]", ""));/**/
        if(!testString1.contains(".")){
            melon.balance = melon.balance*100;
        }
        melon.magazin = aCats[4] ;
        melon.ubitok = aCats[4] ;
        melon.kb = aCats[4] ;

        return melon;
    }
    public static MelonSMS onReceivePerevod(String smska) {
        MelonSMS melon = new MelonSMS();
        melon.dayI = new Date();
        String aCats[] = smska.split("\\s");
        String testString = aCats[1];
        melon.summa = Integer.parseInt(aCats[1].replaceAll("[\\D]", ""));
        if(!testString.contains(".")){
            melon.summa = melon.summa*100;
        }
        int n = 0;
        for (String i : aCats) {
            if (i.equalsIgnoreCase("Баланс")) {
                break;
            }
            n = n + 1;
        }
        String testString1 = aCats[n + 2];
        melon.balance = Integer.parseInt(aCats[n + 2].replaceAll("[\\D]", ""));
        if(!testString1.contains(".")){
            melon.balance = melon.balance*100;
        }
        melon.magazin = (aCats[3]) + " " + (aCats[4]) + " " + (aCats[5]);
        return melon;
    }

}
