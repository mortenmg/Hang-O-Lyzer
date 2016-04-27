package com.morten.hang_o_lyzer;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.morten.shared.Constants;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGTAG = "SynchronizedNotificationsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start synchronized notification
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final SynchronizedNotificationsFragment fragment = new SynchronizedNotificationsFragment();
        transaction.add(fragment, FRAGTAG);
        transaction.commit();

        //Add gui element resctions
        Button btnBoth = (Button) findViewById(R.id.btn_both);
        btnBoth.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                fragment.onOptionsItemSelected(Constants.BOTH_ID);
            }
            });
        Button btnWatch = (Button) findViewById(R.id.btn_wear_only);
        btnWatch.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                fragment.onOptionsItemSelected(Constants.WATCH_ONLY_ID);
            }
        });
        Button btnPhone = (Button) findViewById(R.id.btn_phone_only);
        btnPhone.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                fragment.onOptionsItemSelected(Constants.PHONE_ONLY_ID);
            }
        });


        //Initialize gui objects
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        Button button = (Button) findViewById(R.id.btnNotification);
//        button.setOnClickListener(new View.OnClickListener(){
//            public void onClick (View view){
//                sendWearNotification();
//            }
//            });
    }

    private void sendWearNotification() {
        int notificationId = 001;
// Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra("Extra event id", "eventId");
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_cast_dark)
                        .setContentTitle("Hang_O_Lyzer title")
                        .setContentText("content text here")
                        .setContentIntent(viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
