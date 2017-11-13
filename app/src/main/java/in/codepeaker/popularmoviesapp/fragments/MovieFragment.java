package in.codepeaker.popularmoviesapp.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.codepeaker.popularmoviesapp.BuildConfig;
import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.adapter.MovieListRecyclerViewAdapter;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.info.MovieInfo;
import in.codepeaker.popularmoviesapp.model.MovieModel;

/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class MovieFragment extends Fragment {

    private View view;
    private RecyclerView movieRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.movie_fragment, container, false);
        init(view);
        initAction();
        return view;
    }

    private void init(View view) {
        movieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recyclerview);

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

    private class GetMovies extends AsyncTask<String, Void, MovieModel> {
        private final String sortType;
        ProgressDialog progressDialog;
        BufferedReader bufferedReader;
        HttpURLConnection httpURLConnection;
        private Context context;
        private String responseString;

        GetMovies(Context context, String sortType) {
            this.sortType = sortType;
            progressDialog = new ProgressDialog(context);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(MovieModel movieModel) {

            setRecyclerView(movieModel);
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected MovieModel doInBackground(String... params) {
            URL url;
            try {
                if (sortType.equals(Constants.SORT_BY_POPULARITY)) {


                    url = new URL(Constants.BASE_URL + "popular?api_key=" + BuildConfig.MOVIE_API_KEY);


                } else {
                    url = new URL(Constants.BASE_URL + "top_rated?api_key=" + BuildConfig.MOVIE_API_KEY);
                }


                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                if (inputStream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();


                String singleLine;
                while ((singleLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(singleLine).append("\n");
                }

                if (stringBuilder.length() == 0) {
                    return null;
                }

                responseString = stringBuilder.toString();


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            Gson gson = new Gson();
            Type type = new TypeToken<MovieModel>() {
            }.getType();

            return gson.fromJson(responseString, type);
        }


    }

}
