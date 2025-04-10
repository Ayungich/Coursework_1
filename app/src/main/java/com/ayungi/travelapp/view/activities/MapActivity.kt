package com.ayungi.travelapp.view.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.ayungi.travelapp.R
import com.ayungi.travelapp.utils.Resource
import com.ayungi.travelapp.viewmodel.CoordinateViewModel
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates

// Класс для хранения координат и их id
data class Coordinate(val id: Long, val point: Point)

class MapActivity : AppCompatActivity(), PermissionsListener {

    private lateinit var mapView: MapView
    private lateinit var permissionsManager: PermissionsManager

    // Список для хранения координат добавленных меток
    private val markerCoordinates = mutableListOf<Coordinate>()
    private val markersOnMap = mutableListOf<com.mapbox.maps.plugin.annotation.generated.PointAnnotation>()
    private var tripId by Delegates.notNull<Long>()

    // Менеджер аннотаций для маршрута (полилинии)
    private val polylineAnnotationManager by lazy {
        mapView.annotations.createPolylineAnnotationManager()
    }

    // Менеджер для точечных аннотаций (меток)
    private lateinit var pointAnnotationManager: com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager

    private val mapboxAccessToken = "sk.eyJ1IjoiYXl1bmdpIiwiYSI6ImNtOThzMTNjcTA2MXgybnNldXo4N3Zpd2EifQ.z-NwFFprOxGzk017g0FfRw"

    private val coordinateViewModel: CoordinateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        // Получаем tripId из Intent
        tripId = intent.getLongExtra("TRIP_ID", -1)

        // Инициализируем менеджер для точечных аннотаций один раз
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

        // Устанавливаем начальное положение камеры
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(LONGITUDE, LATITUDE))
                .zoom(9.0)
                .build()
        )

        // Инициализация разрешений на доступ к геолокации
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            loadMapStyle()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {(finish())};

        // Кнопка увеличения зума
        val zoomInButton = findViewById<ImageButton>(R.id.zoom_in_button)
        zoomInButton.setOnClickListener {
            val currentZoom = mapView.getMapboxMap().cameraState.zoom
            val newZoom = currentZoom + 1.0  // шаг увеличения зума
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().zoom(newZoom).build())
        }

        // Кнопка уменьшения зума
        val zoomOutButton = findViewById<ImageButton>(R.id.zoom_out_button)
        zoomOutButton.setOnClickListener {
            val currentZoom = mapView.getMapboxMap().cameraState.zoom
            val newZoom = currentZoom - 1.0  // шаг уменьшения зума
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().zoom(newZoom).build())
        }

        // Обработчик кнопки "Добавить метку"
        val addMarkerButton = findViewById<ImageButton>(R.id.add_marker_button)
        addMarkerButton.setOnClickListener {
            val centerPoint = mapView.getMapboxMap().cameraState.center
            addMarker(centerPoint)
            markerCoordinates.add(Coordinate(0, centerPoint)) // временно добавляем id как 0, будет обновлено после сохранения
            // Сохранить метку на сервере
            saveMarker(centerPoint)
        }

        // Обработчик кнопки "Удалить последнюю метку"
        val deleteMarkerButton = findViewById<ImageButton>(R.id.delete_marker_button)
        deleteMarkerButton.setOnClickListener {
            if (markersOnMap.isNotEmpty() && markerCoordinates.isNotEmpty()) { // Добавлена проверка на пустоту
                val markerToRemove = markersOnMap.removeAt(markersOnMap.size - 1)
                pointAnnotationManager.delete(markerToRemove)
                markerCoordinates.removeAt(markerCoordinates.size - 1)
                // Также удалить метку с сервера (если нужно)
                deleteMarkerFromServer(markerCoordinates.size - 1)
            } else {
                Toast.makeText(this, "Нет меток для удаления", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки "Проложить маршрут"
        val routeButton = findViewById<ImageButton>(R.id.route_button)
        routeButton.setOnClickListener {
            if (markerCoordinates.size >= 2) {
                val origin = markerCoordinates.first().point
                val destination = markerCoordinates.last().point
                fetchRoute(origin, destination)
            } else {
                Toast.makeText(this, "Добавьте минимум 2 метки", Toast.LENGTH_LONG).show()
            }
        }

        // Загрузка меток с бэкенда при старте активности
        loadCoordinatesFromServer()
    }

    private fun loadMapStyle() {
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) {
            initLocationComponent()
        }
    }

    private fun initLocationComponent() {
        mapView.location.updateSettings {
            enabled = true
            puckBearing = PuckBearing.COURSE
            puckBearingEnabled = true
            locationPuck = createDefault2DPuck(withBearing = true)
        }
    }

    private fun addMarker(point: Point) {
        val markerBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(markerBitmap)
        val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
        markersOnMap.add(pointAnnotation)
    }

    private fun fetchRoute(origin: Point, destination: Point) {
        val url = "https://api.mapbox.com/directions/v5/mapbox/driving/" +
                "${origin.longitude()},${origin.latitude()};${destination.longitude()},${destination.latitude()}" +
                "?geometries=geojson&access_token=$mapboxAccessToken"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MapActivity, "Ошибка получения маршрута", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@MapActivity, "Ошибка: ${response.code}", Toast.LENGTH_LONG).show()
                        }
                        return
                    }

                    val json = it.body?.string()
                    if (json != null) {
                        try {
                            val jsonResponse = JSONObject(json)
                            val routes = jsonResponse.getJSONArray("routes")
                            if (routes.length() > 0) {
                                val route = routes.getJSONObject(0)
                                val geometry = route.getJSONObject("geometry")
                                val coordinates = geometry.getJSONArray("coordinates")
                                val routePoints = mutableListOf<Point>()
                                for (i in 0 until coordinates.length()) {
                                    val coord = coordinates.getJSONArray(i)
                                    val lng = coord.getDouble(0)
                                    val lat = coord.getDouble(1)
                                    routePoints.add(Point.fromLngLat(lng, lat))
                                }
                                runOnUiThread {
                                    drawRoute(routePoints)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }

    private fun drawRouteBetweenAllMarkers() {
        for (i in 0 until markerCoordinates.size - 1) {
            val origin = markerCoordinates[i].point
            val destination = markerCoordinates[i + 1].point
            fetchRoute(origin, destination)
        }
    }

    private fun drawRoute(routePoints: List<Point>) {
        val polylineOptions = PolylineAnnotationOptions()
            .withPoints(routePoints)
            .withLineColor("#3bb2d0")
            .withLineWidth(5.0)
        polylineAnnotationManager.create(polylineOptions)
    }

    private fun saveMarker(point: Point) {
        val description = "Описание метки"

        // Отправляем метку на сервер
        coordinateViewModel.createCoordinate(tripId, point.latitude(), point.longitude(), description).observe(this, { resource ->
            if (resource.status == Resource.Status.SUCCESS) {
                // Обновляем объект Coordinate с id, который пришел от сервера
                val createdCoordinate = resource.data
                createdCoordinate?.let {
                    val coordinate = Coordinate(it.id, point) // Используем полученный id
                    markerCoordinates.add(coordinate) // Добавляем метку с реальным id
                    Toast.makeText(this, "Метка успешно сохранена", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun deleteMarkerFromServer(index: Int) {
        if (index >= 0 && index < markerCoordinates.size) { // Проверка на существующий индекс
            val coordinateToDelete = markerCoordinates[index]

            // Удаляем метку с сервера по id
            coordinateViewModel.deleteCoordinate(coordinateToDelete.id).observe(this, { resource ->
                if (resource.status == Resource.Status.SUCCESS) {
                    Toast.makeText(this, "Метка успешно удалена", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Ошибка: индекс метки некорректен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCoordinatesFromServer() {
        coordinateViewModel.getCoordinates(tripId).observe(this, { resource ->
            if (resource.status == Resource.Status.SUCCESS) {
                resource.data?.let {
                    for (coordinate in it) {
                        val point = Point.fromLngLat(coordinate.longitude, coordinate.latitude)
                        val coordinateWithId = Coordinate(coordinate.id, point)
                        addMarker(coordinateWithId.point) // Передаем только точку
                        markerCoordinates.add(coordinateWithId)
                    }
                    if (markerCoordinates.size >= 2) {
                        fetchRoute(markerCoordinates.first().point, markerCoordinates.last().point)
                    }
                }
            }
        })
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "Приложению необходимо разрешение для доступа к вашей геолокации.", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            loadMapStyle()
        } else {
            Toast.makeText(this, "Разрешение не предоставлено", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LATITUDE = 40.0
        private const val LONGITUDE = -74.5
    }
}
