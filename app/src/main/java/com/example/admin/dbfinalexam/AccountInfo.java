package com.example.admin.dbfinalexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.users.FullAccount;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class AccountInfo extends DropboxActivity {
    final static public String DROPBOX_APP_KEY = "aqirx7zrjmg2c1e";
    final static public String DROPBOX_APP_SECRET = "u9pbomdvxn1z8v1";
    CircularImageView user_pic;

    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = (TextView) findViewById(R.id.name_text);
        email = (TextView) findViewById(R.id.email_text);
        user_pic = (CircularImageView)findViewById(R.id.user_pic);





    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasToken()) {
            Auth.startOAuth2Authentication(AccountInfo.this, DROPBOX_APP_KEY);
        } else {
        }
    }
    @Override
    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                if(result.getProfilePhotoUrl() !=null)
                    Picasso.with(getApplicationContext()).load(result.getProfilePhotoUrl()).into(user_pic);
                String jsonString = ""+result.getName();
                try{
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String name_json = jsonObject.getString("display_name");

                    name.setText(name_json);
                }
                catch (Exception e)
                {

                }
                email.setText(result.getEmail());
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }


}