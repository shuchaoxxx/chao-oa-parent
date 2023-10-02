package com.chao.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chao.model.process.Process;
import com.chao.vo.process.ProcessQueryVo;
import com.chao.vo.process.ProcessVo;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author chao
 * @since 2023-10-01
 */
public interface OaProcessMapper extends BaseMapper<Process> {
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

}
