package com.example.admin.dbfinalexam;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.users.FullAccount;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends DropboxActivity {
    final static public String DROPBOX_APP_KEY = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasToken()) {
            Auth.startOAuth2Authentication(MainActivity.this, DROPBOX_APP_KEY);
        } else {
            startActivity(FilesActivity.getIntent(MainActivity.this, ""));
        }
    }
    @Override
    protected void loadData() {
    }
}
