package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ridesharing.fragments.FragmentLoginAuth;

/**
 * Активность логина, содержащая фрагмента авторизации и реагистрации в системе
 */
public class ActivityLogin extends AppCompatActivity {
    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        im = findViewById(R.id.imageLogo);
        Glide.with(this)
                .load(R.drawable.full_ridesharing_launch_logo)
                //.apply(RequestOptions.bitmapTransform(new BlurTransformation(25,3)))
                .into(im);
        loadFragment(FragmentLoginAuth.newInstance());
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentLogin,fragment);
        transaction.commit();
    }
}