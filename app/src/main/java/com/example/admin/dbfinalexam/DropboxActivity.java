package com.example.admin.dbfinalexam;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dropbox.core.android.Auth;


/**
 * Base class for Activities that require auth tokens
 * Will redirect to auth flow if needed
 */
public abstract class DropboxActivity extends AppBaseActivity{

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            accessToken = Auth.getOAuth2Token();
            if (accessToken != null) {
                prefs.edit().putString("access-token", accessToken).apply();
                initAndLoadData(accessToken);
            }
        } else {
            initAndLoadData(accessToken);
        }

        String uid = Auth.getUid();
        String storedUid = prefs.getString("user-id", null);
        if (uid != null && !uid.equals(storedUid)) {
            prefs.edit().putString("user-id", uid).apply();
        }
    }

    private void initAndLoadData(String accessToken) {
        DropboxClientFactory.init(accessToken);
        loadData();
    }

    protected abstract void loadData();
    protected boolean hasToken() {
        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;

    }
}
