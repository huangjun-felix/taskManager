package com.huangjun.taskmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private  int code;
    private  String msg;
    private  Object data;
    public static Result ok(){
        return new Result(200,"success",null);
    }
    public static Result ok(Object data){
        return new Result(200,"success",data);
    }
    public static Result fail(String msg){
        return new Result(500,msg,null);
    }
}
