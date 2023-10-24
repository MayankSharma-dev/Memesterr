package com.ms.memesterr;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class RecyclerVIew_Twitter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model_Twitter> modelTwitters = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Twitter_Interface twitterInterface;
    private Context context;

    public RecyclerVIew_Twitter(ArrayList<Model_Twitter> modelTwitters, Context context) {
        this.modelTwitters = modelTwitters;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_list,parent,false);
            return new ItemHolder(view,twitterInterface);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemHolder){
            populateItemHolder((RecyclerVIew_Twitter.ItemHolder) holder, position);
        }
        else if(holder instanceof LoadingHolder) {
            showLoadingView((RecyclerVIew_Twitter.LoadingHolder) holder, position);
        }

    }

    private void showLoadingView(LoadingHolder holder, int position) {
    }


    private void populateItemHolder(ItemHolder holder, int position) {

        Model_Twitter currentData = modelTwitters.get(position);
        //holder.image.layout(0,0,0,0)

        Glide.with(context)
                .asBitmap()
                .load(currentData.getPost_media_url())
                .into(holder.post_img);

        Glide.with(context)
                .asBitmap()
                .load(currentData.getPost_url_profile_url())
                .into(holder.user_img);



        holder.user_name.setText(currentData.getPost_user());
        holder.title.setText(currentData.getPost_text());
        holder.likes.setText(String.valueOf(currentData.getPost_likes()));
        holder.tweets.setText(String.valueOf(currentData.getPost_retweet()));

    }

    @Override
    public int getItemCount() {
        return modelTwitters == null ? 0 : modelTwitters.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modelTwitters.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        ShapeableImageView user_img,post_img;
        ImageView download_t,share_t,bookmark;
        TextView user_name, title, likes, tweets;

        public ItemHolder(@NonNull View itemView, Twitter_Interface twitter_interface) {
            super(itemView);

            user_img = itemView.findViewById(R.id.post_user_img);
            post_img = itemView.findViewById(R.id.post);

            user_name = itemView.findViewById(R.id.post_user);
            title = itemView.findViewById(R.id.title_twitter);
            likes = itemView.findViewById(R.id.tweet_likes);
            tweets =  itemView.findViewById(R.id.retweet);

            download_t = itemView.findViewById(R.id.download_tweet);
            share_t = itemView.findViewById(R.id.share_twitter);
            bookmark = itemView.findViewById(R.id.bookmark_twitter);

            download_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(twitter_interface!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            ShapeableImageView cache = post_img;
                            cache.buildDrawingCache();
                            Bitmap bitmap = cache.getDrawingCache();
                            twitter_interface.onItemClick_T(bitmap);
                        }
                    }

                }
            });

            share_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(twitter_interface!= null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            twitter_interface.onItemPosition_T(position);
                        }
                    }

                }
            });

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(twitter_interface!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            ShapeableImageView cache = post_img;
                            cache.buildDrawingCache();
                            Bitmap bitmap = cache.getDrawingCache();
                            twitter_interface.onItemBulk_T(position,bitmap);
                        }
                    }
                }
            });

        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemLoading);
        }
    }

    public void setOnItemClickedListener(Twitter_Interface twitterInterface){
        this.twitterInterface = twitterInterface;
    }

}
