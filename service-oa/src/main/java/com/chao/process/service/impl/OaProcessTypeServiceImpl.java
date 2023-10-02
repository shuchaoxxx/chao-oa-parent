package com.chao.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chao.model.process.ProcessTemplate;
import com.chao.model.process.ProcessType;
import com.chao.process.mapper.OaProcessTypeMapper;
import com.chao.process.service.OaProcessTemplateService;
import com.chao.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-09-17
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService processTemplateService;

    // 查询所有的审批分类和每个分类所有的的审批模板
    @Override
    public List<ProcessType> findProcessType() {
        // 1、查询所有审批分类，返回list集合
        List<ProcessType> processTypes = baseMapper.selectList(null);
        // 2、遍历返回所有的审批分类list集合
        for (ProcessType processType : processTypes) {
            // 3、得到每个审批分类，根据审批分类id查询对应审批模板
            Long typeId = processType.getId();
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, typeId);
            List<ProcessTemplate> processTemplateList = processTemplateService.list(wrapper);
            // 4、根据审批分类id查询对应审批模板数据（List）封装到每个审批分类对象里面
            processType.setProcessTemplateList(processTemplateList);
        }
        return processTypes;
    }

}
