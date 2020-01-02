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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<String, Void, ResultForGetNews> {
    private WeakReference<View> view;
    private int position;

    public DownloadTask(final View view,
                        final int position) {
        this.view = new WeakReference<>(view);
        this.position = position;
    }

    private void saveImage(final Bitmap finalBitmap) {
        final String root = Environment.getExternalStorageDirectory().toString();
        final File file = new File(root, "image" + position + ".jpg");

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveText(final String text,
                          final String prefixName) {
        final String root = Environment.getExternalStorageDirectory().toString();
        final File file = new File(root, prefixName + position + ".txt");

        try {
            final FileOutputStream out = new FileOutputStream(file);
            out.write(text.getBytes());

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHtml(final String link) {
        final String root = Environment.getExternalStorageDirectory().toString();
        final File file = new File(root, "cache" + position + ".html");

        try {
            final FileOutputStream out = new FileOutputStream(file);

            URLConnection connection = (new URL(link)).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; )
                html.append(line);
            in.close();

            out.write(html.toString().getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
       data[0] -> url for image
       data[1] -> title text
       data[2] -> description text
       data[3] -> link to news
    */
    protected ResultForGetNews doInBackground(String... data) {
        try {
            final InputStream in = new java.net.URL(data[0]).openStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(in);

            if (position < 10) {
                saveImage(bitmap);
                saveText(data[1], "title");
                saveText(data[2], "description");
                saveHtml(data[3]);
            }

            return new ResultForGetNews(data[1], data[2], bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(ResultForGetNews result) {
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