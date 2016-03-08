package com.grab.movielib.com.grab.movielib.home;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class MovieLibData implements Response.Listener, Response.ErrorListener{

    int pageCount;
    int currentPage;
    ArrayList<MovieInfo> movieInfoArrayList;

    @Override
    public void onResponse(Object response) {

        String res = (String) response;
//        Log.i(getClass().getSimpleName(), "onResponse :: " + response.toString());

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
                movieInfo.id = ((JSONObject) dataArray.get(i)).getString("id");
                movieInfo.title = ((JSONObject) dataArray.get(i)).getString("title");
                movieInfo.originalTitle = ((JSONObject) dataArray.get(i)).getString("original_title");
                movieInfo.language = ((JSONObject) dataArray.get(i)).getString("original_language");
                movieInfo.posterPath = ((JSONObject) dataArray.get(i)).getString("poster_path");
                movieInfo.backdropPath = ((JSONObject) dataArray.get(i)).getString("backdrop_path");
                movieInfo.overview = ((JSONObject) dataArray.get(i)).getString("overview");
                movieInfo.releaseDate = ((JSONObject) dataArray.get(i)).getString("release_date");
                movieInfo.voteCount = ((JSONObject) dataArray.get(i)).getInt("vote_count");
                movieInfo.video = ((JSONObject) dataArray.get(i)).getBoolean("video");
//                Log.i(getClass().getSimpleName(),"Movie Object :: "+movieInfo.toString());
                movieInfoArrayList.add(movieInfo);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(),"Exception in JSON conversion :: "+e.toString());
        }

        dataArray = null;

        this.dataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Log.i(getClass().getSimpleName(), "onErrorResponse :: "+error.toString());
    }

    public abstract void dataSetChanged();
}
