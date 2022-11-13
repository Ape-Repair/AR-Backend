package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.application.dto.response.ApiExceptionResponseDto;
import com.aperepair.aperepair.authorization.domain.exception.AwsS3ImageException;
import com.aperepair.aperepair.authorization.domain.exception.AwsUploadException;
import com.aperepair.aperepair.authorization.domain.exception.CustomerNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        String errorList = ex.getBindingResult().getAllErrors().toString();

        ApiExceptionResponseDto response = new ApiExceptionResponseDto(
                status, errorList, ex.getClass().getName()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AwsUploadException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiExceptionResponseDto handleBadGatewayException(
            Exception ex
    ) {
        return new ApiExceptionResponseDto(
                HttpStatus.BAD_GATEWAY,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler({AwsS3ImageException.class, CustomerNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionResponseDto handleNotFoundException(
            Exception ex
    ) {
        return new ApiExceptionResponseDto(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }
}
