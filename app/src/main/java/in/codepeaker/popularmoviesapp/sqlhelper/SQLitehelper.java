package in.codepeaker.popularmoviesapp.sqlhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.codepeaker.popularmoviesapp.contentprovider.MovieContract;
import in.codepeaker.popularmoviesapp.model.MovieModel;

/**
 * Created by github.com/codepeaker on 23/11/17.
 */

public class SQLitehelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "MOVIES_TABLE";
    private static final String COLUMN_id = "movie_id";
    private static final String COLUMN_overview = "overview";
    private static final String COLUMN_release_date = "releasedate";
    private static final String COLUMN_backdrop_path = "backdrop_path";
    private static final String COLUMN_vote_average = "vote_average";
    private static final String COLUMN_poster_path = "poster_path";
    private static final String COLUMN_title = "title";
    private static final String DATABASE_NAME = "SQLiteDatabase.db";

    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_isfavorite = "isFavourite";
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private SQLiteDatabase database;

    public SQLitehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( "
                + COLUMN_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_overview + " VARCHAR, "
                + COLUMN_backdrop_path + " VARCHAR, "
                + COLUMN_vote_average + " DOUBLE, "
                + COLUMN_poster_path + " VARCHAR, "
                + COLUMN_title + " VARCHAR, "
                + COLUMN_isfavorite + " INTEGER, "
                + COLUMN_release_date + " VARCHAR);");
    }

    public boolean checkIfFav(int id) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_id + "=" + id
                , null, null, null, null);
        if (cursor.getCount() == 0) {
            return false;
        }
        MovieModel.ResultsBean resultsBean = new MovieModel.ResultsBean();
        cursor.moveToNext();
        boolean isFav = cursor.getInt(6) > 0;
        cursor.close();
        database.close();
        return isFav;
    }

    public ArrayList<MovieModel.ResultsBean> getAllRecords(String table, String[] columns, String selection,
                                                           String[] selectionArgs, String groupBy, String having,
                                                           String orderBy) {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<MovieModel.ResultsBean> contacts = new ArrayList<>();
        MovieModel.ResultsBean resultsBean;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                resultsBean = new MovieModel.ResultsBean();
                resultsBean.setId(Integer.parseInt(cursor.getString(0)));
                resultsBean.setOverview(cursor.getString(1));
                resultsBean.setBackdrop_path(cursor.getString(2));
                resultsBean.setVote_average(Double.parseDouble(decimalFormat.format(cursor.getDouble(3))));
                resultsBean.setPoster_path(cursor.getString(4));
                resultsBean.setTitle(cursor.getString(5));
                resultsBean.setFav(cursor.getInt(6) > 0);
                resultsBean.setRelease_date(cursor.getString(7));
                contacts.add(resultsBean);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public ArrayList<MovieModel.ResultsBean> getAllFavMovieRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<MovieModel.ResultsBean> movies = new ArrayList<>();
        MovieModel.ResultsBean resultsBean;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                if (!(cursor.getInt(6) > 0)) {
                    continue;
                }
                resultsBean = new MovieModel.ResultsBean();
                resultsBean.setId(Integer.parseInt(cursor.getString(0)));
                resultsBean.setOverview(cursor.getString(1));
                resultsBean.setBackdrop_path(cursor.getString(2));
                resultsBean.setVote_average(Double.parseDouble(decimalFormat.format(cursor.getDouble(3))));
                resultsBean.setPoster_path(cursor.getString(4));
                resultsBean.setTitle(cursor.getString(5));
                resultsBean.setFav(cursor.getInt(6) > 0);
                resultsBean.setRelease_date(cursor.getString(7));
                movies.add(resultsBean);
            }
        }
        cursor.close();
        database.close();
        return movies;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertFavouriteMovieRecord(ContentValues contentValues) {
        database = this.getReadableDatabase();

        if (database.insert(TABLE_NAME, null, contentValues) == -1) {
            database.update(TABLE_NAME, contentValues, COLUMN_id + "=" + contentValues.get(MovieContract.MovieEntry.COLUMN_id),
                    null);
        }
        database.close();
    }

    public void insertAllPopularRecord(ContentValues contentValues) {

        database = this.getReadableDatabase();

        database.insert(TABLE_NAME, null, contentValues);

        database.close();
    }


    public int deleteRecord(int id) {
        database = this.getReadableDatabase();
        int count = database.delete(TABLE_NAME, COLUMN_id + " = ?", new String[]{id + ""});
        database.close();
        return count;
    }


}