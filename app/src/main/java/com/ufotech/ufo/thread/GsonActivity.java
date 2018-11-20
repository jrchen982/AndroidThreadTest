package com.ufotech.ufo.thread;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GsonActivity extends AppCompatActivity {

    protected Button jsonbtnobj;
    protected TextView jsontvobj;
    protected com.google.gson.Gson gson;
    protected Context context;
    protected String jsondataobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        findview();
    }
    protected void findview() {
        gson = new com.google.gson.Gson();
        jsontvobj = (TextView) findViewById(R.id.gsontv);
        jsonbtnobj = (Button) findViewById(R.id.gsonbtn);

        jsonbtnobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsondataobj = getString(R.string.jsondata);
                GSONData user = gson.fromJson( jsondataobj, GSONData.class);
                jsontvobj.setText("USER ID : " + user.getUserId() + "\n" +
                                    "Password : " + user.getPwd() + "\n" +
                                    "Book 1 : " + user.getBooklist()[0] + "\n" +
                                    "Book 2 : " + user.getBooklist()[1] + "\n" +
                                    "Book 3 : " + user.getBooklist()[2] + "\n"
                );
            }
        });

    }

    public class GSONData {

        private String userId;
        private int pwd;
        private String[] booklist;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getPwd() {
            return pwd;
        }

        public void setPwd(int pwd) {
            this.pwd = pwd;
        }

        public String[] getBooklist() {
            return booklist;
        }

        public void setBooklist(String[] booklist) {
            this.booklist = booklist;
        }
    }
}
