package com.wt.basestationclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.wt.basestationclient.util.LogUtil;

public class RecordScrollingActivity extends AppCompatActivity {

    final CharSequence[] charSequences = {"高新区", "奎文区", "潍城区", "坊子区", "寒亭区", "滨海区", "寿光市", "青州市", "昌乐县", "临朐县", "诸城市", "安丘市", "昌邑市高密市"};

    EditText latiET = null;
    EditText longtiET = null;

    public LocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scrolling);

        final EditText stationQuEdit = (EditText) findViewById(R.id.ed_station_name_qu);
        EditText stationNameEdit = (EditText) findViewById(R.id.ed_station_name);

        String stationName = getIntent().getStringExtra("stationName");
        stationNameEdit.setText(stationName);
        latiET = (EditText) findViewById(R.id.recordLatitudeET);
        longtiET = (EditText) findViewById(R.id.recordLongtitudeET);
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
