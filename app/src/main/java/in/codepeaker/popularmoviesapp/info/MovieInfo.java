package in.codepeaker.popularmoviesapp.info;

/**
 * Created by root on 10/11/17.
 */

public class MovieInfo {
    public String overview;
    public String release_date;
    public String backdrop_path;
    public boolean video;
    public double vote_average;
    public String title;
    public String poster_path;

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
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

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
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

    @Override

    public String toString() {
        return "MovieInfo{" +
                "overview='" + overview + '\'' +
                ", release_date='" + release_date + '\'' +
                ", video=" + video +
                ", vote_average=" + vote_average +
                ", title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                '}';
    }
}
