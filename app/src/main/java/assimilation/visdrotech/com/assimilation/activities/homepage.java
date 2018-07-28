package assimilation.visdrotech.com.assimilation.activities;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.utils.baseApplicationClass;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String prefName =  ((baseApplicationClass) this.getApplication()).PREF_NAME ;
    private View headerView ;
    private TextView userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        Boolean studentStatus = prefs.getBoolean("isStudent", true);
        if (studentStatus) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_createEvent).setVisible(false);

        }
        Boolean superAdminStatus = prefs.getBoolean("isSuperAdmin", false);
        if (!(superAdminStatus)) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_complaint).setVisible(false);
        }
        initialiseVariable();


        displayFragment(R.id.nav_upcomingevent);



    }




    private void initialiseVariable(){
        SharedPreferences prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        userName = (TextView) headerView.findViewById(R.id.username);
        userName.setText(prefs.getString("name","User"));
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
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();
            editor.remove("isLoggedIn");
            editor.apply();
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        displayFragment(item.getItemId());
        return true;
    }

    public void displayFragment(int itemId){
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_createEvent :
                fragment = new homepageFragmentCreateEvent();
                break;
            case R.id.nav_upcomingevent :
                fragment = new homepageFragmnetUpcomingEvent();
                break;
            case R.id.nav_changepassword :
                fragment = new homepageFragmentChangePassword();
                break;
            case R.id.nav_complaint :
                fragment = new homepageFragmnetComplaints();
                break;

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
