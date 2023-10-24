package com.ms.memesterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityManager;

import android.app.Dialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private long pressedTime;

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user==null){
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }

        DatabaseReference databaseReference = Database_Singleton.getInstance().getImageDatabase();
        databaseReference.child("user_email").setValue(user.getEmail());
//        Log.d(">>>>>>>>>>>>>>>>Email_ID : ",""+user.getEmail());



        viewPager = findViewById(R.id.viewPager2);

        viewPager.setCurrentItem(0);
        setupViewPager(viewPager);
//        viewPager.setCurrentItem(0);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        NavigationView navigationView  = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        TextView user_email = (TextView) header.findViewById(R.id.user_email);
        user_email.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.clear_data:
//                        getInstance().getRequestQueue().getCache().remove(key);

                        Glide.get(MainActivity.this).clearMemory();
                        Log.d(TAG, ">>>>>>>>>>>>>Glide memory cleared");
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                Log.d("Current Thread", ">>>>>>>>>>>>>Running");
                                // Thread.sleep(1000);
                                //CLear Disk cache method should be called in the background thread...
                                Glide.get(MainActivity.this).clearDiskCache();
                                Log.d(TAG, ">>>>>>>>>>>>The ClearDisk Cache Initiated");

                            }
                        };
                        thread.start();

                        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                            ((ActivityManager)getApplicationContext().getSystemService(ACTIVITY_SERVICE))
                                    .clearApplicationUserData(); // note: it has a return value!
                        }
                        else {
                            String packageName = getApplicationContext().getPackageName();
                            Runtime runtime = Runtime.getRuntime();
                            try {
                                runtime.exec("pm clear "+packageName);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(MainActivity.this, "ClearData", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bookmark:
                        Intent i = new Intent(MainActivity.this,Bookmarks.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        break;

                    case R.id.log_out:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "SignOut", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.downloads:
                        Toast.makeText(MainActivity.this, "Downloads", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,  new Fragment()).commit();
                        break;
                    case R.id.about:

                        Dialog dialogAbout = new Dialog(MainActivity.this);
                        dialogAbout.setContentView(R.layout.dialog_about);
                        dialogAbout.setCancelable(true);
                        dialogAbout.show();

                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                        break;

//                    case R.id.mode:
//
//                        if((getApplicationContext().getResources().getConfiguration().uiMode) == Configuration.UI_MODE_NIGHT_YES){
//                            setTheme(R.style.Theme_Memesterr);
////                            recreate();
//                            TaskStackBuilder.create(MainActivity.this)
//                                    .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
//                                    .addNextIntent(MainActivity.this.getIntent())
//                                    .startActivities();
//                        }
//                        else {
//                            setTheme(R.style.Theme_Memesterr);
//                            TaskStackBuilder.create(MainActivity.this)
//                                    .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
//                                    .addNextIntent(MainActivity.this.getIntent())
//                                    .startActivities();
////                            recreate();
//                        }
//
//                        Toast.makeText(MainActivity.this, "Dark/Light Mode", Toast.LENGTH_SHORT).show();
//                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
//        else {
////            super.onBackPressed();
//        }

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Log.d(TAG,"the onBackPressed is called");
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.chat){
//            Toast.makeText(MainActivity.this,"Chat",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this,Chat.class);
            startActivity(i);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.reddit_item:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.twitter_item:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        }
    };

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Reddit());
        adapter.addFragment(new Twitter());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        Glide.get(MainActivity.this).clearMemory();
        Log.d(TAG, ">>>>>>>>>>>>>Glide memory cleared");
        Thread thread = new Thread(() -> {
            Log.d("Current Thread", ">>>>>>>>>>>>>Running");
            // Thread.sleep(1000);
            //CLear Disk cache method should be called in the background thread...
            Glide.get(MainActivity.this).clearDiskCache();
            Log.d(TAG, ">>>>>>>>>>>>The ClearDisk Cache Initiated");

        });
        thread.start();

        try {
            Runtime.getRuntime().exec("pm clear" + getApplicationContext().getPackageName());
            Log.d("OnDestroy","Data Deleted");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}