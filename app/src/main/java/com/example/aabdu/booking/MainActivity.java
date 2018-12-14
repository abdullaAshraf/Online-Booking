package com.example.aabdu.booking;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Block> blockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        fillBlockData();


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        //isLoggedIn = false;
        if(isLoggedIn) {

            Fragment fragment = new HomeFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }else {
            Fragment fragment = new LoginFragment();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://murmuring-citadel-12769.herokuapp.com/auth/facebook"));
        //startActivity(browserIntent);

        //DataHandler dh = new DataHandler(this);
        //dh.addUser("1","a","abdulla", "ashraf");
        //dh.getData();
    }

    private void fillBlockData() {
        for(int i=0; i<48; i++)
            blockList.add(new Block(i));

        for(int i=4; i<13; i++)
            blockList.get(i).setReserved(true);

        for(int i=20; i<25; i++)
            blockList.get(i).setReserved(true);
    }
}
