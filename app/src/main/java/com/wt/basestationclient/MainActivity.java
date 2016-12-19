package com.wt.basestationclient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
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
import com.wt.basestationclient.util.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recordBtn = (Button) findViewById(R.id.record);
        Button queryBtn = (Button) findViewById(R.id.query);
        Button modifyBtn = (Button) findViewById(R.id.modify);
        progressDialog = new ProgressDialog(this);

        final EditText editText = (EditText) findViewById(R.id.main_station_edit);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == -1) {
                    Toast.makeText(MainActivity.this, "请求超时，请重试！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    progressDialog.dismiss();
                    String stationName = msg.obj.toString();
                    Intent intent = new Intent(MainActivity.this, RecordScrollingActivity.class);
                    intent.putExtra("stationName", stationName);
                    startActivity(intent);
                }


            }
        };

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String stationName = editText.getText().toString().trim();

                if (stationName.isEmpty()) {
                    Toast.makeText(MainActivity.this,"请输入基站名称",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在努力的加载中。。");
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("Android");
                        BasicHttpContext basicHttpContext = new BasicHttpContext();
                        basicHttpContext.setAttribute(ClientContext.COOKIE_STORE,new BasicCookieStore());

                        try {
                            String url = ConstantUtil.REQUEST_URL + "getStationInfoByName?stationName=" + stationName;
                            HttpGet httpGet = new HttpGet(new URI(url));
                            HttpResponse httpResponse = androidHttpClient.execute(httpGet,basicHttpContext);
                            HttpEntity httpEntity = httpResponse.getEntity();
                            String httpResult = EntityUtils.toString(httpEntity);
                            LogUtil.i(httpResult);
                            JSONObject jsonObject = JSON.parseObject(httpResult);
                            if (jsonObject.getIntValue("retcode") != 0) {
                                Toast.makeText(MainActivity.this,"服务器端出错，请联系管理员",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Object bodyObject = jsonObject.get("body");
                            if (bodyObject == null) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = stationName;
                                handler.sendMessage(message);
                            }

                        } catch (IOException e) {
                            progressDialog.dismiss();
                            handler.sendEmptyMessage(-1);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });





    }

}
