package com.chao.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chao.auth.mapper.SysRoleMapper;
import com.chao.model.system.SysRole;
import com.sun.org.apache.xalan.internal.xsltc.dom.SortingIterator;
import org.junit.jupiter.api.Test;
//import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName: TestMpDemo1
 * Package: com.chao.auth
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/12 - 19:32
 */
@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper mapper;

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));
        //UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        //所以不填写就是无任何条件
        List<SysRole> users = mapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        int insert = mapper.insert(sysRole);
        System.out.println("插入了:" + insert);
        System.out.println(sysRole);

    }

    @Test
    public void update() {
        SysRole sysRole = mapper.selectById(10);
        sysRole.setRoleName("chao");
        int i = mapper.updateById(sysRole);
        System.out.println(i);
    }

    @Test
    public void deleteById() {
        int i = mapper.deleteById(10);
        System.out.println(i);
    }

    @Test
    public void deleteByBatchIds() {
        int i = mapper.deleteBatchIds(Arrays.asList(9, 10));
        System.out.println(i);
    }

    // 条件查询
    @Test
    public void testQuery1() {
//        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
//        wrapper.like("role_name", "角色");
//        List<SysRole> sysRoles = mapper.selectList(wrapper);
//        sysRoles.forEach(System.out::println);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(SysRole::getRoleName,"角色");
        List<SysRole> sysRoles = mapper.selectList(wrapper);
        sysRoles.forEach(System.out::println);
    }

}
