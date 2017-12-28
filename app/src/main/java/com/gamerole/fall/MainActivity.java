package com.gamerole.fall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.gamerole.fallview.FallObject;
import com.gamerole.fallview.FallingView;
import com.yhao.floatwindow.FloatWindow;

public class MainActivity extends AppCompatActivity {

    private Button fallingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化一个雪花样式的fallObject


        fallingView = (Button) findViewById(R.id.bt);
        fallingView.setText("第一页");

        fallingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main1Activity.class));
            }
        });
        //初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.mipmap.ic_cloud));
        FallObject fallObject = builder
                .setSpeed(3, true)
                .setSize(50, 50, true)
                .setWind(5, true, true)
                .build();

        FallingView fallingView = new FallingView(this);
        fallingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        fallingView.addFallObject(fallObject, 100);//添加50个下落物体对象
        FloatWindow
                .with(getApplicationContext())
                .setView(fallingView)
                .build();
    }
}
