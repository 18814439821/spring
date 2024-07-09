package com.sms.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2024-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工作名称
     */
    private String jobName;

    /**
     * 工作岗位数
     */
    private String jobPosts;

    /**
     * 剩余岗位
     */
    private String surplusPosition;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 联系方式
     */
    private String phone;


}
