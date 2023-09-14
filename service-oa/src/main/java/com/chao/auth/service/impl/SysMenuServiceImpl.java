package com.chao.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.auth.mapper.SysMenuMapper;
import com.chao.auth.service.SysMenuService;
import com.chao.auth.service.SysRoleMenuService;
import com.chao.auth.utils.MenuHelper;
import com.chao.common.config.handler.CustomExecption;
import com.chao.model.system.SysMenu;
import com.chao.model.system.SysRoleMenu;
import com.chao.model.wechat.Menu;
import com.chao.vo.system.AssginMenuVo;
import com.chao.vo.system.MetaVo;
import com.chao.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-09-14
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Override
    public List<SysMenu> findNodes() {

        // 查询所有菜单数据
        List<SysMenu> sysMenus = baseMapper.selectList(null);

        List<SysMenu> resultList = MenuHelper.buildTree(sysMenus);

        return resultList;
    }

    // 删除菜单
    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有下一层菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);

        if (count > 0) {
            throw new CustomExecption(201, "存有子带单，不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        // 查询所有菜单-添加条件 status=1

        LambdaQueryWrapper<SysMenu> sysMenuWrapper = new LambdaQueryWrapper<>();
        sysMenuWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenusList = baseMapper.selectList(sysMenuWrapper);

        // 根据角色id查询菜单id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuWrapper);
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        // 根据菜单id，获取菜单对象

        allSysMenusList.stream().forEach(item -> {
            if (menuIdList.contains(item.getId())) {
                item.setSelect(true);
            } else {
                item.setSelect(false);
            }
        });
        List<SysMenu> sysMenusList = MenuHelper.buildTree(allSysMenusList);


        // 返回树形菜单格式列表

        return sysMenusList;
    }

    // 给角色分配菜单
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {

        // 根据jueseid，删除菜单角色表， 分配数据
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assginMenuVo.getRoleId());
        boolean remove = sysRoleMenuService.remove(wrapper);

        // 进行遍历，把每个id数据添加到菜单角色表
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> sysMenuList = null;
        // 1 判断当前用户是不是管理员，usreId=1 是管理员
        if (userId.longValue() == 1) {
            // 1.1 如果是管理员，查询所有的菜单列表，
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        } else {
            // 1.2 如果不是管理员，根据userId查询可以操作的菜单列表
            sysMenuList = baseMapper.findMenuListByUserId(userId);

        }
        // -- 多表关联查询，用户角色关系表、角色菜单关系表、菜单表

        // 2 把查询出来数据列表构建成框架要求的路由数据结构
        // 使用菜单工具类构建树形结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerList = this.buildRouter(sysMenuTreeList);


        return routerList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        // 创建list集合，存储最终数据
        ArrayList<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            router.setPath(getRouterPath(menu));
            router.setComponent(menu.getComponent());
            router.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
            // 下一层数据部分
            List<SysMenu> children = menu.getChildren();
            if (menu.getType() == 1) {
                // 隐藏路由处理
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        router.setAlwaysShow(true);
                    }
                    // 递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    //    根据用户id获取用户可以控制按钮权限
    @Override
    public List<String> findUserPermsByUserId(Long userId) {

        // 1 判断是否是管理员，如果是管理员，查询所有按钮
        // 2 如果不是管理员，根据userId查询可以操作的按钮列表
        //  -- 多表关联查询，用户角色关系、角色菜单关系、菜单表

        // 3 从查询出来的数据里面，获取可以操作按钮值的list集合，返回

        //超级管理员admin账号id为：1
        List<SysMenu> sysMenuList = null;
        if (userId.longValue() == 1) {
            // 查询所有菜单列表
            //            sysMenuList = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus, 1);
            sysMenuList = baseMapper.selectList(wrapper);

        } else {
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

//    @Override
//    public List<SysMenu> findNodes(SysMenu sysMenu) {
//        return null;
//    }
}
