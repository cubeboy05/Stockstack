package com.example.angersleek.stockstack;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProductDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText category, name, brand, quantity, weight, barcode, supplier, supplierContact, expiryDate, comment;
    String tempCategory, tempName, tempBrand,tempQuantity, tempWeight, tempBarcode, tempSupplier, tempSupplierContact, tempExpiryDate,tempComment;
    final String KARSV_URL_EDIT = "http://www.karsv.com/app/edit_all_item.php";
    ArrayList<String> detailArray;
    ArrayList<EditText> emptyTextFields;
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;
    Calendar cal;

    private StringRequest requestEdit;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_detail);

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
        getSupportActionBar().setTitle("          Edit Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        category = (EditText)findViewById(R.id.etCategoryEdit);
        name = (EditText)findViewById(R.id.etNameEdit);
        brand = (EditText)findViewById(R.id.etBrandEdit);
        quantity = (EditText)findViewById(R.id.etQuantityEdit);
        weight = (EditText)findViewById(R.id.etWeightEdit);
        barcode = (EditText)findViewById(R.id.etBarcodeEdit);
        supplier = (EditText)findViewById(R.id.etSupplierEdit);
        supplierContact = (EditText)findViewById(R.id.etSupplierContactEdit);
        expiryDate = (EditText)findViewById(R.id.etExpiryDateEdit);
        comment = (EditText)findViewById(R.id.etCommentEdit);

        Intent i = getIntent();
        detailArray = i.getStringArrayListExtra("details");
        emptyTextFields = new ArrayList<>(Arrays.asList(
                category, name, brand, quantity, weight, barcode, supplier, supplierContact, expiryDate, comment));

        for(int z = 0; z < emptyTextFields.size(); z++) {
            emptyTextFields.get(z).setText(detailArray.get(z));
        }

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
                    editItem();
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
        //no checking tempExpiryDate on purpose

        return MainActivity.isAlphaNumeric(allEntry, EditProductDetailActivity.this);
    }

    void editItem(){
        requestEdit = new StringRequest(Request.Method.POST, KARSV_URL_EDIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.get("result").toString().equals("Edit successful")){
                        Toast.makeText(getApplicationContext(), "Edit successful", Toast.LENGTH_SHORT).show();
                    }
                    else if(jsonObject.get("result").toString().equals("Item doesn't exists!")){
                        Toast.makeText(getApplicationContext(), "Item doesn't exists!", Toast.LENGTH_SHORT).show();
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

        requestQueue.add(requestEdit);
    }

    //handling datepicker
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
