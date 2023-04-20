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
import com.example.ridesharing.commonClasses.ClassChats;
import com.example.ridesharing.commonClasses.ClassMessage;
import com.example.ridesharing.commonClasses.ClassPublication;
import com.example.ridesharing.recycler.RecyclerChatAdapter;
import com.example.ridesharing.recycler.RecyclerYourPublicationsAndTripsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Фрагмент, содержащий чат
 */
public class FragmentHomeChat extends Fragment{
    View view;
    ArrayList<ClassMessage> dialogs = new ArrayList<ClassMessage>();
    private Bundle bundleSet = new Bundle();

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

        view = inflater.inflate(R.layout.fragment_home_chat, container, false);
        createOrdersList();

        return view;
    }

    /**
     * Создание листа путем получения данных с сервера
     */
    public void createOrdersList() {
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
                                for(DataSnapshot messages : childChildSnapshot.getChildren()){
                                    ClassMessage message = messages.getValue(ClassMessage.class);
                                    messagesFill.add(message);
                                }
                            }
                        }
                    }
                }
                rvPublications(messagesFill);
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
    public void rvPublications(ArrayList<ClassMessage> list) {
        dialogs.clear();
        dialogs.addAll(list);
        RecyclerView rvChat = view.findViewById(R.id.recyclerChat);
        RecyclerChatAdapter.OnChatClickListener publicationsClickListener = (dialogs, position) -> {
            bundleSet.putString("ChatId", dialogs.getChat_id());
            loadFragmentFromTop(FragmentHomeChatMessages.newInstance(bundleSet), "ChatMessages");
        };
        RecyclerChatAdapter adapter = new RecyclerChatAdapter(getContext(), dialogs, publicationsClickListener);
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
