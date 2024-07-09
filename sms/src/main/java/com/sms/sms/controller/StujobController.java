package com.sms.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.comment.QueryPageParam;
import com.sms.sms.comment.Result;
import com.sms.sms.entity.Job;
import com.sms.sms.entity.Stujob;
import com.sms.sms.entity.StujobResult;
import com.sms.sms.mapper.StujobMapper;
import com.sms.sms.service.IStujobService;
import com.sms.sms.service.impl.JobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-06-08
 */
@RestController
@RequestMapping("/stujob")
public class StujobController {
    @Autowired
    IStujobService stujobService;
//    @Autowired
//    IJobService jobService;
    @Autowired
    JobServiceImpl jobService;
    @Autowired
    StujobMapper stujobMapper;
    @PostMapping("/result")
    public Result result(@RequestBody QueryPageParam queryPageParam){
        Page<Stujob> page = new Page<>();

        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap map = queryPageParam.getParam();
        String name = (String)map.get("name");
        String jobId = (String)map.get("jobId");
        String stuId = (String)map.get("stuId");
        QueryWrapper<Stujob> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("sb.job=j.id and sb.stu=s.id");
        if (StringUtils.isNotBlank(jobId)){
            queryWrapper.eq("j.id",jobId);
        }
        if (StringUtils.isNotBlank(stuId)){
            queryWrapper.eq("s.id",stuId);
        }
        if (StringUtils.isNotBlank(name)){
            queryWrapper.like("s.name",name);
        }
        IPage result = stujobService.pageCC(page,queryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());

    }
    @GetMapping("/list")
    public Result list(){
        List list = stujobService.list();
        return Result.suc(list);
    }

    @PostMapping("/update")
    public Result update(@RequestBody StujobResult result){
        System.out.println(result);
        Stujob stujob = new Stujob();
        stujob.setStu(result.getStu());
        stujob.setJob(result.getJob());
        stujob.setId(result.getId());
        Integer old = result.getOld();
        Integer _new = result.getJob();
        System.out.println(old);
        System.out.println(stujob);
        if (!old.equals(_new)){
            Job oldJob = jobService.getById(old);
            Job _newJob = jobService.getById(_new);
            int oldsur = Integer.parseInt(oldJob.getSurplusPosition());
            int _newsur = Integer.parseInt(_newJob.getSurplusPosition());
            oldsur++;
            _newsur--;
            oldJob.setSurplusPosition(String.valueOf(oldsur));
            _newJob.setSurplusPosition(String.valueOf(_newsur));
            jobService.updateById(oldJob);
            jobService.updateById(_newJob);
        }
//
        return stujobService.updateById(stujob)?Result.suc():Result.fail();
    }

    @PostMapping("/add")
    public Result add(@RequestBody HashMap hashMap){
        String surplusPosition = (String) hashMap.get("surplusPosition");
        int result = Integer.parseInt(surplusPosition);
        if (result-1<0){
            return Result.fail();
        }else {
            result = result - 1;
        }
        String surplusPosition1 = Integer.toString(result);
        System.out.println(surplusPosition1);
        Integer stuId = (Integer) hashMap.get("stu");
        Integer jobId = (Integer) hashMap.get("job");
        Stujob stujob = new Stujob();
        Job job = new Job();
        job.setId(jobId);
        job.setSurplusPosition(surplusPosition1);
        jobService.updateById(job);
        stujob.setJob(jobId);
        stujob.setStu(stuId);
        System.out.println(stujob);
        return stujobService.saveOrUpdate(stujob)?Result.suc():Result.fail();
    }
}
