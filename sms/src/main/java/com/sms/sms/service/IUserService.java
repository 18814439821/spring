package com.sms.sms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.Wrapper;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2024-06-01
 */
public interface IUserService extends IService<User> {
    IPage page(Page<User> page, Wrapper wrapper);
}
