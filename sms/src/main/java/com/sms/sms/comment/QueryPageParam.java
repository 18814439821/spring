package com.sms.sms.comment;

import lombok.Data;

import java.util.HashMap;

@Data
public class QueryPageParam {
    private static int PAGE_SIZE=10;
    private static int PAGE_NUM=1;
    //设置默认值 ，避免翻车
    private int PageSize=PAGE_SIZE;
    private int PageNum=PAGE_NUM;
    //当前端传入的参数不知道具体有什么的时候，可以额外定义一个map来接收
    private HashMap param = new HashMap(); //注意，这里的参数名要跟json传入时一致
    //{
    //    "pageSize":10,
    //    "pageNum":1,
    //    "param":{
    //        "name":"lili",
    //        "no":"saaa"
    //    }
    //}
}
