package com.sms.sms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2024-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 菜单编码
     */
    @TableField("menuCode")
    private String menucode;

    /**
     * 菜单名字
     */
    @TableField("menuName")
    private String menuname;

    /**
     * 菜单级别
     */
    @TableField("menuLevel")
    private String menulevel;

    /**
     * 菜单的父code
     */
    @TableField("menuParenCode")
    private String menuparencode;

    /**
     * 点击触发的函数
     */
    @TableField("menuClick")
    private String menuclick;

    /**
     * 权限 0超级管理员 1管理员 2普通员工
     */
    @TableField("menuRight")
    private String menuright;

    /**
     * 组件
     */
    @TableField("menuComponent")
    private String menucomponent;

    @TableField("menuIcon")
    private String menuicon;


}
