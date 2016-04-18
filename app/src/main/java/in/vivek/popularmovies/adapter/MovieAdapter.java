package in.vivek.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.vivek.popularmovies.MainActivity;
import in.vivek.popularmovies.MovieDetailActivity;
import in.vivek.popularmovies.MovieDetailFragment;
import in.vivek.popularmovies.R;
import in.vivek.popularmovies.api.Movie;
import in.vivek.popularmovies.util.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by vivek on 16/4/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
        public static final int INITIAL_POSITION = 0;

        private final List<Movie> mValues;
        private MainActivity mMainActivity;

        public int lastClicked;

        public MovieAdapter(final MainActivity activity, List<Movie> items) {
            mValues = items;
            mMainActivity = activity;
            lastClicked = INITIAL_POSITION;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        public void clearDataSet(){
            mValues.clear();
            notifyDataSetChanged();
        }

        //When Sorting criteria is changed
        public void changeDataSet(List<Movie> items){
            //deleting old movies
            mValues.clear();
            lastClicked = INITIAL_POSITION;
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        //When LoadMore is is called
        public void addDataSet(List<Movie> items){
            mValues.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.position = position;

            holder.mTitle.setText(holder.mItem.getTitle());
            //Loading Image
            if (holder.mPosterPortrait == null) {
                holder.mTitle.setVisibility(View.VISIBLE);
            }

            Picasso.with(mMainActivity)
                    .load(Utility.getPortraitPosterUrl(mMainActivity, holder.mItem.getPosterPath()))
                    .placeholder(R.drawable.poster_portrait_placeholder)
                    .into(holder.mPosterPortrait,
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                    //Setting poster visible
                                    holder.mPosterPortrait.setVisibility(View.VISIBLE);
                                    holder.mPosterPortrait.setAlpha(1.0f);
                                    //Making title invisible
                                    holder.mTitle.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {
                                    //Setting poster not found image with opacity
                                    holder.mPosterPortrait.setImageDrawable(
                                            mMainActivity.getResources().getDrawable(R.drawable.poster_not_found));
                                    holder.mPosterPortrait.setAlpha(0.5f);
                                    //Making title visible
                                    holder.mTitle.setVisibility(View.VISIBLE);
                                }
                            }
                    );

            //For Two Pane View for first time setup
            if(mMainActivity.ismTwoPane() && position == this.lastClicked){
                holder.onClick(holder.mView);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public final View mView;
            public Movie mItem;
            public int position;
            @Bind(R.id.ivPosterPortrait) ImageView mPosterPortrait;
            @Bind(R.id.tvTitle) TextView mTitle;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this,view);
            }

            @OnClick(R.id.ivPosterPortrait)
            public void onClick(View v) {
                //Loading fragment in the MainActivity in Two Pane Mode and storing lastClicked
                if (mMainActivity.ismTwoPane()) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, mItem);
                    Fragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    mMainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                    lastClicked = position;
                } else {
                    //Starting Details Activity
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.ARG_MOVIE, mItem);
                    context.startActivity(intent);
                }
            }
        }
    }

