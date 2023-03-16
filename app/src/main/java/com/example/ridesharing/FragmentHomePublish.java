package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHomePublish extends Fragment{
    View view;
    Button toPublishMain;

    public FragmentHomePublish() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_publish, container, false);
        toPublishMain = view.findViewById(R.id.btnToPublishMain);
        toPublishMain.setOnClickListener(v -> {
            loadFragmentFromTop(FragmentHomePublishMain.newInstance(), "publishing");
        });
        return view;
    }

    public void loadFragmentFromTop(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_top);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static FragmentHomePublish newInstance(){
        return new FragmentHomePublish();
    }
}
