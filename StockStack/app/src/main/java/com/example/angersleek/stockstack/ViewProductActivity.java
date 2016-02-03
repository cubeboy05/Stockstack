package com.example.angersleek.stockstack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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

public class ViewProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    String categoryName;
    Intent i;

    ListView productListView;
    private StringRequest request, request2;
    private RequestQueue requestQueue, requestQueue2;
    String KARSV_URL;
    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    private static final String KARSV_DELETE_NAME_URL = "http://www.karsv.com/app/deletename.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

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
        getSupportActionBar().setTitle("              View Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        productListView = (ListView)findViewById(R.id.productListView);
        requestQueue = Volley.newRequestQueue(this);
        KARSV_URL = "http://www.karsv.com/app/view_product_name.php";
        names = new ArrayList<>();
        requestQueue2 = Volley.newRequestQueue(this);

        i = getIntent();
        categoryName = i.getStringExtra("category");

        getProductName();

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ViewProductDetailActivity.class);
                i.putExtra("name", names.get(position));
                startActivity(i);
            }
        });

        deleteItem();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.clear();
        productListView.setAdapter(adapter);
        getProductName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {

            Intent i = new Intent(ViewProductActivity.this, AddNewItemActivity.class);
            i.putExtra("categoryname", categoryName);
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
                        //Log.i("zx", jsonPart.getString("name"));
                        names.add(jsonPart.getString("name"));
                    }
                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, names);
                    productListView.setAdapter(adapter);

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

    void deleteItem(){
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(ViewProductActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Delete..")
                        .setMessage("are you sure to delete item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String temp = names.get(position);
                                names.remove(position);
                                adapter.notifyDataSetChanged();
                                requestListen(temp);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                //if false here, you will end up moving on to details anyway
                return true;
            }
        });
    }

    //sending info over to server to delete. calling this inside onitemlongclick
    private void requestListen(final String delName){
        request2 = new StringRequest(Request.Method.POST, KARSV_DELETE_NAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.i("zxx", jsonObject.get("result").toString());
                    if(jsonObject.get("result").toString().equals("Item removed")){
                        Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                    }
                    else if(jsonObject.get("result").toString().equals("Item not in database")){
                        Toast.makeText(getApplicationContext(), "Item not in database", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("Action failed. Try later"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("delName", delName);
                return hashMap;
            }
        };

        requestQueue2.add(request2);
    }



}
