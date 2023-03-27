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

    private String dayMonthValidation(int initial){
        String result;

        if (initial>=1 && initial<=9){
            result = "0" + String.valueOf(initial);
        } else {
            result = String.valueOf(initial);
        }

        return result;
    }

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

    public void backToParent(){
        if (bundleGet.getString("Parent").equals("Search")){
            loadFragmentFromDown(FragmentHomeSearch.newInstance(bundleSet), "search");
        }
        else if (bundleGet.getString("Parent").equals("Publish")){
            loadFragmentFromDown(FragmentHomePublishMain.newInstance(bundleSet), "publish");
        }
    }

    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        calendarView = view.findViewById(R.id.calendarView);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
        backSearch = view.findViewById(R.id.backToSearch);
    }

    public void loadFragmentFromDown(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_down);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static HelperFragmentDate newInstance(Bundle bundle){
        HelperFragmentDate helperFragmentDate = new HelperFragmentDate();
        helperFragmentDate.setArguments(bundle);
        return helperFragmentDate;
    }
    public static HelperFragmentDate newInstance(){
        return new HelperFragmentDate();
    }
}