package com.example.ridesharing.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Класс-адаптер для RecyclerView, который связывает источник данных с визуальным отображением
 * элемента на активности/фрагменте (в зависимости от того, где вызывается)
 */
public class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerMessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private final List<ClassMessage> mMessages;
    private final LayoutInflater inflater;

    private FirebaseUser user;

    /**
     * Конструктор класса адаптера
     * @param context Выбор места назождения (Чаще всего передается this, или getContext(),
     *                или getActivity() - по ситуации)
     * @param mMessages Лист публикаций (источник данных)
     */
    public RecyclerMessageAdapter(Context context, List<ClassMessage> mMessages) {
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
    public RecyclerMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = inflater.inflate(R.layout.recycler_message_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.recycler_message_left, parent, false);
            return new ViewHolder(view);
        }
    }

    /**
     * Имплементируемый метод onBindViewHolder - отвечает за установку информации на каждую
     * конкретную ячейку RecyclerView. Здесь же срабатывает прослушиватель.
     * @param holder Контейнер
     * @param position Определяет позицию конкретно нажатого ViewHolder в RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerMessageAdapter.ViewHolder holder, int position) {
        ClassMessage messages = mMessages.get(position);

        long time = messages.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        //holder.userName.setText("");
        holder.message.setText(messages.getMessage_text());
        holder.time.setText(format.format(cal.getTime()));
        //holder.countOfMessages.setText(messages.getCountOfMessages());
    }

    /**
     * @return Возвращает количество элементов в списке
     */
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (mMessages.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    /**
     * Непосредственно класс ViewHolder, позволяющий заполнять пространство внутри ячейки
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.rv_chat_user_message);
            time = itemView.findViewById(R.id.rv_chat_time);
        }
    }
}
