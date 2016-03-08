package com.grab.movielib.com.grab.movielib.home;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.grab.movielib.MovieDetails;
import com.grab.movielib.R;
import com.grab.movielib.com.grab.movielib.util.FileOperations;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryMovies.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryMovies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryMovies extends Fragment implements
        AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;

    GridView mGridView;
    ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
    CategoriesMovieLibAdapter movieLibAdapter = null;
    public String mCategory;

    public CategoryMovies() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category MovieCategory
     * @return A new instance of fragment CategoryMovies.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryMovies newInstance(String category) {
        CategoryMovies fragment = new CategoryMovies();

        Bundle args = new Bundle();
        args.putString("category", category);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCategory = getArguments().getString("category");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_movies, container, false);
        mGridView = (GridView) view.findViewById(R.id.category_movies_gridview);
        movieLibAdapter = new CategoriesMovieLibAdapter(getActivity().getApplicationContext());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(movieLibAdapter);
        swingBottomInAnimationAdapter.setAbsListView(mGridView);
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(swingBottomInAnimationAdapter);

        CategoryMovieLibData.getInstance(getActivity().getApplicationContext(), movieLibAdapter).getFreshCategory(mCategory);

        return view;
    }

    public void updateFreshCategory(String category) {
        mCategory = category;
        CategoryMovieLibData.getInstance(getActivity().getApplicationContext(), movieLibAdapter).getFreshCategory(mCategory);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent movieDetails = new Intent(getActivity(), MovieDetails.class);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "movie_poster");
        Bundle bundle = new Bundle();
        MovieInfo movieInfo = CategoryMovieLibData.getInstance(getActivity().getApplicationContext(), movieLibAdapter).getMovieInfoAtPosition(position);
        if(null != movieInfo) {
//            Log.i(getClass().getSimpleName(), "backdropPath :: " + movieInfo.backdropPath);
            bundle.putString("backdropPath", movieInfo.backdropPath);
            bundle.putString("posterPath", movieInfo.posterPath);
            bundle.putString("title", movieInfo.title);
            bundle.putString("orignalTitle", movieInfo.originalTitle);
            bundle.putString("overview", movieInfo.overview);
            bundle.putInt("position", position);
            bundle.putString("id",movieInfo.id);
            if(null != FavoriteMovieLibData.getInstance().getMovieInfoForId(movieInfo.id))
                bundle.putString("bookmark","Y");
            else
                bundle.putString("bookmark","N");
        }

        movieDetails.putExtras(bundle);

        getActivity().startActivityForResult(movieDetails, 100, activityOptions.toBundle());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i(getClass().getSimpleName(), "onActivityResult() requestCode::" + requestCode);

        String id = data.getStringExtra("id");
        int position = data.getIntExtra("position", 0);
        String bookmark = data.getStringExtra("bookmark");

        if(bookmark.equalsIgnoreCase("Y")) {
            MovieInfo movieInfo = CategoryMovieLibData.getInstance(context, CategoryMovieLibData.getInstance().mMovieLibAdapter).getMovieInfoAtPosition(position);
            FileOperations.appendObject(context, CategoryMovieLibData.getInstance().mCategory, movieInfo);
            FavoriteMovieLibData.getInstance().getFreshCategory(CategoryMovieLibData.getInstance().mCategory);
        } else if(bookmark.equalsIgnoreCase("N")) {
            MovieInfo movieInfo = CategoryMovieLibData.getInstance(context, CategoryMovieLibData.getInstance().mMovieLibAdapter).getMovieInfoAtPosition(position);
            FileOperations.deleteObject(context, CategoryMovieLibData.getInstance().mCategory, movieInfo);
            FavoriteMovieLibData.getInstance().getFreshCategory(CategoryMovieLibData.getInstance().mCategory);
        }

//        Log.i(getClass().getSimpleName(), "id::" + id +"  position::"+position);
    }
}
