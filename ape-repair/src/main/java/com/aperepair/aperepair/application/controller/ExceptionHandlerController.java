package com.aperepair.aperepair.application.controller;

import com.aperepair.aperepair.application.dto.response.ApiExceptionResponseDto;
import com.aperepair.aperepair.domain.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
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
    public ApiExceptionResponseDto handleBadGatewayException(Exception ex) {
        return new ApiExceptionResponseDto(
                HttpStatus.BAD_GATEWAY,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler({
            AlreadyRegisteredException.class,
            HttpClientErrorException.BadRequest.class,
            InvalidServiceTypeException.class,
            InvalidProposalForThisOrderException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponseDto handleBadRequestException(Exception ex) {
        return new ApiExceptionResponseDto(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler({AwsS3ImageException.class, NotFoundException.class})
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

    @ExceptionHandler({
            NotAuthenticatedException.class,
            SpecialtyNotMatchWithServiceTypeException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiExceptionResponseDto handleNotAuthenticated(
            Exception ex
    ) {
        return new ApiExceptionResponseDto(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiExceptionResponseDto handleBadCredentials(Exception ex) {
        return new ApiExceptionResponseDto(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiExceptionResponseDto handleNoContent(Exception ex) {
        return new ApiExceptionResponseDto(
                HttpStatus.NO_CONTENT,
                ex.getMessage(),
                ex.getClass().getName()
        );
    }
}