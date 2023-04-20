package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassPublication;
import com.example.ridesharing.recycler.RecyclerYourPublicationsAndTripsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Фрагмент вывода найденных поездок
 */
public class FragmentHomeSearchResult extends Fragment{
    View view;
    Bundle bundleGet = new Bundle();
    Bundle bundleSet = new Bundle();
    String key = null;
    ArrayList<ClassPublication> orders = new ArrayList<ClassPublication>();

    /**
     * Конструктор фрагмента
     */
    public FragmentHomeSearchResult() {
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

        view = inflater.inflate(R.layout.fragment_home_search_result, container, false);
        bundleGet = getArguments();
        if (bundleGet != null){
            createOrdersList(bundleGet.getString("From"),
                    bundleGet.getString("To"),
                    bundleGet.getString("Date"),
                    bundleGet.getString("Peoples"));
        }
        return view;
    }

    /**
     * Метод заполнения RecyclerView
     * @param list Передать источник данных
     */
    public void rvPublications(ArrayList<ClassPublication> list){
        orders.clear();
        orders.addAll(list);
        RecyclerView rvNews = view.findViewById(R.id.recyclerSearchOffer);
        RecyclerYourPublicationsAndTripsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {
            if (key != null){
                bundleSet.putString("key", key);
                loadFragmentFromDown(FragmentHomeSearchResultOrder.newInstance(bundleSet), "SearchResultOrder");
            }
        };
        RecyclerYourPublicationsAndTripsAdapter adapter = new RecyclerYourPublicationsAndTripsAdapter(getContext() , orders, publicationsClickListener);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    /**
     * Создание листа путем получения данных с сервера
     * @param from --
     * @param to --
     * @param date --
     * @param peoples --
     */
    public void createOrdersList(String from, String to, String date, String peoples) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("publish");
        myRef.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ClassPublication> ordersFill = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    ClassPublication publication = childSnapshot.getValue(ClassPublication.class);
                    if (publication.getFrom().equals(from) && publication.getTo().equals(to)){
                        ordersFill.add(publication);
                        key = childSnapshot.getKey();
                    }
                }
                rvPublications(ordersFill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
     * Метод вызова фрагмента сверху
     * @param fragment Передать сюда искомый фрагмент
     * @param tag Добавить новому фрагменут тег
     */
    public void loadFragmentFromTop(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_top);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Метод создания нового фрагмента с указанием передачи данных
     * @param bundle Передаваемые во фрагмент данные
     * @return возврат нового фрагмента
     */
    public static FragmentHomeSearchResult newInstance(Bundle bundle){
        FragmentHomeSearchResult fragmentHomeSearchResult = new FragmentHomeSearchResult();
        fragmentHomeSearchResult.setArguments(bundle);
        return fragmentHomeSearchResult;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static FragmentHomeSearchResult newInstance(){
        return new FragmentHomeSearchResult();
    }
}
