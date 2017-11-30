package in.codepeaker.popularmoviesapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import in.codepeaker.popularmoviesapp.BuildConfig;
import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.contentprovider.MovieContract;
import in.codepeaker.popularmoviesapp.info.MovieInfo;
import in.codepeaker.popularmoviesapp.model.ReviewModel;
import in.codepeaker.popularmoviesapp.model.VideosModel;
import in.codepeaker.popularmoviesapp.rest.ApiService;
import in.codepeaker.popularmoviesapp.rest.RestClient;
import in.codepeaker.popularmoviesapp.sqlhelper.SQLitehelper;
import retrofit2.Call;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private FloatingActionButton fab;
    private SQLitehelper sqLitehelper;
    private MovieInfo movieInfo = null;
    private ImageView coverPicture;
    private TextView movieName;
    private ImageView moviePic;
    private TextView movieRatings;
    private TextView movieReleaseDate;
    private TextView movieOverview;
    private ImageView movieTrailerImageView;
    private String videourl;
    private TextView movieReviewsTextView;
    private boolean isfav = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("movieModel")) {
            movieInfo = savedInstanceState.getParcelable("movieModel");
        }
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initScreen();

        initAction();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initAction() {

        Bundle data = getIntent().getExtras();

        if (data != null) {
            movieInfo = data.getParcelable(Constants.selectedMovie);
        }

        sqLitehelper = new SQLitehelper(DetailsActivity.this);
        if (movieInfo == null) {
            return;
        }


        setTitle(movieInfo.getTitle());
        movieName.setText(movieInfo.getTitle());
        movieOverview.setText(movieInfo.getOverview());
        movieRatings.setText(String.format("%s / 10", decimalFormat.format(movieInfo.getVote_average())));
        movieReleaseDate.setText(String.format("Release Date - %s", movieInfo.getRelease_date()));
        movieReviewsTextView.setOnClickListener(this);
        movieReviewsTextView.setMaxLines(5);
        if (movieInfo.isFav() || checkIfFav(movieInfo.id)) {
            fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this
                    , R.drawable.ic_favorite_white_24dp));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this
                    , R.drawable.ic_favorite_border_white_24dp));
        }

        Picasso.with(DetailsActivity.this)
                .load(Constants.imageUrl + movieInfo.getPoster_path())
                .placeholder(R.drawable.moviepic)
                .into(moviePic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {moviePic.setImageDrawable(
                            ContextCompat.getDrawable(DetailsActivity.this,R.drawable.moviepic));

                    }
                });

        Picasso.with(DetailsActivity.this)
                .load(Constants.imageUrl + movieInfo.getBackdrop_path())
                .placeholder(R.drawable.moviebackground)
                .into(coverPicture, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {coverPicture.setImageDrawable(
                            ContextCompat.getDrawable(DetailsActivity.this,R.drawable.moviepic));

                    }
                });

        callVideosApi(movieInfo.id);

        callReviewsApi(movieInfo.id);

        movieTrailerImageView.setOnClickListener(this);


        fab.setOnClickListener(this);


    }

    private boolean checkIfFav(int id) {
        Uri uri = MovieContract.BASE_CONTENT_URI.buildUpon().appendPath(MovieContract.PATH_MOVIE).build();
        Cursor cursor = getContentResolver().query(uri
                , null
                , MovieContract.MovieEntry.COLUMN_id + "=" + id + " and "
                        + MovieContract.MovieEntry.COLUMN_isfavorite + "=" + 1
                , null
                , null);

        if (cursor != null) {
            boolean isFav = cursor.getCount() > 0;
            cursor.close();
            return isFav;
        }
        return false;
    }

    private void callReviewsApi(int id) {
        Call<ReviewModel> reviewModelCall = new RestClient().getApiService().callReviewModel(id, BuildConfig.MOVIE_API_KEY);
        reviewModelCall.enqueue(new retrofit2.Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                if (response.code() == Constants.OK) {
                    ReviewModel reviewModel = response.body();
                    StringBuilder reviewTextString = new StringBuilder();
                    if (reviewModel != null) {
                        int authorNumber = 1;
                        for (ReviewModel.ResultsBean resultsBean : reviewModel.getResults()) {
                            reviewTextString.append(authorNumber).append(". ")
                                    .append(resultsBean.getAuthor())
                                    .append("\n")
                                    .append(resultsBean.getContent()).append("\n\n\n");
                            authorNumber++;
                        }

                        movieReviewsTextView.setText(reviewTextString);
                    }
                } else {
                    movieReviewsTextView.setText(R.string.review_not_offline);
                }
            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {

                movieReviewsTextView.setText(R.string.review_not_offline);
            }
        });
    }

    private void callVideosApi(int id) {
        ApiService apiService = new RestClient().getApiService();
        retrofit2.Call<VideosModel> videosModelCall = apiService.callVideosModelCall(id, BuildConfig.MOVIE_API_KEY);

        videosModelCall.enqueue(new retrofit2.Callback<VideosModel>() {
            @Override
            public void onResponse(retrofit2.Call<VideosModel> call, Response<VideosModel> response) {
                if (response.code() == Constants.OK) {
                    VideosModel videosModel = response.body();
                    if (videosModel == null || videosModel.getResults() == null) {
                        return;
                    }
                    videourl = response.body().getResults().get(0).getKey();
                    Picasso.with(DetailsActivity.this)
                            .load(Constants.thumbnailUrl + response.body().getResults().get(0).getKey() + "/0.jpg")
                            .placeholder(R.drawable.moviebackground)
                            .into(movieTrailerImageView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {movieTrailerImageView.setImageDrawable(
                                        ContextCompat.getDrawable(DetailsActivity.this,R.drawable.moviepic));

                                }
                            });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<VideosModel> call, Throwable t) {

            }
        });


    }

    private void initScreen() {
        coverPicture = findViewById(R.id.cover_picture);
        moviePic = findViewById(R.id.movie_image);
        fab = findViewById(R.id.fab);
        movieName = findViewById(R.id.movie_name);
        movieOverview = findViewById(R.id.movie_overview);
        movieRatings = findViewById(R.id.movie_ratings);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieTrailerImageView = findViewById(R.id.trailer_imageview);
        movieReviewsTextView = findViewById(R.id.reviews_textview);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("movieModel", movieInfo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.trailer_imageview) {
            Intent intent = new Intent();
            if (videourl == null) {
                return;
            }
            String url = "https://www.youtube.com/watch?v=" + videourl;
            intent.setData(Uri.parse(url));
            startActivity(intent);


        } else if (v.getId() == R.id.reviews_textview) {
            if (movieReviewsTextView.getMaxLines()
                    == 5) {
                movieReviewsTextView.setMaxLines(Integer.MAX_VALUE);
            } else {
                movieReviewsTextView.setMaxLines(5);
            }
        } else if (v.getId() == R.id.fab) {
            if (!checkIfFav(movieInfo.getId())) {
                fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this
                        , R.drawable.ic_favorite_white_24dp));

                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.MovieEntry.COLUMN_backdrop_path, movieInfo.getBackdrop_path());
                contentValues.put(MovieContract.MovieEntry.COLUMN_id, movieInfo.getId());
                contentValues.put(MovieContract.MovieEntry.COLUMN_overview, movieInfo.getOverview());
                contentValues.put(MovieContract.MovieEntry.COLUMN_poster_path, movieInfo.getPoster_path());
                contentValues.put(MovieContract.MovieEntry.COLUMN_release_date, movieInfo.getRelease_date());
                contentValues.put(MovieContract.MovieEntry.COLUMN_isfavorite, true);
                contentValues.put(MovieContract.MovieEntry.COLUMN_title, movieInfo.getTitle());
                contentValues.put(MovieContract.MovieEntry.COLUMN_vote_average, movieInfo.getVote_average());

                String stringId = Integer.toString(movieInfo.getId());
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().insert(uri, contentValues);

            } else {
                fab.setImageDrawable(ContextCompat.getDrawable(DetailsActivity.this
                        , R.drawable.ic_favorite_border_white_24dp));

//                sqLitehelper.deleteRecord(movieInfo.getId());
                String stringId = Integer.toString(movieInfo.getId());
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                // COMPLETED (2) Delete a single row of data using a ContentResolver
                getContentResolver().delete(uri, null, null);


            }


        }
    }
}
