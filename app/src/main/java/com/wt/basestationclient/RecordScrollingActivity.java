package com.wt.basestationclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    EditText stationAddressEdit = null;
    EditText latiET = null;
    EditText longtiET = null;
    NumberPicker platNumNumPicker = null;
    EditText tianxianPlatEt = null;
    EditText fangweijiaoEt =null;
    EditText jixieXQJET = null;
    EditText dianziXQJET = null;
    EditText stationHeightET = null;
    RadioGroup beautTianxianRG = null;
    View tianxianTypeLayout = null;
    EditText tianxianTypeET = null;
    EditText floorET = null;
    EditText wirelessComanyET = null;
    EditText tianxianModelET = null;
    EditText RRULocationET = null;
    RadioGroup hasCnetDeviceRG = null;
    EditText cNetDeviceModelET  = null;
    View cNetDeviceLayout = null;
    RadioGroup hasFartherRG = null;
    RadioGroup hasRoomShareRG = null;
    RadioGroup hasAirConditioningRG = null;
    RadioGroup hasBatteryRG = null;
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

        initComponent();

        String stationName = getIntent().getStringExtra("stationName");
        stationNameEdit.setText(stationName);

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
                        paramMap.put("address",stationAddressEdit.getText().toString());
                        paramMap.put("latitude",latiET.getText().toString());
                        paramMap.put("longtitude",longtiET.getText().toString());
                        paramMap.put("platformNumber",String.valueOf(platNumNumPicker.getValue()));
                        paramMap.put("tianxianPlatform",tianxianPlatEt.getText().toString());
                        paramMap.put("fangweijiao",fangweijiaoEt.getText().toString());
                        paramMap.put("jixieXiaqingjiao",jixieXQJET.getText().toString());
                        paramMap.put("dianziXiaqingjiao",dianziXQJET.getText().toString());
                        paramMap.put("stationHeight",stationHeightET.getText().toString());
                        paramMap.put("floor",floorET.getText().toString());
                        paramMap.put("wirelessCompany",wirelessComanyET.getText().toString());
                        paramMap.put("tianxianModel",tianxianModelET.getText().toString());
                        paramMap.put("RRULocation",RRULocationET.getText().toString());
                        paramMap.put("question",questionET.getText().toString());
                        paramMap.put("picPath",picET.getText().toString());

                        int beautTianxian = changeRGToIntValue(beautTianxianRG);
                        if (beautTianxian == 1) {
                            paramMap.put("beautifulTianxian","1");
                            paramMap.put("tianxianType",tianxianTypeET.getText().toString());
                        } else {
                            paramMap.put("beautifulTianxian","0");
                        }

                        int hasCnetDevice = changeRGToIntValue(hasCnetDeviceRG);
                        if (hasCnetDevice == 1) {
                            paramMap.put("haveCnetDevice","1");
                            paramMap.put("cNetDeviceModel",cNetDeviceModelET.getText().toString());
                        } else {
                            paramMap.put("haveCnetDevice","0");
                        }

                        paramMap.put("hasFarther",changeRGToIntValue(hasFartherRG) + "");
                        paramMap.put("roomShare",changeRGToIntValue(hasRoomShareRG) + "");
                        paramMap.put("hasAirConditioning",changeRGToIntValue(hasAirConditioningRG) + "");
                        paramMap.put("hasBattery",changeRGToIntValue(hasBatteryRG) + "");

                        String url = ConstantUtil.REQUEST_URL + "/addInfo";
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

    private int changeRGToIntValue(RadioGroup radioGroup) {
        int checkedButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(checkedButtonId);
        if (radioButton.getText().toString().equals("是")) {
            return 1;
        }

        return 0;

    }

    private void initComponent() {
        stationQuEdit = (EditText) findViewById(R.id.ed_station_name_qu);
        stationNameEdit = (EditText) findViewById(R.id.ed_station_name);
        latiET = (EditText) findViewById(R.id.recordLatitudeET);
        longtiET = (EditText) findViewById(R.id.recordLongtitudeET);
        recordBtn = (Button) findViewById(R.id.recordBtn);

        stationAddressEdit = (EditText) findViewById(R.id.recordStationAddressET);
        platNumNumPicker = (NumberPicker) findViewById(R.id.recordPlatNumNP);
        platNumNumPicker.setMaxValue(100);
        platNumNumPicker.setMinValue(1);
        platNumNumPicker.setValue(4);

        tianxianPlatEt = (EditText) findViewById(R.id.recordTianxianPlatET);
        fangweijiaoEt = (EditText) findViewById(R.id.recordFangweijiaoET);
        jixieXQJET = (EditText) findViewById(R.id.recordJixieXQJET);
        dianziXQJET = (EditText) findViewById(R.id.recordDianziXQJET);
        stationHeightET = (EditText) findViewById(R.id.recordStationHeightET);
        beautTianxianRG = (RadioGroup) findViewById(R.id.beautTianxianRG);
        tianxianTypeLayout = findViewById(R.id.tianxianTypeLayout);
        beautTianxianRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) RecordScrollingActivity.this.findViewById(radioButtonId);
                if (radioButton.getText().toString().equals("是")) {
                    tianxianTypeLayout.setVisibility(View.VISIBLE);
                } else {
                    tianxianTypeLayout.setVisibility(View.GONE);
                }
            }
        });

        tianxianTypeET = (EditText) findViewById(R.id.recordTianxianTypeET);
        floorET = (EditText) findViewById(R.id.recordFloorET);
        wirelessComanyET = (EditText) findViewById(R.id.recordTianxianCJET);
        tianxianModelET = (EditText) findViewById(R.id.recordTianxianModelET);
        RRULocationET = (EditText) findViewById(R.id.recordRRULocationET);
        hasCnetDeviceRG = (RadioGroup) findViewById(R.id.hasCnetDeviceRG);
        cNetDeviceModelET = (EditText) findViewById(R.id.recordCNetModelET);
        cNetDeviceLayout = findViewById(R.id.cNetModelLayout);
        hasCnetDeviceRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) RecordScrollingActivity.this.findViewById(radioButtonId);
                if (radioButton.getText().toString().equals("是")) {
                    cNetDeviceLayout.setVisibility(View.VISIBLE);
                } else {
                    cNetDeviceLayout.setVisibility(View.GONE);
                }
            }
        });


        hasFartherRG = (RadioGroup) findViewById(R.id.hasFartherRG);
        hasRoomShareRG = (RadioGroup) findViewById(R.id.hasRoomShareRG);
        hasAirConditioningRG = (RadioGroup) findViewById(R.id.hasAirConditionRG);
        hasBatteryRG = (RadioGroup) findViewById(R.id.hasBatteryRG);
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
