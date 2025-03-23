package ru.anger.nexignlab.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.anger.nexignlab.controllers.CdrGeneratorController;

import java.io.IOException;
import java.time.DateTimeException;

@ControllerAdvice(assignableTypes = CdrGeneratorController.class)
public class CdrGeneratorExceptionHandler {

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<CustomErrorResponse> handleDateTimeException(DateTimeException e) {
        CustomErrorResponse response = new CustomErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    private ResponseEntity<CustomErrorResponse> handleException(IOException e) {
        CustomErrorResponse response = new CustomErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
