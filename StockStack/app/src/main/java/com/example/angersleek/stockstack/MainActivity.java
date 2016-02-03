package com.example.angersleek.stockstack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText username, password;
    Button loginBtn;

    private RequestQueue requestQueue;
    private static final String KARSV_URL = "http://www.karsv.com/app/login.php";
    private StringRequest request;
    RelativeLayout relativeLayout;
    ImageView logoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.etUsername);
        password = (EditText)findViewById(R.id.etPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setBackgroundResource(R.drawable.loginbutton);
        relativeLayout = (RelativeLayout)findViewById(R.id.loginRelativeLayout);
        logoView = (ImageView)findViewById(R.id.logoView);
        relativeLayout.setOnClickListener(this);
        logoView.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAlphaNumeric(username.getText().toString(), MainActivity.this) && isAlphaNumeric(password.getText().toString(), MainActivity.this)) {
                    request = new StringRequest(Request.Method.POST, KARSV_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("failed"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("username", username.getText().toString());
                            hashMap.put("password", password.getText().toString());
                            return hashMap;
                        }
                    };

                    requestQueue.add(request);
                }
            }
        });
    }

    /*public static boolean isAlphaNumeric(String input, Activity act){
        if(input.matches("[A-Za-z0-9]+")){
            return true;
        }
        else {
            Toast.makeText(act.getApplicationContext(), "Enter only Alphabets and Numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
    }*/

    public static boolean isAlphaNumeric(String input, Activity act){
        String pattern= "^[a-zA-Z0-9 ]*$";
        if(!(input !=null && input.matches(pattern))){
            Toast.makeText(act.getApplicationContext(), "Enter only Alphabets and Numbers", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //If I want to use enter key to log in. Currently not using. need to setup onkeylistener(this) at oncreate on the user/pw inputs for this to work
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            //
        }

        return false;
    }

    //close the keyboard when user clicks anywhere on screen
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginRelativeLayout || v.getId() == R.id.logoView){
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
