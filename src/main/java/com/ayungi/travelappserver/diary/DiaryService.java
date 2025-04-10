package com.ayungi.travelappserver.diary;

import com.ayungi.travelappserver.dto.diary.DiaryResponseDto;
import com.ayungi.travelappserver.exception.DiaryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<Diary> getAllDiaries() {
        return diaryRepository.findAll();
    }

    public Diary getDiaryById(Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryNotFoundException("Diary with ID " + id + " not found"));
    }

    @Transactional
    public Diary createDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    @Transactional
    public Diary updateDiary(Long id, Diary diaryDetails) {
        Diary existingDiary = getDiaryById(id);
        existingDiary.setTitle(diaryDetails.getTitle());
        existingDiary.setDate(diaryDetails.getDate());
        existingDiary.setContent(diaryDetails.getContent());
        return diaryRepository.save(existingDiary);
    }

    @Transactional
    public void deleteDiary(Long id) {
        if (!diaryRepository.existsById(id)) {
            throw new DiaryNotFoundException("Diary with ID " + id + " not found");
        }
        diaryRepository.deleteById(id);
    }

    public DiaryResponseDto toDiaryResponseDto(Diary diary) {
        return new DiaryResponseDto(
                diary.getId(),
                diary.getTitle(),
                diary.getDate(),
                diary.getContent(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }
}
