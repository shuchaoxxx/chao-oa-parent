package com.chao.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chao.auth.service.SysMenuService;
import com.chao.auth.service.SysUserService;
import com.chao.common.config.handler.CustomExecption;
import com.chao.common.jwt.JwtHelper;
import com.chao.common.result.Result;
import com.chao.common.utils.MD5;
import com.chao.model.system.SysUser;
import com.chao.vo.system.LoginVo;
import com.chao.vo.system.RouterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: IndexController
 * Package: com.chao.auth.controller
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/13 - 16:16
 */
@Api(tags = "后台登陆管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录
     *
     * @return
     */
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("token", "admin-token");
//        return Result.ok(map);

        // 1、获取输入的用户名和密码

        // 2、根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);

        SysUser sysUser = sysUserService.getOne(wrapper);
        // 3、用户信息是否存在
        if (sysUser == null) {
            throw new CustomExecption(201, "用户不存在");
        }
        // 4、判断密码
        // 获取数据库密码
        String password_db = sysUser.getPassword();
        //获取输入的密码
        String password_input = MD5.encrypt(loginVo.getPassword());

        if (!password_db.equals(password_input)) {
            throw new CustomExecption(201, "密码错误");
        }
        // 5、判断用户是否被禁用
        if (sysUser.getStatus().intValue() == 0) {
            throw new CustomExecption(201, "用户已经被禁用");
        }
        // 6、使用jwt根据id和用户名生成token。
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        // 返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        return Result.ok(map);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        // 1 从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");
        // 2 从token字符串中获取用户id 、用户名称
        Long userId = JwtHelper.getUserId(token);
        // 3 根据id查询数据库，把用户信息查询出来
        SysUser sysUser = sysUserService.getById(userId);


        // 4 根据用户id获取用户可以操作的菜单列表-查询数据库动态构建路由结构

        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);
        // 5 根据用户id获取用户可以控制按钮权限

        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        // 6 返回
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", sysUser.getName());
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("buttons", permsList);
        map.put("routers", routerList);
        return Result.ok(map);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

}
