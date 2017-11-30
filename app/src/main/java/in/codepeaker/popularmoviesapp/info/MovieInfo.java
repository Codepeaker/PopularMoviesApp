package in.codepeaker.popularmoviesapp.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 10/11/17.
 */

public class MovieInfo implements Parcelable {

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
    public String overview;
    public String release_date;
    public String backdrop_path;
    public double vote_average;
    public String title;
    public String poster_path;
    public int id;
    public boolean isFav;

    public MovieInfo() {
    }

    private MovieInfo(Parcel in) {
        overview = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readDouble();
        title = in.readString();
        poster_path = in.readString();
        id = in.readInt();
        isFav = in.readByte() != 0;
    }

    public static Creator<MovieInfo> getCREATOR() {
        return CREATOR;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(backdrop_path);
        dest.writeDouble(vote_average);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeInt(id);
        dest.writeByte((byte) (isFav ? 1 : 0));
    }
}
