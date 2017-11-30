package in.codepeaker.popularmoviesapp.rest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import in.codepeaker.popularmoviesapp.BuildConfig;
import in.codepeaker.popularmoviesapp.activities.HomeActivity;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.contentprovider.MovieContract;
import in.codepeaker.popularmoviesapp.fragments.MovieFragment;
import in.codepeaker.popularmoviesapp.model.MovieModel;


/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class GetMovies extends AsyncTask<String, Void, MovieModel> {
    private final String sortType;
    private final ProgressDialog progressDialog;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private boolean isPopular = false;
    private BufferedReader bufferedReader;
    private HttpURLConnection httpURLConnection;

    public GetMovies(Context context, String sortType) {
        this.sortType = sortType;
        progressDialog = new ProgressDialog(context);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Please wait..");


    }

    @Override
    protected void onPostExecute(MovieModel movieModel) {

        if (movieModel == null || movieModel.getResults() == null) {
            Toast.makeText(context, "Make sure you have active internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        FragmentManager fr = ((HomeActivity) context).getSupportFragmentManager();
        MovieFragment movieFragment = (MovieFragment) fr.findFragmentByTag(Constants.MovieFragment);
        movieFragment.setMovieList(movieModel.getResults());


        if (isPopular) {
//            SQLitehelper sqLitehelper = new SQLitehelper(context);
//            sqLitehelper.insertAllPopularRecord(movieModel.getResults());

            for (MovieModel.ResultsBean singleMovieModel : movieModel.getResults()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_backdrop_path, singleMovieModel.getBackdrop_path());
                contentValues.put(MovieContract.MovieEntry.COLUMN_id, singleMovieModel.getId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_overview, singleMovieModel.getOverview());
                contentValues.put(MovieContract.MovieEntry.COLUMN_poster_path, singleMovieModel.getPoster_path());
                contentValues.put(MovieContract.MovieEntry.COLUMN_release_date, singleMovieModel.getRelease_date());
                contentValues.put(MovieContract.MovieEntry.COLUMN_title, singleMovieModel.getTitle());
                contentValues.put(MovieContract.MovieEntry.COLUMN_vote_average, singleMovieModel.getVote_average());
                context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected MovieModel doInBackground(String... params) {
        URL url;
        String responseString;
        try {
            if (sortType.equals(Constants.SORT_BY_POPULARITY)) {
                isPopular = true;

                url = new URL(Constants.BASE_URL + "popular?api_key=" + BuildConfig.MOVIE_API_KEY);


            } else {
                isPopular = false;
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
