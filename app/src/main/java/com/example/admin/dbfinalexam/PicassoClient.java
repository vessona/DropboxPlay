package com.example.admin.dbfinalexam;

import android.content.Context;

import com.dropbox.core.v2.DbxClientV2;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Singleton instance of Picasso pre-configured
 */
public class PicassoClient {
    private static Picasso sPicasso;

    public static void init(Context context, DbxClientV2 dbxClient) {
        OkHttpClient client = new OkHttpClient();
        // Configure picasso to know about special thumbnail requests
        sPicasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(context))
                .addRequestHandler(new FileThumbnailRequestHandler(dbxClient))
                .build();
    }


    public static Picasso getPicasso() {
        return sPicasso;
    }
}
