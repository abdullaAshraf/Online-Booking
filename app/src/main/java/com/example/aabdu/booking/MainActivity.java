package com.example.aabdu.booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Block> blockList = new ArrayList<>();
    public static String userEmail;

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
            SharedPreferences sharedPref = this.getSharedPreferences("pref",Context.MODE_PRIVATE);
            userEmail = sharedPref.getString("userEmail" , "");

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
