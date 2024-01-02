package me.lukaszpisarczyk.Hospital.services.implementation;

import jakarta.transaction.Transactional;
import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
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
}
