package com.ayw.weathertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather = (TextView)findViewById(R.id.textView);

        new WeatherAsynTask(weather).execute("http://weather.naver.com/today/08470680", "td[class=data]");
    }
}

class WeatherAsynTask extends AsyncTask<String, Void, String> {
    TextView textView2;

    public WeatherAsynTask(TextView textView2) {
        this.textView2 = textView2;
    }

    @Override
    protected String doInBackground(String... params) {
        String URL = params[0];
        String EI = params[1];
        String result = "";

        try {
            Document document = Jsoup.connect(URL).get();
            Elements elements = document.select(EI);

            result = elements.get(1).text();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);

        textView2.setText(s);
    }
}