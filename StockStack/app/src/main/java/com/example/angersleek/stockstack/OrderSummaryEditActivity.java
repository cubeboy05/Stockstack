package com.example.angersleek.stockstack;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderSummaryEditActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button updtOrderBtn;
    ListView summaryEditListView;
    ArrayList<String> orderLists, numLists;
    ArrayAdapter<String> adapter;
    CustomOrderEditListAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary_edit);

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
        getSupportActionBar().setTitle("             Edit Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        updtOrderBtn = (Button)findViewById(R.id.updtOrderBtn);
        summaryEditListView = (ListView)findViewById(R.id.summaryEditListView);
        orderLists = new ArrayList<>();
        numLists = new ArrayList<>();
        databaseSetup();
    }

    public void updateOrder(View view){
        updtOrder();
    }

    void updtOrder(){
        HashMap<String, String> editedOrders =  new HashMap<>();
        TextView tv;
        EditText et;

        for(int i=0; i < summaryEditListView.getCount(); i++){
            tv = (TextView)summaryEditListView.getChildAt(i).findViewById(R.id.list_edit_item_string);
            et = (EditText)summaryEditListView.getChildAt(i).findViewById(R.id.etEditOrderSummary);
            if(et != null){
                editedOrders.put(String.valueOf(tv.getText()), String.valueOf(et.getText()));
            }

            try{
                SQLiteDatabase orderDb = OrderSummaryEditActivity.this.openOrCreateDatabase("myorder", MODE_PRIVATE, null);
                orderDb.execSQL("CREATE TABLE IF NOT EXISTS orders(name VARCHAR(50), quantity INT(4))");

                orderDb.execSQL("DELETE FROM orders");

                if(editedOrders != null) {
                    for (Map.Entry<String, String> x : editedOrders.entrySet()) {
                        orderDb.execSQL("INSERT INTO orders (name, quantity) VALUES" +
                                "('" + x.getKey() + "', " + Integer.parseInt(x.getValue()) + ")");
                    }
                }
                orderDb.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        Intent i = new Intent(getApplicationContext(), OrderSummaryActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.additem) {
            updtOrder();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void databaseSetup(){

        try {
            SQLiteDatabase orderDb = OrderSummaryEditActivity.this.openOrCreateDatabase("myorder", MODE_PRIVATE, null);
            orderDb.execSQL("CREATE TABLE IF NOT EXISTS orders(name VARCHAR(50), quantity INT(4))");

            //orderDb.execSQL("DELETE FROM orders");

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
                //orderLists.add(c.getString(nameIndex) + " " + c.getInt(quantityIndex));
                orderLists.add(c.getString(nameIndex));
                numLists.add(Integer.toString(c.getInt(quantityIndex)));
            }
            //adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, orderLists);
            //summaryEditListView.setAdapter(adapter);
            customAdapter = new CustomOrderEditListAdapter(orderLists, OrderSummaryEditActivity.this, numLists);
            summaryEditListView.setAdapter(customAdapter);
            c.close();
            orderDb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
