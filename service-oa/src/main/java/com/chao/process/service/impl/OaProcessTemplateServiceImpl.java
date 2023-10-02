package com.chao.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chao.model.process.ProcessTemplate;
import com.chao.model.process.ProcessType;
import com.chao.process.mapper.OaProcessTemplateMapper;
import com.chao.process.service.OaProcessService;
import com.chao.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-09-17
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {


    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessService processService;


    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParam) {
        // 1. 调用mapper的方法，实现分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);

        // 2. 第一步分页查询返回分页数据，    从分页数据获取list集合
        List<ProcessTemplate> records = processTemplatePage.getRecords();

        // 3. 遍历list集合，得到每个对象的审批类型id
        for (ProcessTemplate record : records) {

            //得到每个类型的审批id
            Long processTypeId = record.getProcessTypeId();
            // 4. 根据审批id类型，查询获取对应名称
            LambdaQueryWrapper<ProcessType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId, processTypeId);
            ProcessType processType = oaProcessTypeService.getOne(wrapper);
            if (processType == null) {
                continue;
            }

            // 5. 完成最终封装processTypeName
            record.setProcessTypeName(processType.getName());
        }


        return processTemplatePage;
    }

    // 部署流程定义（发布）
    @Override
    public void publish(Long id) {
        // 修改模板发布状态 1 已经发布
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //  TODO流程定义的部署
          if(!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
          }
    }


}
