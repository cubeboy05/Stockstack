package com.example.angersleek.stockstack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterNewUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText username, password, passwordConfirm;
    Button registerBtn;

    private static final String KARSV_URL = "http://www.karsv.com/app/register.php";
    private StringRequest request1;
    private RequestQueue requestQueue1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("               Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (EditText)findViewById(R.id.etRegisterUsername);
        password = (EditText)findViewById(R.id.etRegisterPassword);
        passwordConfirm = (EditText)findViewById(R.id.etRegisterPasswordConfirm);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        requestQueue1 = Volley.newRequestQueue(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password.getText().toString().equals(passwordConfirm.getText().toString()) ) {
                    if(MainActivity.isAlphaNumeric(username.getText().toString(),
                            RegisterNewUserActivity.this) && MainActivity.isAlphaNumeric(password.getText().toString(), RegisterNewUserActivity.this)){

                        request1 = new StringRequest(Request.Method.POST, KARSV_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if(jsonObject.get("result").toString().equals("Registration successful")){
                                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                                        username.getText().clear();
                                        password.getText().clear();
                                        passwordConfirm.getText().clear();
                                    }
                                    else if(jsonObject.get("result").toString().equals("Username already exists!")){
                                        Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("Registration failed. Try later"), Toast.LENGTH_SHORT).show();
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
                                hashMap.put("username", username.getText().toString());
                                hashMap.put("password", password.getText().toString());
                                return hashMap;
                            }
                        };

                        requestQueue1.add(request1);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
