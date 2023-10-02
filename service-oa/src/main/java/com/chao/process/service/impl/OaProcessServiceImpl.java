package com.chao.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chao.auth.service.SysUserService;
import com.chao.model.process.Process;
import com.chao.model.process.ProcessTemplate;
import com.chao.model.system.SysUser;
import com.chao.process.mapper.OaProcessMapper;
import com.chao.process.service.OaProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.process.service.OaProcessTemplateService;
import com.chao.security.custom.LoginUserInfoHelper;
import com.chao.vo.process.ProcessFormVo;
import com.chao.vo.process.ProcessQueryVo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chao.vo.process.ProcessVo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author chao
 * @since 2023-10-01
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private OaProcessTemplateService processTemplateService;

    // 审批管理列表
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 1、根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        // 2、根据审批模板id把模板信息把模板信息查询
        ProcessTemplate processTemplate = processTemplateService.getById(processFormVo.getProcessTemplateId());

        // 3、保存提交审批的信息到业务表，oa_process
        Process process = new Process();
        // processFormVo复制到process对象里面
        BeanUtils.copyProperties(processFormVo, process);
        // 其他值
        process.setStatus(1);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);

        // 4 启动流程实例 - RuntimeService
        // 4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        // 4.2业务key processId
        String businessKey = String.valueOf(process.getId());
        // 4.3流程参数 form表单json数据转换map集合
        String formValues = processFormVo.getFormValues();
        // formData
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        // 遍历formData得到的内容，封装map集合
        HashMap<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("data", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);


        // 5 查询下一个审批人
        // 审批人可能有多个
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        ArrayList<String> namelist = new ArrayList<>();
        for (Task task : taskList) {
            String assigneeName = task.getAssignee();
            SysUser user = sysUserService.getUserByUserName(assigneeName);
            String name = user.getName();
            namelist.add(name);
            // 6 TODO推送消息
        }

        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + StringUtils.join(namelist.toString(), ",") + "审批");
        // 7 业务和流程关联


    }

    // 当前任务列表
    private List<Task> getCurrentTaskList(String id) {

        List<Task> list = taskService.createTaskQuery().processInstanceId(id).list();
        return list;
    }
}
