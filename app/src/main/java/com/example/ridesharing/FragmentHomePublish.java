package com.example.ridesharing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ridesharing.dadata.Address;
import com.example.ridesharing.recycler.RecyclerYourPublicationsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FragmentHomePublish extends Fragment{
    View view;
    private static final String VIEW_NAME = "FragmentHomePublish";
    private String UiD;

    Button toPublishMain;
    ArrayList<ClassPublication> publications = new ArrayList<ClassPublication>();

    public FragmentHomePublish() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_publish, container, false);
        UiD = FragmentLoginAuth.userID;

        createPublicationsList();
        toPublishMain = view.findViewById(R.id.btnToPublishMain);
        toPublishMain.setOnClickListener(v -> {
            loadFragmentFromTop(FragmentHomePublishMain.newInstance(), "publishing");
        });
        return view;
    }

    public void rvPublications(ArrayList<ClassPublication> list){
        publications.addAll(list);
        RecyclerView rvNews = view.findViewById(R.id.recyclerYourPublications);
        RecyclerYourPublicationsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {

        };
        RecyclerYourPublicationsAdapter adapter = new RecyclerYourPublicationsAdapter(getContext() , publications, publicationsClickListener);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

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
                rvPublications(publicationsFill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadFragmentFromTop(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_top);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static FragmentHomePublish newInstance(){
        return new FragmentHomePublish();
    }
}
