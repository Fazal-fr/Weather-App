package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.SpellCheckerInfo;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

double sunrise_time;
double sunset_time;
TextView Condition_today;
TextView tvTemperature;
TextView feels;
TextView max_today;
TextView min_today;
TextView humid;
TextView visible;
TextView speed_of_wind;
TextView direction;
TextView time;
TextView sun_set_time;
TextView city;
Button weather;
double temperature;
double like;
double minimum;
double maximum;
double humidity;
double visibility_today;
double wind_speed;
double wind_direction;

TextView t1, t2;
int permisisonCode = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        min_today=findViewById(R.id.textview_min);
        humid=findViewById(R.id.textview_humid);
        visible=findViewById(R.id.textview_visible);
        speed_of_wind=findViewById(R.id.textview_speed);
        direction=findViewById(R.id.textview_direction);
        time=findViewById(R.id.textview_time);
        sun_set_time=findViewById(R.id.textview_settime);
        city=findViewById(R.id.city_name);
        weather = findViewById(R.id.btnfetch);
        tvTemperature =findViewById(R.id.textview_temp);
        feels=findViewById(R.id.textview_feels);
        max_today=findViewById(R.id.textview_max);

        ///////
        t1 = findViewById(R.id.txtlat);
        t2 = findViewById(R.id.txtlng);


        int a = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int b = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if( a!= PackageManager.PERMISSION_GRANTED && b!= PackageManager.PERMISSION_GRANTED)
        {
            String[] thepermissions = new String[1];
            thepermissions[0]= android.Manifest.permission.ACCESS_FINE_LOCATION;
            this.requestPermissions(thepermissions,permisisonCode);
            return;
        }
        LocationManager lm;
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("*****","*******Last Known LOCATION:"+loc.getLatitude()+","+loc.getLongitude());
        ///////

                Runnable run= new Runnable() {
                    @Override
                    public void run() {
                        char[] data= new char[5000];
                        try {
                            String cityName = city.getText().toString();
                            URL u= new URL("https://api.openweathermap.org/data/2.5/weather?q="+cityName+",pk&appid=90333f406ec2360d140c195c6a8b42a0");
                            InputStream i = u.openStream();
                            BufferedReader b =new BufferedReader(new InputStreamReader(i));
                            int count= b.read(data);
                            String response=new String(data,0,count);
                            Log.d("*********", "******" + response);
                            JSONObject j=new JSONObject(response);
                            JSONObject main= j.getJSONObject("main");
                            temperature= main.getDouble("temp");
                            temperature=temperature-273.15;
                            like= main.getDouble("feels_like");
                            like=like-273.15;
                            minimum= main.getDouble("temp_min");
                            minimum=minimum-273.15;
                            maximum= main.getDouble("temp_max");
                            maximum= maximum-273.15;
                            humidity=main.getDouble("humidity");
                            JSONObject sys= j.getJSONObject("sys");
                            sunrise_time=sys.getDouble("sunrise");
                            sunset_time=sys.getDouble("sunset");
                            JSONObject wind= j.getJSONObject("wind");
                            wind_speed=wind.getDouble("speed");
                            wind_direction=wind.getDouble("deg");
                            visibility_today= j.getDouble("visibility");
                            Date date_sunrise = new Date((long) (sunrise_time * 1000L));
                            SimpleDateFormat formatters = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formatted_sunrise = formatters.format(date_sunrise);
                            Date date_sunset = new Date((long) (sunset_time * 1000L));
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formatted_sunset = formatter.format(date_sunset);
                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    speed_of_wind.setText(""+wind_speed);
                                    direction.setText(""+wind_direction);
                                    time.setText(""+formatted_sunrise);
                                    sun_set_time.setText(""+formatted_sunset);
                                    visible.setText(""+visibility_today);
                                    tvTemperature.setText(""+temperature);
                                    feels.setText(""+like);
                                    min_today.setText(""+minimum);
                                    max_today.setText(""+maximum);
                                    humid.setText(""+humidity);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        };
                    }
                };
                Thread thread = new Thread(run);
                thread.start();
            }
        };
