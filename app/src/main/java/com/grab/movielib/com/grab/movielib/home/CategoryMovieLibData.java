package com.grab.movielib.com.grab.movielib.home;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class CategoryMovieLibData extends MovieLibData {

    private static CategoryMovieLibData movieLibData = null;
    private Context mContext;
    public MovieLibAdapter mMovieLibAdapter;
    public String mCategory;

    private CategoryMovieLibData(){};

    public synchronized static CategoryMovieLibData getInstance(Context context, MovieLibAdapter movieLibAdapter) {
        if(movieLibData == null) {
            movieLibData = new CategoryMovieLibData();
        }

        movieLibData.mContext = context;
        movieLibData.mMovieLibAdapter = movieLibAdapter;
        return  movieLibData;
    }

    public synchronized static CategoryMovieLibData getInstance() {
        if(movieLibData != null)
            return movieLibData;
        else
            return null;
    }

    public ArrayList<MovieInfo> getMovieLibList() {
        if(movieInfoArrayList == null)
            movieInfoArrayList = new ArrayList<>();

        return movieInfoArrayList;
    }

    public boolean fetchNextPageInfo() {
        boolean status = true;
        currentPage++;
        if(currentPage <= pageCount || pageCount == 0) {
            StringRequest stringRequest = new StringRequest("http://api.themoviedb.org/3/movie/"+mCategory+"?api_key=4df263f48a4fe2621749627f5d001bf0"+"&page="+currentPage, this, this);
            Volley.newRequestQueue(mContext).add(stringRequest);
        } else
            status =false;
        return  status;
    }

    public boolean getFreshCategory(String category) {
        boolean result = true;
        movieInfoArrayList.clear();
        pageCount = 0;
        currentPage = 0;
        mCategory = category;
        fetchNextPageInfo();
        return  result;
    }

    @Override
    public void dataSetChanged() {
        Log.i(getClass().getSimpleName(), "dataSetChanged()");
        if(null != mMovieLibAdapter)
            mMovieLibAdapter.dataSetChanged();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public MovieInfo getMovieInfoAtPosition(int position) {
        if(movieInfoArrayList.size() >= position)
            return movieInfoArrayList.get(position);
        else
            return null;
    }
}
