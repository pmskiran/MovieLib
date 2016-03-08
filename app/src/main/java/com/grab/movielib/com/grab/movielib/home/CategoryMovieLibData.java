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
//    private int pageCount;
//    private int currentPage;
    public String mCategory;
//    private ArrayList<MovieInfo> movieInfoArrayList;

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

    /*@Override
    public void onResponse(Object response) {
        String res = (String) response;
        Log.i(getClass().getSimpleName(), "onResponse :: " + response.toString());

        JSONArray dataArray = null;
        int arrayLength = 0;
        try {
            JSONObject jsonObj = new JSONObject(response.toString());
            dataArray = jsonObj.getJSONArray("results");
            pageCount = jsonObj.getInt("total_pages");
            currentPage = jsonObj.getInt("page");
            arrayLength = dataArray.length();

            for (int i = arrayLength - 1; i >= 0; i--) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.title = ((JSONObject) dataArray.get(i)).getString("title");
            movieInfo.language = ((JSONObject) dataArray.get(i)).getString("original_language");
            movieInfo.posterPath = ((JSONObject) dataArray.get(i)).getString("poster_path");
            movieInfo.backdropPath = ((JSONObject) dataArray.get(i)).getString("backdrop_path");
            movieInfo.overview = ((JSONObject) dataArray.get(i)).getString("overview");
            movieInfo.releaseDate = ((JSONObject) dataArray.get(i)).getString("release_date");
            movieInfo.voteCount = ((JSONObject) dataArray.get(i)).getInt("vote_count");
            movieInfo.video = ((JSONObject) dataArray.get(i)).getBoolean("video");
            Log.i(getClass().getSimpleName(),"Movie Object :: "+movieInfo.toString());
            movieInfoArrayList.add(movieInfo);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(),"Exception in JSON conversion :: "+e.toString());
        }

        dataArray = null;

        mMovieLibAdapter.dataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i(getClass().getSimpleName(), "onErrorResponse :: "+error.toString());
    }*/

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
