package com.ayungi.travelapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.requests.DiaryRequestDto;
import com.ayungi.travelapp.model.data.responses.DiaryResponseDto;
import com.ayungi.travelapp.model.repository.DiaryRepository;
import com.ayungi.travelapp.utils.Resource;

import java.util.List;

public class DiaryViewModel extends ViewModel {
    private final DiaryRepository diaryRepository;

    public DiaryViewModel() {
        diaryRepository = DiaryRepository.getInstance();
    }

    public LiveData<Resource<DiaryResponseDto>> createDiary(String title, String date, String content) {
        return diaryRepository.createDiary(title, date, content);
    }

    // Получение списка дневников
    public LiveData<Resource<List<DiaryResponseDto>>> getAllDiaries() {
        return diaryRepository.getAllDiaries();
    }

    public LiveData<Resource<DiaryResponseDto>> getDiaryById(long diaryId) {
        return diaryRepository.getDiaryById(diaryId);
    }

    public LiveData<Resource<DiaryResponseDto>> updateDiary(long diaryId, DiaryRequestDto request) {
        return diaryRepository.updateDiary(diaryId, request);
    }

    public LiveData<Resource<Void>> deleteDiary(Long id) {
        return diaryRepository.deleteDiary(id);
    }
}
