package com.sms.sms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2024-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Stu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String no;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 出生地
     */
    private String birthPlace;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 民族
     */
    private String ethnicGroup;

    /**
     * 家庭年收入
     */
    private Integer annualHouseholdIncome;

    /**
     * 年级
     */
    private String grade;

    /**
     * 班级
     */
    private String className;

    /**
     * 现住址
     */
    private String currentResidence;

    /**
     * 通信地址
     */
    private String mailingAddress;

    /**
     * 家庭地址
     */
    private String familyAddress;

    /**
     * 特殊群体
     */
    private String specialGroup;

    /**
     * 突发状况
     */
    private String emergencySituation;

    /**
     * 是否为贫困生，N为否，其他为是
     */
    private String isPoorStudent;

    /**
     * 是否在校，Y在校，其他为已毕业
     */
    @TableField("is_valid")
    private String isvalid;

    /**
     * 家庭成员数
     */
    private String familyMember;


}
