package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLoginReg extends Fragment {
    Button register;
    Button toAuthentication;

    public FragmentLoginReg() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_reg, container, false);

        register = view.findViewById(R.id.registerBtn);
        toAuthentication = view.findViewById(R.id.toAuthBtn);

        View.OnClickListener listenerReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeFragment();
            }
        };
        register.setOnClickListener(listenerReg);

        View.OnClickListener listenerLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeFragment();
            }
        };
        toAuthentication.setOnClickListener(listenerLogin);

        return view;
    }

    public void swipeFragment (){
        Fragment authetication = FragmentLoginAuth.newInstance();
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                R.anim.exit_to_right).replace(R.id.fragmentLogin, authetication).commit();
    }

    public static FragmentLoginReg newInstance(){
        return new FragmentLoginReg();
    }
}
