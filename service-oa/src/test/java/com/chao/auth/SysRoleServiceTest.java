package com.chao.auth;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chao.auth.service.SysRoleService;
import com.chao.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ClassName: SysServiceTest
 * Package: com.chao.auth
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/13 - 8:16
 */
@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService service;

    @Test
    public void getAll() {
        List<SysRole> list = service.list();
        System.out.println("list:" + list);
    }

    @Test
    public void save() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员111111111");
        sysRole.setRoleCode("role_9-13");
        sysRole.setDescription("角色管理员11111111");
        boolean save = service.save(sysRole);
    }

    @Test
    public void testUpdateById() {
        SysRole sysRole = new SysRole();
        sysRole.setId(1L);
        sysRole.setRoleName("角色管理员1");

        boolean result = service.updateById(sysRole);
        System.out.println(result);
    }

    @Test
    public void testDeleteById() {
        boolean result = service.removeById(11L);
        System.out.println(result);
    }

    @Test
    public void testSelect2() {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(SysRole::getId, 5);
        List<SysRole> users = service.list(queryWrapper);
        System.out.println("users" + users);
    }

}
