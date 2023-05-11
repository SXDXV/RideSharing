package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ridesharing.R;
import com.example.ridesharing.commonClasses.ClassPublication;
import com.example.ridesharing.commonClasses.ClassResizeAnimation;
import com.example.ridesharing.commonClasses.ClassTrip;
import com.example.ridesharing.commonClasses.ClassUser;
import com.example.ridesharing.recycler.RecyclerYourPublicationsAndTripsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Фрагмент, содержащий профиль
 */
public class FragmentHomeProfile extends Fragment{
    private View view;
    private ImageView blurAvatar;
    private ImageView avatar;
    private TextView noTripsText;
    private CardView noTripsCard;
    private TextView userName;
    private Button toEdit;

    /**
     * Констуктор класса фрагмента
     */
    public FragmentHomeProfile() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_profile, container, false);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        initComponents();
        readUserData();

        createTripsList();

        toEdit.setOnClickListener(v -> {
            loadFragmentFromTop(FragmentHomeProfileEdit.newInstance(), "profileEdit");
        });

        return view;
    }

    public void readUserData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users/" + FragmentLoginAuth.userID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ClassUser user = snapshot.getValue(ClassUser.class);
                userName.setText(user.getName());
                blurAvatar.setImageAlpha(100);
                if (!user.getAvatar().equals("")){
                    Glide.with(getActivity()).load(user.getAvatar())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                            .into(blurAvatar);
                    Glide.with(getActivity()).load(user.getAvatar())
                            .into(avatar);
                } else {
                    Glide.with(getActivity()).load(R.drawable.no_photo_avatar)
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                            .into(blurAvatar);
                    Glide.with(getActivity()).load(R.drawable.no_photo_avatar)
                            .into(avatar);
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
    public void rvPublications(ArrayList<ClassPublication> list) {
        RecyclerView rvNews = view.findViewById(R.id.recyclerYourTrips);
        RecyclerYourPublicationsAndTripsAdapter.OnPublicationClickListener publicationsClickListener = (publications, position) -> {

        };
        RecyclerYourPublicationsAndTripsAdapter adapter = new RecyclerYourPublicationsAndTripsAdapter(getContext(), list, publicationsClickListener);
        rvNews.setAdapter(adapter);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void createTripsList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("trips");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> references = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    for (DataSnapshot childChildSnapshot : childSnapshot.getChildren()){
                        if (childChildSnapshot.getKey().equals(FragmentLoginAuth.userID)){
                            ClassTrip trip = childSnapshot.getValue(ClassTrip.class);
                            references.add(trip.getTrip_id());
                        }
                    }
                }
                if (!(references.size() == 0)){
                    resizeEmptyTrips(noTripsCard, 0,0);
                    resizeEmptyTrips(noTripsText, 0,0);
                    createPublicationsList(references);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createPublicationsList(ArrayList<String> references){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("publish");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ClassPublication> yourTrips = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()){
                    for (String reference : references){
                        if (childSnapshot.getKey().equals(reference)){
                            ClassPublication classPublication = childSnapshot.getValue(ClassPublication.class);
                            yourTrips.add(classPublication);
                        }
                    }
                }
                rvPublications(yourTrips);
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

    public void initComponents(){
        blurAvatar = view.findViewById(R.id.blurAvatarProfile);
        noTripsCard = view.findViewById(R.id.noTripsCard);
        noTripsText = view.findViewById(R.id.noTripsText);
        userName = view.findViewById(R.id.userNameProfile);
        toEdit = view.findViewById(R.id.btnToSearchOrderMain);
        avatar = view.findViewById(R.id.imageAvatarFocus);
    }

    /**
     * Метод создания нового фрагмента с указанием передачи данных
     * @param bundle Передаваемые во фрагмент данные
     * @return возврат нового фрагмента
     */
    public static FragmentHomeProfile newInstance(Bundle bundle){
        FragmentHomeProfile fragmentHomeProfile = new FragmentHomeProfile();
        fragmentHomeProfile.setArguments(bundle);
        return fragmentHomeProfile;
    }

    public static FragmentHomeProfile newInstance(){
        return new FragmentHomeProfile();
    }
}
