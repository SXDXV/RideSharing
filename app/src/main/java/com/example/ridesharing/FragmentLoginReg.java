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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FragmentLoginReg extends Fragment {
    final static String BASE_AUTH = "Auth_reg";

    private Button register;
    private Button toAuthentication;
    private TextInputLayout nameInput;
    private TextInputLayout phoneInput;
    private TextInputLayout emailInput;
    private TextInputLayout passInput;
    private TextInputLayout confirmpassInput;

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
//            try {
                registration(emailTxt, passTxt, nameTxt, phoneTxt, confirmpassTxt);
//            }
//            catch (Exception exception) {
//                Log.d(BASE_AUTH, "Reg: " + exception.toString());
//            }
        };
        register.setOnClickListener(listenerReg);

        View.OnClickListener listenerLogin = v -> swipeFragment();
        toAuthentication.setOnClickListener(listenerLogin);

        return view;
    }

    public void registration(String email, String password, String name, String phone, String confirmpass){
        validationColorFields(email, password, name, phone, confirmpass);
        if (!email.equals("") &&
                !password.equals("") &&
                !name.equals("") &&
                !phone.equals("") &&
                !confirmpass.equals("")) {
            if (password.equals(confirmpass)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                userID = user.getUid();
                                Log.d(BASE_AUTH, "createUserWithEmail:success");
                                swipeFragment();
                            } else {
                                Toast toast = Toast.makeText(getContext(),
                                        getResources().getString(R.string.toast_invalid_data), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
            }else {
                Toast toast = Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_password_valid), Toast.LENGTH_LONG);
                toast.show();
            }

        }else {
            Toast toast = Toast.makeText(getContext(),
                    getResources().getString(R.string.toast_empty_data), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void validationColorFields(String email, String password, String name, String phone, String confirmpass){
        ClassValidationColor classValidationColor = new ClassValidationColor(getContext());
        classValidationColor.validationColor("white", emailInput, email);
        classValidationColor.validationColor("white", passInput, password);
        classValidationColor.validationColor("white", nameInput, name);
        classValidationColor.validationColor("white", phoneInput, phone);
        classValidationColor.validationColor("white", confirmpassInput, confirmpass);
    }


    public void swipeFragment(){
        Fragment authetication = FragmentLoginAuth.newInstance();
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left,
                R.anim.exit_to_right).replace(R.id.fragmentLogin, authetication).commit();
    }

    public void initComponents(){
        nameInput = view.findViewById(R.id.inputNameReg);
        phoneInput = view.findViewById(R.id.inputPhoneReg);
        emailInput = view.findViewById(R.id.inputEmailReg);
        passInput = view.findViewById(R.id.inputPasswordReg);
        confirmpassInput = view.findViewById(R.id.inputPasswordConfirmReg);

        nameTxt = nameInput.getEditText().getText().toString();
        phoneTxt = phoneInput.getEditText().getText().toString();
        emailTxt = emailInput.getEditText().getText().toString();
        passTxt = passInput.getEditText().getText().toString();
        confirmpassTxt = confirmpassInput.getEditText().getText().toString();
    }

    public static FragmentLoginReg newInstance(){
        return new FragmentLoginReg();
    }
}
