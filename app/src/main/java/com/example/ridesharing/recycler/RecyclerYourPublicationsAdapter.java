package com.example.ridesharing.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.ClassPublication;
import com.example.ridesharing.R;

import java.util.List;

public class RecyclerYourPublicationsAdapter extends RecyclerView.Adapter<RecyclerYourPublicationsAdapter.ViewHolder> {

    public final OnPublicationClickListener onClickListener;
    private final List<ClassPublication> mPublications;
    private final LayoutInflater inflater;

    public interface OnPublicationClickListener {
        void onPublicationsClick(ClassPublication publications, int position);
    }

    public RecyclerYourPublicationsAdapter(Context context, List<ClassPublication> mPublications, OnPublicationClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.mPublications = mPublications;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerYourPublicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_your_publications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerYourPublicationsAdapter.ViewHolder holder, int position) {
        ClassPublication publications = mPublications.get(position);
        holder.itemView.setOnClickListener(v -> {
            //вызов метода слушателя, передавая ему данные
            onClickListener.onPublicationsClick(publications, position);
        });

        holder.title.setText(publications.getFrom() + " - " + publications.getTo());
        holder.price.setText("$" + publications.getPrice());
    }

    @Override
    public int getItemCount() {
        return mPublications.size();
    }

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
