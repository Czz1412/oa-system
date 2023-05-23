package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.execption.GuiguException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.md5.MD5;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        SysUser sysUser = sysUserService.getByUsername(loginVo.getUsername());
        if(null == sysUser) {
            throw new GuiguException(201,"用户不存在");
        }
        if(!MD5.encrypt(loginVo.getPassword()).equals(loginVo.getPassword())) {
            throw new GuiguException(201,"密码错误");
        }
        if(sysUser.getStatus().intValue() == 0) {
            throw new GuiguException(201,"用户被禁用");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getUsername()));
        return Result.ok(map);
    }
}
