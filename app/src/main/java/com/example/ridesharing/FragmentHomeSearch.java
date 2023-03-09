package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FragmentHomeSearch extends Fragment{
    View view;

    CardView searchCard;
    boolean hidden = true;
    ImageView dropDown;
    TextInputEditText from;
    String txtFrom;

    TextInputEditText to;
    String txtTo;

    TextInputEditText peoples;
    int txtPeoples;

    TextInputEditText date;
    String txtDate;

    Bundle bundle = new Bundle();


    public FragmentHomeSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_search, container, false);
        initComponents();

        searchCard = view.findViewById(R.id.searchCardMain);
        dropDown = view.findViewById(R.id.imageView3);
        searchCard.getBackground().setAlpha(255);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern ("dd/MM/YYYY");
        date.setText(dtf.format(currentDate));


        // Animation cardview
        resizeHeight(searchCard, 950, 530);
        // Listener to animation cardview on touch
        View.OnClickListener listenerDropDown = v -> {
            if (hidden){
                resizeHeight(searchCard, 950, 530);
                hidden = false;
            } else {
                resizeHeight(searchCard, 950, 1300);
                hidden = true;
            }
        };
        dropDown.setOnClickListener(listenerDropDown);

        setListen(from);
        setListen(to);
        setListen(peoples);
        setListen(date);

        return view;
    }

    public void setListen(View view){
        @SuppressLint("NonConstantResourceId") View.OnClickListener listenerField = v -> {
            switch (view.getId()){
                case R.id.fieldFrom: bundle.putString("FieldName", "From"); break;
                case R.id.fieldTo: bundle.putString("FieldName", "To"); break;
                case R.id.fieldPeoples: bundle.putString("FieldName", "Peoples"); break;
                case R.id.fieldDate: bundle.putString("FieldName", "Date"); break;
                default: break;
            }

            Intent helperField = new Intent(getActivity(), HelperActivityFields.class);
            helperField.putExtra("bundle", bundle);
            startActivity(helperField);
        };
        view.setOnClickListener(listenerField);
    }

    public void initComponents(){
        from = view.findViewById(R.id.fieldFrom);
        to = view.findViewById(R.id.fieldTo);
        peoples = view.findViewById(R.id.fieldPeoples);
        date = view.findViewById(R.id.fieldDate);
    }

    public void resizeHeight(View view, int width, int height){
        ClassResizeAnimation resizeAnimation = new ClassResizeAnimation(view, width, height);
        resizeAnimation.setDuration(400);
        view.startAnimation(resizeAnimation);
    }

    public static FragmentHomeSearch newInstance(){
        return new FragmentHomeSearch();
    }
}
