package com.study.demo.response;

import com.sun.net.httpserver.Authenticator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    /**
     * 响应状态码
     */
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"FAIL"),

    USERNAME_EXIST(1001,"用户名已存在")
    ;
    private Integer code;


    /**
     * 响应提示信息
     */
    private String message;


}
