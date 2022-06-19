package com.example.weatherfriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherfriend.Logik.AsyncResponse;
import com.example.weatherfriend.Logik.Stadt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CityList extends Activity implements AsyncResponse {

    //private static final String TAG = "CityList";
    private static final String CITYLIST="citylist";
    private static final String KEY = "StringArrayExtra";

    private EditText edt_searchCity;
    private ImageButton btn_search;
    private LinearLayout ll;

    private ArrayList<String> staedte;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist_activity);
        edt_searchCity=this.findViewById(R.id.editSearch);
        btn_search=this.findViewById(R.id.search);
        ll=this.findViewById(R.id.ll);
        staedte = this.loadList();
        this.staedteAuflisten();

        //button zum suchen + persistente speicherung der Städteliste
        btn_search.setOnClickListener(v -> {
            String cityName=edt_searchCity.getText().toString();

            Stadt stadt = new Stadt(cityName, this);
            stadt.execute();

        });

    }

    //https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public void hinzufuegen(boolean cityIsValid, String cityName){
        if(cityIsValid){
            if(!isDuplikat(cityName)){
            staedte.add(cityName);
            TextView neu=new TextView(this);
            neu.setText(cityName);
            neu.setPadding(20,20,20,20);

            ll.addView(neu);
            doSaveInFile(); //persistent eintragen
            }
            else {
                Toast.makeText(CityList.this, R.string.double_city ,Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(CityList.this, R.string.no_city_found,Toast.LENGTH_SHORT).show();
        }
    }

    boolean isDuplikat(String cityName){
        return (staedte.contains(cityName));
    }

    private void staedteAuflisten(){
        for (String s: staedte){
            TextView t=new TextView(this);
            t.setText(s);
            ll.addView(t);
            t.setPadding(20,20,20,20);
        }
    }

    private void doSaveInFile(){
        try(OutputStreamWriter out = new OutputStreamWriter(
                this.openFileOutput(CITYLIST,Context.MODE_PRIVATE))){
            staedte.stream().forEach(s->{
                try{
                    out.write(s+"\n");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> loadList(){
        ArrayList <String> temp=new ArrayList<String>();
        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            temp = b.getBundle("StaedteBundle").getStringArrayList(KEY);
        }
        return temp;
    }

    public void toMain(View view){
        Intent intent = new Intent(CityList.this,MainActivity.class);
        startActivity(intent);
    }
}
