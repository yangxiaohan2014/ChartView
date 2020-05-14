package com.example.mychartview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;

import com.example.mychartview.ChartView.XPoint;

import java.util.ArrayList;

public class LineAndColumnActivity extends AppCompatActivity {

    ChartView chartView;
    ArrayList<XPoint> xPoints = new ArrayList<XPoint>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        chartView = findViewById(R.id.chart);
        xPoints.add(new XPoint("4/9", 5f, 20f));
        xPoints.add(new XPoint("4/10", 2f, 60f));
        xPoints.add(new XPoint("4/11", 1.5f, 6f));
        xPoints.add(new XPoint("4/12", 08f, 25f));
        xPoints.add(new XPoint("4/9", 5f, 20f));
        xPoints.add(new XPoint("4/10", 2f, 60f));
        xPoints.add(new XPoint("4/11", 1.5f, 6f));
        xPoints.add(new XPoint("4/12", 08f, 25f));
        xPoints.add(new XPoint("4/11", 1.5f, 6f));
        xPoints.add(new XPoint("4/12", 08f, 25f));



        chartView.setStepYNumber(20);
        chartView.setX(xPoints);
        chartView.setY(new String[]{"20", "40", "60", "80"});

    }
}
