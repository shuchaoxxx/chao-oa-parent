package com.chao.auth.utils;

import com.chao.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: MenuHelper
 * Package: com.chao.auth.utils
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/14 - 10:37
 */
public class MenuHelper {

    // 使用递归构建菜单
    public static List<SysMenu> buildTree(List<SysMenu> sysMenus) {
        // 创建list，用于最终数据
        List<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            // 递归入口
            if (sysMenu.getParentId().longValue() == 0) {
                trees.add(getChildren(sysMenu, sysMenus));
            }
        }
        return trees;

    }

    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenus) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        // 遍历所有的菜单数据，判单id和parentId的对应关系
        for (SysMenu it : sysMenus) {
            if (sysMenu.getId().longValue() == it.getParentId()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(it, sysMenus));
            }
        }
        return sysMenu;
    }
}
