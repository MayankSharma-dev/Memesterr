package com.ms.memesterr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.player.autoplayer.AutoPlayer;

import java.util.ArrayList;

public class RecyclerView_Reddit extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model_Reddit> modelReddit = new ArrayList<>();
    private final Context context;

    private Reddit_Interface redditInterface;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final int VIEW_TYPE_VIDEO = 2;

    public RecyclerView_Reddit(ArrayList<Model_Reddit> modelReddit, Context context) {
        this.modelReddit = modelReddit;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reddit_list, parent, false);
            return new ImageViewHolder(view, redditInterface);
        } else if (viewType == VIEW_TYPE_VIDEO) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.reddit_list_video,parent,false);
            return new VideoItemHolder(view,redditInterface);

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImageViewHolder) {

            populateImageRows((ImageViewHolder) holder, position);
        }
        else if(holder instanceof VideoItemHolder){
            populateVideoRows((VideoItemHolder) holder,position);
        }
        else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }


    @Override
    public int getItemCount() {
        return modelReddit == null ? 0 : modelReddit.size();
//        return modelReddit.size();
    }

    @Override
    public int getItemViewType(int position) {


//        if (modelReddit.get(position).getType() == 1) {
//            return VIEW_TYPE_ITEM;
//        } else if (modelReddit.get(position).getType() == 2) {
//            return VIEW_TYPE_VIDEO;
//        } else{
//            return VIEW_TYPE_LOADING;
//        }

        switch (modelReddit.get(position).getType()){
            case Model_Reddit.IMAGE_TYPE:
                return VIEW_TYPE_ITEM;
            case Model_Reddit.VIDEO_TYPE:
                return VIEW_TYPE_VIDEO;
            default:
                return VIEW_TYPE_LOADING;
        }

//        return modelReddit.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView reddit_post,share,download,bookmark;
        TextView reddit_user,content,upvote,author;

        public ImageViewHolder(@NonNull View itemView , Reddit_Interface listener) {
            super(itemView);

            author = itemView.findViewById(R.id.author);
            reddit_post = itemView.findViewById(R.id.post_img);
            share = itemView.findViewById(R.id.share_reddit);
            download =  itemView.findViewById(R.id.download_reddit);

            reddit_user= itemView.findViewById(R.id.subreddit);
            content = itemView.findViewById(R.id.title_reddit);
            upvote = itemView.findViewById(R.id.upvotes);
            bookmark = itemView.findViewById(R.id.bookmar_reddit);


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener!=null){
                        if(position != RecyclerView.NO_POSITION){
                            ImageView cache = reddit_post;
                            cache.buildDrawingCache();
                            Bitmap bitmap = cache.getDrawingCache();
                            listener.onItemClick_R(bitmap);
                        }
                    }
                }
            });


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener!= null){
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemPosition_R(position);
                        }
                    }
                }
            });

            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            ImageView cache = reddit_post;
                            cache.buildDrawingCache();
                            Bitmap bitmap = cache.getDrawingCache();
                            listener.onItemBulk_R(position,bitmap);
                        }
                    }
                }
            });

        }
    }


    public static class VideoItemHolder extends RecyclerView.ViewHolder {

        ImageView share;
        AutoPlayer autoPlayer;
        TextView reddit_user,content,upvote,author;

        ImageView image;

        public VideoItemHolder(@NonNull View itemView, Reddit_Interface listener) {
            super(itemView);

            image = itemView.findViewById(R.id.video_placeholder);

            author = itemView.findViewById(R.id.author);
            autoPlayer = itemView.findViewById(R.id.post_video);
            share = itemView.findViewById(R.id.share_reddit);

            reddit_user= itemView.findViewById(R.id.subreddit);
            content = itemView.findViewById(R.id.title_reddit);
            upvote = itemView.findViewById(R.id.upvotes);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener!= null){
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemPosition_R(position);
                        }
                    }
                }
            });

        }
    }


    @SuppressLint("SetTextI18n")
    private void populateImageRows(ImageViewHolder holder, int position) {

        Model_Reddit currentData = modelReddit.get(position);

        Glide.with(context)
                .asBitmap()
                .load(currentData.getUrl())
                .placeholder(R.drawable.baseline_image_24)
                .into(holder.reddit_post);

        holder.content.setText(currentData.getTitle());
        holder.upvote.setText(currentData.getUps());
        holder.reddit_user.setText("r/"+currentData.getSubreddit());
        holder.author.setText(currentData.getAuthor());

    }


    @SuppressLint("SetTextI18n")
    private void populateVideoRows(VideoItemHolder holder, int position){
        Model_Reddit currentData = modelReddit.get(position);

        Glide.with(context)
                .asBitmap()
                .load(currentData.getImage_previews())
                .into(holder.image);

        holder.autoPlayer.setUrl(currentData.getUrl()+"/DASH_480.mp4");
        holder.autoPlayer.setAnimationTime(500);
        holder.autoPlayer.setPlaceholderView((holder).image);

        holder.content.setText(currentData.getTitle());
        holder.upvote.setText(currentData.getUps());
        holder.reddit_user.setText("r/"+currentData.getSubreddit());
        holder.author.setText(currentData.getAuthor());

    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemLoading);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    public void setOnItemClickListener(Reddit_Interface redditInterface1){
        redditInterface = redditInterface1;
    }

}
