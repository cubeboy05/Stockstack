package com.example.angersleek.stockstack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderSummaryActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button confirmOrderBtn;
    ListView orderSummaryListView;
    CustomOrderSummaryAdapter customAdapter;
    ArrayList<String> nameLists, numLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

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
        getSupportActionBar().setTitle("          Order Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        confirmOrderBtn = (Button)findViewById(R.id.confirmOrderBtn);
        orderSummaryListView = (ListView)findViewById(R.id.orderSummaryListView);
        nameLists = new ArrayList<>();
        numLists = new ArrayList<>();
        databaseSetup();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        orderSummaryListView.setAdapter(null);
        restartDB();
    }

    public void confirmOrder(View view){
        sendParse();
    }

    public void editOrder(View view){
        Intent i = new Intent(getApplicationContext(), OrderSummaryEditActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            Intent i = new Intent(getApplicationContext(), OrderStockActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void databaseSetup(){

        try {
            SQLiteDatabase orderDb = OrderSummaryActivity.this.openOrCreateDatabase("myorder", MODE_PRIVATE, null);
            orderDb.execSQL("CREATE TABLE IF NOT EXISTS orders(name VARCHAR(50), quantity INT(4))");

            if(CustomOrderListAdapter.orders != null) {
                for (Map.Entry<String, String> x : CustomOrderListAdapter.orders.entrySet()) {
                    orderDb.execSQL("INSERT INTO orders (name, quantity) VALUES" +
                            "('" + x.getKey() + "', " + Integer.parseInt(x.getValue()) + ")");
                }
            }
            Cursor c = orderDb.rawQuery("SELECT * FROM orders", null);
            int nameIndex = c.getColumnIndex("name");
            int quantityIndex = c.getColumnIndex("quantity");

            while (c.moveToNext()){
                nameLists.add(c.getString(nameIndex));
                numLists.add(Integer.toString(c.getInt(quantityIndex)));
            }
            customAdapter = new CustomOrderSummaryAdapter(nameLists, OrderSummaryActivity.this, numLists);
            orderSummaryListView.setAdapter(customAdapter);

            c.close();
            orderDb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void restartDB(){
        SQLiteDatabase orderDb = OrderSummaryActivity.this.openOrCreateDatabase("myorder", MODE_PRIVATE, null);
        orderDb.execSQL("CREATE TABLE IF NOT EXISTS orders(name VARCHAR(50), quantity INT(4))");

        Cursor c = orderDb.rawQuery("SELECT * FROM orders", null);
        int nameIndex = c.getColumnIndex("name");
        int quantityIndex = c.getColumnIndex("quantity");

        while (c.moveToNext()){
            //orderLists.add(c.getString(nameIndex) + " " + c.getInt(quantityIndex));
            nameLists.add(c.getString(nameIndex));
            numLists.add(Integer.toString(c.getInt(quantityIndex)));
        }
        c.close();
        orderDb.close();
        customAdapter = new CustomOrderSummaryAdapter(nameLists, OrderSummaryActivity.this, numLists);
        //orderSummaryListView.setAdapter(adapter);
        orderSummaryListView.setAdapter(customAdapter);
    }

    public void sendParse(){
        try {
            // Associate the device with a user
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("user", ParseUser.getCurrentUser());
            installation.saveInBackground();

            ParsePush.subscribeInBackground("employee");

            // Create our Installation query
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("channels", "employee");
            pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setChannel("employee");
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage("New orders Made..");
            push.sendInBackground(new SendCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i("zx", "Push is sent!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
