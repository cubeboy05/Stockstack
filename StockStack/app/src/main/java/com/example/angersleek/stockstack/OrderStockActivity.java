package com.example.angersleek.stockstack;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OrderStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView orderCategoryListView;
    ArrayList<String> categories;
    ArrayAdapter<String> adapter;
    String jsonString;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stock);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("              Make Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderCategoryListView = (ListView)findViewById(R.id.orderCategoryListView);

        BackgroundTask task = new BackgroundTask();
        task.execute();

        orderCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), OrderStockNameActivity.class);
                i.putExtra("category", categories.get(position));
                startActivity(i);
            }
        });

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

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        String jsonUrl;

        @Override
        protected void onPreExecute() {
            jsonUrl = "http://karsv.com/app/main_view_product.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();

                while ((jsonString = bufferedReader.readLine()) != null){
                    stringBuilder.append(jsonString+"\n");
                }

                bufferedReader.close();
                in.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            categories = new ArrayList<>();

            try {
                jsonObject = new JSONObject(s);
                String category = jsonObject.getString("viewCategory");
                JSONArray jsonArray = new JSONArray(category);

                for(int x=0; x < jsonArray.length(); x++){
                    JSONObject jsonPart = jsonArray.getJSONObject(x);
                    categories.add(jsonPart.getString("category"));
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, categories);
                orderCategoryListView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
