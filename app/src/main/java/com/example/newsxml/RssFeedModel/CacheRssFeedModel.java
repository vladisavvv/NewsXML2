package com.example.newsxml.RssFeedModel;

public class CacheRssFeedModel extends RssFeedModelAbstract {
    private final String title;
    private final String description;

    public CacheRssFeedModel(final String title,
                             final String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
