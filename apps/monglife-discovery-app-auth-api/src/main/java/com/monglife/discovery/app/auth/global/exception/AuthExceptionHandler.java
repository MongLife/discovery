package com.monglife.discovery.app.auth.global.exception;

import com.monglife.discovery.app.auth.controller.AuthController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = AuthController.class)
public class AuthExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> validatedExceptionHandler() {
//        ErrorCode errorCode = GlobalErrorCode.INVALID_PARAMETER;
//        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
//    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }

    @ExceptionHandler(AppVersionNotFoundException.class)
    public ResponseEntity<Object> handleAppVersionNotFoundException(AppVersionNotFoundException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }

    @ExceptionHandler(NeedAppUpdateException.class)
    public ResponseEntity<Object> handleNeedAppUpdateException(NeedAppUpdateException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }
}