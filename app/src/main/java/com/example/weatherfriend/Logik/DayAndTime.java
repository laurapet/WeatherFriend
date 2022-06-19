package com.example.weatherfriend.Logik;

import android.util.Log;

import java.util.Calendar;

public class DayAndTime {
    Change change;
    private int weekday;
    private int time;

    private int[]fivedays;
    private int dayptr;

    //TIMEZONE?

    //Sonntag entspricht dayNr = 1
    public DayAndTime(Change change){
        this.change=change;
        Calendar c= Calendar.getInstance();
        this.weekday=c.get(Calendar.DAY_OF_WEEK);
        this.time=timeOfday(c);
        this.fivedays=setFivedays();
        this.dayptr=0;
        change.aktualisiereDuT(weekday,time);
    }

    //Kontrolle des WeekdayIntervalls , weekday muss immer kleiner sein als initialer weekday+5
    public boolean weekdayplus(){
        if(dayptr<4){
            dayptr++;
            change.aktualisiereDuT(fivedays[dayptr],time);
            return true;
        }
        //Weatherupdate durch MAIN
        return false;
    }

    public boolean weekdayminus(){
        if(dayptr>0){
            dayptr--;
            change.aktualisiereDuT(fivedays[dayptr],time);
            return true;
        }
        return false;
    }

    public int getTime() {
        return time;
    }

    public boolean timeplus(){
        if(dayptr<4){
            if(time==21){
                time=0;
                weekdayplus();
                //aktualisiert schon
                return true;
            }
            else{
                time=time+3;
                change.aktualisiereDuT(fivedays[dayptr],time);
                return true;
            }

        }
        return false;
    }

    public boolean timeminus(){
        if(dayptr>0){
            if(time==0){
                time=21;
                weekdayminus();
                //aktualisiert schon
                return true;
            }
            else{
                time=time-3;
                change.aktualisiereDuT(fivedays[dayptr],time);
                return true;
            }

        }
        return false;

    }

    //Temperatur nur im 3-Stunden-Takt verfügbar
    //wecker; wenn bei 13:30 0: bei 15 --> mittelwertvariante (!)
    //sonst gucken was nächst kleineres Tagesachtel ist
    //falls immer hour=21--> returns in else-ifs
    private int timeOfday(Calendar c){
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //0-3
        if(hour<3){
            if(((hour == 1) && minuteOverThirtee(minute)) || (hour > 1)){
                hour=3;
            }
            else {
                hour=0;
            }

        }
        //3-6
        else if(hour<6){
            if(((hour == 4) && minuteOverThirtee(minute)) || (hour > 4)){
                hour=6;
            }
            else {
                hour=3;
            }

        }
        else if(hour<9){
            if(((hour == 7) && minuteOverThirtee(minute)) || (hour > 7)){
                hour=9;
            }
            else {
                hour=6;
            }

        }
        else if(hour<12){
            if(((hour == 10) && minuteOverThirtee(minute)) || (hour > 10)){
                hour=12;
            }
            else {
                hour=9;
            }

        }
        else if(hour<15){
            if(((hour == 13) && minuteOverThirtee(minute)) || (hour > 13)){
                hour=15;
            }
            else {
                hour=12;
            }

        }
        else if(hour<18){
            if(((hour == 16) && minuteOverThirtee(minute)) || (hour > 16)){
                hour=18;
            }
            else {
                hour=15;
            }

        }
        else if(hour<21){
            if(((hour == 19) && minuteOverThirtee(minute)) || (hour > 19)){
                hour=21;
            }
            else {
                hour=18;
            }

        }
        else if(hour<24){
            if(((hour == 22) && minuteOverThirtee(minute)) || (hour > 22)){
                hour=24;
            }
            else {
                hour=21;
            }

        }

        return hour;
    }

    private int[] setFivedays(){
        int[]temp=new int[5];
        int daynr=weekday;
        for(int i=0; i<5;i++){
            if(daynr>7){
                daynr=1;
            }
            temp[i]=daynr;
            daynr++;
            Log.d("5days",""+temp[i]);
        }

        return temp;
    }

    private boolean minuteOverThirtee(int minute){
        return (minute>=30);
    }



}
