package com.study.demo.exception;

import com.study.demo.response.ResponseCode;
import lombok.Data;

@Data
public class BuessnessExcepeion extends RuntimeException{

    private Integer code;
    private String message;

    public BuessnessExcepeion(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public BuessnessExcepeion(String message){
        this.message = message;
    }

    public BuessnessExcepeion(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
}
