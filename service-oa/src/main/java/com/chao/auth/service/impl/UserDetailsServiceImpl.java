package com.chao.auth.service.impl;

import com.chao.auth.service.SysMenuService;
import com.chao.auth.service.SysUserService;
import com.chao.model.system.SysUser;
import com.chao.security.custom.CustomUser;
import com.chao.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: UserDetailsServiceImpl
 * Package: com.chao.auth.service.impl
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/14 - 23:33
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名进行查询
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        if (sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用了");
        }

        // 根据userid查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());

        // 创建list集合，用于封装最终的权限数据
        List<SimpleGrantedAuthority> authList = new ArrayList<>();

        for (String s : userPermsList) {
            authList.add(new SimpleGrantedAuthority(s.trim()));
        }

        return new CustomUser(sysUser, authList);
    }
}
