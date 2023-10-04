package com.chao.process.service;

import com.chao.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author chao
 * @since 2023-10-03
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId, Integer status, String description);
}
