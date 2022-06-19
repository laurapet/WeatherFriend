package com.example.weatherfriend.Logik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.weatherfriend.CityList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Stadt extends AsyncTask<Void,Void,Boolean> {
    private String name;

    private AsyncResponse delegate;

    public Stadt(String name, AsyncResponse delegate) {

        this.name = name;
        this.delegate = delegate;
    }

    private boolean checkIfValid(String name){
        JSONParser parser = new JSONParser();

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + name +
                                        "&units=metric&appid=a46b2315f08f5154c460af1ad9cb70ae");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String input;
            while ((input = in.readLine()) != null) {

                JSONObject a = (JSONObject) parser.parse(input);
                String message = (String) ""+ a.get("message");
                if (message.equals("city not found")){
                    return false;
                }

            }
            in.close();
        }catch (Exception e){
            return false;
        }

        return true;

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return checkIfValid(this.name);

    }
    @Override
    protected void onPostExecute(Boolean aVoid) {
        delegate.hinzufuegen(aVoid,this.name);
        super.onPostExecute(aVoid);



    }
}
