package in.codepeaker.popularmoviesapp.sqlhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.codepeaker.popularmoviesapp.model.MovieModel;

/**
 * Created by github.com/codepeaker on 23/11/17.
 */

public class SQLitehelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "MOVIES_TABLE";
    public static final String COLUMN_id = "ID";
    public static final String COLUMN_overview = "overview";
    public static final String COLUMN_release_date = "releasedate";
    public static final String COLUMN_backdrop_path = "backdrop_path";
    public static final String COLUMN_video = "video";
    public static final String COLUMN_vote_average = "vote_average";
    public static final String COLUMN_poster_path = "poster_path";
    public static final String COLUMN_title = "title";
    public static final String DATABASE_NAME = "SQLiteDatabase.db";

    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_isfavorite = "isFavourite";
    private SQLiteDatabase database;
    private DecimalFormat decimalFormat = new DecimalFormat("#.#");

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

    public ArrayList<MovieModel.ResultsBean> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<MovieModel.ResultsBean> contacts = new ArrayList<MovieModel.ResultsBean>();
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
        ArrayList<MovieModel.ResultsBean> contacts = new ArrayList<MovieModel.ResultsBean>();
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
                contacts.add(resultsBean);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecord(MovieModel.ResultsBean movieModel, boolean isfav) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_backdrop_path, movieModel.getBackdrop_path());
        contentValues.put(COLUMN_id, movieModel.getId());
        contentValues.put(COLUMN_isfavorite, isfav);
        contentValues.put(COLUMN_overview, movieModel.getOverview());
        contentValues.put(COLUMN_poster_path, movieModel.getPoster_path());
        contentValues.put(COLUMN_release_date, movieModel.getRelease_date());
        contentValues.put(COLUMN_title, movieModel.getTitle());
        contentValues.put(COLUMN_vote_average, movieModel.getVote_average());
        if (database.insert(TABLE_NAME, null, contentValues) == -1) {
            database.update(TABLE_NAME, contentValues, COLUMN_id + "=" + movieModel.getId(), null);
        }
        database.close();
    }

    public void insertAllPopularRecord(List<MovieModel.ResultsBean> movieModelResultsBeans) {
        database = this.getReadableDatabase();

        for (MovieModel.ResultsBean movieModel : movieModelResultsBeans) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_backdrop_path, movieModel.getBackdrop_path());
            contentValues.put(COLUMN_id, movieModel.getId());
            contentValues.put(COLUMN_overview, movieModel.getOverview());
            contentValues.put(COLUMN_poster_path, movieModel.getPoster_path());
            contentValues.put(COLUMN_release_date, movieModel.getRelease_date());
            contentValues.put(COLUMN_title, movieModel.getTitle());
            contentValues.put(COLUMN_vote_average, movieModel.getVote_average());
            database.insert(TABLE_NAME, null, contentValues);
        }

        database.close();
    }


    public void deleteRecord(int id) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, COLUMN_id + " = ?", new String[]{id + ""});
        database.close();
    }


}