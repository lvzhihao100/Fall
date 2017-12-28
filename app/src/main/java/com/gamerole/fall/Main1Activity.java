package com.gamerole.fall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yhao.floatwindow.FloatWindow;

public class Main1Activity extends AppCompatActivity {

    private Button fallingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化一个雪花样式的fallObject


        fallingView = (Button) findViewById(R.id.bt);
        fallingView.setText("第二页");

    }
}
