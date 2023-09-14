package com.chao.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.model.system.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chao
 * @since 2023-09-13
 */
public interface SysUserService extends IService<SysUser> {

    // 更新状态
    void updateStatus(Long id, Integer status);


    SysUser getUserByUserName(String username);
}
