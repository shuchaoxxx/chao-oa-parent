package com.chao.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chao.common.result.Result;
import com.chao.model.process.ProcessType;
import com.chao.process.service.OaProcessTypeService;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author chao
 * @since 2023-09-17
 */
@RestController
@RequestMapping(value = "/admin/process/processType")
public class OaProcessTypeController {


    @Autowired
    private OaProcessTypeService processTypeService;


    // 查询所有的审批分类
    @GetMapping("findAll")
    public Result findAll() {
        List<ProcessType> list = processTypeService.list();
        return Result.ok();
    }

    //    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit) {
        Page<ProcessType> pageParam = new Page<>(page, limit);
        IPage<ProcessType> pageModel = processTypeService.page(pageParam);
        return Result.ok(pageModel);
    }

    //    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessType processType = processTypeService.getById(id);
        return Result.ok(processType);
    }

    //    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType) {
        processTypeService.save(processType);
        return Result.ok();
    }

    //    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessType processType) {
        processTypeService.updateById(processType);
        return Result.ok();
    }

    //    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTypeService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        // 获取classes目录位置
        File absoluteFile = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsoluteFile();

        // 设置上传文件夹
        File tempFile = new File(absoluteFile + "/processes/");
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        // 创建文件，实现文件写入
        String filename = file.getOriginalFilename();
        File zipFile = new File(absoluteFile + "/processes/" + filename);

        // 保存文件
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
            return Result.fail();
        }

        Map<String, Object> map = new HashMap<>();
        // 根据上传地址后续部署流程定义，文件名为流程定义的默认的key
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));
        return Result.ok(map);
    }


    public static void main(String[] args) {
//        try {
//            File absoluteFile = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsoluteFile();
//            System.out.println("absoluteFile：" + absoluteFile);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

//        String str = "2222226.xxx";
//        System.out.println(str.lastIndexOf("6"));
//        System.out.println(str.substring(0, str.lastIndexOf(".")));
    }

}

