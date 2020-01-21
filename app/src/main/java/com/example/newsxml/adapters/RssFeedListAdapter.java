package com.example.newsxml.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsxml.Helpers.DownloadTask;
import com.example.newsxml.Helpers.ReadImageTask;
import com.example.newsxml.Helpers.ResultForGetNews;
import com.example.newsxml.R;
import com.example.newsxml.RssFeedModel.CacheRssFeedModel;
import com.example.newsxml.RssFeedModel.OnlineRssFeedModel;
import com.example.newsxml.RssFeedModel.RssFeedModelAbstract;
import com.example.newsxml.activitys.MainActivity;
import com.example.newsxml.activitys.WebViewActivity;

import java.net.InetAddress;
import java.util.List;

public class RssFeedListAdapter extends RecyclerView.Adapter<RssFeedListAdapter.FeedModelViewHolder> {
    private final Context context;
    private List<RssFeedModelAbstract> mRssFeedModels;

    static class FeedModelViewHolder extends RecyclerView.ViewHolder {
        private View rssFeedView;

        FeedModelViewHolder(final View v) {
            super(v);
            rssFeedView = v;
        }
    }

    public RssFeedListAdapter(final List<RssFeedModelAbstract> rssFeedModels,
                              final Context context) {
        this.context = context;
        mRssFeedModels = rssFeedModels;
    }

    @NonNull
    @Override
    public FeedModelViewHolder onCreateViewHolder(final ViewGroup parent,
                                                  final int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss_feed, parent, false);
        FeedModelViewHolder holder = new FeedModelViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedModelViewHolder holder,
                                 final int position) {
        final RssFeedModelAbstract rssFeedModel = mRssFeedModels.get(position);
        final ResultForGetNews resultForGetNews = new ResultForGetNews();

        if (rssFeedModel instanceof OnlineRssFeedModel) {
            final OnlineRssFeedModel onlineRssFeedModel = (OnlineRssFeedModel) rssFeedModel;

            if (MainActivity.getCachePreferences().getBoolean(onlineRssFeedModel.getTitle().hashCode() + "", false)) {
                (new ReadImageTask(holder.rssFeedView, resultForGetNews)).execute(
                        onlineRssFeedModel.getTitle().hashCode() + ".jpg",
                        onlineRssFeedModel.getTitle().hashCode() + ".html",
                        onlineRssFeedModel.getTitle(),
                        onlineRssFeedModel.getDescription()
                );
            } else {
                (new DownloadTask(holder.rssFeedView, position)).execute(
                        onlineRssFeedModel.getLinkToImage(),
                        onlineRssFeedModel.getTitle(),
                        onlineRssFeedModel.getDescription(),
                        onlineRssFeedModel.getLink()
                );
            }
        } else {
            final CacheRssFeedModel cacheRssFeedModel = (CacheRssFeedModel) rssFeedModel;

            (new ReadImageTask(holder.rssFeedView, resultForGetNews)).execute(
                    cacheRssFeedModel.getTitle().hashCode() + ".jpg",
                    cacheRssFeedModel.getTitle().hashCode() + ".html",
                    cacheRssFeedModel.getTitle(),
                    cacheRssFeedModel.getDescription()
            );
        }

        holder.rssFeedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent browserIntent = new Intent(context, WebViewActivity.class);
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (rssFeedModel instanceof OnlineRssFeedModel) {
                    if (isInternetAvailable())
                        browserIntent.putExtra("page", ((OnlineRssFeedModel) rssFeedModel).getLink());
                    else {
                        browserIntent.putExtra("data", resultForGetNews.getHtml());
                        context.startActivity(browserIntent);
                    }
                    context.startActivity(browserIntent);
                } else {
                    if (resultForGetNews.getHtml() != null) {
                        browserIntent.putExtra("data", resultForGetNews.getHtml());
                        context.startActivity(browserIntent);
                    }
                }
            }
        });
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mRssFeedModels.size();
    }
}
