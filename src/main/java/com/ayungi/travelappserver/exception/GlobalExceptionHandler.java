package com.ayungi.travelappserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    static class ErrorResponse {
        private String error;
        private String message;
        private int status;

        public ErrorResponse(String error, String message, int status) {
            this.error = error;
            this.message = message;
            this.status = status;
        }

        public String getError() {return error;}
        public void setError(String error) {this.error = error;}

        public String getMessage() {return message;}
        public void setMessage(String message) {this.message = message;}

        public int getStatus() {return status;}
        public void setStatus(int status) {this.status = status;}
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        logger.error("UserNotFoundException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("UserNotFound", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDiaryNotFoundException(DiaryNotFoundException ex) {
        logger.error("DiaryNotFoundException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("DiaryNotFound", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex) {
        logger.error("AuthorizationException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("AuthorizationError", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException ex) {
        logger.error("RegistrationException: {}", ex.getMessage());
        // Возвращаем 409 Conflict, т.к. уже есть такой пользователь
        ErrorResponse err = new ErrorResponse("RegistrationError", ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FileStoreException.class)
    public ResponseEntity<ErrorResponse> handleFileStoreException(FileStoreException ex) {
        logger.error("FileStoreException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("FileStoreError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserUpdateAvatarException.class)
    public ResponseEntity<ErrorResponse> handleUserUpdateAvatarException(UserUpdateAvatarException ex) {
        logger.error("UserUpdateAvatarException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("UserUpdateAvatarError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        logger.error("IOException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("IOException", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException: {}", ex.getMessage());
        ErrorResponse err = new ErrorResponse("IllegalArgument", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException: {}", ex.getMessage(), ex);
        ErrorResponse err = new ErrorResponse("RuntimeError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("RuntimeException: {}", ex.getMessage(), ex);
        ErrorResponse err = new ErrorResponse("RuntimeError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<ErrorResponse> BudgetNotFoundException(BudgetNotFoundException ex) {
        logger.error("BudgetNotFoundException: {}", ex.getMessage(), ex);
        ErrorResponse err = new ErrorResponse("RuntimeError", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
