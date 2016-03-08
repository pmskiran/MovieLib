package com.grab.movielib;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.grab.movielib.com.grab.movielib.util.ImageRequestQueue;

public class MovieDetails extends AppCompatActivity
            implements View.OnClickListener{


    ImageLoader mImageLoader = null;
    String backdropPath = null;
    String posterPath = null;
    String title = null;
    String originalTitle = null;
    String overview = null;
    String id = null;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        String bookmark = "";
        if (extras != null) {
            backdropPath = extras.getString("backdropPath");
            posterPath = extras.getString("posterPath");
            title = extras.getString("title");
            originalTitle = extras.getString("orignalTitle");
            overview = extras.getString("overview");
            id = extras.getString("id");
            position = extras.getInt("position");
            bookmark = extras.getString("bookmark");
        }
//        Log.i(getClass().getSimpleName(), "backdropPath :: "+backdropPath);
        mImageLoader = ImageRequestQueue.getInstance(getApplicationContext()).getImageLoader();
        String url = "http://image.tmdb.org/t/p/w300/";
//        Log.i(getClass().getSimpleName(), "url :: " + url);

        NetworkImageView posterImageView = (NetworkImageView) findViewById(R.id.movie_poster);
        posterImageView.setBackgroundColor(Color.GRAY);
        if(null != posterPath && !posterPath.equalsIgnoreCase("null")) {
            posterImageView.setImageUrl(url + posterPath, mImageLoader);
        } else {
            posterImageView.setDefaultImageResId(R.drawable.andy);
        }

        NetworkImageView backdropImageView = (NetworkImageView) findViewById(R.id.movie_backdrop);
        backdropImageView.setBackgroundColor(Color.GRAY);
        if(null != backdropPath && !backdropPath.equalsIgnoreCase("null")) {
            backdropImageView.setImageUrl(url + backdropPath, mImageLoader);
        }
         else {
            backdropImageView.setDefaultImageResId(R.drawable.andy);
        }

        ((TextView)findViewById(R.id.movie_title)).setText(title);
        ((TextView)findViewById(R.id.movie_original_title)).setText(originalTitle);
        ((TextView)findViewById(R.id.movie_overview)).setText(overview);
        if(bookmark.equalsIgnoreCase("Y")) {
            ((ImageView) findViewById(R.id.movie_favorite)).setBackground(getDrawable(R.drawable.bookmarkhighlight));
            ((ImageView) findViewById(R.id.movie_favorite)).setTag("Y");
        }

        ((ImageView) findViewById(R.id.movie_favorite)).setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Log.i(getClass().getSimpleName(),"Home button clicked.");

            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("position", position);
            ImageView img = (ImageView)findViewById(R.id.movie_favorite);
            if(null != img.getTag()) {
                intent.putExtra("bookmark", img.getTag().toString());
            } else {
                intent.putExtra("bookmark", "N");
            }

            setResult(RESULT_OK, intent);

            finishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Log.i(getClass().getSimpleName(), "onBackPressed().");

        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("position", position);
        ImageView img = (ImageView)findViewById(R.id.movie_favorite);
        if(null != img.getTag()) {
            intent.putExtra("bookmark", img.getTag().toString());
        } else {
            intent.putExtra("bookmark", "N");
        }
        setResult(RESULT_OK, intent);

        finishAfterTransition();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.movie_favorite) {
//            Log.i(getClass().getSimpleName(), "Movie favorite.");
            if(null != ((ImageView)v).getTag()) {
                if(((ImageView)v).getTag().toString().equalsIgnoreCase("Y")) {
                    ((ImageView)v).setTag("N");
                    ((ImageView)v).setBackground(getDrawable(R.drawable.bookmark));
                } else {
                    ((ImageView)v).setTag("Y");
                    ((ImageView)v).setBackground(getDrawable(R.drawable.bookmarkhighlight));
                }
            } else {
                ((ImageView)v).setTag("Y");
                ((ImageView)v).setBackground(getDrawable(R.drawable.bookmarkhighlight));
            }


        }
    }
}
