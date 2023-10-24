package com.ms.memesterr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageModel {
    private String stringImg;


    private String id;
    private String caption;

    public ImageModel(){}

    public ImageModel(String stringImg, String id, String caption) {
        this.stringImg = stringImg;
        this.id = id;
        this.caption = caption;
    }

    public String getStringImg() {
        return stringImg;
    }

    public void setStringImg(String stringImg) {
        this.stringImg = stringImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    ///
//    Car current = cars.get(position);
//        holder.txt1.setText(current.getCar_make());
//        holder.txt2.setText(current.getCar_model());
//        try {
//        byte[] encodeByte = Base64.decode(current.getImage(), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        holder.imageView.setImageBitmap(bitmap);
//    } catch (Exception e) {
//        e.getMessage();
//    }
    ///


}
