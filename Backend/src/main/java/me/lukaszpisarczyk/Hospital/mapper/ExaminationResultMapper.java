package me.lukaszpisarczyk.Hospital.mapper;

import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.models.Image;

import java.util.List;
import java.util.stream.Collectors;

public class ExaminationResultMapper {
    public ExaminationResultDto mapExaminationResultToDto(ExaminationResult examinationResult) {
        ExaminationResultDto examinationResultDto = new ExaminationResultDto();
        examinationResultDto.setId(examinationResult.getId());
        examinationResultDto.setType(examinationResult.getType());
        examinationResultDto.setTitle(examinationResult.getTitle());
        examinationResultDto.setDescription(examinationResult.getDescription());
        examinationResultDto.setExaminationDate(examinationResult.getExaminationDate());
        examinationResultDto.setPatientId(examinationResult.getPatient().getId());
        examinationResultDto.setDoctorId(examinationResult.getDoctor().getId());

        List<Long> imageIds = examinationResult.getImages().stream()
                .map(Image::getId)
                .collect(Collectors.toList());
        examinationResultDto.setImageIds(imageIds);

        return examinationResultDto;
    }
}
