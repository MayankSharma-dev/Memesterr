package com.ms.memesterr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Bookmarks_Adapter extends RecyclerView.Adapter<Bookmarks_Adapter.ViewHolder> {

    ArrayList<ImageModel> imageModels = new ArrayList<>();
    Context context;

    Bookmark_Interface bookmark_interface;



    public Bookmarks_Adapter(Bookmarks bookmarks, ArrayList<ImageModel> imageModels) {
        context = bookmarks;
        this.imageModels = imageModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bookmarks_list, parent, false);

        return new ViewHolder(view,bookmark_interface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageModel current = imageModels.get(position);
        holder.textView.setText(current.getCaption());
        try {
            byte[] encodeByte = Base64.decode(current.getStringImg(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            holder.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.d("BookmarksAdapter",""+e);
            e.getMessage();
        }

    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public void setOnItemClicked(Bookmark_Interface bookmark_interface) {
        this.bookmark_interface=bookmark_interface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView,download_bookmark,delete;
        TextView textView;

        public ViewHolder(@NonNull View itemView,Bookmark_Interface bookmarkInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bookmark_img);
            textView = itemView.findViewById(R.id.bookmark_title);
            download_bookmark = itemView.findViewById(R.id.bookmark_download);
            delete = itemView.findViewById(R.id.bookmark_delete);

            download_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(bookmarkInterface!=null){
                        int position =  getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            ImageView cache = imageView;
                            cache.buildDrawingCache();
                            Bitmap bitmap = cache.getDrawingCache();
                            bookmarkInterface.onItemClick(bitmap);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(bookmarkInterface!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            bookmarkInterface.itemPosition(position);
                        }
                    }
                }
            });

        }
    }


}
