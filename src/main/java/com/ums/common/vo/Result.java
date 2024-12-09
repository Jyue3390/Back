package com.ums.common.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;       // 成功失败的代码，此处定义2000为成功，2001为失败
    private String message;     // 消息，里面包含数据data
    private T data;             // 未定义的数据形式

    // 此处重载了四个 success 方法，有些能够返回数据，有的只返回代码或信息
    public static <T> Result<T> success() {
        return new Result<>(20000,"success",null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(20000,"success",data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(20000,message,data);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(20000,message,null);
    }

    public static<T>  Result<T> fail(){
        return new Result<>(20001,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }

    public static<T>  Result<T> fail( String message){
        return new Result<>(20001,message,null);
    }
}
