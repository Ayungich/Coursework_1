package com.ayungi.travelapp.view.activities.route;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import com.ayungi.travelapp.R;
import com.ayungi.travelapp.model.data.responses.RouteMarkerResponseDto;
import com.ayungi.travelapp.utils.Resource;
import com.ayungi.travelapp.view.activities.trip.TripsActivity;
import com.ayungi.travelapp.viewmodel.RouteMarkerViewModel;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageButton backButton;
    private RouteMarkerViewModel routeMarkerViewModel;
    private Long tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_routes);

        initViews();

        // Получаем tripId из Intent
        tripId = getIntent().getLongExtra("TRIP_ID", -1);
        if (tripId == -1L) {
            Toast.makeText(this, "Ошибка: ID путешествия не передан", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);

        // Устанавливаем начальную позицию карты (Москва)
        GeoPoint startPoint = new GeoPoint(55.7558, 37.6173);
        mapView.getController().setZoom(10.0);
        mapView.getController().setCenter(startPoint);

        routeMarkerViewModel = new ViewModelProvider(this).get(RouteMarkerViewModel.class);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoutesActivity.this, TripsActivity.class);
            startActivity(intent);
        });

        loadMarkers();
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
    }

    private void loadMarkers() {
        routeMarkerViewModel.getMarkersByTrip(tripId).observe(this, resource -> {
            if (resource == null) return;
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                List<RouteMarkerResponseDto> markers = resource.data;
                for (RouteMarkerResponseDto markerDto : markers) {
                    addMarkerToMap(markerDto);
                }
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Ошибка загрузки меток: " + resource.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addMarkerToMap(RouteMarkerResponseDto markerDto) {
        GeoPoint position = new GeoPoint(markerDto.getLatitude(), markerDto.getLongitude());
        Marker marker = new Marker(mapView);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(markerDto.getTitle());
        marker.setSnippet(markerDto.getDescription());
        marker.setOnMarkerClickListener((m, mapView) -> {
            Toast.makeText(this, m.getTitle() + "\n" + m.getSnippet(), Toast.LENGTH_LONG).show();
            return true;
        });
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }
}
