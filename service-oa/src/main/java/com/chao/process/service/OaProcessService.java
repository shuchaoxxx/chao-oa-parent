package com.chao.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chao.model.process.Process;
import com.chao.vo.process.ProcessFormVo;
import com.chao.vo.process.ProcessQueryVo;
import com.chao.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author chao
 * @since 2023-10-01
 */
public interface OaProcessService extends IService<Process> {

    // 审批管理列表
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, @Param("vo") ProcessQueryVo processQueryVo);

    // 部署流程定义
    void deployByZip(String deployPath);

    // 启动流程实例
    void startUp(ProcessFormVo processFormVo);
}
