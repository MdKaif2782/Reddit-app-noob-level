package com.example.reddit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.reddit.MainActivity;
import com.example.reddit.R;
import com.example.reddit.objexts.Post;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {
    Context context;
    int resource;
    ArrayList<Post> posts;
    public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.posts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        TextView title = convertView.findViewById(R.id.postTitle);
        TextView author = convertView.findViewById(R.id.postSubtitle);
        ImageView image = convertView.findViewById(R.id.postImage);

        Button button = convertView.findViewById(R.id.upvote_button);
        Button comment = convertView.findViewById(R.id.comment_button);

        title.setText(posts.get(position).getTitle());
        author.setText("Posted by u/"+posts.get(position).getAuthor()+" in r/"+posts.get(position).getSubreddit());
        Glide.with(context).load(posts.get(position).getUrl())
                .optionalFitCenter()
                        .into(image);
        button.setText(String.valueOf(posts.get(position).getScore()));
        comment.setText(String.valueOf(posts.get(position).getNum_comments()));

        return convertView;
    }
}
