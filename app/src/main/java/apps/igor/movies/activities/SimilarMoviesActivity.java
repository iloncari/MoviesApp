package apps.igor.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;
import apps.igor.movies.models.Movie;

import java.util.ArrayList;

import apps.igor.movies.R;
import apps.igor.movies.adapter.Adapter;
import apps.igor.movies.api.RetrofitClient;
import apps.igor.movies.constants.Constants;
import apps.igor.movies.models.Movie;
import apps.igor.movies.models.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarMoviesActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Adapter similarMoviesAdapter;
    private Context context;
    private ArrayList<Movie> similarMovies;

    private int similarMoviesPageNumber = 1;

    //variables that are used in onScrollListener
    private boolean isScrolling = false;
    private int visibleItem;
    private int totalItem;
    private int scrolledOutItem;

    private int similarMovieId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_movies);


        similarMovieId = getIntent().getExtras().getInt("SIMILAR_MOVIE_ID");
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewSimilar);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //on Scroll listener detect when users scrolled to the end and then loading new movies
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState ==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true;
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItem = layoutManager.getChildCount();
                scrolledOutItem = layoutManager.findFirstVisibleItemPosition();
                totalItem = layoutManager.getItemCount();
                if(!isScrolling && ((visibleItem+scrolledOutItem) == totalItem)){
                    loadSimilarMovies(++similarMoviesPageNumber);
                    isScrolling = false;
                }
            }
        });

        context = this;
        similarMovies = new ArrayList<>();
        loadSimilarMovies(similarMoviesPageNumber);
    }


    public void loadSimilarMovies(int pageNumber){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            Call<MoviesResponse> call = RetrofitClient.getRetrofitClient().getAPI().getSimilarMovies(similarMovieId, Constants.MOVIEDB_KEY,pageNumber );
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response.code() == 200) {
                        if(response.body().getResults().size() == 0) {
                            Toast.makeText(context, "There is no similar movies!", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        if(similarMoviesAdapter == null ){
                            similarMovies.addAll(response.body().getResults());
                            similarMoviesAdapter = new Adapter(similarMovies, context);
                            similarMoviesAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(similarMoviesAdapter);
                            toolbar.setSubtitle("Similar movies");
                        }else {
                            similarMoviesAdapter.addMoviesToList(response.body().getResults());
                            similarMoviesAdapter.notifyDataSetChanged();
                        }
                    }else if(response.code() == 401){
                        Toast.makeText(getApplicationContext(), "Invalid API key!", Toast.LENGTH_LONG).show();
                    }else if(response.code() == 404){
                        Toast.makeText(getApplicationContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
