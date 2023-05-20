package com.example.ridesharing.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassChats;
import com.example.ridesharing.commonClasses.ClassMessage;
import com.example.ridesharing.commonClasses.ClassUser;
import com.example.ridesharing.fragments.FragmentLoginAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Класс-адаптер для RecyclerView, который связывает источник данных с визуальным отображением
 * элемента на активности/фрагменте (в зависимости от того, где вызывается)
 */
public class RecyclerChatAdapter extends RecyclerView.Adapter<RecyclerChatAdapter.ViewHolder> {

    public final OnChatClickListener onClickListener;
    private final List<ClassMessage> mMessages;
    private final LayoutInflater inflater;
    private Context context;

    /**
     * Интерфейс взаимодействия с RecyclerView, а именно установка прослушивателя на взаимодействие
     */
    public interface OnChatClickListener {
        void onChatClick(ClassMessage messages, int position);
    }

    /**
     * Конструктор класса адаптера
     * @param context Выбор места назождения (Чаще всего передается this, или getContext(),
     *                или getActivity() - по ситуации)
     * @param mMessages Лист публикаций (источник данных)
     * @param onClickListener Прослушиватель
     */
    public RecyclerChatAdapter(Context context, List<ClassMessage> mMessages, OnChatClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.mMessages = mMessages;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Имплементируемый метод onCreateViewHolder - отвечает за создание поверхности каждого элемента
     * RecyclerView
     * @param parent Определяем местонахождение ячейки
     * @param viewType --
     * @return Возврат ViewHolder с заполненной инфомрацией
     */
    @Override
    public RecyclerChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_chat, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    /**
     * Имплементируемый метод onBindViewHolder - отвечает за установку информации на каждую
     * конкретную ячейку RecyclerView. Здесь же срабатывает прослушиватель.
     * @param holder Контейнер
     * @param position Определяет позицию конкретно нажатого ViewHolder в RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerChatAdapter.ViewHolder holder, int position) {
        ClassMessage messages = mMessages.get(position);
        holder.itemView.setOnClickListener(v -> {
            onClickListener.onChatClick(messages, position);
        });

        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),
                R.anim.recycler_item_animation));

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    if(childSnapshot.getKey().equals(messages.getReceiver()) ||
                            childSnapshot.getKey().equals(messages.getSender())){
                        String[] chatMembers = messages.getChat_id().split("-");
                        for(int i=0; i<2; i++){
                            if(!chatMembers[i].equals(FragmentLoginAuth.userID)){
                                getCurrentUser(holder, chatMembers[i]);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        long time = messages.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        //holder.userName.setText(messages);
        try {
            holder.message.setText(messages.getMessage_text().substring(0,27) + "...");
        } catch (Exception e){
            holder.message.setText(messages.getMessage_text());
        }
        holder.time.setText(format.format(cal.getTime()));
        //holder.countOfMessages.setText(messages.getCountOfMessages());
    }

    public void getCurrentUser(@NonNull RecyclerChatAdapter.ViewHolder holder, String id){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUser user = snapshot.getValue(ClassUser.class);
                holder.userName.setText(user.getName());
                if (!user.getAvatar().equals("")){
                    Glide.with(context).load(user.getAvatar())
                            .into(holder.avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * @return Возвращает количество элементов в списке
     */
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * Непосредственно класс ViewHolder, позволяющий заполнять пространство внутри ячейки
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView message;
        public TextView time;
        public ImageView avatar;
        //public TextView countOfMessages;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.rv_chat_user_name);
            message = itemView.findViewById(R.id.rv_chat_message);
            time = itemView.findViewById(R.id.rv_chat_time);
            avatar = itemView.findViewById(R.id.chatAvatar);
            //countOfMessages = itemView.findViewById(R.id.rv_chat_count_of_messages);
        }
    }
}
