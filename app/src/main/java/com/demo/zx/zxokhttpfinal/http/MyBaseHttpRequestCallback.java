package com.demo.zx.zxokhttpfinal.http;

import com.demo.zx.zxokhttpfinal.http.model.BaseApiResponse;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:06
 */
public class MyBaseHttpRequestCallback<T extends BaseApiResponse> extends BaseHttpRequestCallback<T> {

    //这个t就是继承BaseApiResponse的实体对象List或者bean可以直接转换使用的
    @Override
    protected void onSuccess(T t) {
        int code = t.getCode();
        if ( code == 1 ) {
            onLogicSuccess(t);
        } else {
            onLogicFailure(t);
        }
    }

    public void onLogicSuccess(T t) {

    }

    public void onLogicFailure(T t) {

    }
}
