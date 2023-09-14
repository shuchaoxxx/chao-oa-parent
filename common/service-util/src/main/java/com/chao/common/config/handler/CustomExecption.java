package com.chao.common.config.handler;

import com.chao.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * ClassName: CustomExecption
 * Package: com.chao.common.config.handler
 * Description:
 *
 * @Author: chao
 * @Create：2023/9/13 - 14:51
 */
@Data
public class CustomExecption extends RuntimeException {

    private Integer code;
    private String msg;

    public CustomExecption(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;

    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public CustomExecption(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
