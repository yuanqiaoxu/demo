package com.study.demo.exception;

import com.study.demo.response.Response;
import com.study.demo.response.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandle {
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e){
        return Response.ERROR(e.getMessage());
    }
    @ExceptionHandler(BuessnessExcepeion.class)
    public Response HandleBuessnessException(BuessnessExcepeion e){
        return Response.ERROR(e.getCode(),e.getMessage());
    }
}
