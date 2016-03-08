package com.grab.movielib.com.grab.movielib.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.grab.movielib.R;
import com.grab.movielib.com.grab.movielib.util.ImageRequestQueue;

import java.util.ArrayList;

public class FavoriteMovieLibAdapter extends BaseAdapter implements  MovieLibAdapter{


    private Context mContext;
    public ArrayList<MovieInfo> moviesInfo;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public FavoriteMovieLibAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        moviesInfo = FavoriteMovieLibData.getInstance(context, this).getMovieLibList();
        mImageLoader = ImageRequestQueue.getInstance(mContext).getImageLoader();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        NetworkImageView imageView;

        if(v == null) {
            v = (FrameLayout) mInflater.inflate(R.layout.movie_item, parent, false);;
            v.setTag(R.id.movie_image, v.findViewById(R.id.movie_image));
        }

        imageView = (NetworkImageView) v.getTag(R.id.movie_image);
        imageView.setBackgroundColor(Color.GRAY);

        String url = "http://image.tmdb.org/t/p/w300/"+moviesInfo.get(position).posterPath;
//        Log.i(getClass().getSimpleName(), "url :: " + url);
        imageView.setImageUrl(url, mImageLoader);

        if(moviesInfo.size() - position <= 10) {
            FavoriteMovieLibData.getInstance(mContext, this).fetchNextPageInfo();
//            Log.i(getClass().getSimpleName(), "Fetching nextPage info.");
        }

        return v;
    }

    @Override
    public int getCount() {
        return moviesInfo.size();
    }

    @Override
    public void dataSetChanged() {
//        Log.i(getClass().getSimpleName(),"dataSetChanged()");
        this.notifyDataSetChanged();
    }
}
