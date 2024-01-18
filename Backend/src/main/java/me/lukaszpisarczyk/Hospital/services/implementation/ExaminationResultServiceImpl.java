package me.lukaszpisarczyk.Hospital.services.implementation;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExaminationResultServiceImpl implements ExaminationResultService {
    private final ImageService imageService;
    private final UserService userService;
    private final ExaminationResultRepository examinationResultRepository;
    private final TemplateEngine templateEngine;

    private ExaminationResultMapper examinationResultMapper = new ExaminationResultMapper();

    public ExaminationResultServiceImpl(ImageService imageService, UserService userService, ExaminationResultRepository examinationResultRepository, TemplateEngine templateEngine) {
        this.imageService = imageService;
        this.userService = userService;
        this.examinationResultRepository = examinationResultRepository;
        this.templateEngine = templateEngine;
    }

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
        ExaminationType examinationType = ExaminationType.valueOf(type);
        List<ExaminationResult> examinationResults = examinationResultRepository.findAllByPatientAndType(patient, examinationType);
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

    @Override
    @Transactional
    public byte[] processExaminationResultPdf(Long examinationResultId) {
        ExaminationResult examinationResult = examinationResultRepository.findById(examinationResultId).orElse(null);
        Context context = new Context();
        context.setVariable("examination", examinationResult);

        List<String> images = new ArrayList<>();
        for(Image image : examinationResult.getImages()) {
            images.add(imageService.getBase64Image(image.getId()));
        }

        context.setVariable("images", images);

        String processedHtml = templateEngine.process("examination.html", context);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(processedHtml, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public List<ExaminationResultDto> getAllExaminationResult() {
        User user = userService.retrieveUserFromToken();
        List<ExaminationResult> examinationResults = examinationResultRepository.findAllByPatient(user);
        List<ExaminationResultDto> examinationResultDtos = new ArrayList<>();
        for(ExaminationResult examinationResult : examinationResults) {
            examinationResultDtos.add(examinationResultMapper.mapExaminationResultToDto(examinationResult));
        }
        return examinationResultDtos;
    }
}
