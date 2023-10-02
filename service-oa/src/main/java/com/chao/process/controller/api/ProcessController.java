package com.chao.process.controller.api;

import com.chao.common.result.Result;
import com.chao.model.process.ProcessTemplate;
import com.chao.model.process.ProcessType;
import com.chao.process.service.OaProcessService;
import com.chao.process.service.OaProcessTemplateService;
import com.chao.process.service.OaProcessTypeService;
import com.chao.vo.process.ProcessFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
