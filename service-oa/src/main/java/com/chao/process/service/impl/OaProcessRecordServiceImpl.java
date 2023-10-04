package com.chao.process.service.impl;

import com.chao.auth.service.SysUserService;
import com.chao.model.process.ProcessRecord;
import com.chao.model.system.SysUser;
import com.chao.process.mapper.OaProcessRecordMapper;
import com.chao.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-10-03
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processId, Integer status, String description) {

        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(sysUser.getDeptName());
        processRecord.setOperateUserId(userId);
        baseMapper.insert(processRecord);
    }
}
