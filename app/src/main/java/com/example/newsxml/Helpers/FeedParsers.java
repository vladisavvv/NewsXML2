package com.example.newsxml.Helpers;

import android.util.Xml;

import com.example.newsxml.RssFeedModel.CacheRssFeedModel;
import com.example.newsxml.RssFeedModel.OnlineRssFeedModel;
import com.example.newsxml.RssFeedModel.RssFeedModelAbstract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FeedParsers {
    public static List<RssFeedModelAbstract> cacheParseFeed() {
        final List<RssFeedModelAbstract> items = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            final RssFeedModelAbstract item = new CacheRssFeedModel(
                    "title" + i + ".txt",
                    "cache" + i + ".html",
                    "image" + i + ".jpg",
                    "description" + i + ".txt"
            );

            items.add(item);
        }

        return items;
    }

    public static List<RssFeedModelAbstract> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        String linkToImage = null;
        boolean isItem = false;
        final List<RssFeedModelAbstract> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();

                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                    if (name.equalsIgnoreCase("enclosure")) {
                        linkToImage = xmlPullParser.getAttributeValue(null, "url");
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null && linkToImage != null) {
                    if (isItem)
                        items.add(new OnlineRssFeedModel(title, link, description, linkToImage));

                    title = null;
                    link = null;
                    description = null;
                    linkToImage = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
}
