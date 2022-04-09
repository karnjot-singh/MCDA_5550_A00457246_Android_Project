package com.example.hotelreservationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.main_layout);

        navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HotelSearchFragment(), "HOTEL_SEARCH");
            fragmentTransaction.commit();

            navigationView.setCheckedItem(R.id.search_hotels_menu_item);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            ReservationConfirmationFragment confirmationFragment = (ReservationConfirmationFragment) getSupportFragmentManager().findFragmentByTag("CONFIRMATION_FRAGMENT");

            if(confirmationFragment != null && confirmationFragment.isVisible()) {
                drawer.openDrawer(GravityCompat.START);
                return;
            }

            HotelResultsFragment fragment = (HotelResultsFragment) getSupportFragmentManager().findFragmentByTag("HOTEL_RESULTS");

            if(fragment != null && fragment.isVisible()) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new HotelSearchFragment(), "HOTEL_SEARCH");
                fragmentTransaction.commit();
                navigationView.setCheckedItem(R.id.search_hotels_menu_item);
                return;
            }

            AboutFragment fragmentAbout = (AboutFragment) getSupportFragmentManager().findFragmentByTag("ABOUT_FRAGMENT");

            if(fragmentAbout != null && fragmentAbout.isVisible()) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new HotelSearchFragment(), "HOTEL_SEARCH");
                fragmentTransaction.commit();
                navigationView.setCheckedItem(R.id.search_hotels_menu_item);
                return;
            }

            HotelSearchFragment fragmentSearch = (HotelSearchFragment) getSupportFragmentManager().findFragmentByTag("HOTEL_SEARCH");

            if(fragmentSearch != null && fragmentSearch.isVisible()) {
                this.finishAffinity();
                return;
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.search_hotels_menu_item:
                fragmentTransaction.replace(R.id.frame_layout, new HotelSearchFragment(), "HOTEL_SEARCH");
                fragmentTransaction.commit();
                break;

            case R.id.about_menu_item:
                fragmentTransaction.replace(R.id.frame_layout, new AboutFragment(), "ABOUT_FRAGMENT");
                fragmentTransaction.commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}