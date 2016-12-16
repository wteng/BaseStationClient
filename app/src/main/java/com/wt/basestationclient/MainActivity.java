package com.wt.basestationclient;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recordBtn = (Button) findViewById(R.id.record);
        Button queryBtn = (Button) findViewById(R.id.query);
        Button modifyBtn = (Button) findViewById(R.id.modify);

        final EditText editText = (EditText) findViewById(R.id.main_station_edit);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                Intent intent = new Intent(MainActivity.this,RecordScrollingActivity.class);
                startActivity(intent);

            }
        };

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stationName = editText.getText().toString().trim();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AndroidHttpClient 

                    }
                });


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
