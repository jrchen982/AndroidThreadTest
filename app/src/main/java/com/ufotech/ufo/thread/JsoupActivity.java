package com.ufotech.ufo.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

public class JsoupActivity extends AppCompatActivity {

    public final static String URL = "http://www.cwb.gov.tw/rss/forecast/36_02.xml";
    private String aver_temp,area,situation;
    private TextView tv_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        tv_weather = findViewById(R.id.tv_weather);

        //氣象
        Thread thread=new Thread(runnable);             //執行緒
        thread.start();                    //讓執行緒開始工作
    }

    //定義天氣欄
    private Runnable runnable=new Runnable(){
        public void run(){
            try {
                URL url = new URL(URL);
                Document xmlDoc = Jsoup.parse(url, 3000);
                Elements items = xmlDoc.getElementsByTag("item");
                Elements title_select = items.get(0).getElementsByTag("title");
                int i = title_select.get(0).text().indexOf("溫度");
                String temp_range = title_select.get(0).text().substring(i+4,i+11);
                String [] temp = temp_range.split("~");
                int low_temp = Integer.valueOf(temp[0].trim());
                int high_temp = Integer.valueOf(temp[1].trim());
                int average_temp = (low_temp + high_temp)/2;
                //高雄市溫度
                aver_temp=String.valueOf(average_temp);
                //高雄市
                area=title_select.get(0).text().substring(0,3);
                //高雄市天氣狀況
                String [] weather = title_select.get(0).text().split(" ");
                situation=weather[2];

                runOnUiThread(new Runnable() {             //將內容交給UI執行緒做顯示
                    public void run(){
                        tv_weather.append(aver_temp + "℃\n" + area + "\n" + situation);
                    }
                });
//                Thread.sleep(100);    //避免執行緒跑太快而UI執行續顯示太慢,覆蓋掉te01~03內容所以設個延遲,也可以使用AsyncTask-異步任務

            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
