package com.ufotech.ufo.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ================================================
 * @author：UFO（陳俊融）Github：https://github.com/JyunRong
 * @date：2018/5/10
 * @description：讀取網路圖片放進ImageView
 * ================================================
 */
public class UrlToBitmap {
    /**
     * 將圖片 Set 到 ImageView 上
     *
     * @param imgView ImageView 的元件 ID
     * @param imgUrl 圖片網址，String 格式
     */
    public static void convert(final ImageView imgView, String imgUrl) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                imgView.setImageBitmap (result);
                super.onPostExecute(result);
            }
        }.execute(imgUrl);
    }
    /**
     * 讀取網路圖片，型態為Bitmap
     *
     * @param imgUrl 圖片網址
     * @return Bitmap格式的圖片
     */
    private static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
