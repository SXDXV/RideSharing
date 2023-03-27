package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.recycler.RecyclerYourPublicationsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Фрагмент, содержащий первую страницу публикации (содержит в себе кнопку перехода на второй
 * фрагмент, а также список опубликованных поездок)
 */
public class FragmentHomePublish extends Fragment{
    View view;
    private static final String VIEW_NAME = "FragmentHomePublish";
    private String UiD;
    private CardView publicationsCard;

    Button toPublishMain;
    ArrayList<ClassPublication> publications = new ArrayList<ClassPublication>();

    /**
     * Констуктор класса фрагмента
     */
    public FragmentHomePublish() {
    }

    /**
     * Метод, срабатывающий при создании фрагмента
     * @param inflater Из содержимого XML-файла создает View-элемент
     * @param container ViewGroup
     * @param savedInstanceState Сохраненные параметры
     * @return Возвращает View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_publish, container, false);
        UiD = FragmentLoginAuth.userID;

        publicationsCard = view.findViewById(R.id.publishCard);

        createPublicationsList();
        toPublishMain = view.findViewById(R.id.btnToPublishMain);
        toPublishMain.setOnClickListener(v -> {
            loadFragmentFromTop(FragmentHomePublishMain.newInstance(), "publishing");
        });
        return view;
    }

    /**
     * Метод, заполняющий RecyclerView
     * @param list Исходный лист входных данных, получаемый в createPublicationsList
     */
    public void rvPublications(List<ClassPublication> list){
        publications.addAll(list);
        RecyclerView rvNews = view.findViewById(R.id.recyclerYourPublications);
        RecyclerYourPublicationsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {

        };
        RecyclerYourPublicationsAdapter adapter = new RecyclerYourPublicationsAdapter(getContext() , publications, publicationsClickListener);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        resizeHeight(publicationsCard, 950, 1050);
    }

    /**
     * Получение списка экземпляров класса Publication для заполнения RecyclerView.
     * При запросе на сервер задаются параметры, а именно author_id - сортировка
     * по айди текущего пользователя (он видит только свои публикации)
     */
    public void createPublicationsList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("publish");
        myRef.orderByChild("author_id").equalTo(UiD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ClassPublication> publicationsFill = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    //String key = childSnapshot.getKey();
                    ClassPublication publication = childSnapshot.getValue(ClassPublication.class);
                    publicationsFill.add(publication);
                }
                List<ClassPublication> shallowCopy = publicationsFill.subList(0, publicationsFill.size());
                Collections.reverse(shallowCopy);
                rvPublications(shallowCopy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Метод обращения к классу ClassResizeAnimation для анимации элемента
     * @param view --
     * @param width --
     * @param height --
     * Больше документации в классе ClassResizeAnimation.
     */
    public void resizeHeight(View view, int width, int height){
        ClassResizeAnimation resizeAnimation = new ClassResizeAnimation(view, width, height);
        resizeAnimation.setDuration(400);
        view.startAnimation(resizeAnimation);
    }

    /**
     * Метод вызова фрагмента сверху
     * @param fragment Передать нужный фрагмент
     * @param tag Задать тег новому фрагменту
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
     * Метод создания нового фрагмента (действующего). Вызывается из других фрагментов
     * @return Возвращает новый экземпляр данного фрагмента
     */
    public static FragmentHomePublish newInstance(){
        return new FragmentHomePublish();
    }
}
