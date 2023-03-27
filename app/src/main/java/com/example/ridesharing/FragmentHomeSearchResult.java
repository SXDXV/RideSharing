package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.recycler.RecyclerYourPublicationsAdapter;
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
        orders.addAll(list);
        RecyclerView rvNews = view.findViewById(R.id.recyclerSearchOffer);
        RecyclerYourPublicationsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {

        };
        RecyclerYourPublicationsAdapter adapter = new RecyclerYourPublicationsAdapter(getContext() , orders, publicationsClickListener);
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
                    //String key = childSnapshot.getKey();
                    ClassPublication publication = childSnapshot.getValue(ClassPublication.class);
                    if (publication.getFrom().equals(from) && publication.getTo().equals(to)){
                        ordersFill.add(publication);
                    }
                    //ordersFill.add(publication);
                }
                rvPublications(ordersFill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
