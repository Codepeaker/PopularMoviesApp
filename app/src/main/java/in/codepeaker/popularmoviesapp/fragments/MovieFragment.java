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

/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class MovieFragment extends Fragment implements Controller {

    private RecyclerView movieRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        init(view);
        initAction();
        return view;
    }

    private void init(View view) {
        movieRecyclerView = view.findViewById(R.id.movie_recyclerview);

    }

    private void initAction() {

        GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);

        getMovies.execute();

        setHasOptionsMenu(true);

    }

    public void setRecyclerView(MovieModel movieModel) {
        List<MovieModel.ResultsBean> resultsBeanList = movieModel.getResults();

        List<MovieInfo> movieInfoList = new ArrayList<>();
        for (int i = 0; i < movieModel.getResults().size(); i++) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.title = resultsBeanList.get(i).getTitle();
            movieInfo.poster_path = resultsBeanList.get(i).getPoster_path();
            movieInfo.overview = resultsBeanList.get(i).getOverview();
            movieInfo.release_date = resultsBeanList.get(i).getRelease_date();
            movieInfo.backdrop_path = resultsBeanList.get(i).getBackdrop_path();
            movieInfo.vote_average = resultsBeanList.get(i).getVote_average() / 2;
            movieInfoList.add(movieInfo);
        }

        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        MovieListRecyclerViewAdapter movieListRecyclerViewAdapter = new MovieListRecyclerViewAdapter(getActivity(), movieInfoList);

        movieRecyclerView.setAdapter(movieListRecyclerViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_popularirty) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_POPULARITY);

            getMovies.execute();

        } else if (item.getItemId() == R.id.sort_by_ratings) {
            GetMovies getMovies = new GetMovies(getActivity(), Constants.SORT_BY_RATINGS);

            getMovies.execute();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.movie_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void setMovieList(MovieModel movieList) {
        setRecyclerView(movieList);
    }
}
