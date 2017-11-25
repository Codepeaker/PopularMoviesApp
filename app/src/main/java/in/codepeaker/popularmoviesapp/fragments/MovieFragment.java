package in.codepeaker.popularmoviesapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.adapter.MovieListRecyclerViewAdapter;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.info.MovieInfo;
import in.codepeaker.popularmoviesapp.interfaces.Controller;
import in.codepeaker.popularmoviesapp.model.MovieModel;
import in.codepeaker.popularmoviesapp.rest.GetMovies;
import in.codepeaker.popularmoviesapp.sqlhelper.SQLitehelper;
import in.codepeaker.popularmoviesapp.utils.AppUtils;

/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class MovieFragment extends Fragment implements Controller {

    private ArrayList<MovieInfo> movieInfoList;
    private MovieListRecyclerViewAdapter movieListRecyclerViewAdapter;
    private RecyclerView movieRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_fragment, container, false);

        init(view);
        initAction(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("movieList")) {
            movieInfoList = savedInstanceState.getParcelableArrayList("movieList");

        }
        super.onCreate(savedInstanceState);

    }

    private void init(View view) {
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview);

    }

    private void initAction(View view) {

        if (AppUtils.CheckEnabledInternet(getActivity())) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);
            getMovies.execute();

        } else {

            SQLitehelper sqLitehelper = new SQLitehelper(getActivity());
            List<MovieModel.ResultsBean> resultsBeanList = sqLitehelper.getAllRecords();
            setRecyclerView(resultsBeanList);
        }


        setHasOptionsMenu(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieList", movieInfoList);
        super.onSaveInstanceState(outState);

    }

    private void setRecyclerView(List<MovieModel.ResultsBean> resultsBeanList) {


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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_popularirty) {
            if (AppUtils.CheckEnabledInternet(getActivity())) {
                GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);
                getMovies.execute();

            } else {

                SQLitehelper sqLitehelper = new SQLitehelper(getActivity());
                List<MovieModel.ResultsBean> resultsBeanList = sqLitehelper.getAllRecords();
                setRecyclerView(resultsBeanList);
            }


        } else if (item.getItemId() == R.id.sort_by_ratings) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_RATINGS);

            getMovies.execute();

        } else if (item.getItemId() == R.id.favourites) {
            SQLitehelper sqLitehelper = new SQLitehelper(getActivity());
            List<MovieModel.ResultsBean> resultsBeanList = sqLitehelper.getAllFavMovieRecords();
            setRecyclerView(resultsBeanList);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void setMovieList(List<MovieModel.ResultsBean> movieList) {
        setRecyclerView(movieList);
    }
}
