package com.ayungi.travelapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<List<Double>>
)

interface DirectionsApiService {
    @GET("directions/v5/mapbox/driving/{coordinates}")
    fun getRoute(
        @Path("coordinates") coordinates: String,
        @Query("geometries") geometries: String = "geojson",
        @Query("access_token") accessToken: String
    ): Call<DirectionsResponse>
}
