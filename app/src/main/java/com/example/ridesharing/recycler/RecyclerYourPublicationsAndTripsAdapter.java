package com.example.ridesharing.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.commonClasses.ClassPublication;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassTrip;

import java.util.List;

/**
 * Класс-адаптер для RecyclerView, который связывает источник данных с визуальным отображением
 * элемента на активности/фрагменте (в зависимости от того, где вызывается)
 */
public class RecyclerYourPublicationsAndTripsAdapter extends RecyclerView.Adapter<RecyclerYourPublicationsAndTripsAdapter.ViewHolder> {

    public final OnPublicationClickListener onClickListener;
    private final List<ClassPublication> mPublications;
    private final LayoutInflater inflater;

    /**
     * Интерфейс взаимодействия с RecyclerView, а именно установка прослушивателя на взаимодействие
     */
    public interface OnPublicationClickListener {
        void onPublicationsClick(ClassPublication publications, int position);
    }

    /**
     * Конструктор класса адаптера
     * @param context Выбор места нахождения (Чаще всего передается this, или getContext(),
     *                или getActivity() - по ситуации)
     * @param mPublications Лист публикаций (источник данных)
     * @param onClickListener Прослушиватель
     */
    public RecyclerYourPublicationsAndTripsAdapter(Context context, List<ClassPublication> mPublications, OnPublicationClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.mPublications = mPublications;
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
    public RecyclerYourPublicationsAndTripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_your_publications_and_trips, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Имплементируемый метод onBindViewHolder - отвечает за установку информации на каждую
     * конкретную ячейку RecyclerView. Здесь же срабатывает прослушиватель.
     * @param holder Контейнер
     * @param position Определяет позицию конкретно нажатого ViewHolder в RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerYourPublicationsAndTripsAdapter.ViewHolder holder, int position) {
        ClassPublication publications = mPublications.get(position);
        holder.itemView.setOnClickListener(v -> {
            onClickListener.onPublicationsClick(publications, position);
        });

        holder.title.setText(publications.getFrom() + " - " + publications.getTo());
        holder.price.setText("$" + publications.getPrice());
    }

    /**
     * @return Возвращает количество элементов в списке
     */
    @Override
    public int getItemCount() {
        return mPublications.size();
    }

    /**
     * Непосредственно класс ViewHolder, позволяющий заполнять пространство внутри ячейки
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fromTo);
            price = itemView.findViewById(R.id.textViewPricePublication);
        }
    }
}
