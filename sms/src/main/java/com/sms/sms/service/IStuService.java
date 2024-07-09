
package com.sms.sms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Stu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2024-06-04
 */
public interface IStuService extends IService<Stu> {
    IPage pageCC(Page<Stu> page, Wrapper wrapper);
}
