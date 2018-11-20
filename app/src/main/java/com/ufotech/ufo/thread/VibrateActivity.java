package com.ufotech.ufo.thread;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VibrateActivity extends AppCompatActivity {

    private TextView tv_post, tv_counter;
    Button btn_post_start, btn_counter_start, btn_counter_stop, btn_sharedpreference, btn_parse_page, btn_gson_page, btn_jsoup_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);

        initView();
        initListener();

        Spinner spinner = (Spinner) findViewById(R.id.spinnner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"長震動","短震動","連續短震動"});
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                //取得震動服務
                Vibrator myVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                switch(position) {
                    case 0:
                        //震動1秒
                        myVibrator.vibrate(1000);
                        break;
                    case 1:
                        //震動0.1秒
                        myVibrator.vibrate(100);
                        break;
                    case 2:
                        //停0.01秒之後震動0.1秒(重覆三次)
                        myVibrator.vibrate(new long[]{10, 100, 10, 100, 10, 100}, -1);
                        break;
                    default:
                        break;
                }
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(VibrateActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView(){
        tv_post = (TextView) findViewById(R.id.tv_post);
        btn_post_start = (Button) findViewById(R.id.btn_post_start);

        tv_counter = (TextView) findViewById(R.id.tv_counter);
        btn_counter_start = (Button) findViewById(R.id.btn_counter_start);
        btn_counter_stop = (Button) findViewById(R.id.btn_counter_stop);

        btn_sharedpreference = (Button) findViewById(R.id.btn_sharedpreference);
        btn_parse_page = (Button) findViewById(R.id.btn_parse_page);
        btn_gson_page = (Button) findViewById(R.id.btn_gson_page);
        btn_jsoup_page = (Button) findViewById(R.id.btn_jsoup_page);

    }

    private void initListener() {
        /**
             * 第1種 - Thread
             * 被呼叫的時候才出來做事
             * 工作完成就沒他的事了 ( 臨時工 )
             */
        btn_post_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tv_post.setText("完成");
                        System.out.println("t1退出!");
                    }
                }).start();
            }
        });

        btn_counter_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!t2_start) {
                    t2_start = true;
                    start_t2();
                    System.out.println("t2" + t2_start);
                } else {
                    t2_start = false;
                    t2.interrupt();
                }
            }
        });
        btn_counter_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2_start = false;
                t2.interrupt();
                i = 0;
                tv_counter.setText("0");
            }
        });

        btn_sharedpreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VibrateActivity.this, sharedpreferenceActivity.class);
                startActivity(intent);
            }
        });

        btn_parse_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VibrateActivity.this, ParseActivity.class);
                startActivity(intent);
            }
        });

        btn_gson_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VibrateActivity.this, GsonActivity.class);
                startActivity(intent);
            }
        });

        btn_jsoup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VibrateActivity.this, JsoupActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 第2種 - Thread + Handler
     * 特約工人，由 Handler 幫他更新 UI
     */
    private void start_t2() {
        Thread thread = new Thread(t2);
        thread.start();
    }
    private Boolean t2_start = false;
    private Thread t2 = new Thread(new Runnable(){
        @Override
        public void run() {
            // Handler 來幫忙告知你的程式說：你的執行緒跑完了，該更新一下畫面了喔！
            // 所以宣告一個 Message 的物件，利用 Handler 把這個 message send 出去處理
            // 並且每 1000 毫秒就送一次訊息
            while(t2_start) {
                try {
                    Message msg = new Message();
                    msg.what = 1; // 自己定義一個常數丟給 what 參數
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                }
                catch(InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
    private int i = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    i++;
                    tv_counter.setText(Integer.toString(i));
                    break;
            }
            return false;
        }
    });

    /**
     * 第3種 - HandlerThread + Handler + UIHandler
     */
    //找到UI工人的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)
    private Handler mUI_Handler = new Handler();
    //宣告特約工人的經紀人
    private Handler handler_1 = null;//老闆
    //宣告特約工人
    private HandlerThread handlerThread_1 = null;//員工
    //給他一個名字
    private String handlerThread_1_name = "我是1號員工";
    private void start_t3(){
        //聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)
        handlerThread_1 = new HandlerThread(handlerThread_1_name);
        //讓Worker待命，等待其工作 (開啟Thread)
        handlerThread_1.start();
        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
        handler_1 = new Handler(handlerThread_1.getLooper());
        //請經紀人指派工作名稱 runnable_1，給工人做
        handler_1.post(runnable_1);
    }
    //工作名稱 runnable_1 的工作內容
    private Runnable runnable_1 = new Runnable() {
        @Override
        public void run() {
            // TODO 要做的事情寫在這
            //.............................
            //做了很多事
            //請經紀人指派工作名稱 runnable_2，給工人做
            mUI_Handler.post(runnable_2);
            //老闆指定每隔幾秒要做一次工作1 (單位毫秒:1000等於1秒)
            handler_1.postDelayed(this, 1000);
        }
    };
    //工作名稱 runnable_2 的工作內容
    private Runnable runnable_2=new Runnable () {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //.............................
            //顯示畫面的動作
        }
    };

    /**
     * Thread + runOnUiThread
     */
    private void start_t4() {
        Thread t5 = new Thread(r4);
        t5.start();
    }
    private Runnable r4 = new Runnable() {
        @Override
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //TODO handle UI
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();

        if(t2!=null) {
            t2.interrupt();
            System.out.println("t2退出!");
        }

        //移除工人上的工作
        if (handler_1 != null) {
            handler_1.removeCallbacks(runnable_1);
        }
        //解聘工人 (關閉Thread)
        if (handlerThread_1 != null) {
            handlerThread_1.quit();
        }
    }
}
