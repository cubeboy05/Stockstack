package com.example.angersleek.stockstack;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class ViewProductDetailActivity extends AppCompatActivity {

    String name;
    Toolbar toolbar;
    ListView productDetailListView;
    ArrayList<String> details;
    ArrayList<String> sendOverDetails;
    ArrayAdapter<String> adapter;
    String KARSV_URL;
    Intent intentSend;
    Intent i;

    private StringRequest request;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_detail);

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
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("        Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        productDetailListView = (ListView)findViewById(R.id.productDetailListView);
        requestQueue = Volley.newRequestQueue(this);
        KARSV_URL = "http://karsv.com/app/view_product_detail.php";
        details = new ArrayList<>();
        sendOverDetails = new ArrayList<>();

        i = getIntent();
        name = i.getStringExtra("name");

        getDetail();

        productDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentSend = new Intent(ViewProductDetailActivity.this, EditProductDetailActivity.class);
                intentSend.putExtra("details", sendOverDetails);
                startActivity(intentSend);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.clear();
        productDetailListView.setAdapter(adapter);
        getDetail();
    }

    void getDetail(){
        request = new StringRequest(Request.Method.POST, KARSV_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String viewDetail = jsonObject.getString("viewDetail");
                    JSONArray jsonArray = new JSONArray(viewDetail);

                    for(int x=0; x < jsonArray.length(); x++) {
                        JSONObject jsonPart = jsonArray.getJSONObject(x);
                        details.add("Category: " + jsonPart.getString("category"));
                        details.add("Name: " + jsonPart.getString("name"));
                        details.add("Brand: " + jsonPart.getString("brand"));
                        details.add("Quantity: " + jsonPart.getString("quantity"));
                        details.add("Weight: " + jsonPart.getString("weight"));
                        details.add("Barcode: " + jsonPart.getString("barcode"));
                        details.add("Supplier: " + jsonPart.getString("supplier"));
                        details.add("Supplier Contact: " + jsonPart.getString("suppliercontact"));
                        details.add("Expiry Date: " + jsonPart.getString("expirydate"));

                        sendOverDetails.add(jsonPart.getString("category"));
                        sendOverDetails.add(jsonPart.getString("name"));
                        sendOverDetails.add(jsonPart.getString("brand"));
                        sendOverDetails.add(jsonPart.getString("quantity"));
                        sendOverDetails.add(jsonPart.getString("weight"));
                        sendOverDetails.add(jsonPart.getString("barcode"));
                        sendOverDetails.add(jsonPart.getString("supplier"));
                        sendOverDetails.add(jsonPart.getString("suppliercontact"));
                        sendOverDetails.add(jsonPart.getString("expirydate"));

                        if(jsonPart.getString("comment") != "null") {
                            details.add("Comment: " + jsonPart.getString("comment"));
                            sendOverDetails.add(jsonPart.getString("comment"));
                        }
                        else {
                            details.add("Comment: ");
                            sendOverDetails.add(" ");
                        }
                    }
                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, details);
                    productDetailListView.setAdapter(adapter);

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
                hashMap.put("name", name);
                return hashMap;
            }
        };

        requestQueue.add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edititem) {

            intentSend = new Intent(ViewProductDetailActivity.this, EditProductDetailActivity.class);
            intentSend.putExtra("details", sendOverDetails);
            startActivity(intentSend);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
