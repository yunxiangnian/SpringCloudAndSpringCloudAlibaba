package com.scalibaba.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;

    private String msg;

    private T data;

    public CommonResult(Integer code , String msg){
        this(code,msg,null);
    }
}
