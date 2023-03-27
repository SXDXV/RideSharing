package com.example.ridesharing;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentLoginAuth extends Fragment {
    final static String BASE_AUTH = "Auth_sign";
    public static String userID;

    private Button login;
    private TextView toRegistration;
    private Intent toHome;
    private TextInputLayout emailInput;
    private TextInputLayout passInput;

    private FirebaseAuth mAuth;

    private View view;
    private String emailTxt;
    private String passTxt;

    public FragmentLoginAuth() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_auth, container, false);

        toRegistration = view.findViewById(R.id.textToReg);
        login = view.findViewById(R.id.loginBtn);

        View.OnClickListener listenerReg = v -> {
            Fragment registration = FragmentLoginReg.newInstance();
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                    R.anim.exit_to_left).replace(R.id.fragmentLogin, registration).commit();
        };
        toRegistration.setOnClickListener(listenerReg);

        View.OnClickListener listenerLogin = v -> {
            mAuth = FirebaseAuth.getInstance();
            try {
                initComponents();
                sign(emailTxt, passTxt);
                toHome = new Intent(getContext(), ActivityHome.class);
                //startActivity(toHome);
            } catch (Exception exception){
                Log.d(BASE_AUTH, "Log: " + exception);
            }
        };
        login.setOnClickListener(listenerLogin);

        return view;
    }

    public void sign(String email, String password){
        validationColorFields(email, password);
        if (!email.equals("") && !password.equals("")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(BASE_AUTH, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            startActivity(toHome);
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    getResources().getString(R.string.toast_invalid_data), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
        }else {
            Toast toast = Toast.makeText(getContext(),
                    getResources().getString(R.string.toast_empty_data), Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public void validationColorFields(String email, String password){
        ClassValidationColor classValidationColor = new ClassValidationColor(getContext());
        classValidationColor.validationColor("white", emailInput, email);
        classValidationColor.validationColor("white", passInput, password);
    }

    public void initComponents(){
        emailInput = view.findViewById(R.id.inputEmailAuth);
        passInput = view.findViewById(R.id.inputPasswordAuth);

        emailTxt = emailInput.getEditText().getText().toString();
        passTxt = passInput.getEditText().getText().toString();
    }

    public static FragmentLoginAuth newInstance(){
        return new FragmentLoginAuth();
    }
}
