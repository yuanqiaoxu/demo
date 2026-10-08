package com.study.demo.response;
import com.study.demo.response.ResponseCode;
import lombok.Getter;

/**
 * 统一的返回结果
 */
@Getter
public class Response<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 泛型类型的数据，自定义类型
     */
    private T data;

    private Response (Integer code){
        this.code = code;
    }
    private Response (Integer code,String message){
        this.code = code;
        this.message = message;
    }
    private Response (Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T>Response<T> success(){
        return new Response<>(ResponseCode.SUCCESS.getCode());
    }
    public static <T>Response<T> success(String message){
        return new Response<>(ResponseCode.SUCCESS.getCode(),message);
    }
    public static <T> Response<T> data(T data){
        return new Response<>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getMessage(),data);
    }
    public static <T> Response<T> ERROR(){
        return new Response<>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getMessage());
    }
    public static <T> Response<T> ERROR(String message){
        return new Response<>(ResponseCode.ERROR.getCode(),message);
    }
    public static <T> Response<T> ERROR(ResponseCode responseCode){
        return new Response<>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getMessage());
    }
    public static <T> Response<T> ERROR(Integer code,String message){
        return new Response<>(code, message);
    }


}
