package com.sms.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Stu;
import com.sms.sms.mapper.StuMapper;
import com.sms.sms.service.IStuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2024-06-04
 */
@Service
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu> implements IStuService {
    @Autowired
    StuMapper stuMapper;
    public IPage pageCC(Page<Stu> page, Wrapper wrapper) {
        return stuMapper.pageCC(page,wrapper);
    }

}
