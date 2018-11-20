package com.ufotech.ufo.thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.util.ArrayList;

public class ParseActivity extends AppCompatActivity {

    protected Button jsonbtnobj;
    protected ListView jsonlvobj;
    private String TAG = "ParseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);
        Func();
    }

    protected void Func() {
        jsonbtnobj = (Button) findViewById(R.id.jsonbtn);
        jsonlvobj = (ListView) findViewById(R.id.jsonlv);

        String urlParkingArea = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a880adf3-d574-430a-8e29-3192a41897a5";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                urlParkingArea,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        jsonbtnobj.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    parserJson(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error : " + error.toString());
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    protected void parserJson(JSONObject jsonObject) throws JSONException {
        ArrayList<String> list = new ArrayList();
        JSONArray data = jsonObject.getJSONObject("result").getJSONArray("results");
        for (int i = 0; i < data.length(); i++) {
            JSONObject o = data.getJSONObject(i);
            String str ="_id : "+o.getString("_id")+"\n"+
                    "項次 : "+o.getString("項次")+"\n"+
                    "停車場名稱 : "+o.getString("停車場名稱")+"\n"+
                    "經度(WGS84) : "+o.getString("經度(WGS84)")+"\n"+
                    "緯度(WGS84) : "+o.getString("緯度(WGS84)");
            list.add(str);
        }
        jsonlvobj.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list));

    }
}
