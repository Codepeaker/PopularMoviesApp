package in.codepeaker.popularmoviesapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.fragments.MovieFragment;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView movieRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        initAction();

    }

    private void init() {
        movieRecyclerView = (RecyclerView) findViewById(R.id.movie_recyclerview);

    }

    private void initAction() {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MovieFragment movieFragment = new MovieFragment();
        fragmentTransaction.replace(android.R.id.content, movieFragment);

        fragmentTransaction.commit();


    }
}
