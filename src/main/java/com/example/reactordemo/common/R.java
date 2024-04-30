package com.example.reactordemo.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoufe
 * @date 2024/4/25 16:21
 */
@Data
public class R {
    private Integer code;
    private String message;
    private Boolean success;
    private Map<String, Object> data;

    private R() {
    }

    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(2000);
        r.setMessage("success");
        r.setData(new HashMap<>());
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(5000);
        r.setMessage("error");
        r.setData(new HashMap<>());
        return r;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }


    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }



}
