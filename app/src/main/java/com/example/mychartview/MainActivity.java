package com.example.mychartview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.line).setOnClickListener(this);
        findViewById(R.id.pie).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.line:
                startActivity(new Intent(this,LineAndColumnActivity.class));
                break;
            case R.id.pie:
                startActivity(new Intent(this,PieActivity.class));
                break;
        }
    }
}
