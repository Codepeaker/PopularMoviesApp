package in.codepeaker.popularmoviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.codepeaker.popularmoviesapp.R;
import in.codepeaker.popularmoviesapp.activities.DetailsActivity;
import in.codepeaker.popularmoviesapp.constants.Constants;
import in.codepeaker.popularmoviesapp.info.MovieInfo;

/**
 * Created by root on 10/11/17.
 */

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.MovieViewHolder> {

    private final List<MovieInfo> movieInfoList;
    private final Context context;


    public MovieListRecyclerViewAdapter(Context context, List<MovieInfo> movieInfoList) {
        this.movieInfoList = movieInfoList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.movieTitle.setText(movieInfoList.get(position).title);
        Picasso.with(context)
                .load(Constants.imageUrl + movieInfoList.get(position).poster_path)
                .placeholder(R.drawable.moviepic)
                .into(holder.imageView);
        holder.ratingBar.setRating((float) movieInfoList.get(position).vote_average/2);

    }

    @Override
    public int getItemCount() {
        return movieInfoList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;
        final TextView movieTitle;
        final RatingBar ratingBar;

        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_thumbnail_id);
            imageView.setOnClickListener(this);

            movieTitle = itemView.findViewById(R.id.movie_title_id);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.movie_thumbnail_id) {
                Intent todetailIntent = new Intent(context, DetailsActivity.class);
                MovieInfo movieInfo = (movieInfoList.get(getAdapterPosition()));
                todetailIntent.putExtra(Constants.selectedMovie, movieInfo);
                context.startActivity(todetailIntent);
            }

        }
    }
}
