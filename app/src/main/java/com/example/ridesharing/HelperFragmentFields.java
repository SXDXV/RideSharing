package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Класс фрагмента, отвечающего за переход на себя после нажатия на любое из полей родительского
 * фрагмента. Предназначен для заполнения текстовых полей.
 */
public class HelperFragmentFields extends Fragment{
    View view;
    private static final String VIEW_NAME = "HelperFragmentFields";

    TextView fieldName;
    String txtFieldName;
    TextInputEditText field;
    String txtField;
    Button btnContinue;
    ImageView backSearch;

    Bundle bundleGet;
    Bundle bundleSet;

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
        view = inflater.inflate(R.layout.helper_fragment_fields, container, false);
        initComponents();

        bundleGet = getArguments();
        if (bundleGet != null) {
            txtFieldName = bundleGet.getString("FieldName");
            fieldName.setText(txtFieldName);
            field.setText(bundleGet.getString("FieldText"));
        }

        backToSearch(btnContinue);
        backToSearch(backSearch);
        return view;
    }

    /**
     * Сллушатель для кнопок возвращения назад
     * @param btn Пережать элемент
     */
    @SuppressLint("NonConstantResourceId")
    public void backToSearch(View btn){
        btn.setOnClickListener(v -> {
            bundleSet = new Bundle();
            bundleSet.putAll(bundleGet);
            switch (btn.getId()){
                case R.id.btnContinueFragmentSearch:
                    bundleSet.putString("fieldName", fieldName.getText().toString());
                    bundleSet.putString("fieldText", field.getText().toString());

                    backToParent();
                    break;
                case R.id.backToSearch:
                    bundleSet.putString("fieldName", "null");
                    bundleSet.putString("fieldText", "null");

                    backToParent();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Определение, на какой конкретно фрагмент необходимо возвращаться
     */
    public void backToParent(){
        if (bundleGet.getString("Parent").equals("Search")){
            loadFragmentFromDown(FragmentHomeSearch.newInstance(bundleSet), "search");
        }
        else if (bundleGet.getString("Parent").equals("Publish")){
            loadFragmentFromDown(FragmentHomePublishMain.newInstance(bundleSet), "publish");
        }
    }

    /**
     * Инициализация копонентов View
     */
    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        field = view.findViewById(R.id.fieldFragmentSearch);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
        backSearch = view.findViewById(R.id.backToSearch);
    }

    /**
     * Загрузка фрагмента снизу
     * @param fragment Передать фрагмент
     * @param tag Указать тег для нового фрагмента
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
    public static HelperFragmentFields newInstance(Bundle bundle){
        HelperFragmentFields helperFragmentFields = new HelperFragmentFields();
        helperFragmentFields.setArguments(bundle);
        return helperFragmentFields;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static HelperFragmentFields newInstance(){
        return new HelperFragmentFields();
    }
}