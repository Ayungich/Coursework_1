package com.ayungi.travelappserver.diary;

import com.ayungi.travelappserver.dto.diary.DiaryResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // Получить список всех дневников
    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getAllDiaries() {
        List<DiaryResponseDto> diaries = diaryService.getAllDiaries()
                .stream()
                .map(diaryService::toDiaryResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaries);
    }

    // Получить конкретный дневник по ID
    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponseDto> getDiaryById(@PathVariable Long id) {
        Diary diary = diaryService.getDiaryById(id);
        return ResponseEntity.ok(diaryService.toDiaryResponseDto(diary));
    }

    // Создать новый дневник
    @PostMapping
    public ResponseEntity<DiaryResponseDto> createDiary(@RequestBody Diary diary) {
        Diary createdDiary = diaryService.createDiary(diary);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diaryService.toDiaryResponseDto(createdDiary));
    }

    // Обновить существующий дневник
    @PutMapping("/{id}")
    public ResponseEntity<DiaryResponseDto> updateDiary(@PathVariable Long id, @RequestBody Diary diaryDetails) {
        Diary updatedDiary = diaryService.updateDiary(id, diaryDetails);
        return ResponseEntity.ok(diaryService.toDiaryResponseDto(updatedDiary));
    }

    // Удалить дневник по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.noContent().build();
    }
}
