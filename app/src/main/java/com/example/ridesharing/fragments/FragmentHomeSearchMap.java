package com.example.ridesharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ridesharing.R;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class FragmentHomeSearchMap extends Fragment{
    View view;
    private MapView mapView;
    private final String MAPKIT_API_KEY = "bbc0102a-36e1-428d-bf12-05720600cffc";
    private final Point TARGET_LOCATION = new Point(59.873808, 30.316197);

    public FragmentHomeSearchMap() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_search_map, container, false);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(getContext());


        mapView = (MapView) view.findViewById(R.id.mapwiew);
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null
        );
        return view;
    }

    public void two(View view){
//        Intent intent = new Intent(this, MainActivity2.class);
//        startActivity(intent);
    }

//    @Override
//    public void onStop() {
//        mapView.onStop();
//        MapKitFactory.getInstance().onStop();
//        super.onStop();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        MapKitFactory.getInstance().onStart();
//        mapView.onStart();
//    }

    public static FragmentHomeSearchMap newInstance(){
        return new FragmentHomeSearchMap();
    }
}
