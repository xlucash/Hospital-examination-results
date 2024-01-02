package me.lukaszpisarczyk.Hospital.controllers;

import lombok.RequiredArgsConstructor;
import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.services.ExaminationResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/examination-result")
@RequiredArgsConstructor
public class ExaminationResultController {
    private final ExaminationResultService examinationResultService;

    @PostMapping
    public ResponseEntity<?> saveExaminationResult(
            @RequestPart("examinationResult")ExaminationRequestDto examinationRequestDto,
            @RequestPart("image") List<MultipartFile> images) {
        ExaminationResult examinationResult = examinationResultService.saveExaminationResult(examinationRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(examinationResult);
    }
}
