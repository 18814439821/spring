package com.sms.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Stujob;
import com.sms.sms.mapper.StujobMapper;
import com.sms.sms.service.IStujobService;
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
public class StujobServiceImpl extends ServiceImpl<StujobMapper, Stujob> implements IStujobService {
    @Autowired
    StujobMapper stujobMapper;
    public IPage pageCC(Page<Stujob> page, Wrapper wrapper) {
        return stujobMapper.pageCC(page,wrapper);
    }

    public void removeByStuId(Wrapper wrapper){
        stujobMapper.removeByStuId(wrapper);
    }
}
