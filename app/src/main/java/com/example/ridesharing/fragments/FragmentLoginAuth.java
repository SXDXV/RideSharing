package com.example.ridesharing.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ridesharing.ActivityHome;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassValidationColor;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Класс фрагмента аутентификации
 */
public class FragmentLoginAuth extends Fragment {
    final static String BASE_AUTH = "Auth_sign";
    public static String userID;

    private Button login;
    private TextView toRegistration;
    private Intent toHome;
    private TextInputLayout emailInput;
    private TextInputLayout passInput;
    private ImageView googleAuth;
    private CheckBox remember;

    private FirebaseAuth mAuth;

    private View view;
    private String emailTxt;
    private String passTxt;

    private static final String PREF_FIRST_RUN = "first_run";
    private static final String APP_PREFERENCES = "uidPref";
    private static final String APP_PREFERENCES_USERID = "userID";
    private SharedPreferences mSettings;

    /**
     * Конструктор класса фрагмента
     */
    public FragmentLoginAuth() {
    }

    /**
     * Метод, срабатывающий при создании фрагмента
     * @param inflater связывает содержимое XML-файла с View
     * @param container ViewGroup
     * @param savedInstanceState Хранилище данных
     * @return возвращает фрагмент поиска
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_auth, container, false);
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (isFirstRun()) {
            clearSharedPreferences();
            setFirstRunFlag(false);
        }

        if(mSettings.contains(APP_PREFERENCES_USERID)) {
            userID = mSettings.getString(APP_PREFERENCES_USERID, "");
            toHome = new Intent(getContext(), ActivityHome.class);
            startActivity(toHome);
        }

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
            } catch (Exception exception){
                Log.d(BASE_AUTH, "Log: " + exception);
            }
        };
        login.setOnClickListener(listenerLogin);

        return view;
    }

    /**
     * Метод авторизации со всеми проверками
     * @param email Входная почта
     * @param password Входной пароль
     */
    public void sign(String email, String password){
        validationColorFields(email, password);
        if (!email.equals("") && !password.equals("")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(BASE_AUTH, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            if (remember.isChecked()){
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString(APP_PREFERENCES_USERID, userID);
                                editor.apply();
                            }
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

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(PREF_FIRST_RUN, true);
    }

    private void setFirstRunFlag(boolean isFirstRun) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_FIRST_RUN, isFirstRun);
        editor.apply();
    }

    private void clearSharedPreferences() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Валидация полей на цвет
     * @param email --
     * @param password --
     */
    public void validationColorFields(String email, String password){
        ClassValidationColor classValidationColor = new ClassValidationColor(getContext());
        classValidationColor.validationColor("white", emailInput, email);
        classValidationColor.validationColor("white", passInput, password);
    }

    /**
     * Инициализация компонентов View
     */
    public void initComponents(){
        emailInput = view.findViewById(R.id.inputEmailAuth);
        passInput = view.findViewById(R.id.inputPasswordAuth);
        remember = view.findViewById(R.id.checkBox);

        emailTxt = emailInput.getEditText().getText().toString().trim();
        passTxt = passInput.getEditText().getText().toString().trim();
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static FragmentLoginAuth newInstance(){
        return new FragmentLoginAuth();
    }
}
