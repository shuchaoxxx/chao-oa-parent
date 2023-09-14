package com.chao.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chao.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author chao
 * @since 2023-09-14
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);

//    void selectList();
}
