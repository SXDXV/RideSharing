package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ridesharing.ActivityHome;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassPublication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Фрагмент вывода найденных поездок
 */
public class FragmentHomeSearchResultOrder extends Fragment{
    private View view;
    private static final String VIEW_NAME = "FragmentHomeSearchResultOrder";

    private TextInputEditText from;
    private TextInputEditText to;
    private TextInputEditText peoples;
    private TextInputEditText date;
    private TextInputEditText time;
    private TextInputEditText price;
    private TextInputEditText comment;
    private ImageView music;
    private ImageView pets;
    private ImageView talk;
    private ImageView smoking;
    private Button finish;

    private Bundle bundleGet = new Bundle();
    private Bundle bundleSet = new Bundle();
    private String key = null;
    private String authorID;
    private String tripID;
    private String firstMessage;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Конструктор фрагмента
     */
    public FragmentHomeSearchResultOrder() {
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

        view = inflater.inflate(R.layout.fragment_home_search_result_order, container, false);
        initComponents();

        bundleGet = getArguments();
        if (bundleGet != null){
            key = bundleGet.getString("key");
        }

        try {
            parseData(key);

        }catch (Exception e){
            Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
        }

        View.OnClickListener finishListener = v -> {
            createChat();
            createTripList();
            loadFragmentFromTop(FragmentHomeSearch.newInstance(), "search");

            Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.toast_your_seat_is_reserved), Toast.LENGTH_LONG);
            toast.show();
        };
        finish.setOnClickListener(finishListener);

        return view;
    }

    /**
     *
     * @param keyValue
     */
    public void parseData(String keyValue) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("publish/" + keyValue);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassPublication publicationVal = snapshot.getValue(ClassPublication.class);
                authorID = publicationVal.getAuthor_id();
                tripID = snapshot.getKey();
                firstMessage = "Здравствуйте! Еду с вами от '" + publicationVal.getFrom() + "' " +
                        "до '" + publicationVal.getTo() + "' " + publicationVal.getDate() + ". " +
                        "Все в силе?";
                fillFields(publicationVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createChat(){
        DatabaseReference myRef = database.getReference("chats");

        long time = System. currentTimeMillis();

        Map<String, Object> chatInfo = new HashMap<>();
        chatInfo.put("user_first", FragmentLoginAuth.userID);
        chatInfo.put("user_second", authorID);
        chatInfo.put("chat_name", FragmentLoginAuth.userID + "-" + authorID);
        chatInfo.put("joint_trip", tripID);

        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("chat_id", FragmentLoginAuth.userID + "-" + authorID);
        chatMessage.put("sender", FragmentLoginAuth.userID);
        chatMessage.put("receiver", authorID);
        chatMessage.put("message_text",firstMessage);
        chatMessage.put("time", time);

        myRef.child(FragmentLoginAuth.userID + "-" + authorID).child("ChatInfo").setValue(chatInfo);
        myRef.child(FragmentLoginAuth.userID + "-" + authorID).child("MessagesPool").child("Message" + time).setValue(chatMessage);
    }

    public void createTripList(){
        DatabaseReference myRef = database.getReference("trips");

        Map<String, Object> tripInfo = new HashMap<>();
        tripInfo.put(FragmentLoginAuth.userID, "passenger");
        tripInfo.put(authorID,"driver");
        tripInfo.put("trip_id", tripID);
        tripInfo.put("status", true);

        myRef.child(tripID).setValue(tripInfo);
    }

    public void fillFields(ClassPublication publication){
        from.setText(publication.getFrom());
        to.setText(publication.getTo());
        peoples.setText(publication.getPeoples());
        date.setText(publication.getDate());
        time.setText(publication.getTime());
        price.setText(publication.getPrice());
        comment.setText(publication.getComment());

        if (publication.isMusic()){
            music.setImageResource(R.drawable.music_green);
        }else{
            music.setImageResource(R.drawable.music_red);
        }

        if (publication.isPets()){
            pets.setImageResource(R.drawable.pets_green);
        }else{
            pets.setImageResource(R.drawable.pets_red);
        }

        if (publication.isTalk()){
            talk.setImageResource(R.drawable.talk_green);
        }else{
            talk.setImageResource(R.drawable.talk_red);
        }

        if (publication.isSmoking()){
            smoking.setImageResource(R.drawable.smoking_green);
        }else{
            smoking.setImageResource(R.drawable.smoking_red);
        }
    }

    public void initComponents(){
        from = view.findViewById(R.id.fieldSearchOrderFrom);
        to = view.findViewById(R.id.fieldSearchOrderTo);
        peoples = view.findViewById(R.id.fieldSearchOrderPeoples);
        date = view.findViewById(R.id.fieldSearchOrderDate);
        time = view.findViewById(R.id.fieldSearchOrderTime);
        price = view.findViewById(R.id.fieldSearchOrderPrice);
        comment = view.findViewById(R.id.fieldSearchOrderComment);
        music = view.findViewById(R.id.imageViewMusic);
        pets = view.findViewById(R.id.imageViewPets);
        talk = view.findViewById(R.id.imageViewTalk);
        smoking = view.findViewById(R.id.imageViewSmoking);
        finish = view.findViewById(R.id.btnToSearchOrderMain);
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
    public static FragmentHomeSearchResultOrder newInstance(Bundle bundle){
        FragmentHomeSearchResultOrder fragmentHomeSearchResult = new FragmentHomeSearchResultOrder();
        fragmentHomeSearchResult.setArguments(bundle);
        return fragmentHomeSearchResult;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static FragmentHomeSearchResultOrder newInstance(){
        return new FragmentHomeSearchResultOrder();
    }
}
