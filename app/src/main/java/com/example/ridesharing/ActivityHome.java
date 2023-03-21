package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class ActivityHome extends AppCompatActivity {
    public static final String MAIN_TAG = "MAIN_TAG";

    BottomNavigationView navigationView;
    HashMap<Integer, Integer> map;
    public int oldIndex = 3;
    int newIndex;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.getMenu().getItem(2).setChecked(true);

        loadFragmentFromLeft(FragmentHomeTrips.newInstance(),"home");

        mapInit();
        navigationView.setOnItemSelectedListener(item -> {
            newIndex = (Integer) map.get(item.getItemId());
            switch (item.getItemId()){
                case R.id.men_search:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeSearch.newInstance(), "search");
                    //loadFragment(FragmentHomeSearch.newInstance());
                    return true;
                case R.id.men_publish:
                    chooseAnimation(oldIndex, newIndex, FragmentHomePublish.newInstance(), "publish");
                    //loadFragment(FragmentHomePublish.newInstance());
                    return true;
                case R.id.men_trips:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeTrips.newInstance(), "trips");
                    //loadFragment(FragmentHomeTrips.newInstance());
                    return true;
                case R.id.men_chat:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeChat.newInstance(), "chat");
                    //loadFragment(FragmentHomeChat.newInstance());
                    return true;
                case R.id.men_profile:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeProfile.newInstance(), "profile");
                    //loadFragment(FragmentHomeProfile.newInstance());
                    return true;
            }
            return false;
        });
    }

    public void loadFragmentFromRight(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void loadFragmentFromLeft(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void chooseAnimation(int oldI, int newI, Fragment fragment, String tag){

        if (oldI < newI){
            loadFragmentFromRight(fragment, tag);
        } else if (oldI > newI){
            loadFragmentFromLeft(fragment, tag);
        }
        oldIndex = newIndex;
    }

    public void mapInit(){
        map = new HashMap<>();
        map.put(R.id.men_search, 1);
        map.put(R.id.men_publish, 2);
        map.put(R.id.men_trips, 3);
        map.put(R.id.men_chat, 4);
        map.put(R.id.men_profile, 5);
    }
}