package in.codepeaker.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.info.MovieInfo;

public class DetailsActivity extends AppCompatActivity {

    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private ImageView coverPicture;
    private TextView movieName;
    private ImageView moviePic;
    private TextView movieRatings;
    private TextView movieReleaseDate;
    private TextView movieOverview;

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
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initScreen();

        initAction();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initAction() {
        String selectedMovie = getIntent().getStringExtra(Constants.selectedMovie);
        Gson gson = new Gson();
        MovieInfo movieInfo = gson.fromJson(selectedMovie, MovieInfo.class);

        setTitle(movieInfo.getTitle());
        movieName.setText(movieInfo.getTitle());
        movieOverview.setText(movieInfo.getOverview());
        movieRatings.setText(decimalFormat.format(movieInfo.getVote_average()));
        movieReleaseDate.setText(movieInfo.getRelease_date());


        Picasso.with(DetailsActivity.this)
                .load(Constants.imageUrl + movieInfo.getPoster_path())
                .into(moviePic);

        Picasso.with(DetailsActivity.this)
                .load(Constants.imageUrl + movieInfo.getBackdrop_path())
                .into(coverPicture);

    }

    private void initScreen() {
        coverPicture = (ImageView) findViewById(R.id.cover_picture);
        moviePic = (ImageView) findViewById(R.id.movie_image);
        movieName = (TextView) findViewById(R.id.movie_name);
        movieOverview = (TextView) findViewById(R.id.movie_overview);
        movieRatings = (TextView) findViewById(R.id.movie_ratings);
        movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
    }

}
