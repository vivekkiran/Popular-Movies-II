package in.vivek.popularmovies.api;

import android.support.annotation.StringDef;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vivek on 16/4/16.
 */
public interface MovieDbOrgApiService {

    String API_KEY = "API_KEY_HERE";
    String API_BASE_URL = "http://api.themoviedb.org/3/";
    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    String SORT_BY_POPULAR = "popular";
    String SORT_BY_TOP_RATED = "top_rated";
    String SORT_BY_NOW_PLAYING = "now_playing";
    String SORT_BY_UPCOMING = "upcoming";
    //Only for offline but a lot of place is using this StringDef like SharedPreference
    String SORT_BY_FAVORITE = "favorite";

    @StringDef({SORT_BY_POPULAR, SORT_BY_TOP_RATED, SORT_BY_NOW_PLAYING, SORT_BY_UPCOMING, SORT_BY_FAVORITE})
    @interface SORT_BY {
    }

    //Provides List of Movies Sorted by Some Category
    @GET("movie/{sort_by}")
    Call<MovieServiceResponse> movieList(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    //Provides List of Movies on some query
    //Without Search Feature App feels incomplete
    @GET("search/movie")
    Call<MovieServiceResponse> searchResult(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );

    //Provides Movie Trailer and other video clips related to movie
    @GET("movie/{movie_id}/videos")
    Call<VideoServiceResponse> videoList(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    //Provides User Reviews
    @GET("movie/{movie_id}/reviews")
    Call<ReviewServiceResponse> reviewList(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

}
