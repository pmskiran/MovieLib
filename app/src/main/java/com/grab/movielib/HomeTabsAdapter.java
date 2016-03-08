package com.grab.movielib;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.grab.movielib.com.grab.movielib.home.CategoryMovies;
import com.grab.movielib.com.grab.movielib.home.FavoriteMovies;

public class HomeTabsAdapter extends FragmentPagerAdapter{

    String mCategory;

    public  HomeTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public synchronized void setCategory(String category) {
        mCategory = category;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                CategoryMovies categoryMovies = CategoryMovies.newInstance(mCategory);
                return categoryMovies;
            case 1:
                FavoriteMovies favoriteMovies = FavoriteMovies.newInstance(mCategory);
                return favoriteMovies;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Now Showing";
            case 1:
                return "Favourites";
        }
        return "";
    }
}
