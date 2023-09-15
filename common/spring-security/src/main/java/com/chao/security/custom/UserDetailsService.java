package com.chao.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * ClassName: UserDetailsService
 * Package: com.chao.security.custom
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/14 - 23:31
 */
public interface UserDetailsService  extends org.springframework.security.core.userdetails.UserDetailsService {
    /**
     * 根据用户名获取用户对象（获取不到直接抛异常）
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
