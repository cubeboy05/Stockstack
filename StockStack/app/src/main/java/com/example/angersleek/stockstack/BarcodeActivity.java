package com.example.angersleek.stockstack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BarcodeActivity extends AppCompatActivity {

    Toolbar toolbar;
    final String ZXING_SCAN = "com.google.zxing.client.android.SCAN";
    Intent i;
    AlertDialog.Builder alert;
    RingButton ringButton;
    boolean isAdd = true;

    private StringRequest requestBarcode;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("          Scan Barcode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ringButton = (RingButton)findViewById(R.id.ringButton);
        requestQueue = Volley.newRequestQueue(this);

        ringButton.setOnClickListener(new RingButton.OnClickListener() {
            @Override
            public void clickUp() {
                isAdd = true;
                scanItem();
            }

            @Override
            public void clickDown() {
                isAdd = false;
                scanItem();
            }
        });
    }

    public void scanItem(){
        try {
            i = new Intent(ZXING_SCAN);
            i.putExtra("SCAN_MODE", "PRODUCT_MODE");
            startActivityForResult(i, 0);
        } catch (ActivityNotFoundException e) {
            showAlert(BarcodeActivity.this, "No Scanner Found", "Download a Scanner?", "Yes", "No");
            e.printStackTrace();
        }
    }

    private Dialog showAlert(final Activity act, CharSequence title, CharSequence message, CharSequence yes, CharSequence no){

        alert = new AlertDialog.Builder(act);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://search?q-pname:" + "com.google.zxing.client.android");

                try {
                    act.startActivity(i);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        alert.setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                String scanResult = data.getStringExtra("SCAN_RESULT");
                sendBarcodeForUpdate(isAdd, scanResult);
                //String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                //Toast.makeText(getApplicationContext(), "Content: " + scanResult + ", Format: " + format, Toast.LENGTH_LONG).show();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Barcode not working...", Toast.LENGTH_LONG).show();
            }
        }
    }

    void sendBarcodeForUpdate(boolean isAddition, final String scanResult){

        final String KARSV_BARCODE_ADD = "http://www.karsv.com/app/barcode_add.php";
        final String KARSV_BARCODE_REMOVE = "http://www.karsv.com/app/barcode_remove.php";
        String temp;

        if(isAddition)
            temp = KARSV_BARCODE_ADD;
        else
            temp = KARSV_BARCODE_REMOVE;

        requestBarcode = new StringRequest(Request.Method.POST, temp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.get("result").toString().equals("Item Added")){
                        Toast.makeText(getApplicationContext(), "1 Item Added", Toast.LENGTH_SHORT).show();
                    }
                    else if(jsonObject.get("result").toString().equals("Item doesn't exists!")){
                        new AlertDialog.Builder(BarcodeActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Item not recognized")
                                .setMessage("Create new item for this barcode?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(BarcodeActivity.this, AddNewItemActivity.class);
                                        i.putExtra("barcode", scanResult);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    else if(jsonObject.get("result").toString().equals("Item removed")){
                        Toast.makeText(getApplicationContext(), "1 Item removed", Toast.LENGTH_SHORT).show();
                    }
                    else if(jsonObject.get("result").toString().equals("Item has 0 quantity")){
                        Toast.makeText(getApplicationContext(), "Item has 0 quantity", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("Failed.. Try later"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("barcode", scanResult);
                return hashMap;
            }
        };
        requestQueue.add(requestBarcode);
    }
}

