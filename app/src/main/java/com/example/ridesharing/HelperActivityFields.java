package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class HelperActivityFields extends AppCompatActivity {
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_activity_fields);
        bundle.putAll(getIntent().getBundleExtra("bundle"));


        loadFragmentFromRight(HelperFragmentFields.newInstance(bundle));
    }

    public void finishHelper(View view){
        finish();
    }

    public void loadFragmentFromRight(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameHelperFragment,fragment);
        transaction.commit();
    }
}