package com.example.angersleek.stockstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ChartMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button pieChartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("                   Charts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pieChartBtn = (Button)findViewById(R.id.pieChartBtn);
    }

    public void pieChart(View view){
        Intent i = new Intent(getApplicationContext(), PieChartActivity.class);
        startActivity(i);
    }
}
