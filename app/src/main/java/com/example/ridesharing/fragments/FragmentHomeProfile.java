package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ridesharing.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Фрагмент, содержащий профиль
 */
public class FragmentHomeProfile extends Fragment{
    /**
     * Констуктор класса фрагмента
     */
    public FragmentHomeProfile() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        return view;
    }

    public static FragmentHomeProfile newInstance(){
        return new FragmentHomeProfile();
    }
}
