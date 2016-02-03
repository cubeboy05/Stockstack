package com.example.angersleek.stockstack;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ViewStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    String jsonString;
    JSONObject jsonObject;
    ArrayList<String> categories;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("              View Stock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listViewCategory);

        BackgroundTask task = new BackgroundTask();
        task.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), categories.get(position), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), ViewProductActivity.class);
                i.putExtra("category", categories.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        BackgroundTask task = new BackgroundTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {

            Intent i = new Intent(getApplicationContext(), AddNewItemActivity.class);
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
            //TextView viewStockCategory = (TextView)findViewById(R.id.viewStockCategory);
            //viewStockCategory.setText(s);
            super.onPostExecute(s);

            categories = new ArrayList<>();

            try {
                jsonObject = new JSONObject(s);
                String category = jsonObject.getString("viewCategory");
                JSONArray jsonArray = new JSONArray(category);

                for(int x=0; x < jsonArray.length(); x++){
                    JSONObject jsonPart = jsonArray.getJSONObject(x);
                    //Log.i("zxmain", jsonPart.getString("category"));
                    categories.add(jsonPart.getString("category"));

                    //can log another if there was one. in this case only got category. Useful for subs.
                    //Log.i("zxmain", jsonPart.getString("category"));
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, categories);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

