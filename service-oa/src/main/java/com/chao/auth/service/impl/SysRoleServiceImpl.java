package com.chao.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.auth.mapper.SysRoleMapper;
import com.chao.auth.service.SysRoleService;
import com.chao.auth.service.SysUserRoleService;
import com.chao.model.system.SysRole;
import com.chao.model.system.SysUserRole;
import com.chao.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: SysRoleServiceImpl
 * Package: com.chao.auth.service.impl
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/13 - 7:41
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        // 1  查询所有角色，返回list集合
        List<SysRole> allRolesList = baseMapper.selectList(null);
        // 2 根据userid查询角色用户关系表，查询userid所对应的所有的角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

//        ArrayList<Long> list = new ArrayList<>();
//        for (SysUserRole sysUserRole : existUserRoleList) {
//            Long roleId = sysUserRole.getRoleId();
//            list.add(roleId);
//        }

        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());
        // 3 根据角色id，查找角色信息

        ArrayList<SysRole> assignRoleList = new ArrayList<>();

        for (SysRole sysRole : allRolesList) {
            if (existRoleIdList.contains(sysRole.getId())) {
                assignRoleList.add(sysRole);
            }
        }

        // 4 把得到的两部分数据封装到map集合中返回

        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRolesList", allRolesList);
        return roleMap;
    }

    // 为用户分配角色
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前的角色数据删除，在用户角色关系表里面，过呢据userId删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        // 重新分配角色
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            if(StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);
        }


    }
}
