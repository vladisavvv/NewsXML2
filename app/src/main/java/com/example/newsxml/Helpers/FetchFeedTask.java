package com.example.newsxml.Helpers;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsxml.R;
import com.example.newsxml.RssFeedModel.RssFeedModelAbstract;
import com.example.newsxml.activitys.MainActivity;
import com.example.newsxml.adapters.RssFeedListAdapter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {
    private String link;
    private final WeakReference<MainActivity> mainActivity;
    private List<RssFeedModelAbstract> mFeedModelList;
    private final WeakReference<SwipeRefreshLayout> swipeRefreshLayout;
    private final WeakReference<RecyclerView> mRecyclerView;

    public FetchFeedTask(final MainActivity mainActivity,
                         final String link) {
        this.link = link;
        this.mainActivity = new WeakReference<>(mainActivity);
        this.swipeRefreshLayout = new WeakReference<>((SwipeRefreshLayout) mainActivity.findViewById(R.id.swipeRefreshLayout));
        this.mRecyclerView = new WeakReference<>((RecyclerView) mainActivity.findViewById(R.id.recyclerView));
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (TextUtils.isEmpty(link))
            return false;

        try {
            if (!link.startsWith("http://") && !link.startsWith("https://"))
                link = "https://" + link;

            final URL url = new URL(link);

            try {
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = FeedParsers.parseFeed(inputStream, url, false);
            } catch (UnknownHostException | XmlPullParserException exc) { // TODO
                final String root = Environment.getExternalStorageDirectory().toString();

                File file = new File(root, "xml.xml");
                InputStream inputStream = new FileInputStream(file);

                mFeedModelList = FeedParsers.parseFeed(inputStream, null, true);
            }

            return true;
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        swipeRefreshLayout.get().setRefreshing(false);

        if (success) {
            mRecyclerView.get().setAdapter(new RssFeedListAdapter(mFeedModelList, mainActivity.get().getBaseContext()));
        } else {
            Toast.makeText(mainActivity.get(),
                    "Enter a valid Rss feed url",
                    Toast.LENGTH_LONG).show();
        }
    }
}
