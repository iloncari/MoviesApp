package apps.igor.movies.activities;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    private TextView tv_tryAgain;
    private TextView tv_internetConnection;

    private android.support.v7.widget.Toolbar toolbar;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Adapter topRatedMovieadapter;
    private Adapter mostPopularMovieAdapter;
    private Context context;
    private ArrayList<Movie> topRatedmovieList;
    private ArrayList<Movie> mostPopularMovieList;

    //page number that function loadTopRated/MostPopular is fetching
    private int topRatedPageNumber = 1;
    private int mostPopularPageNumber = 1;

    //variables that are used in onScrollListener
    private boolean isScrolling = false;
    private int visibleItem;
    private int totalItem;
    private int scrolledOutItem;

    //which mvies are showing at the moment
    private boolean isTopRadetShowing = false;
    private boolean isMostPopularShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if connection is avilable, loading starts
        tv_tryAgain = (TextView)findViewById(R.id.tv_try_again);
        tv_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topRatedPageNumber = 1;
                topRatedMovieadapter = null;
                mostPopularPageNumber = 1;
                mostPopularMovieAdapter = null;
                loadTopRatedMovies(topRatedPageNumber);
            }
        });

        tv_internetConnection = (TextView)findViewById(R.id.tv_not_connected);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

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
                    if(isMostPopularShowing)
                        loadMostPopularMovies(++mostPopularPageNumber);
                    else if(isTopRadetShowing)
                        loadTopRatedMovies(++topRatedPageNumber);
                    isScrolling = false;
                }
            }
        });

         context = this;
         topRatedmovieList = new ArrayList<>();
         mostPopularMovieList = new ArrayList<>();
         //default: first load top rated movies
         loadTopRatedMovies(topRatedPageNumber);


    }

    //showing menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
       return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuMostPoular:
                mostPopularMovieAdapter = null;
                mostPopularPageNumber = 1;
                topRatedPageNumber = 1;
                topRatedMovieadapter = null;
                loadMostPopularMovies(mostPopularPageNumber);
                return true;
            case R.id.menuTopRated:
                mostPopularMovieAdapter = null;
                mostPopularPageNumber = 1;
                topRatedPageNumber = 1;
                topRatedMovieadapter = null;
                loadTopRatedMovies(topRatedPageNumber);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //check is Internet Connection Avilable
    protected boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            tv_tryAgain.setVisibility(View.GONE);
            tv_internetConnection.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if(isTopRadetShowing)
                toolbar.setSubtitle("Top rated movies");
            else  if(isMostPopularShowing)
                toolbar.setSubtitle("Most popular movies");
            return true;
        }else{
            tv_tryAgain.setVisibility(View.VISIBLE);
            tv_internetConnection.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            toolbar.setSubtitle("");
            return false;
        }
    }

    //method which load Top rated movies using Retrofit API Call
    public void loadTopRatedMovies(final int pageNumber){
        isTopRadetShowing = true;
        isMostPopularShowing = false;
        mostPopularMovieList.clear();
        mostPopularPageNumber = 1;
        mostPopularMovieAdapter = null;

        if(isConnectedToInternet()){
            Call<MoviesResponse> call = RetrofitClient.getRetrofitClient().getAPI().getTopRatedMovies(Constants.MOVIEDB_KEY, pageNumber);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (isResponseCodeOK(response.code())) {
                        if(topRatedMovieadapter == null ){
                            topRatedmovieList.addAll(response.body().getResults());
                            topRatedMovieadapter = new Adapter(topRatedmovieList, context);
                            topRatedMovieadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(topRatedMovieadapter);
                        }else {
                            topRatedMovieadapter.addMoviesToList(response.body().getResults());
                            topRatedMovieadapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //method which load Most popular movies using Retrofit API Call
    public void loadMostPopularMovies(final int pageNumber){
        isTopRadetShowing = false;
        isMostPopularShowing = true;
        topRatedmovieList.clear();
        topRatedPageNumber = 1;
        topRatedMovieadapter = null;
        if(isConnectedToInternet()){
            Call<MoviesResponse> call = RetrofitClient.getRetrofitClient().getAPI().getPopularMovies(Constants.MOVIEDB_KEY, pageNumber);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (isResponseCodeOK(response.code())) {
                        if(mostPopularMovieAdapter == null ){
                            mostPopularMovieList.addAll(response.body().getResults());
                            mostPopularMovieAdapter = new Adapter(mostPopularMovieList, context);
                            mostPopularMovieAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(mostPopularMovieAdapter);
                        }else {
                            mostPopularMovieAdapter.addMoviesToList(response.body().getResults());
                            mostPopularMovieAdapter.notifyDataSetChanged();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //check is response from api OK
    public boolean isResponseCodeOK(Integer resoponse_code){
        if(resoponse_code == 401){
            Toast.makeText(getApplicationContext(), "Invalid API key!", Toast.LENGTH_LONG).show();
        }else if(resoponse_code == 404){
            Toast.makeText(getApplicationContext(), "The resource you requested could not be found.", Toast.LENGTH_LONG).show();
        }else if(resoponse_code == 200){
            return true;
        }
        return false;
    }
}
