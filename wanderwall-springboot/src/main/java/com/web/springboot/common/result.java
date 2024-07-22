package com.web.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class result {

    private String code;
    private String msg;
    private Object data;

    public static result success() {
        return new result(Constants.CODE_200, "", null);
    }

    public static result success(Object data) {
        return new result(Constants.CODE_200, "", data);
    }

    public static result error(String code, String msg) {
        return new result(code, msg, null);
    }

    public static result error() {
        return new result(Constants.CODE_500, "系统错误", null);
    }

}
