package me.lukaszpisarczyk.Hospital.controllers;

import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.dto.ExaminationResultDto;
import me.lukaszpisarczyk.Hospital.exceptions.ExaminationResultNotFoundException;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.services.ExaminationResultService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/examination-result")
public class ExaminationResultController {
    private final ExaminationResultService examinationResultService;

    public ExaminationResultController(ExaminationResultService examinationResultService) {
        this.examinationResultService = examinationResultService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> saveExaminationResult(
            @RequestPart("examinationResult")ExaminationRequestDto examinationRequestDto,
            @RequestPart("image") List<MultipartFile> images) {
        ExaminationResult examinationResult = examinationResultService.saveExaminationResult(examinationRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(examinationResult);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> getExaminationResult(@PathVariable Long id) {
        try {
            ExaminationResultDto examinationResult = examinationResultService.getExaminationResult(id);
            return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
        } catch (ExaminationResultNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> getExaminationResultPdf(@PathVariable Long id) {
        byte[] pdf = examinationResultService.processExaminationResultPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "badanie.pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/patient/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> getExaminationResultByPatient(@PathVariable String type) {
    	List<ExaminationResultDto> examinationResult = examinationResultService.getExaminationResultByPatient(type);
    	return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }

    @GetMapping("/doctor/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> getExaminationResultByDoctor(@PathVariable String type) {
    	List<ExaminationResultDto> examinationResult = examinationResultService.getExaminationResultByDoctor(type);
    	return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT') or hasRole('USER')")
    public ResponseEntity<?> getAllExaminationResult() {
    	List<ExaminationResultDto> examinationResult = examinationResultService.getAllExaminationResult();
    	return ResponseEntity.status(HttpStatus.OK).body(examinationResult);
    }
}
