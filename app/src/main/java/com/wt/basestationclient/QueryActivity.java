package com.wt.basestationclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wt.basestationclient.util.LogUtil;

import java.lang.reflect.Field;


public class QueryActivity extends AppCompatActivity {

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
    RadioButton beautTianxinaRBN = null;
    View tianxianTypeLayout = null;
    EditText tianxianTypeET = null;
    EditText floorET = null;
    EditText wirelessCompanyET = null;
    EditText tianxianModelET = null;
    EditText RRULocationET = null;
    RadioGroup hasCnetDeviceRG = null;
    RadioButton hasCnetDeviceRBN = null;
    EditText cNetDeviceModelET  = null;
    View cNetDeviceLayout = null;
    TextView hasFartherTV = null;
    TextView hasRoomShareTV = null;
    TextView hasAirConditioningTV = null;
    TextView hasBatteryTV = null;
    EditText questionET = null;
    EditText picET = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        initComponent();

        String bodyStr = getIntent().getStringExtra("bodyStr");
        JSONObject jsonObject = JSON.parseObject(bodyStr);
        String stationName = jsonObject.getString("stationName");
        stationNameEdit.setText(stationName);
        stationQuEdit.setText(jsonObject.getString("addressQu"));
        stationAddressEdit.setText(jsonObject.getString("address"));
        latiET.setText(jsonObject.getString("latitude"));
        longtiET.setText(jsonObject.getString("longtitude"));
        int platformNumber = jsonObject.getIntValue("platformNumber");
        platNumNumPicker.setMaxValue(platformNumber);
        platNumNumPicker.setMinValue(platformNumber);
        platNumNumPicker.setValue(platformNumber);

        tianxianPlatEt.setText(jsonObject.getString("tianxianPlatform"));
        fangweijiaoEt.setText(jsonObject.getString("fangweijiao"));
        jixieXQJET.setText(jsonObject.getString("jixieXiaqingjiao"));
        dianziXQJET.setText(jsonObject.getString("dianziXiaqingjiao"));
        stationHeightET.setText(jsonObject.getString("stationHeight"));

        boolean beautifulTianxian = jsonObject.getBoolean("beautifulTianxian");
        if (!beautifulTianxian) {
            beautTianxinaRBN.setChecked(true);
            tianxianTypeLayout.setVisibility(View.GONE);
        } else {
            tianxianTypeET.setText(jsonObject.getString("tianxianType"));
        }

        boolean hasCnetDevice = jsonObject.getBoolean("haveCnetDevice");
        if (!hasCnetDevice) {
            hasCnetDeviceRBN.setChecked(true);
            cNetDeviceLayout.setVisibility(View.GONE);
        } else {
            cNetDeviceModelET.setText(jsonObject.getString("cnetDeviceModel"));
        }

        floorET.setText(jsonObject.getString("floor"));
        wirelessCompanyET.setText(jsonObject.getString("wirelessCompany"));
        tianxianModelET.setText(jsonObject.getString("tianxianModel"));
        RRULocationET.setText(jsonObject.getString("rRULocation"));
        questionET.setText(jsonObject.getString("question"));
        picET.setText(jsonObject.getString("picPath"));

        hasFartherTV.setText(changeBooleanToText(jsonObject.getBoolean("hasFarther")));
        hasRoomShareTV.setText(changeBooleanToText(jsonObject.getBoolean("roomShare")));
        hasAirConditioningTV.setText(changeBooleanToText(jsonObject.getBoolean("hasAirConditioning")));
        hasBatteryTV.setText(changeBooleanToText(jsonObject.getBoolean("hasBattery")));


    }

    private String changeBooleanToText(Boolean bool) {
        if (bool) {
            return "是";
        }

        return "否";
    }

    private void initComponent() {

        stationQuEdit = (EditText) findViewById(R.id.ed_station_name_qu);
        stationQuEdit.setEnabled(false);

        latiET = (EditText) findViewById(R.id.queryLatitudeET);

        longtiET = (EditText) findViewById(R.id.queryLongtitudeET);
        
        stationNameEdit = (EditText) findViewById(R.id.ed_query_station_name);
        stationAddressEdit = (EditText) findViewById(R.id.queryStationAddressET);
        platNumNumPicker = (NumberPicker) findViewById(R.id.queryPlatNumNP);

        tianxianPlatEt = (EditText) findViewById(R.id.queryTianxianPlatET);
        fangweijiaoEt = (EditText) findViewById(R.id.queryFangweijiaoET);
        jixieXQJET = (EditText) findViewById(R.id.queryJixieXQJET);
        dianziXQJET = (EditText) findViewById(R.id.queryDianziXQJET);
        stationHeightET = (EditText) findViewById(R.id.queryStationHeightET);
        beautTianxianRG = (RadioGroup) findViewById(R.id.beautTianxianRG);
        setClickable(beautTianxianRG);
        beautTianxinaRBN = (RadioButton) findViewById(R.id.beautTianxianRBN);

        tianxianTypeLayout = findViewById(R.id.tianxianTypeLayout);

        tianxianTypeET = (EditText) findViewById(R.id.queryTianxianTypeET);
        floorET = (EditText) findViewById(R.id.queryFloorET);
        wirelessCompanyET = (EditText) findViewById(R.id.queryTianxianCJET);
        tianxianModelET = (EditText) findViewById(R.id.queryTianxianModelET);
        RRULocationET = (EditText) findViewById(R.id.queryRRULocationET);
        hasCnetDeviceRG = (RadioGroup) findViewById(R.id.hasCnetDeviceRG);
        setClickable(hasCnetDeviceRG);
        hasCnetDeviceRBN = (RadioButton) findViewById(R.id.hasCnetDeviceRBN);
        cNetDeviceModelET = (EditText) findViewById(R.id.queryCNetModelET);
        cNetDeviceLayout = findViewById(R.id.cNetModelLayout);

        hasFartherTV = (TextView) findViewById(R.id.hasFartherTV);
        hasRoomShareTV = (TextView) findViewById(R.id.hasRoomShareTV);
        hasAirConditioningTV = (TextView) findViewById(R.id.hasAirconditionTV);
        hasBatteryTV = (TextView) findViewById(R.id.hasBatteryTV);

        questionET = (EditText) findViewById(R.id.queryQuestionET);
        picET = (EditText) findViewById(R.id.queryImgET);

        setETDisable();
    }

    public void setClickable(RadioGroup radioGroup) {
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setClickable(false);
        }
    }

    public void setETDisable() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field tmpField : fields) {
            try {
                Object fieldObj = tmpField.get(this);

                String fieldClassName = fieldObj.getClass().getSimpleName();
                if (fieldClassName.equals("AppCompatEditText")) {
                    ((EditText)fieldObj).setEnabled(false);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }




}
