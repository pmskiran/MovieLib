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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoriteMovies.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoriteMovies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMovies extends Fragment  implements
        AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    GridView mGridView;
    //ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
    FavoriteMovieLibAdapter movieLibAdapter = null;
    String mCategory;


    private OnFragmentInteractionListener mListener;

    public FavoriteMovies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category category.
     * @return A new instance of fragment FavoriteMovies.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteMovies newInstance(String category) {
        FavoriteMovies fragment = new FavoriteMovies();
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
        movieLibAdapter = new FavoriteMovieLibAdapter(getActivity().getApplicationContext());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(movieLibAdapter);
        swingBottomInAnimationAdapter.setAbsListView(mGridView);
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);
        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(swingBottomInAnimationAdapter);

        FavoriteMovieLibData.getInstance(getActivity().getApplicationContext(), movieLibAdapter).getFreshCategory(mCategory);

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
        MovieInfo movieInfo = FavoriteMovieLibData.getInstance(getActivity().getApplicationContext(), movieLibAdapter)
                                .getMovieInfoAtPosition(position);
        if(null != movieInfo) {
            Log.i(getClass().getSimpleName(), "backdropPath :: " + movieInfo.backdropPath);
            bundle.putString("backdropPath", movieInfo.backdropPath);
            bundle.putString("posterPath", movieInfo.posterPath);
            bundle.putString("title", movieInfo.title);
            bundle.putString("orignalTitle", movieInfo.originalTitle);
            bundle.putString("overview", movieInfo.overview);
            bundle.putInt("position", position);
            bundle.putString("id", movieInfo.id);
            bundle.putString("bookmark","Y");
        }

        movieDetails.putExtras(bundle);
        getActivity().startActivityForResult(movieDetails, 101, activityOptions.toBundle());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(getClass().getSimpleName(), "onActivityResult() requestCode::"+requestCode);

        String id = data.getStringExtra("id");
        int position = data.getIntExtra("position", 0);
        String bookmark = data.getStringExtra("bookmark");

        if(bookmark.equalsIgnoreCase("Y")) {
            MovieInfo movieInfo = FavoriteMovieLibData.getInstance(context, FavoriteMovieLibData.getInstance().mMovieLibAdapter).getMovieInfoAtPosition(position);
            FileOperations.appendObject(context, FavoriteMovieLibData.getInstance().mCategory, movieInfo);
            FavoriteMovieLibData.getInstance().dataSetChanged();
        } else if(bookmark.equalsIgnoreCase("N")) {
            MovieInfo movieInfo = FavoriteMovieLibData.getInstance(context, FavoriteMovieLibData.getInstance().mMovieLibAdapter).getMovieInfoAtPosition(position);
            FileOperations.deleteObject(context, FavoriteMovieLibData.getInstance().mCategory, movieInfo);
            FavoriteMovieLibData.getInstance().dataSetChanged();
        }

//        Log.i(getClass().getSimpleName(), "id::" + id +"  position::"+position);
    }
}
