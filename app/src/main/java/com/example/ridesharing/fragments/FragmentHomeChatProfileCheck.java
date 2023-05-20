package com.example.ridesharing.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.ridesharing.ActivityHome;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassUser;
import com.example.ridesharing.commonClasses.ClassValidationColor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс фрагмента осуществленных поездок
 */
public class FragmentHomeChatProfileCheck extends Fragment{
    private View view;
    private ImageView avatar;
    private TextInputEditText name;
    private TextInputLayout nameInput;
    private TextInputEditText phone;
    private TextInputLayout phoneInput;
    private TextInputEditText passport;
    private TextInputLayout passportInput;
    private TextInputEditText email;
    private TextInputLayout emailInput;
    private ImageView back;

    private Bundle bundleGet = new Bundle();
    private Bundle bundleSet = new Bundle();
    private String opponentId;

    private static final String VIEW_NAME = "FragmentHomeChatProfileCheck";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    /**
     * Конструктор класса фрагмента осуществленных поездок
     */
    public FragmentHomeChatProfileCheck() {
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
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
        view = inflater.inflate(R.layout.fragment_home_chat_profile_check, container, false);
        initComponents();

        bundleGet = getArguments();

        /**
         * Заполнение полей существубщими значениями при возвращении на фрагмент
         */
        try {
            if(bundleGet != null){
                readUserData(bundleGet.getString("id"));
            }

        }catch (Exception e){
            //Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
        }

        back.setOnClickListener(V ->{
            bundleSet.putString("ChatId", bundleGet.getString("ChatId"));
            loadFragmentFromDown(FragmentHomeChatMessages.newInstance(bundleSet), "ChatMessages");
        });

        return view;
    }

    public void readUserData(String id) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUser user = snapshot.getValue(ClassUser.class);
                name.setText(user.getName());
                phone.setText(user.getPhone());
                try {
                    if (!user.getPassport().equals("")){
                        passport.setText(getActivity().getString(R.string.text_passport_verified));
                    }

                } catch(Exception exception) {
                    passport.setText(getActivity().getString(R.string.text_passpor_not_verified));
                }
                email.setText(user.getEmail());
                if (!user.getAvatar().equals("")){
                    Glide.with(getActivity()).load(user.getAvatar())
                            .into(avatar);
                } else {
                    Glide.with(getActivity()).load(R.drawable.no_photo_avatar)
                            .into(avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Инициализация компонентов View
     */
    public void initComponents(){
        avatar = view.findViewById(R.id.avatarEdit);
        name = view.findViewById(R.id.textEditProfileName);
        nameInput = view.findViewById(R.id.outlinedTextFieldPriceEditName);
        phone = view.findViewById(R.id.textEditProfilePhone);
        phoneInput = view.findViewById(R.id.outlinedTextFieldPriceEditPhone);
        passport = view.findViewById(R.id.textEditProfilePassport);
        passportInput = view.findViewById(R.id.outlinedTextFieldPriceEditPassport);
        email = view.findViewById(R.id.textEditProfileEmail);
        emailInput = view.findViewById(R.id.outlinedTextFieldPriceEditEmail);
        back = view.findViewById(R.id.backToProfile);
    }

    /**
     * Метод вызова фрагмента снизу
     * @param fragment Передать сюда искомый фрагмент
     * @param tag Добавить новому фрагменут тег
     */
    public void loadFragmentFromDown(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_down);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Метод создания нового фрагмента с указанием передачи данных
     * @param bundle Передаваемые во фрагмент данные
     * @return возврат нового фрагмента
     */
    public static FragmentHomeChatProfileCheck newInstance(Bundle bundle){
        FragmentHomeChatProfileCheck fragmentHomeProfileEdit = new FragmentHomeChatProfileCheck();
        fragmentHomeProfileEdit.setArguments(bundle);
        return fragmentHomeProfileEdit;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return возвращает новый пустой фрагмент
     */
    public static FragmentHomeChatProfileCheck newInstance(){
        return new FragmentHomeChatProfileCheck();
    }
}
