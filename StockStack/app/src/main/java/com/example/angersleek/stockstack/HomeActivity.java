package com.example.angersleek.stockstack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    ImageView viewStock, barcodeUpdate, orderStock, addDelStock, registerNewUser, chartMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initiateImgViews();
    }

    public void switchActivity(View view){
        Intent i;
        switch (view.getId()) {
            case R.id.imgV_View_Stock:
                i = new Intent(getApplicationContext(), ViewStockActivity.class);
                startActivity(i);
                break;

            case R.id.imgV__Barcode_Update:
                i = new Intent(getApplicationContext(), BarcodeActivity.class);
                startActivity(i);
                break;

            case R.id.imgV_Order_Stock:
                i = new Intent(getApplicationContext(), OrderStockActivity.class);
                startActivity(i);
                break;

            case R.id.imgV_Add_delete_Stock:
                i = new Intent(getApplicationContext(), AddDeleteActivity.class);
                startActivity(i);
                break;

            case R.id.imgV_Register_New_User:
                i = new Intent(getApplicationContext(), RegisterNewUserActivity.class);
                startActivity(i);
                break;

            case R.id.imgV_Charts:
                i = new Intent(getApplicationContext(), ChartMainActivity.class);
                startActivity(i);
                break;
        }
    }

    public void initiateImgViews(){
        viewStock = (ImageView)findViewById(R.id.imgV_View_Stock);
        barcodeUpdate = (ImageView)findViewById(R.id.imgV__Barcode_Update);
        orderStock = (ImageView)findViewById(R.id.imgV_Order_Stock);
        addDelStock = (ImageView)findViewById(R.id.imgV_Add_delete_Stock);
        registerNewUser = (ImageView)findViewById(R.id.imgV_Register_New_User);
        chartMainPage = (ImageView)findViewById(R.id.imgV_Charts);
    }
}

