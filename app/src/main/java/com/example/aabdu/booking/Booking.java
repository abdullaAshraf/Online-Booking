package com.example.aabdu.booking;

import android.text.style.TtsSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    private String id;
    private boolean confirmed;
    private String date;
    private String user;
    private int sHour, sMin;
    private int eHour, eMin;

    public Booking(String d, int sh, int sm, int eh, int em) {
        confirmed = false;
        date = d;
        sHour = sh;
        sMin = sm;
        eHour = eh;
        eMin = em;
        id = "";
        user = "test";
    }

    public Booking(String _id, String email, int day, int month, int year, int starth, int startm, int durationinm , int confirm) {
        date = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        user = email;
        id = _id;
        sHour = starth;
        sMin = startm;
        eMin = (durationinm + sMin) % 60;
        eHour = durationinm / 60 + sHour;
        confirmed = confirm == 1 ? true : false;
    }

    public String getDate() {
        return date;
    }

    public Date getDateTime(){
        Date datetime = null;
        try {
            datetime = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            datetime.setHours(sHour);
            datetime.setMinutes(sMin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public int getDur(){
        return 60*(eHour - sHour) + eMin - sMin;
    }

    public String getTime(boolean start) {
        int h, m;
        if (start) {
            h = sHour;
            m = sMin;
        } else {
            h = eHour;
            m = eMin;
        }

        String time = "";

        if (h < 10)
            time += "0" + h + ":";
        else
            time += h + ":";

        if (m < 10)
            time += "0" + m;
        else
            time += m;

        return time;
    }
}
