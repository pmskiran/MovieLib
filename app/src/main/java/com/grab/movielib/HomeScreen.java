package com.grab.movielib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.grab.movielib.com.grab.movielib.home.CategoryMovies;
import com.grab.movielib.com.grab.movielib.home.FavoriteMovieLibData;
import com.grab.movielib.com.grab.movielib.home.FavoriteMovies;
import com.grab.movielib.com.grab.movielib.home.CategoryMovieLibData;
import com.grab.movielib.com.grab.movielib.util.FileOperations;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FavoriteMovies.OnFragmentInteractionListener,
        CategoryMovies.OnFragmentInteractionListener,
        ViewPager.OnPageChangeListener{

    HomeTabsAdapter mHomeTabsAdapter = null;
    ViewPager mPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mHomeTabsAdapter = new HomeTabsAdapter(getSupportFragmentManager());
        mHomeTabsAdapter.setCategory(getResources().getString(R.string.now_playing));
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mHomeTabsAdapter);
        mPager.addOnPageChangeListener(this);
        checkNetworkStatus();
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
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        Log.i(getClass().getSimpleName(), "onOptionsItemSelected() :: "+item.getItemId());
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        Log.i(getClass().getSimpleName(), "onNavigationItemSelected() :: "+item.getItemId());

        if(!checkNetworkStatus()) {
//            Log.i(getClass().getSimpleName(),"Network not available.");
            return  true;
        }

        int id = item.getItemId();

        if (id == R.id.nav_popular) {
            mHomeTabsAdapter.setCategory(getResources().getString(R.string.popular));
            if(null != CategoryMovieLibData.getInstance())
                CategoryMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.popular));
            if(null != FavoriteMovieLibData.getInstance())
                FavoriteMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.popular));

        } else if (id == R.id.nav_top_reated || id == R.id.nav_latest) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.pager), "Yet to Implemented.", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (id == R.id.nav_now_playing) {
            mHomeTabsAdapter.setCategory(getResources().getString(R.string.now_playing));
            if(null != CategoryMovieLibData.getInstance())
                CategoryMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.now_playing));
            if(null != FavoriteMovieLibData.getInstance())
                FavoriteMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.now_playing));

        } else if (id == R.id.nav_upcoming) {
            mHomeTabsAdapter.setCategory(getResources().getString(R.string.upcoming));
            if(null != CategoryMovieLibData.getInstance())
                CategoryMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.upcoming));
            if(null != FavoriteMovieLibData.getInstance())
                FavoriteMovieLibData.getInstance().getFreshCategory(getResources().getString(R.string.upcoming));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
//        Log.i(getClass().getSimpleName(), "onFragmentInteraction() :: " + uri);
    }

    @Override
    public void onPageSelected(int position) {
//        Log.i(getClass().getSimpleName(), "onPageSelected() :: "+position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getSimpleName(), "onActivityResult() requestCode -- "+requestCode);
        if(requestCode == 100) {
            CategoryMovies categoryMovies = (CategoryMovies)((HomeTabsAdapter) mPager.getAdapter()).getItem(0);
            categoryMovies.onActivityResult(requestCode, resultCode, data, getApplicationContext());
        } else if(requestCode == 101) {
            FavoriteMovies favoriteMovies = (FavoriteMovies)((HomeTabsAdapter) mPager.getAdapter()).getItem(1);
            favoriteMovies.onActivityResult(requestCode, resultCode, data, getApplicationContext());
        }

    }

    private boolean checkNetworkStatus() {
        if(!FileOperations.isOnline(getApplicationContext())) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.pager), "Network not available.", Snackbar.LENGTH_LONG);
            snackbar.show();
            return  false;
        }

        return true;
    }
}
