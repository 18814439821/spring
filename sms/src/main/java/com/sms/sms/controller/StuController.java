package com.sms.sms.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.sms.comment.QueryPageParam;
import com.sms.sms.comment.Result;
import com.sms.sms.entity.Job;
import com.sms.sms.entity.Stu;
import com.sms.sms.entity.Stujob;
import com.sms.sms.entity.User;
import com.sms.sms.mapper.StuMapper;
import com.sms.sms.mapper.StujobMapper;
import com.sms.sms.service.IStuService;
import com.sms.sms.service.IStujobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.ArrayStack;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/stu")
public class StuController {
    @Autowired
    StuMapper stuMapper;
    @Autowired
    IStuService stuService;
    @Autowired
    IStujobService stujobService;
    @Autowired
    StujobMapper stujobMapper;
    @PostMapping("/result")
    public Result result(@RequestBody QueryPageParam queryPageParam){
        Page<Stu> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Stu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String isvalid = (String) param.get("isvalid");
        String name = (String) param.get("name");
        String sp = (String) param.get("specialGroup");
//        System.out.println(isvalid);
//        System.out.println(param);
//        System.out.println(queryPageParam);
        if (isvalid.equals("Y")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (isvalid.equals("N")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if(StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Stu::getName,name);
        }
        IPage result = stuMapper.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    @PostMapping("/resultPS")
    public Result resultPS(@RequestBody QueryPageParam queryPageParam){
        Page<Stu> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Stu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String isvalid = (String) param.get("isvalid");
        String name = (String) param.get("name");
        String sp = (String) param.get("specialGroup");
//        System.out.println(isvalid);
//        System.out.println(param);
//        System.out.println(queryPageParam);
        if (isvalid.equals("Y")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (isvalid.equals("N")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if(StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Stu::getName,name);
        }
        lambdaQueryWrapper.ne(Stu::getIsPoorStudent,"N");
        IPage result = stuMapper.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    @PostMapping("/resultES")
    public Result resultES(@RequestBody QueryPageParam queryPageParam){
        Page<Stu> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Stu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String isvalid = (String) param.get("isvalid");
        String name = (String) param.get("name");
        String es = (String) param.get("emergencySituation");
        System.out.println(es);
        if (es.equals("null")||es.equals("")){
            es = "null";
        }
        if (isvalid.equals("Y")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (isvalid.equals("N")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (es.equals("Y")){
            lambdaQueryWrapper.ne(Stu::getEmergencySituation,"null");
        }
        if (es.equals("N")){
            lambdaQueryWrapper.eq(Stu::getEmergencySituation,"null");
        }
        if(StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Stu::getName,name);
        }
        IPage result = stuMapper.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    @PostMapping("/resultSP")
    public Result resultSP(@RequestBody QueryPageParam queryPageParam){
        Page<Stu> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Stu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String isvalid = (String) param.get("isvalid");
        String name = (String) param.get("name");
        String sp = (String) param.get("specialGroup");
//        System.out.println(isvalid);
//        System.out.println(param);
//        System.out.println(queryPageParam);
        if (isvalid.equals("Y")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (isvalid.equals("N")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if(StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Stu::getName,name);
        }
        if (sp.equals("Y")){
            lambdaQueryWrapper.ne(Stu::getSpecialGroup,"null");
        }
        if (sp.equals("N")){
            lambdaQueryWrapper.eq(Stu::getSpecialGroup,"null");
        }
        IPage result = stuMapper.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    @PostMapping("/special")
    public Result special(@RequestBody QueryPageParam queryPageParam){
        Page<Stu> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Stu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String isvalid = (String) param.get("isvalid");
        String name = (String) param.get("name");
        String specialGroup = (String) param.get("specialGroup");
//        System.out.println(isvalid);
//        System.out.println(param);
//        System.out.println(queryPageParam);
        if (specialGroup.equals("null")||specialGroup.equals("")){
            specialGroup = "null";
        }
        if (specialGroup.equals("yse")){
            lambdaQueryWrapper.ne(Stu::getSpecialGroup,specialGroup);
        }else {
            lambdaQueryWrapper.eq(Stu::getSpecialGroup,specialGroup);
        }

        if (isvalid.equals("Y")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if (isvalid.equals("N")){
            lambdaQueryWrapper.eq(Stu::getIsvalid,isvalid);
        }
        if(StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Stu::getName,name);
        }

        IPage result = stuMapper.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    //删除
    @PostMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids){
        List id = null;
        QueryWrapper<Stujob> stujob = new QueryWrapper<>();
        LambdaQueryWrapper<Stujob> lambdaQueryWrapper = new LambdaQueryWrapper();
        for (int i = 0; i < ids.size(); i++) {
            stujob.eq("stu",ids.get(i));
            List<Stujob> stujobs = stujobMapper.selectList(stujob);
            if (stujobs.size()>0){
                lambdaQueryWrapper.eq(Stujob::getStu,stujobs.get(0).getStu());
                stujobService.removeByStuId(lambdaQueryWrapper);
                stujobs.clear();
            }
            if(!stuService.removeById(ids.get(i))){
                id.add(ids.get(i));
            }
        }

        return id==null?Result.suc():Result.fail();
    }

    @PostMapping("/save")
    public Result save(@RequestBody Stu stu){
        return stuService.saveOrUpdate(stu)?Result.suc():Result.fail();
    }
    @PostMapping("update")
    public Result update(@RequestBody Stu stu){
        return stuService.updateById(stu)?Result.suc():Result.fail();
    }
    @GetMapping("/list")
    public Result list(){
        List list = stuService.list();
        return Result.suc(list);
    }
    @GetMapping("/findById")
    public Stu findById(String stuId){
        System.out.println(stuId);
        Stu result = stuService.getById(stuId);
        return result;
    }

}
