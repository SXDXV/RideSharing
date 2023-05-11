package com.example.ridesharing.fragments;

import static android.app.Activity.RESULT_OK;

import static com.yandex.runtime.Runtime.getApplicationContext;

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
import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Objects;
import java.util.Random;

/**
 * Класс фрагмента осуществленных поездок
 */
public class FragmentHomeProfileEdit extends Fragment{
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
    private Button finish;
    private ImageView back;

    private static final String VIEW_NAME = "FragmentHomeProfileEdit";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    static final int GALLERY_REQUEST = 1;
    private Task<Uri> urlTask;

    /**
     * Конструктор класса фрагмента осуществленных поездок
     */
    public FragmentHomeProfileEdit() {
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
        view = inflater.inflate(R.layout.fragment_home_profile_edit, container, false);
        initComponents();
        readUserData();

        finish.setOnClickListener(v -> {
            validationColorFields(name.getText().toString(), phone.getText().toString(),
                    passport.getText().toString(), email.getText().toString());
            if (!name.getText().toString().equals("") &&
                    !phone.getText().toString().equals("") &&
                    !passport.getText().toString().equals("") &&
                    !email.getText().toString().equals("")){
                try {
                    storageUpload();
                }catch (Exception e){
                    Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
                }
            } else {
                Toast toast = Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_empty_data), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        back.setOnClickListener(V ->{
            loadFragmentFromDown(FragmentHomeProfile.newInstance(), "profile");
        });

        avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                int width = originalBitmap.getWidth();
                int height = originalBitmap.getHeight();
                int size = Math.min(width, height);
                int x = (width - size) / 2;
                int y = (height - size) / 2;
                Bitmap squareBitmap = Bitmap.createBitmap(originalBitmap, x, y, size, size);

                int newWidth = 600;
                int newHeight = 600;
                float scaleWidth = ((float) newWidth) / size;
                float scaleHeight = ((float) newHeight) / size;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap scaledBitmap = Bitmap.createBitmap(squareBitmap, 0, 0, size, size, matrix, true);

                avatar.setImageBitmap(scaledBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storageUpload(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        long time = System. currentTimeMillis();
        final StorageReference imageReference =  storageRef.child("users/useravatar-" + FragmentLoginAuth.userID + "-" + String.valueOf(time));

        avatar.setDrawingCacheEnabled(true); //addImg - imageView ПОСЛЕ того, как туда встала фотка из галереи
        avatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageReference.putBytes(data);

        urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String uriPath = task.getResult().toString();
                realtimeDatabaseSetData(uriPath);
            } else {

            }
        });
    }

    public void readUserData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + FragmentLoginAuth.userID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUser user = snapshot.getValue(ClassUser.class);
                name.setText(user.getName());
                phone.setText(user.getPhone());
                passport.setText(user.getPassport());
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
     * Заполнение базы данных по шаблону appointMap
     */
    public void realtimeDatabaseSetData(String uri){
        Map<String, Object> appointMap = new HashMap<>();
        appointMap.put("user_id", FragmentLoginAuth.userID);
        appointMap.put("avatar", uri);
        appointMap.put("name", name.getText().toString().trim());
        appointMap.put("phone", phone.getText().toString().trim());
        appointMap.put("passport", passport.getText().toString().trim());
        appointMap.put("email", email.getText().toString().trim());
        myRef.child(FragmentLoginAuth.userID).setValue(appointMap);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email.getText().toString());

        loadFragmentFromDown(FragmentHomeProfile.newInstance(), "profile");
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
        finish = view.findViewById(R.id.btnToProfile);
        back = view.findViewById(R.id.backToProfile);
    }

    /**
     * Метод установки валидации ClassValidationColor на существующие поля
     * @param name --
     * @param phone --
     * @param passport --
     * @param email --
     */
    public void validationColorFields(String name, String phone, String passport, String email){
        ClassValidationColor classValidationColor = new ClassValidationColor(getContext());
        classValidationColor.validationColor("very_light_dark", nameInput, name);
        classValidationColor.validationColor("very_light_dark", phoneInput, phone);
        classValidationColor.validationColor("very_light_dark", passportInput, passport);
        classValidationColor.validationColor("very_light_dark", emailInput, email);
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
    public static FragmentHomeProfileEdit newInstance(Bundle bundle){
        FragmentHomeProfileEdit fragmentHomeProfileEdit = new FragmentHomeProfileEdit();
        fragmentHomeProfileEdit.setArguments(bundle);
        return fragmentHomeProfileEdit;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return возвращает новый пустой фрагмент
     */
    public static FragmentHomeProfileEdit newInstance(){
        return new FragmentHomeProfileEdit();
    }
}
