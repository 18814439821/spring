package com.sms.sms.entity;

import lombok.Data;

@Data
public class StujobResult extends Stujob{
    private String name;
    private String no;
    private String jobName;
    private String stuName;
    private String principal;
    private String phone;
    private String stuId;
    private String jobId;
    private Integer old;

    @Override
    public String toString() {
        return "StujobResult(" +
                "name=" + name +
                ", no=" + no +
                // ... 其他字段 ...
                ", jobName=" + jobName + // 假设这是从Stujob继承的字段
                ", stuName=" + stuName + // 假设这也是从Stujob继承的字段
                ", principal=" + principal +
                ", phone=" + phone +
                ", stuId=" + stuId +
                ", jobId=" + jobId +
                ", old=" + old +
                ", stu=" + super.toString() + // 调用父类的toString方法（如果合适）
                ")";
    }
}
