package com.ayungi.travelapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class GeocodingResponse(
    val features: List<GeocodingFeature>
)

data class GeocodingFeature(
    val id: String,
    val place_name: String, // Полное название места
    val text: String,       // Основной текст (например, имя объекта)
)

interface ReverseGeocodingApi {
    // Обратное геокодирование: передаём координаты в формате "longitude,latitude"
    @GET("geocoding/v5/mapbox.places/{coordinates}.json")
    fun getReverseGeocode(
        @Path("coordinates") coordinates: String,
        @Query("access_token") accessToken: String,
        @Query("limit") limit: Int = 1
    ): Call<GeocodingResponse>
}
