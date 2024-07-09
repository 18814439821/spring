package com.sms.sms.comment;


import lombok.Data;

@Data
public class Result {
    private int code;
    private String msg;
    private Long total;
    private Object data;

    private static Result result(int code,String msg,Long total,Object data){
        Result res = new Result();
        res.code = code;
        res.msg = msg;
        res.total = total;
        res.data = data;
        return res;
    }

    public static Result fail(){
        return result(404,"失败",0L,null);
    }

    public static Result succ(Object data,Long total){
        return result(200,"成功",total,data);
    }

    public static Result suc(Object data){
        return result(200,"成功",4L,data);
    }

    public static Result suc(){
        return result(200,"成功",0L,null);
    }
}
