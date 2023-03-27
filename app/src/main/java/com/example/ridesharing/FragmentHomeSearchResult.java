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

public class FragmentHomeSearchResult extends Fragment{
    View view;
    Bundle bundleGet = new Bundle();
    ArrayList<ClassPublication> orders = new ArrayList<ClassPublication>();

    public FragmentHomeSearchResult() {
    }

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

    public void rvPublications(ArrayList<ClassPublication> list){
        orders.addAll(list);
        RecyclerView rvNews = view.findViewById(R.id.recyclerSearchOffer);
        RecyclerYourPublicationsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {

        };
        RecyclerYourPublicationsAdapter adapter = new RecyclerYourPublicationsAdapter(getContext() , orders, publicationsClickListener);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

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

    public static FragmentHomeSearchResult newInstance(Bundle bundle){
        FragmentHomeSearchResult fragmentHomeSearchResult = new FragmentHomeSearchResult();
        fragmentHomeSearchResult.setArguments(bundle);
        return fragmentHomeSearchResult;
    }

    public static FragmentHomeSearchResult newInstance(){
        return new FragmentHomeSearchResult();
    }
}
