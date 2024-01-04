package me.lukaszpisarczyk.Hospital.services.implementation;

import jakarta.transaction.Transactional;
import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;
import me.lukaszpisarczyk.Hospital.mapper.ExaminationResultMapper;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.models.Image;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.ExaminationResultRepository;
import me.lukaszpisarczyk.Hospital.services.ExaminationResultService;
import me.lukaszpisarczyk.Hospital.services.ImageService;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExaminationResultServiceImpl implements ExaminationResultService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExaminationResultRepository examinationResultRepository;

    private ExaminationResultMapper examinationResultMapper = new ExaminationResultMapper();
    @Override
    @Transactional
    public ExaminationResult saveExaminationResult(ExaminationRequestDto examinationRequestDto, List<MultipartFile> images) {
        User doctor = userService.retrieveUserFromToken();
        User patient = userService.findUserByPesel(examinationRequestDto.getPesel());

        List<Image> imagesToSave = new ArrayList<>();
        for(MultipartFile image : images) {
            try {
                Image uploadedImage = imageService.uploadImage(image);
                imagesToSave.add(uploadedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ExaminationResult examinationResult = new ExaminationResult();
        examinationResult.setType(examinationRequestDto.getType());
        examinationResult.setTitle(examinationRequestDto.getTitle());
        examinationResult.setDescription(examinationRequestDto.getDescription());
        examinationResult.setImages(imagesToSave);
        examinationResult.setDoctor(doctor);
        examinationResult.setPatient(patient);

        return examinationResultRepository.save(examinationResult);
    }

    @Override
    @Transactional
    public ExaminationResultDto getExaminationResult(Long id) {
        ExaminationResult examinationResult = examinationResultRepository.findById(id).orElse(null);
        assert examinationResult != null;
        return examinationResultMapper.mapExaminationResultToDto(examinationResult);
    }

    @Override
    @Transactional
    public List<ExaminationResultDto> getExaminationResultByPatient(String type) {
        User patient = userService.retrieveUserFromToken();
        System.out.println(patient.getId());
        ExaminationType examinationType = ExaminationType.valueOf(type);
        System.out.println(examinationType);
        List<ExaminationResult> examinationResults = examinationResultRepository.findAllByPatientAndType(patient, examinationType);
        System.out.println(examinationResults);
        List<ExaminationResultDto> examinationResultDtos = new ArrayList<>();
        for(ExaminationResult examinationResult : examinationResults) {
            examinationResultDtos.add(examinationResultMapper.mapExaminationResultToDto(examinationResult));
        }
        System.out.println(examinationResultDtos);
        return examinationResultDtos;
    }

    @Override
    @Transactional
    public List<ExaminationResultDto> getExaminationResultByDoctor(String type) {
        User doctor = userService.retrieveUserFromToken();
        ExaminationType examinationType = ExaminationType.valueOf(type);
        List<ExaminationResult> examinationResults = examinationResultRepository.findAllByDoctorAndType(doctor, examinationType);
        List<ExaminationResultDto> examinationResultDtos = new ArrayList<>();
        for(ExaminationResult examinationResult : examinationResults) {
            examinationResultDtos.add(examinationResultMapper.mapExaminationResultToDto(examinationResult));
        }
        return examinationResultDtos;
    }
}
