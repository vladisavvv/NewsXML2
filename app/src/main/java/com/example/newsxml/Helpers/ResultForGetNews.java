package com.example.newsxml.Helpers;

import android.graphics.Bitmap;

public class ResultForGetNews {
    private String title;
    private String description;
    private Bitmap imageBitmap;
    private String html;

    public ResultForGetNews() {}

    ResultForGetNews(final String title,
                     final String description,
                     final Bitmap imageBitmap) {
        this.title = title;
        this.description = description;
        this.imageBitmap = imageBitmap;
        this.html = null;
    }

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
