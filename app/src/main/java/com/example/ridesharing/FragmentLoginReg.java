package com.example.ridesharing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FragmentLoginReg extends Fragment {
    final static String BASE_AUTH = "Auth_reg";

    private Button register;
    private Button toAuthentication;
    private TextInputLayout name;
    private TextInputLayout phone;
    private TextInputLayout email;
    private TextInputLayout pass;
    private TextInputLayout confirmpass;

    private FirebaseAuth mAuth;

    private View view;
    private String userID;
    private String nameTxt;
    private String phoneTxt;
    private String emailTxt;
    private String passTxt;
    private String confirmpassTxt;

    public FragmentLoginReg() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_reg, container, false);

        register = view.findViewById(R.id.registerBtn);
        toAuthentication = view.findViewById(R.id.toAuthBtn);

        View.OnClickListener listenerReg = v -> {
            mAuth = FirebaseAuth.getInstance();
            initComponents();
            try {
                registration(emailTxt, passTxt);

            }
            catch (Exception exception) {
                Log.d(BASE_AUTH, "Reg: " + exception.toString());
            }
            swipeFragment();
        };
        register.setOnClickListener(listenerReg);

        View.OnClickListener listenerLogin = v -> swipeFragment();
        toAuthentication.setOnClickListener(listenerLogin);

        return view;
    }

    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        userID = user.getUid();
                        Log.d(BASE_AUTH, "createUserWithEmail:success");
                    } else {
                        Log.w(BASE_AUTH, "createUserWithEmail:failure", task.getException());
                    }
                });
    }


    public void swipeFragment(){
        Fragment authetication = FragmentLoginAuth.newInstance();
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                R.anim.exit_to_right).replace(R.id.fragmentLogin, authetication).commit();
    }

    public void initComponents(){
        name = view.findViewById(R.id.inputNameReg);
        phone = view.findViewById(R.id.inputPhoneReg);
        email = view.findViewById(R.id.inputEmailReg);
        pass = view.findViewById(R.id.inputPasswordReg);
        confirmpass = view.findViewById(R.id.inputPasswordConfirmReg);

        nameTxt = name.getEditText().getText().toString();
        phoneTxt = phone.getEditText().getText().toString();
        emailTxt = email.getEditText().getText().toString();
        passTxt = pass.getEditText().getText().toString();
        confirmpassTxt = confirmpass.getEditText().getText().toString();
    }

    public static FragmentLoginReg newInstance(){
        return new FragmentLoginReg();
    }
}
