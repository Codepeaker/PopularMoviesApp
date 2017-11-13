package in.codepeaker.popularmoviesapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.fragments.MovieFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        initAction();

    }

    private void init() {

    }

    private void initAction() {

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MovieFragment movieFragment = new MovieFragment();
        fragmentTransaction.add(android.R.id.content, movieFragment, Constants.MovieFragment);

        fragmentTransaction.commit();


    }
}
