package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExaminationResultService {
    ExaminationResult saveExaminationResult(ExaminationRequestDto examinationRequestDto, List<MultipartFile> images);
    ExaminationResultDto getExaminationResult(Long id);
    List<ExaminationResultDto> getExaminationResultByPatient(String type);
    List<ExaminationResultDto> getExaminationResultByDoctor(String type);
    byte[] processExaminationResultPdf(Long examinationResultId);
}
