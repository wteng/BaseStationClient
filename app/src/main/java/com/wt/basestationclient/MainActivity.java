package com.wt.basestationclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wt.basestationclient.util.ConstantUtil;
import com.wt.basestationclient.util.HttpUtil;
import com.wt.basestationclient.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog = null;
    EditText serverInfoET = null;
    EditText stationNameET = null;
    Button settingBtn = null;
    Button recordBtn = null;
    Button queryBtn = null;
    Button modifyBtn = null;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            selfHandleMessage(msg);
        }
    };

    public void selfHandleMessage(Message msg) {
        if (msg.what == -2) {
            Toast.makeText(MainActivity.this,"服务器端出错，请联系管理员",Toast.LENGTH_SHORT).show();
        } else if (msg.what == -1) {
            Toast.makeText(MainActivity.this, "请求超时，请重试！", Toast.LENGTH_SHORT).show();
        } else if (msg.what == 1) {
            progressDialog.dismiss();
            if (msg.arg1 == ConstantUtil.RECORD_BUTTON_ID) {
                String stationName = msg.obj.toString();
                Intent intent = new Intent(MainActivity.this, RecordScrollingActivity.class);
                intent.putExtra("stationName", stationName);
                startActivity(intent);
            } else if (msg.arg1 == ConstantUtil.QUERY_BUTTON_ID) {
                Toast.makeText(MainActivity.this,"该基站不存在，请点击信息录入按钮！！！",Toast.LENGTH_SHORT).show();
            } else if (msg.arg1 == ConstantUtil.MODIFY_BUTTON_ID) {
                Toast.makeText(MainActivity.this,"该基站不存在，请点击信息录入按钮！！！",Toast.LENGTH_SHORT).show();
            }

        } else if (msg.what == 2) {
            progressDialog.dismiss();
            if (msg.arg1 == ConstantUtil.RECORD_BUTTON_ID) {
                Toast.makeText(MainActivity.this,"该基站已经存在，请点击信息查询按钮或信息修改按钮！！！",Toast.LENGTH_SHORT).show();
            } else if (msg.arg1 == ConstantUtil.QUERY_BUTTON_ID) {
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                intent.putExtra("bodyStr", msg.obj.toString());
                startActivity(intent);
            } else if (msg.arg1 == ConstantUtil.MODIFY_BUTTON_ID) {
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                intent.putExtra("bodyStr", msg.obj.toString());
                startActivity(intent);
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

        //查看服务器是否已经设置
        final SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String serverInfo = sharedPreferences.getString("serverInfo",null);
        if (serverInfo != null && !serverInfo.isEmpty()) {
            serverInfoET.setText(serverInfo);
            ConstantUtil.REQUEST_URL = serverInfo;
        }



        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDown(ConstantUtil.RECORD_BUTTON_ID);
            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDown(ConstantUtil.QUERY_BUTTON_ID);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonDown(ConstantUtil.MODIFY_BUTTON_ID);

                //预留的登录按钮
//                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverInfoStr = serverInfoET.getText().toString().trim();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("serverInfo",serverInfoStr);
                editor.commit();

                ConstantUtil.REQUEST_URL = serverInfoStr;

                Toast.makeText(MainActivity.this,"设置服务器信息成功！！",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void initComponent() {
        recordBtn = (Button) findViewById(R.id.record);
        queryBtn = (Button) findViewById(R.id.query);
        modifyBtn = (Button) findViewById(R.id.modify);
        progressDialog = new ProgressDialog(this);
        serverInfoET = (EditText) findViewById(R.id.server_info_edit);
        settingBtn = (Button) findViewById(R.id.set_server_info_btn);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在努力的加载中。。");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);

        stationNameET = (EditText) findViewById(R.id.main_station_edit);
    }

    public void onButtonDown(final int buttonId) {

        if (!checkServerInfo()) {
            Toast.makeText(MainActivity.this, "请先设置服务器信息！", Toast.LENGTH_SHORT).show();
            return;
        }

        final String stationName = stationNameET.getText().toString().trim();

        if (stationName.isEmpty()) {
            Toast.makeText(MainActivity.this, "请输入基站名称", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = ConstantUtil.REQUEST_URL + "/getStationInfoByName?stationName=" + stationName;
                String httpResult = HttpUtil.doGet(url);
                LogUtil.i(httpResult);
                if (httpResult == null) {
                    progressDialog.dismiss();
                    handler.sendEmptyMessage(-1);
                    return;
                }

                JSONObject jsonObject = JSON.parseObject(httpResult);
                if (jsonObject.getIntValue("retcode") != 0) {
                    handler.sendEmptyMessage(-2);
                    return;
                }

                Object bodyObject = jsonObject.get("body");
                if (bodyObject == null) {
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = buttonId;
                    message.obj = stationName;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = bodyObject;
                    message.arg1 = buttonId;
                    handler.sendMessage(message);
                }

            }
        }).start();

    }


    public boolean checkServerInfo() {
        if (ConstantUtil.REQUEST_URL == null || ConstantUtil.REQUEST_URL.isEmpty()) {
            return false;
        }
        return true;
    }

}
