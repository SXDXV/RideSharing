package com.example.ridesharing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassFirstLastItemDecorator;
import com.example.ridesharing.commonClasses.ClassMessage;
import com.example.ridesharing.commonClasses.ClassUser;
import com.example.ridesharing.recycler.RecyclerMessageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс фрагмента публикации объявления и заполнения полей.
 */
public class FragmentHomeChatMessages extends Fragment{
    private View view;

    private ImageView backToChats;
    private ImageView sendMessageIcon;
    private TextInputEditText messageInput;
    private TextView userName;

    private String chatName;
    private String receiver;

    private Bundle bundleGet = new Bundle();
    private Bundle bundleSet = new Bundle();

    private Context mContext;

    RecyclerMessageAdapter messageAdapter;
    List<ClassMessage> messageList;
    RecyclerView chatMessagesRecycler;

    /**
     * Конструктор класса фрагмента
     */
    public FragmentHomeChatMessages() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * Метод, срабатываемый при создании фрагмента
     * @param inflater Связывает содержимое XML-файла с View
     * @param container ViewGroup
     * @param savedInstanceState Сохраненные параметры и аргументы фрагмента
     * @return Возвращает View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        view = inflater.inflate(R.layout.fragment_home_chat_messages, container, false);
        initComponents();
        bundleGet = getArguments();

        /**
         * Заполнение полей существубщими значениями при возвращении на фрагмент
         */
        try {
            if(bundleGet != null){
                chatName = bundleGet.getString("ChatId");
                createUserProfile(chatName);
            }

        }catch (Exception e){
            //Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
        }

        String[] opponents = chatName.split("-");

        if (opponents[0].equals(FragmentLoginAuth.userID)){
            receiver = opponents[1];
        } else {
            receiver = opponents[0];
        }

        chatMessagesRecycler.setHasFixedSize(true);
        chatMessagesRecycler.addItemDecoration(new ClassFirstLastItemDecorator(250));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        chatMessagesRecycler.setLayoutManager(linearLayoutManager);

        readMessages(mContext ,FragmentLoginAuth.userID, receiver);

        /**
         * Возврат на фрагмент чатов
         */
        backToChats.setOnClickListener(v -> {
            loadFragmentFromDown(FragmentHomeChat.newInstance(), "Chats");
        });

        sendMessageIcon.setOnClickListener(v ->{
            sendMessage();
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    /**
     * Заполнение базы данных по шаблону appointMap
     */
    public void sendMessage(){
        long time = System. currentTimeMillis();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats/" + chatName + "/MessagesPool");

        Map<String, Object> appointMap = new HashMap<>();
        appointMap.put("chat_id", chatName);
        appointMap.put("message_text", messageInput.getText().toString().trim());
        appointMap.put("receiver", receiver);
        appointMap.put("sender", FragmentLoginAuth.userID);
        appointMap.put("time", time);

        myRef.child("Message" + time).setValue(appointMap);
        messageInput.setText("");
    }

    public void readMessages(Context context ,String userId, String opponentId){
        messageList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats/" + chatName + "/MessagesPool");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    ClassMessage message = childSnapshot.getValue(ClassMessage.class);
                    if (message.getReceiver().equals(userId) && message.getSender().equals(opponentId) ||
                            message.getReceiver().equals(opponentId) && message.getSender().equals(userId)){
                        messageList.add(message);
                    }
                }
                messageAdapter = new RecyclerMessageAdapter(context, messageList);
                chatMessagesRecycler.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createUserProfile(String chatId){
        chatId = chatId.replace(FragmentLoginAuth.userID, "");
        chatId = chatId.replace("-", "");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + chatId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUser user = snapshot.getValue(ClassUser.class);
                userName.setText(user.getName().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
     * Инициализация компонентов View
     */
    public void initComponents(){
        backToChats = view.findViewById(R.id.backToChats);
        chatMessagesRecycler = view.findViewById(R.id.recyclerChatMessages);
        messageInput = view.findViewById(R.id.fieldMessageSend);
        sendMessageIcon = view.findViewById(R.id.sendMessage);
        userName = view.findViewById(R.id.messages_chat_user_name);
    }

    /**
     * Метод создания нового фрагмента с указанием передачи данных
     * @param bundle Передаваемые во фрагмент данные
     * @return возврат нового фрагмента
     */
    public static FragmentHomeChatMessages newInstance(Bundle bundle){
        FragmentHomeChatMessages fragmentHomeChatMessages = new FragmentHomeChatMessages();
        fragmentHomeChatMessages.setArguments(bundle);
        return fragmentHomeChatMessages;
    }

    /**
     * Метод создания нового фрагмента без указания передачи данных
     * @return вощвращает новый пустой фрагмент
     */
    public static FragmentHomeChatMessages newInstance(){
        return new FragmentHomeChatMessages();
    }
}
