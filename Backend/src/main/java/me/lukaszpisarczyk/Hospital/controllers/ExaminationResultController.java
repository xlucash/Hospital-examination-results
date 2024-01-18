package me.lukaszpisarczyk.Hospital.controllers;

import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.services.ExaminationResultService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/examination-result")
public class ExaminationResultController {
    private final ExaminationResultService examinationResultService;

    public ExaminationResultController(ExaminationResultService examinationResultService) {
        this.examinationResultService = examinationResultService;
    }

    @PostMapping
    public ResponseEntity<?> saveExaminationResult(
            @RequestPart("examinationResult")ExaminationRequestDto examinationRequestDto,
            @RequestPart("image") List<MultipartFile> images) {
        ExaminationResult examinationResult = examinationResultService.saveExaminationResult(examinationRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(examinationResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExaminationResult(@PathVariable Long id) {
        ExaminationResultDto examinationResult = examinationResultService.getExaminationResult(id);
        return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> getExaminationResultPdf(@PathVariable Long id) {
        try {
            byte[] pdf = examinationResultService.processExaminationResultPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "badanie.pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Błąd podczas generowania PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patient/{type}")
    public ResponseEntity<?> getExaminationResultByPatient(@PathVariable String type) {
    	List<ExaminationResultDto> examinationResult = examinationResultService.getExaminationResultByPatient(type);
    	return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }

    @GetMapping("/doctor/{type}")
    public ResponseEntity<?> getExaminationResultByDoctor(@PathVariable String type) {
    	List<ExaminationResultDto> examinationResult = examinationResultService.getExaminationResultByDoctor(type);
    	return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }
}
