package com.example.weatherfriend.Logik;

//Quelle Icons https://icons8.de/icons/set/weekday

import android.os.AsyncTask;
import android.util.Log;

import com.example.weatherfriend.MainActivity;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;//https://askcodez.com/java-lang-classcastexception-org-json-simple-jsonarray-ne-peut-pas-etre-lance-pour-org-json-jsonarray.html
import org.json.JSONException;
import org.json.simple.JSONObject;
//import org.json.JSONObject;

import org.json.simple.parser.JSONParser; //https://carlofontanos.com/java-parsing-json-data-from-a-url/
import org.json.simple.parser.ParseException;


//Klasse, um Wetterdaten aus dem Internet abzurufen
public class Weather extends AsyncTask<Void, Void, Void> {

    Change change;
    private double grad;
    private String description;
    private String stadt;
    private int dayNr;

    private boolean coordinateFlag;
    private double lat;
    private double lon;

    public Weather(String stadt, Change change, int dayNr) {
        this.stadt = stadt;
        this.change = change;
        this.dayNr = dayNr;
    }

    public Weather(Change change, double lat, double lon, boolean coordinateFlag) {

        this.change = change;
        this.coordinateFlag = true;
        this.lat = lat;
        this.lon = lon;
    }

    public void weather_five_three(int daynr) {
        JSONParser parser = new JSONParser();
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + stadt +
                    "&units=metric&appid=a46b2315f08f5154c460af1ad9cb70ae");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String input;

            while ((input = in.readLine()) != null) {

                JSONObject a = (JSONObject) parser.parse(input);
                JSONArray list = (JSONArray) a.get("list");
                JSONObject day = (JSONObject) list.get(daynr);
                JSONObject main = (JSONObject) day.get("main");
                this.grad = (double) main.get("temp");

                JSONArray weather = (JSONArray) day.get("weather");
                JSONObject zero = (JSONObject) weather.get(0);
                this.description = (String) zero.get("description");
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void weather_coordinates(double lat, double lon) {
        JSONParser parser = new JSONParser();
        try {

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=a46b2315f08f5154c460af1ad9cb70ae");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String input;

            while ((input = in.readLine()) != null) {

                JSONObject a = (JSONObject) parser.parse(input);
                this.stadt = (String) a.get("name");

                JSONObject main = (JSONObject) a.get("main");
                this.grad = (double) main.get("temp");

                JSONArray weather = (JSONArray) a.get("weather");
                JSONObject zero = (JSONObject) weather.get(0);
                this.description = (String) zero.get("description");
                Log.d("desc", description);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void current_weather() {
        JSONParser parser = new JSONParser();
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + stadt + "&units=metric&appid=a46b2315f08f5154c460af1ad9cb70ae");

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String input;
            while ((input = in.readLine()) != null) {
                JSONObject a = (JSONObject) parser.parse(input);
                JSONObject main = (JSONObject) a.get("main");
                this.grad = (double) main.get("temp");

                JSONArray weather = (JSONArray) a.get("weather");
                JSONObject zero = (JSONObject) weather.get(0);
                this.description = (String) zero.get("description");
                Log.d("desc", description);

            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (coordinateFlag) {
            weather_coordinates(lat, lon);
        } else if (dayNr == 0) {
            current_weather();
        } else {
            weather_five_three(this.dayNr);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        change.aktualisiereW((int) this.grad, this.description, this.stadt);
    }
}
