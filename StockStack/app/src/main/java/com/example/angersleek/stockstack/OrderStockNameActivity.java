package com.example.angersleek.stockstack;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderStockNameActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView orderNameListView;
    ArrayList<String> orderNames;
    String categoryName;
    Intent i;
    final String KARSV_URL = "http://www.karsv.com/app/view_product_name.php";
    private StringRequest request;
    private RequestQueue requestQueue;
    CustomOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stock_name);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_icon);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(x);
            }
        });
        getSupportActionBar().setTitle("              Make Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        orderNameListView = (ListView)findViewById(R.id.orderNameListView);
        orderNames = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        i = getIntent();
        categoryName = i.getStringExtra("category");
        getProductName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_item_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.orderSummary) {
            Intent i = new Intent(getApplicationContext(), OrderSummaryActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getProductName(){
        request = new StringRequest(Request.Method.POST, KARSV_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String viewName = jsonObject.getString("viewName");
                    JSONArray jsonArray = new JSONArray(viewName);

                    for(int x=0; x < jsonArray.length(); x++) {
                        JSONObject jsonPart = jsonArray.getJSONObject(x);
                        orderNames.add(jsonPart.getString("name"));
                    }
                    adapter = new CustomOrderListAdapter(orderNames,OrderStockNameActivity.this);
                    orderNameListView.setAdapter(adapter);

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
                hashMap.put("name", categoryName);
                return hashMap;
            }
        };

        requestQueue.add(request);

    }
}
