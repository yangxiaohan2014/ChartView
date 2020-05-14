package com.example.mychartview;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PathActivity extends AppCompatActivity {
    private static final String TAG = "PathActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        double arc = Math.toRadians(30);
        double sin = Math.sin(arc);
        double cos = Math.cos(arc);
        Log.i(TAG, "onCreate: ========" + sin);
        Log.i(TAG, "onCreate: ========" + cos);
    }
}
