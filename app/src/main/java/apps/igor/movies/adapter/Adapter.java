package apps.igor.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import apps.igor.movies.R;
import apps.igor.movies.activities.MovieDetailsActivity;
import apps.igor.movies.constants.Constants;
import apps.igor.movies.models.Movie;

public class Adapter extends RecyclerView.Adapter<Adapter.VHolder> {
    private List<Movie> moviesData = new ArrayList<>();
    private Context context;

    public Adapter(List<Movie> movies, Context context) {
        this.moviesData.addAll(movies);
        this.context = context;
    }

    //adding movies to list when user want to load more items
    public void addMoviesToList(List<Movie> newMovies){
        moviesData.addAll(newMovies);
    }

    public Movie getMovie(int position){
        return moviesData.get(position);
    }

    @Override
    public int getItemCount() {
        return moviesData.size();
    }

    @NonNull
    @Override
    public Adapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item_movie1, parent, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.VHolder holder, final int position) {
        Movie movie = moviesData.get(position);

        holder.voteTextView.setText(movie.getVoteAverage() + " (" + movie.getVoteCount() + " votes)");
        holder.titleTextView.setText(movie.getTitle());

        if(movie.getPosterPath() != null)
            Glide.with(context).load(Constants.MOVIEDB_IMAGES_BASE_URL + movie.getPosterPath()).into(holder.posterImageView);
        else
            Glide.with(context).load(Constants.IMAGE_PLACEHOLDER_POSTER_BASE_URL).into(holder.posterImageView);

        //when user click on some movie item, OnClickListener open activty with movie details
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("MOVIE_DETAIL_OBJECT", getMovie(position));
                context.startActivity(intent);
            }
        });
    }

    class VHolder extends RecyclerView.ViewHolder{
        TextView popularityTextView;
        TextView titleTextView;
        TextView voteTextView;
        ImageView posterImageView;
        LinearLayout linearLayout;

        public VHolder(View itemView) {
            super(itemView);
            voteTextView = (TextView)itemView.findViewById(R.id.textViewVoteAverage);
            titleTextView = (TextView)itemView.findViewById(R.id.textViewTitleItem);
            posterImageView = (ImageView)itemView.findViewById(R.id.imageView);
           linearLayout = (LinearLayout)itemView.findViewById(R.id.linLayoutItem);
        }
    }
}
