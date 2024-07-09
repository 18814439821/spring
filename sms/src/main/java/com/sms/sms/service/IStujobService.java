package com.sms.sms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Stujob;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2024-06-08
 */
public interface IStujobService extends IService<Stujob> {
    IPage pageCC(Page<Stujob> page, Wrapper wrapper);
    void removeByStuId(Wrapper wrapper);
}
