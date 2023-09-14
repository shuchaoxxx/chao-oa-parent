package com.chao.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.model.system.SysRole;
import com.chao.vo.system.AssginRoleVo;

import java.util.Map;

/**
 * ClassName: SysRoleService
 * Package: com.chao.auth.service
 * Description: 创建一个接口继承IService接口
 *
 * @Author: chao
 * @Create：2023/9/13 - 7:37
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssginRoleVo assginRoleVo);
}
