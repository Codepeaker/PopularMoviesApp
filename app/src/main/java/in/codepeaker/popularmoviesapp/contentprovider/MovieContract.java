package in.codepeaker.popularmoviesapp.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by github.com/codepeaker on 29/11/17.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.provider.movielist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIE = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String TABLE_NAME = "MOVIES_TABLE";
        public static final String COLUMN_id = "movie_id";
        public static final String COLUMN_overview = "overview";
        public static final String COLUMN_isfavorite = "isFavourite";
        public static final String COLUMN_release_date = "releasedate";
        public static final String COLUMN_backdrop_path = "backdrop_path";
        public static final String COLUMN_vote_average = "vote_average";
        public static final String COLUMN_poster_path = "poster_path";
        public static final String COLUMN_title = "title";
    }
}
