package com.example.newsxml.RssFeedModel;

public class CacheRssFeedModel extends RssFeedModelAbstract {
    private final String pathToTitle;
    private final String pathToHtml;
    private final String pathToImage;
    private final String pathToDescription;

    public CacheRssFeedModel(final String pathToTitle,
                             final String pathToHtml,
                             final String pathToImage,
                             final String pathToDescription) {
        this.pathToTitle = pathToTitle;
        this.pathToHtml = pathToHtml;
        this.pathToImage = pathToImage;
        this.pathToDescription = pathToDescription;
    }

    public String getPathToTitle() {
        return pathToTitle;
    }

    public String getPathToHtml() {
        return pathToHtml;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public String getPathToDescription() {
        return pathToDescription;
    }
}
