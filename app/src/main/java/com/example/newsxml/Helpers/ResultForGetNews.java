package com.example.newsxml.Helpers;

import android.graphics.Bitmap;

public class ResultForGetNews {
    private String title;
    private String description;
    private Bitmap imageBitmap;
    private String html;

    public ResultForGetNews() {}
    
    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getHtml() {
        return html;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    void setHtml(String html) {
        this.html = html;
    }
}
