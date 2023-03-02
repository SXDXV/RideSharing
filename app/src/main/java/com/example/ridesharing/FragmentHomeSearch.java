package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FragmentHomeSearch extends Fragment{
    View view;

    CardView searchCard;
    TextInputLayout from;
    String txtFrom;

    TextInputLayout to;
    String txtTo;

    TextInputLayout date;
    String txtDate;


    public FragmentHomeSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_search, container, false);

        initComponents();


        searchCard = view.findViewById(R.id.searchCardMain);
        searchCard.getBackground().setAlpha(255);


        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern ("dd/MM/YYYY");
        date.getEditText().setText(dtf.format(currentDate).toString());


        return view;
    }

    public void initComponents(){
        from = view.findViewById(R.id.outlinedTextFieldFrom);
        to = view.findViewById(R.id.outlinedTextFieldTo);
        date = view.findViewById(R.id.outlinedTextFieldDate);
    }

    public static FragmentHomeSearch newInstance(){
        return new FragmentHomeSearch();
    }
}
