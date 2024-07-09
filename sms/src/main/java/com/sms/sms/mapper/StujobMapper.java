package com.sms.sms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.entity.Stujob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2024-06-08
 */
@Mapper
public interface StujobMapper extends BaseMapper<Stujob> {
    IPage pageCC(Page<Stujob> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    void removeByStuId(@Param(Constants.WRAPPER) Wrapper wrapper);
}
