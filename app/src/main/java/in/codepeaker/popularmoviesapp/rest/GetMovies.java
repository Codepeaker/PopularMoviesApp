package in.codepeaker.popularmoviesapp.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.model.MovieModel;

/**
 * Created by github.com/codepeaker on 11/11/17.
 */

public class GetMovies extends AsyncTask<String, Void, MovieModel> {
    private final String sortType;
    ProgressDialog progressDialog;
    BufferedReader bufferedReader;
    HttpURLConnection httpURLConnection;
    private Context context;
    private String responseString;

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
