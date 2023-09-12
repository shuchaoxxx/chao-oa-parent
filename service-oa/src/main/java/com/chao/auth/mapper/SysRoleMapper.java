package com.chao.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chao.model.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ClassName: SysRoleMapper
 * Package: com.chao.auth.mapper
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/12 - 19:29
 */

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
