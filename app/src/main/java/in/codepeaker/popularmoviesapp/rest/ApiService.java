package in.codepeaker.popularmoviesapp.rest;

import in.codepeaker.popularmoviesapp.model.ReviewModel;
import in.codepeaker.popularmoviesapp.model.VideosModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by github.com/codepeaker on 20/11/17.
 */

public interface ApiService {

    @GET("{id}/videos")
    Call<VideosModel> callVideosModelCall(@Path("id") int id, @Query("api_key") String apikey);

    @GET("{id}/reviews")
    Call<ReviewModel> callReviewModel(@Path("id") int id, @Query("api_key") String apikey);

}
