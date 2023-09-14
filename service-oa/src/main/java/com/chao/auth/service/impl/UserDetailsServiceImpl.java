package com.chao.auth.service.impl;

import com.chao.auth.service.SysUserService;
import com.chao.model.system.SysUser;
import com.chao.security.custom.CustomUser;
import com.chao.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
