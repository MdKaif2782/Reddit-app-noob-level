package com.example.reddit.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.reddit.R;
import com.example.reddit.objexts.Post;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostAdpaterRecycler extends RecyclerView.Adapter<PostAdpaterRecycler.ViewHolder> {
    Context context;
    ArrayList<Post> posts;

    public PostAdpaterRecycler(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_interface_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdpaterRecycler.ViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.author.setText("Posted by u/"+posts.get(position).getAuthor()+" in r/"+posts.get(position).getSubreddit());
        if (posts.get(position).getImageUrl() != null) {
            holder.image.requestLayout();
            //drawable thumbnail from async task
            final Drawable[] placeholederss = {null};
            Glide.with(context)
                    .asBitmap()
                    .load(posts.get(position).getThumbnail())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            placeholederss[0] = placeholder;
                        }
                    });


            try {
               int screenWidth = posts.get(position).getScreenWidth();
               int height = (screenWidth*posts.get(position).getHeight())/posts.get(position).getWidth();
                holder.image.getLayoutParams().height = height;
                holder.image.requestLayout();

                //CircularProgressDrawable
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
                circularProgressDrawable.setStrokeWidth(12f);
                circularProgressDrawable.setColorFilter(context.getResources().getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                circularProgressDrawable.setCenterRadius(60f);
                circularProgressDrawable.start();

                Glide.with(context).load(posts.get(position).getImageUrl())
                        .optionalFitCenter()
                        .placeholder(circularProgressDrawable)
                        .into(holder.image);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        holder.score.setText(String.valueOf(posts.get(position).getScore()));
        holder.comments.setText(String.valueOf(posts.get(position).getNum_comments()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public Button score;
        public Button comments;
        public ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.postTitle);
            author = itemView.findViewById(R.id.postSubtitle);
            score = itemView.findViewById(R.id.upvote_button);
            comments = itemView.findViewById(R.id.comment_button);
            image = itemView.findViewById(R.id.postImage);
        }

    }

}

