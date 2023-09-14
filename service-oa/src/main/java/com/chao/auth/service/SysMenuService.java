package com.chao.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.model.system.SysMenu;
import com.chao.vo.system.AssginMenuVo;
import com.chao.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author chao
 * @since 2023-09-14
 */
public interface SysMenuService extends IService<SysMenu> {


     default List<RouterVo> findUserMenuListByUserId(Long userId) {
        return null;
    }

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssginMenuVo assginMenuVo);

//    根据用户id获取用户可以控制按钮权限
    List<String> findUserPermsByUserId(Long userId);
}
