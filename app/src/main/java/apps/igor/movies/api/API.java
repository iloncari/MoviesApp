package apps.igor.movies.api;

import apps.igor.movies.models.DetailResponse;
import apps.igor.movies.models.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") Integer page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("page") Integer page
    );

    @GET("movie/{movie_id}")
    Call<DetailResponse> getMovieDetails(
            @Path("movie_id") Integer movieId,
            @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/similar")
    Call<MoviesResponse> getSimilarMovies(
            @Path("movie_id") Integer id,
            @Query("api_key") String apiKey,
            @Query("page") Integer page
    );
}
