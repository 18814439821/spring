
package com.sms.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Job;
import com.sms.sms.mapper.JobMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2024-06-08
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>  {
    @Autowired
    JobMapper jobMapper;
    public IPage pageCC(Page<Job> page, Wrapper wrapper) {
        return jobMapper.pageCC(page,wrapper);
    }


    public void updateSurplusPosition(Integer id) {
            jobMapper.updateSurplusPosition(id);
    }
}

