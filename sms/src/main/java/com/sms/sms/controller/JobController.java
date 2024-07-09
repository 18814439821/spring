package com.sms.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.comment.QueryPageParam;
import com.sms.sms.comment.Result;
import com.sms.sms.entity.Job;
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
@RequestMapping("/job")
public class JobController {
//    @Autowired
//    IJobService jobService;
    @Autowired
    JobServiceImpl jobService;
    @PostMapping("/result")
    public Result result(@RequestBody QueryPageParam queryPageParam){
        Page<Job> page = new Page<>();
        page.setSize(queryPageParam.getPageSize());
        page.setCurrent(queryPageParam.getPageNum());
        HashMap param = queryPageParam.getParam();
        LambdaQueryWrapper<Job> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String name = (String) param.get("name");
        if (StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(Job::getJobName,name);
        }
        IPage result = jobService.pageCC(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(),result.getTotal());
    }
    @GetMapping("/list")
    public Result list(){
        List list = jobService.list();
        return Result.suc(list);
    }
    @PostMapping("/update")
    public Result update(@RequestBody Job job){
        return jobService.updateById(job)?Result.suc():Result.fail();
    }
    @PostMapping("/save")
    public Result save(@RequestBody Job job){
        return jobService.save(job)?Result.suc():Result.fail();
    }
    @GetMapping("/findById")
    public Job findById(String jobId){
        System.out.println(jobId);
        Job result = jobService.getById(jobId);
        return result;
    }
}
