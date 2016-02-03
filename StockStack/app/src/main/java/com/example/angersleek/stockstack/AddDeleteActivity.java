package com.example.angersleek.stockstack;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

public class AddDeleteActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delete);

        toolbar = (Toolbar)findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_icon);
        toolbar.setNavigationIcon(drawable);
        getSupportActionBar().setTitle("                Testing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_icon_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendParse(View view){
        try {
            // Associate the device with a user
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("user", ParseUser.getCurrentUser());
            installation.saveInBackground();

            ParsePush.subscribeInBackground("employee");

            // Create our Installation query
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("channels", "employee");
            //pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setChannel("employee");
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage("Test Message");
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
