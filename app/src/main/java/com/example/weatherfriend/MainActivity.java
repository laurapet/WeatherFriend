package com.example.weatherfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherfriend.Darstellung.UI;
import com.example.weatherfriend.Logik.DayAndTime;
import com.example.weatherfriend.Logik.Standort;
import com.example.weatherfriend.Logik.Weather;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends Activity {


    private static final String KEY = "StringArrayExtra";
    private static final String CITYLIST = "citylist";

    public static TextView stadt;

    DayAndTime dat;
    UI ui;
    ArrayList<String> staedte;
    int citypointer;
    int day;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gdInit();
        this.uiInit();
        this.logikInit(staedte);

    }

    private void gdInit() {
        gestureDetector = new GestureDetector(this, new MyGestureListener(this));
        this.findViewById(R.id.cityText).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void uiInit() {
        stadt = (TextView) findViewById(R.id.cityText);
        staedte = loadList();
        citypointer = loadPointer();
        ui = new UI(this);
        ui.initIfNoCity(staedte, citypointer);

    }

    private void logikInit(ArrayList<String> staedte) {

        dat = new DayAndTime(ui);
        day = 0;

        if (!staedte.isEmpty()) {
            find_weather();
        } else {
            Standort standort = new Standort(this, this.ui);
            //standort ermittelt und dabei neues wetter-objekt erstellt und executed und per UI in staedte eingetragen
        }
    }

    private ArrayList<String> loadList() {
        ArrayList<String> temp = new ArrayList<String>();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(this.openFileInput(CITYLIST)))) {
            in.lines().forEach(s -> temp.add(s));
        } catch (FileNotFoundException e) {
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : temp) {
            Log.d("staedte", s);
        }
        return temp;
    }

    public void aktualisieren(View view) {
        find_weather();
    }

    public void toCityList(View view) {
        Intent intent = new Intent(MainActivity.this, CityList.class);
        Bundle bundle = new Bundle();//Um StringArryList zu übermitteln
        bundle.putStringArrayList(KEY, staedte);
        intent.putExtra("StaedteBundle", bundle);
        startActivity(intent);

    }

    public void newStadtDown() {

        if (citypointer < staedte.size() - 1) {
            citypointer++;
        } else {
            citypointer = 0;
        }
        pointerSave(citypointer);

        String cityString = staedte.get(citypointer);
        //stadt.setText(cityString);
        find_weather();
    }

    public void newStadtUp() {

        if (citypointer > 0) {
            citypointer--;
        } else {
            citypointer = staedte.size() - 1;
        }
        pointerSave(citypointer);

        String cityString = staedte.get(citypointer);
        find_weather();
    }

    void pointerSave(int ptr) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Pointer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cityPointer", ptr);
        editor.apply();
    }

    int loadPointer() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("Pointer", Context.MODE_PRIVATE);
        int ptr = sharedPreferences.getInt("cityPointer", 0);
        return ptr;
    }

    public void nextDay() {
        //day + 8 um im JSONArray einen Tag weiterzukommen
        if (dat.weekdayplus()) {
            day = day + 8;
            find_weather();
        } else {
            Toast.makeText(this, R.string.future, Toast.LENGTH_SHORT).show();
        }

    }

    public void prevDay() {
        if (dat.weekdayminus()) {
            day = day - 8;
            find_weather();
        } else {
            Toast.makeText(this, R.string.past, Toast.LENGTH_SHORT).show();
        }
    }

    //zukunft
    public void nextTime(View view) {

        if (dat.timeplus()) {
            day++; //Um im JSONArray einen Tagesabschnitt weiterzukommen
            find_weather();
        } else {
            Toast.makeText(this, R.string.future, Toast.LENGTH_SHORT).show();
        }

    }

    public void prevTime(View view) {
        if (dat.timeminus()) {
            day--;
            find_weather();
        } else {
            Toast.makeText(this, R.string.past, Toast.LENGTH_SHORT).show();
        }

    }

    public void find_weather() {
        try {
            Weather temp = new Weather(staedte.get(citypointer), ui, day);
            temp.execute();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(this, R.string.no_weather_possible, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    //innere Klasse
    class MyGestureListener implements GestureDetector.OnGestureListener {
        private Context context;

        public MyGestureListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > 75) {
                newStadtUp();
                return true;
            }
            if (e2.getY() - e1.getY() > 75) {
                newStadtDown();
                return true;
            }
            if (e1.getX() - e2.getX() > 75) {
                nextDay();
                return true;
            }
            if (e2.getX() - e1.getX() > 75) {
                prevDay();
                return true;
            }
            return false;

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                    Standort standort = new Standort(this, this.ui);

                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Toast.makeText(this, "Unidentified permissions granted or denied", Toast.LENGTH_SHORT).show();
        }

    }
}
