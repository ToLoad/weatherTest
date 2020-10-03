package com.ayw.weathertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView weather;
    TextView count;
    Button button;
    String notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather = findViewById(R.id.textView);
        count = findViewById(R.id.countDown);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDown("000010");
                notify = "0";
//                Intent intent = new Intent(getApplicationContext(), MyService.class);
//                intent.putExtra("no", notify);
//                startService(intent);

            }
        });

        new WeatherAsynTask(weather).execute("http://weather.naver.com/today/08470680", "td[class=data]");
    }


    public void countDown(String time) {

        long conversionTime = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 시간단위
                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                // 시간이 한자리면 0을 붙인다
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }

                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                count.setText(hour + ":" + min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
                count.setText("촬영종료!");
                notify = "1";

                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra("no", notify);
                startService(intent);



                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

            }
        }.start();

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
