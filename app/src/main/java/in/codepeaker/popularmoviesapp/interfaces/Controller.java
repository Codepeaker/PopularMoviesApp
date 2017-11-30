package in.codepeaker.popularmoviesapp.interfaces;

import java.util.ArrayList;

import in.codepeaker.popularmoviesapp.model.MovieModel;

/**
 * Created by github.com/codepeaker on 13/11/17.
 */

public interface Controller {
    void setMovieList(ArrayList<MovieModel.ResultsBean> movieList);
}
