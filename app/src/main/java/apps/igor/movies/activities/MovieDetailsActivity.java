
package apps.igor.movies.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import apps.igor.movies.R;
import apps.igor.movies.api.RetrofitClient;
import apps.igor.movies.constants.Constants;
import apps.igor.movies.models.DetailResponse;
import apps.igor.movies.models.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tv_title;
    private TextView tv_relase_date;
    private TextView tv_genres;
    private TextView tv_overview;
    private TextView tv_popuarity;
    private TextView tv_votes;
    private TextView tv_link;
    private TextView tv_production_country;

    private ImageView iv_posterImage;
    private ImageView iv_coverImage;

    private Movie movie;

    private TextView tv_loadSimilarMovies;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setSubtitle("Movie details");

        movie = (Movie)getIntent().getExtras().getSerializable("MOVIE_DETAIL_OBJECT");

        tv_title = (TextView)findViewById(R.id.textViewTitle);
        tv_title.setText(movie.getTitle());

        tv_relase_date = (TextView)findViewById(R.id.textViewReleaseDate);
        tv_relase_date.setText("Released on " + movie.getReleaseDate());

        tv_overview = (TextView)findViewById(R.id.textViewOverview);
        if(movie.getOverview() != null)
            tv_overview.setText(movie.getOverview());
        else
            tv_overview.setVisibility(View.GONE);

        tv_link = (TextView)findViewById(R.id.textViewHomepage);
        tv_genres = (TextView)findViewById(R.id.textViewGenres);
        tv_production_country = (TextView)findViewById(R.id.textViewCountry);
        loadMoviesDetails();

        iv_coverImage = (ImageView)findViewById(R.id.imageViewCover);
        iv_coverImage.setScaleType(ImageView.ScaleType.FIT_XY);
        //if response return null, load placeholder
        if(movie.getCoverPath() != null)
          Glide.with(getApplicationContext()).load(Constants.MOVIEDB_IMAGES_BASE_URL + movie.getCoverPath()).into(iv_coverImage);
        else
            Glide.with(getApplicationContext()).load(Constants.IMAGE_PLACEHOLDER_COVER_BASE_URL).into(iv_posterImage);

        iv_posterImage = (ImageView)findViewById(R.id.imageViewPoster);
        if(movie.getPosterPath() != null)
            Glide.with(getApplicationContext()).load(Constants.MOVIEDB_IMAGES_BASE_URL + movie.getPosterPath()).into(iv_posterImage);
        else
            Glide.with(getApplicationContext()).load(Constants.IMAGE_PLACEHOLDER_POSTER_BASE_URL).into(iv_posterImage);

        tv_votes = (TextView)findViewById(R.id.textVoteAverage);
        tv_votes.setText(String.valueOf(movie.getVoteAverage())+ " ("+ (String.valueOf(movie.getVoteCount())) +" votes)");

        tv_popuarity = (TextView)findViewById(R.id.textViewPopularity);
        tv_popuarity.setText(String.valueOf(movie.getPopularity()));

        context = this;

        tv_loadSimilarMovies = (TextView)findViewById(R.id.loadSimilarMovies);
        tv_loadSimilarMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SimilarMoviesActivity.class);
                intent.putExtra("SIMILAR_MOVIE_ID", movie.getId());
                startActivity(intent);
            }
        });
    }

    public void loadMoviesDetails(){
        Call<DetailResponse> callDetails = RetrofitClient.getRetrofitClient().getAPI().getMovieDetails(movie.getId(), Constants.MOVIEDB_KEY);
        callDetails.enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                         if (response.code() == 200) {
                             System.out.println(response.body().getGenresList().size() + " " + response.body().getGenresList());
                                tv_genres.setText(response.body().getGenresNames().toString().replace("[", "").replace("]", ""));

                                if(response.body().getHomepage() != null) {
                                    tv_link.setText(response.body().getHomepage());
                                    tv_link.setMovementMethod(LinkMovementMethod.getInstance());
                                }else
                                     tv_link.setVisibility(View.GONE);
                                     tv_production_country.setText(response.body().getProductionCountriesName().toString().replace("[", "").replace("]", ""));
                         }
                        else if (response.code() == 401)
                            Toast.makeText(getApplicationContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_LONG).show();
                        else if (response.code() == 404)
                            Toast.makeText(getApplicationContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}
