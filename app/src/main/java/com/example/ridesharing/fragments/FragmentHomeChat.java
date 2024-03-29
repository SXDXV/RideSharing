package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassMessage;
import com.example.ridesharing.commonClasses.ClassResizeAnimation;
import com.example.ridesharing.recycler.RecyclerChatAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Фрагмент, содержащий чат
 */
public class FragmentHomeChat extends Fragment{
    private View view;
    private int viewHeight;
    private ArrayList<ClassMessage> dialogs = new ArrayList<ClassMessage>();
    private Bundle bundleSet = new Bundle();
    private TextView noChatsText;
    private CardView noChatsCard;
    private RecyclerView rvChat;

    /**
     * Констуктор класса фрагмента
     */
    public FragmentHomeChat() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        view = inflater.inflate(R.layout.fragment_home_chat, container, false);
        viewHeight = view.getLayoutParams().height;
        noChatsCard = view.findViewById(R.id.noChatsCard);
        noChatsText = view.findViewById(R.id.noChatsText);
        rvChat = view.findViewById(R.id.recyclerChat);

        createChatsList();

        return view;
    }

    /**
     * Создание листа путем получения данных с сервера
     */
    public void createChatsList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("chats");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ClassMessage> messagesFill = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    String[] keyValues = childSnapshot.getKey().split("-");
                    if (keyValues[0].equals(FragmentLoginAuth.userID) || keyValues[1].equals(FragmentLoginAuth.userID)){
                        for(DataSnapshot childChildSnapshot : childSnapshot.getChildren()){
                            if (childChildSnapshot.getKey().equals("MessagesPool")){
                                ClassMessage message = null;
                                for(DataSnapshot messages : childChildSnapshot.getChildren()){
                                    message = messages.getValue(ClassMessage.class);
                                }
                                messagesFill.add(message);
                            }
                        }
                    }
                }
                if (!(messagesFill.size() == 0)){
                    resizeEmptyTrips(noChatsCard, 0,0);
                    resizeEmptyTrips(noChatsText, 0,0);

                    messagesFill.sort(Comparator.comparingLong(ClassMessage::getTime));
                    Collections.reverse(messagesFill);
                    rvChats(messagesFill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Метод заполнения RecyclerView
     * @param list Передать источник данных
     */
    public void rvChats(ArrayList<ClassMessage> list) {
        dialogs.clear();
        dialogs.addAll(list);
        RecyclerChatAdapter.OnChatClickListener publicationsClickListener = (dialogs, position) -> {
            bundleSet.putString("ChatId", dialogs.getChat_id());
            loadFragmentFromTop(FragmentHomeChatMessages.newInstance(bundleSet), "ChatMessages");
        };
        RecyclerChatAdapter adapter = new RecyclerChatAdapter(getContext(), dialogs, publicationsClickListener);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvChat.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.enter_from_down_recycler));
    }

    /**
     * Анимация изменения размера
     * @param view --
     * @param width --
     * @param height --
     */
    public void resizeEmptyTrips(View view, int width, int height){
        ClassResizeAnimation resizeAnimation = new ClassResizeAnimation(view, width, height);
        resizeAnimation.setDuration(400);
        view.startAnimation(resizeAnimation);
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

    public static FragmentHomeChat newInstance(){
        return new FragmentHomeChat();
    }
}
