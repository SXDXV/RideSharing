package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHomeSearch extends Fragment{
    public FragmentHomeSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_search, container, false);
        return view;
    }

    public static FragmentHomeSearch newInstance(){
        return new FragmentHomeSearch();
    }
}
