package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassPublication;
import com.example.ridesharing.commonClasses.ClassTrip;
import com.example.ridesharing.recycler.RecyclerYourPublicationsAndTripsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Класс фрагмента осуществленных поездок
 */
public class FragmentHomeTrips extends Fragment{
    View view;

    /**
     * Конструктор класса фрагмента осуществленных поездок
     */
    public FragmentHomeTrips() {
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
        bottomNavigationView.setVisibility(View.VISIBLE);

        view = inflater.inflate(R.layout.fragment_home_trips, container, false);
        return view;
    }



    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return возвращает новый пустой фрагмент
     */
    public static FragmentHomeTrips newInstance(){
        return new FragmentHomeTrips();
    }
}
