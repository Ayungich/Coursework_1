package com.ayungi.travelapp.network;

import com.ayungi.travelapp.model.data.requests.BudgetCategoryRequestDto;
import com.ayungi.travelapp.model.data.requests.BudgetItemRequestDto;
import com.ayungi.travelapp.model.data.requests.CoordinateRequestDto;
import com.ayungi.travelapp.model.data.requests.DiaryRequestDto;
import com.ayungi.travelapp.model.data.requests.LoginRequestDto;
import com.ayungi.travelapp.model.data.requests.PackingItemEditRequestDto;
import com.ayungi.travelapp.model.data.requests.PackingItemRequestDto;
import com.ayungi.travelapp.model.data.requests.PackingItemUpdateRequestDto;
import com.ayungi.travelapp.model.data.requests.RegistrationRequestDto;
import com.ayungi.travelapp.model.data.requests.TripRequestDto;
import com.ayungi.travelapp.model.data.requests.UserCredentialsDto;
import com.ayungi.travelapp.model.data.responses.AvatarUploadResponseDto;
import com.ayungi.travelapp.model.data.responses.BudgetCategoryDto;
import com.ayungi.travelapp.model.data.responses.BudgetItemResponseDto;
import com.ayungi.travelapp.model.data.responses.CoordinateResponseDto;
import com.ayungi.travelapp.model.data.responses.DiaryResponseDto;
import com.ayungi.travelapp.model.data.responses.LoginResponseDto;
import com.ayungi.travelapp.model.data.responses.PackingItemResponseDto;
import com.ayungi.travelapp.model.data.responses.RegistrationResponseDto;
import com.ayungi.travelapp.model.data.responses.RouteMarkerResponseDto;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsDto;
import com.ayungi.travelapp.model.data.responses.TripBudgetAnalyticsResponseDto;
import com.ayungi.travelapp.model.data.responses.TripResponseDto;
import com.ayungi.travelapp.model.data.responses.UserResponseDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ЛОГИН: POST /api/auth/login
    @POST("/api/auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequest);

    // РЕГИСТРАЦИЯ: POST /api/registration
    @POST("/api/registration")
    Call<RegistrationResponseDto> register(@Body RegistrationRequestDto registrationRequest);

    @GET("/api/users/{id}")
    Call<UserResponseDto> getUserById(@Path("id") long userId);

    @PATCH("/api/users/{id}/credentials")
    Call<UserResponseDto> patchUserCredentials(
            @Path("id") long userId,
            @Body UserCredentialsDto dto
    );

    @DELETE("/api/users/{id}")
    Call<Void> deleteUser(@Path("id") long userId);

    // Создание дневника: POST /api/diaries
    @POST("/api/diaries")
    Call<DiaryResponseDto> createDiary(@Body DiaryRequestDto diaryRequest);

    // Получение списка дневников: GET /api/diaries
    @GET("/api/diaries")
    Call<List<DiaryResponseDto>> getAllDiaries();

    // Получение подробностей дневника по id
    @GET("/api/diaries/{id}")
    Call<DiaryResponseDto> getDiaryById(@Path("id") long id);

    // Обновление дневника по id
    @PUT("/api/diaries/{id}")
    Call<DiaryResponseDto> updateDiary(@Path("id") long id, @Body DiaryRequestDto diaryRequest);

    @DELETE("/api/diaries/{id}")
    Call<Void> deleteDiary(@Path("id") Long id);

    @Multipart
    @POST("/api/avatar/{userId}/upload")
    Call<AvatarUploadResponseDto> uploadAvatar(
            @Path("userId") long userId,
            @Part MultipartBody.Part file
    );

    @GET("/api/avatar/{avatarId}")
    Call<ResponseBody> getAvatar(@Path("avatarId") Long avatarId);

    @POST("/api/trips")
    Call<TripResponseDto> createTrip(@Body TripRequestDto tripRequest);

    @GET("/api/trips")
    Call<List<TripResponseDto>> getAllTrips();

    @PUT("/api/trips/{id}")
    Call<TripResponseDto> updateTrip(@Path("id") long id, @Body TripRequestDto tripRequest);

    @DELETE("/api/trips/{id}")
    Call<Void> deleteTrip(@Path("id") Long id);

    // Получить список вещей для конкретного путешествия по tripId
    @GET("/api/packing-items/by-trip")
    Call<List<PackingItemResponseDto>> getAllPackingItems(@Query("tripId") Long tripId);

    // Создать новую вещь для конкретного путешествия
    @POST("/api/packing-items/by-trip")
    Call<PackingItemResponseDto> createPackingItem(@Body PackingItemRequestDto request);

    // Обновить состояние вещи по её id
    @PUT("/api/packing-items/{id}")
    Call<PackingItemResponseDto> updatePackingItem(@Path("id") Long id,
                                                   @Body PackingItemUpdateRequestDto request);

    @PUT("/api/packing-items/{id}/edit")
    Call<PackingItemResponseDto> editPackingItem(@Path("id") Long id, @Body PackingItemEditRequestDto request);

    @DELETE("/api/packing-items/{id}")
    Call<Void> deletePackingItem(@Path("id") Long id);

    // Эндпоинты для категорий бюджета
    @GET("/api/budget-categories/by-trip")
    Call<List<BudgetCategoryDto>> getBudgetCategories(@Query("tripId") Long tripId);

    @POST("/api/budget-categories/by-trip")
    Call<BudgetCategoryDto> createBudgetCategory(@Body BudgetCategoryRequestDto dto);

    @PUT("/api/budget-categories/{id}")
    Call<BudgetCategoryDto> updateBudgetCategory(@Path("id") Long id, @Body BudgetCategoryRequestDto dto);

    @DELETE("/api/budget-categories/{id}")
    Call<Void> deleteBudgetCategory(@Path("id") Long id);

    // Новый метод для получения категории бюджета по её ID
    @GET("/api/budget-categories/{id}")
    Call<BudgetCategoryDto> getBudgetCategoryById(@Path("id") Long id);

    // Эндпоинты для статей расходов
    @GET("/api/budget-items/by-category")
    Call<List<BudgetItemResponseDto>> getBudgetItems(@Query("categoryId") Long categoryId);

    @POST("/api/budget-items/by-category")
    Call<BudgetItemResponseDto> createBudgetItem(@Body BudgetItemRequestDto dto);

    @PUT("/api/budget-items/{id}")
    Call<BudgetItemResponseDto> updateBudgetItem(@Path("id") Long id, @Body BudgetItemRequestDto dto);

    @DELETE("/api/budget-items/{id}")
    Call<Void> deleteBudgetItem(@Path("id") Long id);

    // Получить список меток для конкретного путешествия
    @GET("/api/trips/{tripId}/markers")
    Call<List<RouteMarkerResponseDto>> getMarkersByTrip(@Path("tripId") Long tripId);

    // Создать новую метку для конкретного путешествия
    @POST("/api/trips/{tripId}/markers")
    Call<RouteMarkerResponseDto> createRouteMarker(@Path("tripId") Long tripId, @Body RouteMarkerResponseDto request);

    // Обновить метку маршрута
    @PUT("/api/markers/{id}")
    Call<RouteMarkerResponseDto> updateRouteMarker(@Path("id") Long id, @Body RouteMarkerResponseDto request);

    // Удалить метку маршрута
    @DELETE("/api/markers/{id}")
    Call<Void> deleteRouteMarker(@Path("id") Long id);

    @GET("/api/coordinates/by-trip")
    Call<List<CoordinateResponseDto>> getCoordinatesByTrip(@Query("tripId") Long tripId);

    @POST("/api/coordinates/by-trip")
    Call<CoordinateResponseDto> createCoordinate(@Query("tripId") Long tripId, @Body CoordinateRequestDto dto);

    @DELETE("/api/coordinates/{id}")
    Call<Void> deleteCoordinate(@Path("id") Long id);

    // Подтверждение email (GET, параметр token)
    @GET("/api/auth/confirm")
    Call<Void> confirmEmail(@Query("token") String token);

    // Запрос ссылки для сброса пароля (POST, параметр email)
    @POST("/api/auth/request-password-reset")
    Call<Void> requestPasswordReset(@Query("email") String email);

    // Сброс пароля (POST, параметры token и newPassword)
    @POST("/api/auth/reset-password")
    Call<Void> resetPassword(@Query("token") String token,
                             @Query("newPassword") String newPassword);

    @GET("/api/trip-analytics")
    Call<TripBudgetAnalyticsDto> getTripBudgetAnalytics(@Query("tripId") Long tripId);

    @GET("/api/analytics/trip/{tripId}")
    Call<TripBudgetAnalyticsResponseDto> getTripAnalytics(
            @Path("tripId") long tripId,
            @Query("totalBudget") double totalBudget
    );
}

