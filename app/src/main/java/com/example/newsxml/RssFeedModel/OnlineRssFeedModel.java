package com.example.newsxml.RssFeedModel;

public class OnlineRssFeedModel extends RssFeedModelAbstract {
    private String title;
    private String link;
    private String linkToImage;
    private String description;

    public OnlineRssFeedModel(final String title,
                              final String link,
                              final String description,
                              final String linkToImage) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.linkToImage = linkToImage;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLinkToImage() {
        return linkToImage;
    }
}
