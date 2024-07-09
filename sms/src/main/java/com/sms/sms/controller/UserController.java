package com.sms.sms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.sms.comment.QueryPageParam;
import com.sms.sms.comment.Result;
import com.sms.sms.entity.Menu;
import com.sms.sms.entity.User;
import com.sms.sms.service.IMenuService;
import com.sms.sms.service.IUserService;
import freemarker.core.CSSOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-06-01
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    IMenuService menuService;
//    @GetMapping("/test")
//    public Result list(){
//        return userService.list()!=null?Result.succ(userService.list(), (long) userService.count()):Result.fail();
//    }
    //login
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        List list = userService.lambdaQuery()
                .eq(User::getNo,user.getNo())
                .eq(User::getPassword,user.getPassword())
                .eq(User::getIsValid,"Y")
                .list();
        List list1 = userService.lambdaQuery()
                .eq(User::getPhone,user.getNo())
                .eq(User::getPassword,user.getPassword())
                .eq(User::getIsValid,"Y")
                .list();
        if(list.size()>0){
            User user1 = (User)list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.suc(res);
        }else if (list1.size()>0){
            User user1 = (User)list1.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuright,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.suc(res);
        }
        return Result.fail();
    }

//    @PostMapping("menu")
//    public Result menu(@RequestBody Menu menu){
//        List list = menuService.lambdaQuery().eq(Menu::getMenuright,menu.getMenuright()).list();
//        return list.size()>0?Result.suc(list.get(0)):Result.fail();
//    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List list = userService.lambdaQuery().eq(User::getNo,no).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        return userService.save(user)?Result.suc():Result.fail();
    }

    //找回密码
    @PostMapping("/findPwd")
    public Result findPwd(@RequestBody User user){
        List list = userService.lambdaQuery().eq(User::getNo,user.getNo())
                .eq(User::getName,user.getName())
                .eq(User::getPhone,user.getPhone())
                .list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody User user){
        return userService.saveOrUpdate(user)?Result.suc():Result.fail();
    }
    @PostMapping("/result")
    public Result reslut(@RequestBody QueryPageParam queryPageParam){
        HashMap param = queryPageParam.getParam();
        Page<User> page = new Page<>();
        page.setCurrent(queryPageParam.getPageNum());
        page.setSize(queryPageParam.getPageSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String roleId = (String) param.get("roleId");
        String isValid = (String) param.get("isValid");
        String name = (String) param.get("name");
        if (StringUtils.isNotBlank(name)){
            lambdaQueryWrapper.like(User::getName,name);
        }
        if(StringUtils.isNotBlank(roleId)){
            lambdaQueryWrapper.gt(User::getRoleId,roleId);
        }
        if(StringUtils.isNotBlank(isValid)){
            lambdaQueryWrapper.eq(User::getIsValid,isValid);
        }
        IPage result = userService.page(page,lambdaQueryWrapper);
        return Result.succ(result.getRecords(), result.getTotal());
    }
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        return userService.saveOrUpdate(user)?Result.suc():Result.fail();
    }

    @GetMapping("/delete")
    public Result delete(String id){
        return userService.removeById(id)?Result.suc():Result.fail();
    }
}
