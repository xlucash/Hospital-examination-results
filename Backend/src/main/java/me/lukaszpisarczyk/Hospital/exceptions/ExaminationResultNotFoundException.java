package me.lukaszpisarczyk.Hospital.exceptions;

public class ExaminationResultNotFoundException extends RuntimeException {

    public ExaminationResultNotFoundException(String message) {
        super(message);
    }
}
