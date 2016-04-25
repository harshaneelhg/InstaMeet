package computing.mobile.instameet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GPSTracker gps;
    UserGlobalData ugd = UserGlobalData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     /*   ugd.username = "Harsh";
        ugd.password = "abcd";
        ugd.email = "harsh@asu.edu";
        ugd.phone = "9898989898";*/
        ugd.location = "0.1,0.1";
        //ugd.displayName = "MacBook_Pro";
        ugd.discover = true;
        ugd.interests = new int[10];

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView)findViewById(R.id.mainNameView)).setText("Hello " + ugd.username + "!");
        try {
            new APIClient().getHistory(ugd.username, ugd.password, getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }
        setInterests(ugd);
        RequestCheckerFlag.getInstance().setIsAppFinished(false);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        new APIClient().checkPendingRequests(ugd.username,ugd.password,getApplicationContext());

        /*gps = new GPSTracker(MainActivity.this, this);
        Log.d("GPS", "GPS started...");
        Location l  = gps.getLocation();*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker gps = new GPSTracker(MainActivity.this, this);
                    Log.d("GPS", "GPS started...");
                    Location l = gps.getLocation();
                    if (l != null) {
                        ugd.location = Double.toString(l.getLatitude()) + " " + Double.toString(l.getLongitude());
                    }
                } else {
                    ugd.location = null;
                }
                return;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        UserGlobalData ugd = UserGlobalData.getInstance();
        setInterests(ugd);
        try {
            new APIClient().getHistory(ugd.username, ugd.password, getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }
        new APIClient().checkPendingRequests(ugd.username,ugd.password,getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        UserGlobalData ugd = UserGlobalData.getInstance();
        TextView nameView = (TextView)findViewById(R.id.textNameDrawer);
        System.out.println(ugd.username);
        nameView.setText(ugd.username);
        TextView emailView = (TextView)findViewById(R.id.textEmailDrawer);
        emailView.setText(ugd.email);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestCheckerFlag reqFlag = RequestCheckerFlag.getInstance();
        reqFlag.setIsAppFinished(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_history) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        } else if (id == R.id.nav_search_users) {
            UserGlobalData ugd = UserGlobalData.getInstance();
            new APIClient().findUsers(ugd.username,ugd.password,MainActivity.this);
        } else if (id == R.id.nav_pending_requests) {
            MainActivity.this.startActivity(new Intent(MainActivity.this, RequestsActivity.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id == R.id.nav_events){
          MainActivity.this.startActivity(new Intent(MainActivity.this,EventsActivity.class));
        }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void toggleInterest(String username, String password, String interestID, String interestValue){
        APIClient apiHandle = new APIClient();
        apiHandle.updateInterest(username,password,interestID,interestValue);
    }
    public void onInterestToggle1(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest1);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "0", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "0", "0");
        }
    }
    public void onInterestToggle2(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest2);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "1", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "1", "0");
        }
    }
    public void onInterestToggle3(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest3);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "2", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "2", "0");
        }
    }
    public void onInterestToggle4(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest4);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "3", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "3", "0");
        }
    }
    public void onInterestToggle5(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest5);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "4", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "4", "0");
        }
    }
    public void onInterestToggle6(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest6);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "5", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "5", "0");
        }
    }
    public void onInterestToggle7(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest7);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "6", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "6", "0");
        }
    }
    public void onInterestToggle8(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest8);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "7", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "7", "0");
        }
    }
    public void onInterestToggle9(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest9);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "8", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "8", "0");
        }
    }
    public void onInterestToggle10(View v){
        CheckBox cb1 = (CheckBox) findViewById(R.id.cbInterest10);
        UserGlobalData ugd = UserGlobalData.getInstance();
        if(cb1.isChecked()){
            toggleInterest(ugd.username, ugd.password, "9", "1");
        }
        else{
            toggleInterest(ugd.username, ugd.password, "9", "0");
        }
    }

    private void setInterests(UserGlobalData ugd){
        int[] interests = ugd.interests;
        if(interests[0]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest1)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest1)).setChecked(false);

        if(interests[1]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest2)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest2)).setChecked(false);

        if(interests[2]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest3)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest3)).setChecked(false);

        if(interests[3]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest4)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest4)).setChecked(false);

        if(interests[4]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest5)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest5)).setChecked(false);

        if(interests[5]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest6)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest6)).setChecked(false);

        if(interests[6]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest7)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest7)).setChecked(false);

        if(interests[7]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest8)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest8)).setChecked(false);

        if(interests[8]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest9)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest9)).setChecked(false);

        if(interests[9]==1)
            ((CheckBox) this.findViewById(R.id.cbInterest10)).setChecked(true);
        else
            ((CheckBox) this.findViewById(R.id.cbInterest10)).setChecked(false);
    }
}
