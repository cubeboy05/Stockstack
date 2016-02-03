package com.example.angersleek.stockstack;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText category, name, brand, quantity, weight, barcode, supplier, supplierContact, expiryDate, comment;
    String tempCategory, tempName, tempBrand,tempQuantity, tempWeight, tempBarcode, tempSupplier, tempSupplierContact, tempExpiryDate,tempComment;
    final String KARSV_URL_INSERT = "http://www.karsv.com/app/insert_item.php";
    private StringRequest requestInsert;
    private RequestQueue requestQueue;
    String barcodeNum;
    String categoryName;
    static final int DIALOG_ID = 0;
    int year_x, month_x, day_x;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

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
        getSupportActionBar().setTitle("               Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent i = getIntent();
        barcodeNum = i.getStringExtra("barcode");
        categoryName = i.getStringExtra("categoryname");

        category = (EditText)findViewById(R.id.etCategoryAdd);
        name = (EditText)findViewById(R.id.etNameAdd);
        brand = (EditText)findViewById(R.id.etBrandAdd);
        quantity = (EditText)findViewById(R.id.etQuantityAdd);
        weight = (EditText)findViewById(R.id.etWeightAdd);
        barcode = (EditText)findViewById(R.id.etBarcodeAdd);
        supplier = (EditText)findViewById(R.id.etSupplierAdd);
        supplierContact = (EditText)findViewById(R.id.etSupplierContactAdd);
        expiryDate = (EditText)findViewById(R.id.etExpiryDateAdd);
        comment = (EditText)findViewById(R.id.etCommentAdd);

        barcode.setText(barcodeNum);

        if(categoryName != null && categoryName != "")
            category.setText(categoryName);

        cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnClick();

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.additem) {
            if (isLegalEntry()) {
                if (checkNotEmpty(category.getText().toString(), name.getText().toString(), quantity.getText().toString())) {
                    insertItem();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean checkNotEmpty(String category, String name, String quantity){

        if(category.equals(null) || category.equals("")){
            Toast.makeText(getApplicationContext(), "Category cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(name.equals(null) || name.equals("")){
            Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(quantity.equals(null) || quantity.equals("")){
            Toast.makeText(getApplicationContext(), "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    boolean isLegalEntry(){
        tempCategory = category.getText().toString();
        tempName = name.getText().toString();
        tempBrand = brand.getText().toString();
        tempQuantity = quantity.getText().toString();
        tempWeight = weight.getText().toString();
        tempBarcode = barcode.getText().toString();
        tempSupplier = supplier.getText().toString();
        tempSupplierContact = supplierContact.getText().toString();
        tempExpiryDate = expiryDate.getText().toString();
        tempComment = comment.getText().toString();
        String allEntry = tempCategory + tempName + tempBrand + tempQuantity +
                tempWeight + tempBarcode + tempSupplier + tempSupplierContact + tempComment;

        //tempExpiryDate not being checked on purpose

        return MainActivity.isAlphaNumeric(allEntry, AddNewItemActivity.this);
    }

    void insertItem(){
        requestInsert = new StringRequest(Request.Method.POST, KARSV_URL_INSERT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.get("result").toString().equals("Item Added")){
                        Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();

                        category.getText().clear();
                        name.getText().clear();
                        brand.getText().clear();
                        quantity.getText().clear();
                        weight.getText().clear();
                        barcode.getText().clear();
                        supplier.getText().clear();
                        supplierContact.getText().clear();
                        expiryDate.getText().clear();
                        comment.getText().clear();
                    }
                    else if(jsonObject.get("result").toString().equals("Item already exists!")){
                        Toast.makeText(getApplicationContext(), "Item already exists!", Toast.LENGTH_SHORT).show();
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
                hashMap.put("category", tempCategory);
                hashMap.put("name", tempName);
                hashMap.put("brand", tempBrand);
                hashMap.put("quantity", tempQuantity);
                hashMap.put("weight", tempWeight);
                hashMap.put("barcode", tempBarcode);
                hashMap.put("supplier", tempSupplier);
                hashMap.put("suppliercontact", tempSupplierContact);
                hashMap.put("expirydate", tempExpiryDate);
                hashMap.put("comment", tempComment);
                return hashMap;
            }
        };

        requestQueue.add(requestInsert);
    }

    //handling datetimepicker dialog popup
    public void showDialogOnClick(){
        expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID){
            return new DatePickerDialog(this, dPickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dPickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    expiryDate.setText(day_x + "/" + month_x + "/" + year_x);
                }
            };
}

