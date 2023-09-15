package com.chao.common.config.handler;

import com.chao.common.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.chao.common.config.handler
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/13 - 14:39
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        System.out.println("错误执行了！！！");
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理！");
    }


    // 处理特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("执行了特定异常处理");
    }

    // 自定义异常
    @ExceptionHandler(CustomExecption.class)
    @ResponseBody
    public Result error(CustomExecption e) {
        e.printStackTrace();
        return Result.fail().message(e.getMsg()).code(e.getCode());
    }

    /**
     * spring security异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(205).message("没有权限");
    }
}
