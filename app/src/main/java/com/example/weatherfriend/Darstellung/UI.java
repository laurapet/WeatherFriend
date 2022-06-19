package com.example.weatherfriend.Darstellung;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.weatherfriend.Logik.Change;
import com.example.weatherfriend.MainActivity;
import com.example.weatherfriend.R;

import java.util.ArrayList;

public class UI implements Change {

    Context context;
    View layout;

    TextView city;
    TextView temperature;
    ImageView descriptionImg;
    ImageView human;

    ImageView weekdayIV;
    ImageView clockIV;
    TextView ampm;

    private ArrayList<String>staedte;

    public UI(Context context){
        this.context=context;
        layout= ((Activity)context).findViewById(R.id.cl);
        city=((Activity)context).findViewById(R.id.cityText);
        temperature=((Activity)context).findViewById(R.id.temperatureText);
        descriptionImg = ((Activity)context).findViewById(R.id.descriptionImg);
        human = ((Activity)context).findViewById(R.id.human);
        weekdayIV=(ImageView)((Activity)context).findViewById(R.id.calender); //https://stackoverflow.com/questions/10996479/how-to-update-a-textview-of-an-activity-from-another-class
        weekdayIV.setImageResource(R.drawable.sunday); //https://stackoverflow.com/questions/2974862/changing-imageview-source
        clockIV=(ImageView)((Activity)context).findViewById(R.id.clock);
        ampm=((Activity)context).findViewById(R.id.ampm);
        layout.setBackgroundColor(context.getResources().getColor(R.color.white,context.getTheme()));
    }

    public void initIfNoCity(ArrayList<String> al, int ptr){
        staedte=al;
        TextView nocity=((Activity)context).findViewById(R.id.showiflistempty);

        try{
            MainActivity.stadt.setText(al.get(ptr));
            nocity.setVisibility(View.INVISIBLE);
        }catch (IndexOutOfBoundsException e){
            nocity.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void aktualisiereW(int grad, String description, String city) {
        Log.d("description",description);
        //int color=context.getResources().getColor(R.color.colorPrimary,context.getTheme());
        if (description.equals("clear sky")){
            descriptionImg.setImageResource(R.drawable.clear);
        }
        else if (description.equals("few clouds") || description.equals("scattered clouds") || description.equals("broken clouds") || description.equals("overcast clouds") ){
            descriptionImg.setImageResource(R.drawable.cloud);
        }
        else if(description.equals("shower rain") || description.equals("rain") || description.equals("light rain")){
            descriptionImg.setImageResource(R.drawable.rain);
        }
        else if(description.equals("snow") || description.equals("light snow") ){
            descriptionImg.setImageResource(R.drawable.snow);
        }

        temperature.setText(grad+"°C");


        //human-Image ändern
        if(grad>=25){
            human.setImageResource(R.drawable.ofuenfundzwanzig);
        }
        else if(grad>=20){
            human.setImageResource(R.drawable.ozwanzig);
        }
        else if(grad>=15){
            human.setImageResource(R.drawable.ofuenfzehn);
        }
        else if(grad>=10){
            human.setImageResource(R.drawable.ozehn);
        }
        else if(grad>=5){
            human.setImageResource(R.drawable.ofuenf);
        }
        else if(grad>=0){
            human.setImageResource(R.drawable.onull);
        }
        else if(grad>= -5){
            human.setImageResource(R.drawable.ominusfuenf);
        }

        this.city.setText(city);
        if (!staedte.contains(city)){
            staedte.add(city);
        }

    }

    public void aktualisiereDuT(int day, int time){
        switch (day){
            case 1:

                weekdayIV.setImageResource(R.drawable.sunday);
                break;
            case 2:
                weekdayIV.setImageResource(R.drawable.monday);
                break;
            case 3:
                weekdayIV.setImageResource(R.drawable.tuesday);
                break;
            case 4:
                weekdayIV.setImageResource(R.drawable.wednesday);
                break;
            case 5:
                weekdayIV.setImageResource(R.drawable.thursday);
                break;
            case 6:
                weekdayIV.setImageResource(R.drawable.friday);
                break;
            case 7:
                weekdayIV.setImageResource(R.drawable.saturday);
                break;

        }

        //clock
        if(time<12){
            ampm.setText(R.string.am);
            if(time==0){
                clockIV.setImageResource(R.drawable.twelve);
            }
            else if(time==3){
                clockIV.setImageResource(R.drawable.three);
            }
            else if(time==6){
                clockIV.setImageResource(R.drawable.six);
            }
            else if(time==9 ){
                clockIV.setImageResource(R.drawable.nine);
            }
        }

        else{
            ampm.setText(R.string.pm);
            if(time==12){
                clockIV.setImageResource(R.drawable.twelve);
            }
            else if(time ==15){
                clockIV.setImageResource(R.drawable.three);
            }
            else if(time ==18){
                clockIV.setImageResource(R.drawable.six);
            }
            else if(time ==21){
                clockIV.setImageResource(R.drawable.nine);
            }
        }


    }

}