package com.grab.movielib.com.grab.movielib.home;

import android.content.Context;
import android.util.Log;

import com.grab.movielib.com.grab.movielib.util.FileOperations;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteMovieLibData extends MovieLibData {

    private static FavoriteMovieLibData movieLibData = null;
    private Context mContext;
    public MovieLibAdapter mMovieLibAdapter;
    public String mCategory;
    HashMap<String, MovieInfo> movieList = null;


    private FavoriteMovieLibData(){};

    public synchronized static FavoriteMovieLibData getInstance(Context context, MovieLibAdapter movieLibAdapter) {
        if(movieLibData == null) {
            movieLibData = new FavoriteMovieLibData();
        }

        movieLibData.mContext = context;
        movieLibData.mMovieLibAdapter = movieLibAdapter;
        return  movieLibData;
    }

    public synchronized static FavoriteMovieLibData getInstance() {
        if(movieLibData != null)
            return movieLibData;
        else
            return null;
    }

    public ArrayList<MovieInfo> getMovieLibList() {
        if(movieInfoArrayList == null && movieList != null)
            movieInfoArrayList = new ArrayList<MovieInfo>(movieList.values());
        else if(movieInfoArrayList == null)
            movieInfoArrayList = new ArrayList<MovieInfo>();

        return movieInfoArrayList;
    }

    public boolean fetchNextPageInfo() {
        boolean status = true;
        currentPage++;
        Object object = FileOperations.readFromFile(mContext, mCategory);
        if (null != object) {
            movieList = (HashMap<String, MovieInfo>) object;
            if(null != movieList)
                ((FavoriteMovieLibAdapter)mMovieLibAdapter).moviesInfo = new ArrayList<MovieInfo>(movieList.values());
            mMovieLibAdapter.dataSetChanged();
        } else {
            status = false;
        }

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
        fetchNextPageInfo();
        //mMovieLibAdapter.dataSetChanged();
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
        if(position <= ((FavoriteMovieLibAdapter)mMovieLibAdapter).moviesInfo.size())
            return ((FavoriteMovieLibAdapter)mMovieLibAdapter).moviesInfo.get(position);
        else
            return null;
    }

    public MovieInfo getMovieInfoForId(String movieId) {
        if(null != movieList && movieList.containsKey(movieId))
            return movieList.get(movieId);
        else
            return null;
    }

}
