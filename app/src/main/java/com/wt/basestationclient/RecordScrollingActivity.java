package com.wt.basestationclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.wt.basestationclient.util.ConstantUtil;
import com.wt.basestationclient.util.HttpUtil;
import com.wt.basestationclient.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class RecordScrollingActivity extends AppCompatActivity {

    final CharSequence[] charSequences = {"高新区", "奎文区", "潍城区", "坊子区", "寒亭区", "滨海区", "寿光市", "青州市", "昌乐县", "临朐县", "诸城市", "安丘市", "昌邑市高密市"};

    EditText stationNameEdit = null;
    EditText stationQuEdit = null;
    EditText latiET = null;
    EditText longtiET = null;
    EditText platNumEt = null;
    EditText tianxianEt = null;
    EditText fangweijiaoEt =null;
    EditText jixieXQJET = null;
    EditText dianziXQJET = null;
    EditText stationHeightET = null;
    EditText beautTXET = null;
    EditText tianxianTypeET = null;
    EditText floorET = null;
    EditText tianxianModelET = null;
    EditText RRULocationET = null;
    EditText haveCnetDeviceET = null;
    EditText cNetDeviceModelET  = null;
    EditText hasFartherET = null;
    EditText roomShareET = null;
    EditText hasAirConditionET = null;
    EditText hasBatteryET = null;
    EditText questionET = null;
    EditText picET = null;


    Button recordBtn = null;

    public LocationClient mLocationClient = null;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                Toast.makeText(RecordScrollingActivity.this,"录入信息成功",Toast.LENGTH_LONG).show();
            } else if (msg.what == -1) {
                Toast.makeText(RecordScrollingActivity.this,"录入信息失败",Toast.LENGTH_LONG).show();
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scrolling);

        stationQuEdit = (EditText) findViewById(R.id.ed_station_name_qu);
        stationNameEdit = (EditText) findViewById(R.id.ed_station_name);

        final String stationName = getIntent().getStringExtra("stationName");
        stationNameEdit.setText(stationName);
        latiET = (EditText) findViewById(R.id.recordLatitudeET);
        longtiET = (EditText) findViewById(R.id.recordLongtitudeET);
        recordBtn = (Button) findViewById(R.id.recordBtn);

        initComponent();

        //获取经纬度
        getLocation();

        stationQuEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordScrollingActivity.this);

                builder.setTitle("请选择基站所在县区")
                        .setItems(charSequences, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stationQuEdit.setText(charSequences[which]);
                            }
                        }).show();
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,String> paramMap = new HashMap<String,String>();
                        paramMap.put("stationName",stationNameEdit.getText().toString());
                        paramMap.put("addressQu",stationQuEdit.getText().toString());
//                paramMap.put("address",stationQuEdit.getText().toString());
                        paramMap.put("latitude",latiET.getText().toString());
                        paramMap.put("longtitude",longtiET.getText().toString());

                        String url = ConstantUtil.REQUEST_URL + "addInfo";
                        String httpResult = HttpUtil.doPost(url,paramMap);
                        LogUtil.i("record result:" + httpResult);
                        if (httpResult == null) {
                            handler.sendEmptyMessage(-1);
                            return;
                        }
                        JSONObject jsonObject = JSON.parseObject(httpResult);
                        if (jsonObject.getIntValue("retcode") == 0) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(-1);
                        }
                    }
                }).start();
            }
        });

    }

    private void initComponent() {
        platNumEt = (EditText) findViewById(R.id.recordPlatNumET);
        tianxianEt = (EditText) findViewById(R.id.recordTianxianPlatET);
        fangweijiaoEt = (EditText) findViewById(R.id.recordFangweijiaoET);
        jixieXQJET = (EditText) findViewById(R.id.recordJixieXQJET);
        dianziXQJET = (EditText) findViewById(R.id.recordDianziXQJET);
        stationHeightET = (EditText) findViewById(R.id.recordDianziXQJET);
        beautTXET = (EditText) findViewById(R.id.recordBeautTianxianET);
        tianxianTypeET = (EditText) findViewById(R.id.recordTianxianTypeET);
        floorET = (EditText) findViewById(R.id.recordFloorET);
        tianxianModelET = (EditText) findViewById(R.id.recordTianxianModelET);
        RRULocationET = (EditText) findViewById(R.id.recordRRULocationET);
        haveCnetDeviceET = (EditText) findViewById(R.id.recordHasCnetDeviceET);
        cNetDeviceModelET = (EditText) findViewById(R.id.recordCNetModelET);
        hasFartherET = (EditText) findViewById(R.id.recordHasFartherET);
        roomShareET = (EditText) findViewById(R.id.recordRoomShareET);
        hasAirConditionET = (EditText) findViewById(R.id.recordHasAirConditioningET);
        hasBatteryET = (EditText) findViewById(R.id.recordHasBatteryET);
        questionET = (EditText) findViewById(R.id.recordQuestionET);
        picET = (EditText) findViewById(R.id.recordImgET);

    }


    public void getLocation() {

        mLocationClient = new LocationClient(getApplicationContext());

        MyLocationListener myLocationListener = new MyLocationListener();

        mLocationClient.registerLocationListener(myLocationListener);

        mLocationClient.start();



    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            latiET.setText(bdLocation.getLatitude() + "");
            longtiET.setText(bdLocation.getLongitude() + "");

            LogUtil.i(bdLocation.getLocType() + "----" + bdLocation.getLatitude() + "," + bdLocation.getLongitude());
        }
    }

}
