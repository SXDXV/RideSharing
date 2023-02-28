package com.example.ridesharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLoginAuth extends Fragment {
    Button login;
    TextView toRegistration;
    Intent toHome;

    public FragmentLoginAuth() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_auth, container, false);

        toRegistration = view.findViewById(R.id.textToReg);
        login = view.findViewById(R.id.loginBtn);

        View.OnClickListener listenerReg = v -> {
            Fragment registration = FragmentLoginReg.newInstance();
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                    R.anim.exit_to_left).replace(R.id.fragmentLogin, registration).commit();
        };
        toRegistration.setOnClickListener(listenerReg);

        View.OnClickListener listenerLogin = v -> {
            toHome = new Intent(getContext(), Home.class);
            startActivity(toHome);
        };
        login.setOnClickListener(listenerLogin);

        return view;
    }

    public static FragmentLoginAuth newInstance(){
        return new FragmentLoginAuth();
    }
}
