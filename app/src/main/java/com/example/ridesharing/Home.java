package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class Home extends AppCompatActivity {
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

        loadFragmentFromLeft(FragmentHomeTrips.newInstance());

        mapInit();
        navigationView.setOnItemSelectedListener(item -> {
            newIndex = (Integer) map.get(item.getItemId());
            switch (item.getItemId()){
                case R.id.men_search:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeSearch.newInstance());
                    //loadFragment(FragmentHomeSearch.newInstance());
                    return true;
                case R.id.men_publish:
                    chooseAnimation(oldIndex, newIndex, FragmentHomePublish.newInstance());
                    //loadFragment(FragmentHomePublish.newInstance());
                    return true;
                case R.id.men_trips:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeTrips.newInstance());
                    //loadFragment(FragmentHomeTrips.newInstance());
                    return true;
                case R.id.men_chat:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeChat.newInstance());
                    //loadFragment(FragmentHomeChat.newInstance());
                    return true;
                case R.id.men_profile:
                    chooseAnimation(oldIndex, newIndex, FragmentHomeProfile.newInstance());
                    //loadFragment(FragmentHomeProfile.newInstance());
                    return true;
            }
            return false;
        });
    }

    public void loadFragmentFromRight(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.commit();
    }

    public void loadFragmentFromLeft(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.commit();
    }

    public void chooseAnimation(int oldI, int newI, Fragment fragment){

        if (oldI < newI){
            loadFragmentFromRight(fragment);
        } else if (oldI > newI){
            loadFragmentFromLeft(fragment);
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