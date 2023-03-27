package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Класс фрагмента, отвечающего за переход на себя после нажатия на любое из полей родительского
 * фрагмента. Предназначен для заполнения полей даты.
 */
public class HelperFragmentDate extends Fragment{
    View view;
    private static final String VIEW_NAME = "HelperFragmentDate";

    TextView fieldName;
    String txtFieldName;
    CalendarView calendarView;
    String txtDate;
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
        view = inflater.inflate(R.layout.helper_fragment_date, container, false);
        initComponents();

        bundleGet = getArguments();
        if (bundleGet != null) {
            txtFieldName = bundleGet.getString("FieldName");
            fieldName.setText(txtFieldName);

            Calendar calendar = Calendar.getInstance();
            String parts[] = bundleGet.getString("FieldText").split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            calendar.set(year, month-1, day);

            calendarView.setDate(calendar.getTimeInMillis());
        }

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            txtDate = String.valueOf(dayMonthValidation(dayOfMonth) + "-" + dayMonthValidation(month+1) + "-" + year);
        });

        backToSearch(btnContinue);
        backToSearch(backSearch);
        return view;
    }

    /**
     * Метод валидации для красивого вывода полей
     * @param initial Входные данные
     * @return Возврат правильного значения
     */
    private String dayMonthValidation(int initial){
        String result;

        if (initial>=1 && initial<=9){
            result = "0" + String.valueOf(initial);
        } else {
            result = String.valueOf(initial);
        }

        return result;
    }

    /**
     * Сллушатель для кнопок возвращения назад
     * @param btn Пережать элемент
     */
    @SuppressLint("NonConstantResourceId")
    public void backToSearch(View btn){
        btn.setOnClickListener(v -> {
            //long checkedDate = calendarView.getDate();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern ("dd-MM-YYYY");
            bundleSet = new Bundle();
            bundleSet.putAll(bundleGet);
            switch (btn.getId()){
                case R.id.btnContinueFragmentSearch:
                    bundleSet.putString("fieldName", fieldName.getText().toString());
                    bundleSet.putString("fieldText", txtDate);

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
     * Инициализация компонетов View
     */
    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        calendarView = view.findViewById(R.id.calendarView);
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
    public static HelperFragmentDate newInstance(Bundle bundle){
        HelperFragmentDate helperFragmentDate = new HelperFragmentDate();
        helperFragmentDate.setArguments(bundle);
        return helperFragmentDate;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static HelperFragmentDate newInstance(){
        return new HelperFragmentDate();
    }
}