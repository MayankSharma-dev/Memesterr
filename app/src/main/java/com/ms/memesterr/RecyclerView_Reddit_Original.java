package com.ms.memesterr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerView_Reddit_Original extends RecyclerView.Adapter<RecyclerView_Reddit_Original.ViewHolder> {

    ArrayList<Model_Reddit> modelReddits = new ArrayList<>();
    Context context;


    public RecyclerView_Reddit_Original(ArrayList<Model_Reddit> modelReddit, Context context) {
        this.modelReddits = modelReddit;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.reddit_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Model_Reddit currentData = modelReddits.get(position);

        Glide.with(context)
                .asBitmap()
                .load(currentData.getUrl())
                .into(holder.reddit_post);

        holder.content.setText(currentData.getTitle());
        holder.upvote.setText(currentData.getUps());
        holder.reddit_user.setText("/r/"+currentData.getSubreddit());
        holder.author.setText(currentData.getAuthor());

    }

    @Override
    public int getItemCount() {
        return modelReddits.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView reddit_post,share,download;
        TextView reddit_user,content,upvote,author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            reddit_post = itemView.findViewById(R.id.post_img);
            share = itemView.findViewById(R.id.share_reddit);
            download =  itemView.findViewById(R.id.download_reddit);

            reddit_user= itemView.findViewById(R.id.subreddit);
            content = itemView.findViewById(R.id.title_reddit);
            upvote = itemView.findViewById(R.id.upvotes);

        }
    }


}
