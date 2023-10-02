package com.chao.process.service;

import com.chao.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author chao
 * @since 2023-09-17
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    // 查询所有审批分类和每个分类的所有的审批模板
    List<ProcessType> findProcessType();
}
