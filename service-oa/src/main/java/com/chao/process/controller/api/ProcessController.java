package com.chao.process.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chao.auth.service.SysUserService;
import com.chao.common.result.Result;
import com.chao.model.process.Process;
import com.chao.model.process.ProcessTemplate;
import com.chao.model.process.ProcessType;
import com.chao.process.service.OaProcessService;
import com.chao.process.service.OaProcessTemplateService;
import com.chao.process.service.OaProcessTypeService;
import com.chao.vo.process.ApprovalVo;
import com.chao.vo.process.ProcessFormVo;
import com.chao.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ProcessController
 * Package: com.chao.process.controller.api
 * Description:
 *
 * @Author: chao
 * @Create：2023/10/2 - 9:27
 */


@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin // 跨域
public class ProcessController {

    @Autowired
    private OaProcessTypeService processTypeService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private OaProcessService processService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findfindPending(pageParam);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result startUp(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.ok();
    }


    // 获取审批模板的数据
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    // 查询所有的审批分类和每个分类所有的审批模板
    @GetMapping("findProcessType")
    public Result findProcessType() {
        List<ProcessType> list = processTypeService.findProcessType();
        return Result.ok(list);
    }

    // 查看审批详细信息
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        Map<String, Object> map = processService.show(id);
        return Result.ok();

    }

    // 审批
    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result aprove(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }

    // 查询已处理任务
    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findProcessed(pageParam);
        return Result.ok(pageModel);
    }

    // 查询已发起任务
    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findStarted(pageParam);
        return Result.ok(pageModel);
    }

    @GetMapping("getCurrentUser")
    public Result getCurrentUser(){
       Map<String, Object> map= sysUserService.getCurrentUser();
       return  Result.ok(map);
    }
}
