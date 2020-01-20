package com.example.newsxml.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsxml.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ReadImageTask extends AsyncTask<String, Void, ResultForGetNews> {
    private WeakReference<View> view;
    private ResultForGetNews resultForGetNews;

    public ReadImageTask(final View view,
                         final ResultForGetNews resultForGetNews) {
        this.view = new WeakReference<>(view);
        this.resultForGetNews = resultForGetNews;

        ((TextView) view.findViewById(R.id.titleText)).setText("...");
        ((TextView) view.findViewById(R.id.descriptionText)).setText(Html.fromHtml("..."));
        ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(null);
    }

    protected ResultForGetNews doInBackground(final String... pathsToFile) {
        try {
            final String root = Environment.getExternalStorageDirectory().toString();
            File file = new File(root, pathsToFile[0]);
            InputStream in = new FileInputStream(file);
            final Bitmap bitmap = BitmapFactory.decodeStream(in);

            file = new File(root, pathsToFile[1]);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[fis.available()];
            if (fis.read(data) == -1)
                return null;
            fis.close();
            final String html = new String(data);

            resultForGetNews.setDescription(pathsToFile[3]);
            resultForGetNews.setTitle(pathsToFile[2]);
            resultForGetNews.setImageBitmap(bitmap);
            resultForGetNews.setHtml(html);

            return resultForGetNews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(final ResultForGetNews result) {
        if (result == null) {
            view.clear();
            return;
        }

        ((TextView) view.get().findViewById(R.id.titleText)).setText(result.getTitle());
        ((TextView) view.get().findViewById(R.id.descriptionText)).setText(Html.fromHtml(result.getDescription()));
        ((ImageView) view.get().findViewById(R.id.imageView)).setImageBitmap(result.getImageBitmap());

        view.clear();
    }
}
