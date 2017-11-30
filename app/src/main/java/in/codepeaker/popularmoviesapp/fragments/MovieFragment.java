package in.codepeaker.popularmoviesapp.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.adapter.MovieListRecyclerViewAdapter;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.contentprovider.MovieContract;
import in.codepeaker.popularmoviesapp.info.MovieInfo;
import in.codepeaker.popularmoviesapp.interfaces.Controller;
import in.codepeaker.popularmoviesapp.model.MovieModel;
import in.codepeaker.popularmoviesapp.rest.GetMovies;
import in.codepeaker.popularmoviesapp.utils.AppUtils;

/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class MovieFragment extends Fragment implements Controller {

    private static final String SAVED_LAYOUT_MANAGER = "savedLayoutManager";
    Parcelable layoutManagerSavedState;
    private ArrayList<MovieInfo> movieInfoList;
    private MovieListRecyclerViewAdapter movieListRecyclerViewAdapter;
    private RecyclerView movieRecyclerView;
    private View view;

    public MovieFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.movie_fragment, container, false);
        init(view);

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("movieList") != null) {
            ArrayList<MovieInfo> arrayList = savedInstanceState.getParcelableArrayList("movieList");
            ArrayList<MovieModel.ResultsBean> resultsBeans = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                MovieModel.ResultsBean resultsBean = new MovieModel.ResultsBean();
                resultsBean.setVote_average(arrayList.get(i).getVote_average());
                resultsBean.setId(arrayList.get(i).getId());
                resultsBean.setTitle(arrayList.get(i).getTitle());
                resultsBean.setPoster_path(arrayList.get(i).getPoster_path());
                resultsBean.setBackdrop_path(arrayList.get(i).getBackdrop_path());
                resultsBean.setOverview(arrayList.get(i).getOverview());
                resultsBean.setFav(arrayList.get(i).isFav());
                resultsBeans.add(resultsBean);
            }
            layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
            setRecyclerView(resultsBeans);
        } else {
            initAction();
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

    }

    private void init(View view) {
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview);

    }

    private void initAction() {

        if (AppUtils.CheckEnabledInternet(getActivity())) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);
            getMovies.execute();

        } else {

            Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_MOVIE).build();

            Cursor cursor = getActivity().getContentResolver().query(uri
                    , null
                    , null
                    , null
                    , null);

            setRecyclerView(getAllMovies(cursor));
        }


    }

    private ArrayList<MovieModel.ResultsBean> getAllMovies(Cursor cursor) {
        ArrayList<MovieModel.ResultsBean> contacts = new ArrayList<>();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                MovieModel.ResultsBean resultsBean = new MovieModel.ResultsBean();
                resultsBean.setId(Integer.parseInt(cursor.getString(0)));
                resultsBean.setOverview(cursor.getString(1));
                resultsBean.setBackdrop_path(cursor.getString(2));
                resultsBean.setVote_average(Double.parseDouble(Constants.decimalFormat.format(cursor.getDouble(3))));
                resultsBean.setPoster_path(cursor.getString(4));
                resultsBean.setTitle(cursor.getString(5));
                resultsBean.setFav(cursor.getInt(6) > 0);
                resultsBean.setRelease_date(cursor.getString(7));

                contacts.add(resultsBean);
            }
        }
        return contacts;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_LAYOUT_MANAGER, movieRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList("movieList", movieInfoList);
        super.onSaveInstanceState(outState);

    }


    private void setRecyclerView(ArrayList<MovieModel.ResultsBean> resultsBeanList) {

        if (resultsBeanList == null) {
            return;
        }

        movieInfoList = new ArrayList<>();
        for (int i = 0; i < resultsBeanList.size(); i++) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.id = resultsBeanList.get(i).getId();
            movieInfo.title = resultsBeanList.get(i).getTitle();
            movieInfo.poster_path = resultsBeanList.get(i).getPoster_path();
            movieInfo.overview = resultsBeanList.get(i).getOverview();
            movieInfo.release_date = resultsBeanList.get(i).getRelease_date();
            movieInfo.backdrop_path = resultsBeanList.get(i).getBackdrop_path();
            movieInfo.vote_average = resultsBeanList.get(i).getVote_average();
            movieInfo.isFav = resultsBeanList.get(i).isFav();
            movieInfoList.add(movieInfo);
        }

        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieListRecyclerViewAdapter = new MovieListRecyclerViewAdapter(getActivity(), movieInfoList);

        movieRecyclerView.setAdapter(movieListRecyclerViewAdapter);
        restoreLayoutManagerPosition();
    }


    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            movieRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_popularirty) {
            if (AppUtils.CheckEnabledInternet(getActivity())) {
                GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);
                getMovies.execute();

            } else {

                Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_MOVIE).build();
                Cursor cursor = getActivity().getContentResolver().query(uri
                        , null
                        , null
                        , null
                        , null);


                setRecyclerView(getAllMovies(cursor));
            }


        } else if (item.getItemId() == R.id.sort_by_ratings) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_RATINGS);

            getMovies.execute();

        } else if (item.getItemId() == R.id.favourites) {
            Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_MOVIE).build();
            int isFavourite = 1;
            Cursor cursor = getActivity().getContentResolver().query(uri
                    , null
                    , "isFavourite=?"
                    , new String[]{isFavourite + ""}
                    , null);
            setRecyclerView(getAllMovies(cursor));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void setMovieList(ArrayList<MovieModel.ResultsBean> movieList) {
        setRecyclerView(movieList);
    }
}
