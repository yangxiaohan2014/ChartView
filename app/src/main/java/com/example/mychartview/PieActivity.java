package com.example.mychartview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PieActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        getData();


    }

    private void getData() {
        PieChart chart = findViewById(R.id.pie_chart);
        ArrayList<PieChart.DataEntity> data = new ArrayList<>();
        data.add(new PieChart.DataEntity("11",0.05f));
        data.add(new PieChart.DataEntity("22",0.5f));
        data.add(new PieChart.DataEntity("33",0.3f));
        data.add(new PieChart.DataEntity("44",0.15f));
        chart.setDataleList(data);

    }
}
