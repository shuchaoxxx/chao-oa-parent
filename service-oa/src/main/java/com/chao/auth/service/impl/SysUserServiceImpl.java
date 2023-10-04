package com.chao.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chao.auth.mapper.SysUserMapper;
import com.chao.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.model.system.SysUser;
import com.chao.security.custom.LoginUserInfoHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-09-13
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    // 更新状态
    @Override
    public void updateStatus(Long id, Integer status) {

        // 根据userId查询用户信息
        SysUser sysUser = baseMapper.selectById(id);
        // 设置修改状态值
        sysUser.setStatus(status);
        // 调用方法进行修改
        int i = baseMapper.updateById(sysUser);
    }

    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return sysUser;
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        return map;
    }


}
