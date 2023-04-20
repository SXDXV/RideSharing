package com.example.ridesharing.fragments.helper;

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

import com.example.ridesharing.R;
import com.example.ridesharing.fragments.FragmentHomePublishMain;
import com.example.ridesharing.fragments.FragmentHomeSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Класс фрагмента, отвечающего за переход на себя после нажатия на любое из полей родительского
 * фрагмента. Предназначен для заполнения полей времени.
 */
public class HelperFragmentTime extends Fragment{
    View view;
    private static final String VIEW_NAME = "HelperFragmentTime";

    TextView fieldName;
    String txtFieldName;
    TextInputEditText hours;
    TextInputEditText minutes;
    Button btnContinue;
    ImageView backSearch;

    Button plusHours;
    Button minusHours;
    Button plusMinutes;
    Button minusMinutes;

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
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        view = inflater.inflate(R.layout.helper_fragment_time, container, false);
        initComponents();

        bundleGet = getArguments();
        if (bundleGet != null) {
            txtFieldName = bundleGet.getString("FieldName");
            fieldName.setText(txtFieldName);

            if (bundleGet.getString("FieldText") != null){
                String parts[] = bundleGet.getString("FieldText").split(":");
                hours.setText(parts[0]);
                minutes.setText(parts[1]);
            }else {
                hours.setText("12");
                minutes.setText("30");
            }
        }

        setListenHoursMinutes(minusHours);
        setListenHoursMinutes(plusHours);
        setListenHoursMinutes(minusMinutes);
        setListenHoursMinutes(plusMinutes);

        backToSearch(btnContinue);
        backToSearch(backSearch);
        return view;
    }

    /**
     * Слушатель для добавления и уменьшения значений часов и минут
     * @param button Передать элемент
     */
    @SuppressLint("NonConstantResourceId")
    public void setListenHoursMinutes(View button){
         button.setOnClickListener(v -> {
            int workingHours = Integer.parseInt(hours.getText().toString());
            int workingMinutes = Integer.parseInt(minutes.getText().toString());

            switch (button.getId()){
                case R.id.buttonMinusHours:
                    if (workingHours>0) workingHours--;
                    break;
                case R.id.buttonPlusHours:
                    if (workingHours<23) workingHours++;
                    break;
                case R.id.buttonMinusMinutes:
                    if (workingMinutes>0) workingMinutes--;
                    break;
                case R.id.buttonPlusMinutes:
                    if (workingMinutes<59) workingMinutes++;
                    break;
                default:
                    break;
            }

            hours.setText(String.valueOf(workingHours));
            minutes.setText(String.valueOf(workingMinutes));
        });
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
                    bundleSet.putString("fieldText", hoursMinutesValidation(hours.getText().toString()) +
                            ":" + hoursMinutesValidation(minutes.getText().toString()));

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
     * Валидация введенных минут и часов для красивого вывода в поле
     * @param initial Входные данные
     * @return Выходное валидное значение
     */
    private String hoursMinutesValidation(String initial){
        String result;

        if (Integer.parseInt(initial)>=1 && Integer.parseInt(initial)<=9){
            result = "0" + String.valueOf(initial);
        } else {
            result = String.valueOf(initial);
        }

        return result;
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
     * Инициализация компонентов View
     */
    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        hours = view.findViewById(R.id.fieldHelperHours);
        minutes = view.findViewById(R.id.fieldHelperMinutes);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
        backSearch = view.findViewById(R.id.backToSearch);

        minusHours = view.findViewById(R.id.buttonMinusHours);
        plusHours = view.findViewById(R.id.buttonPlusHours);
        minusMinutes = view.findViewById(R.id.buttonMinusMinutes);
        plusMinutes = view.findViewById(R.id.buttonPlusMinutes);
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
    public static HelperFragmentTime newInstance(Bundle bundle){
        HelperFragmentTime helperFragmentTime = new HelperFragmentTime();
        helperFragmentTime.setArguments(bundle);
        return helperFragmentTime;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static HelperFragmentTime newInstance(){
        return new HelperFragmentTime();
    }
}