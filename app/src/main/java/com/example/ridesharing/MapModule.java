package com.example.ridesharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapModule extends AppCompatActivity {

    private MapView mapView;
    private final String MAPKIT_API_KEY = "bbc0102a-36e1-428d-bf12-05720600cffc";
    private final Point TARGET_LOCATION = new Point(59.873808, 30.316197);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_map_module);

        mapView = (MapView) findViewById(R.id.mapwiew);
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 18.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null
        );
    }

    public void two(View view){
//        Intent intent = new Intent(this, MainActivity2.class);
//        startActivity(intent);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}