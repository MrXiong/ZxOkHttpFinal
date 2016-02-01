package com.demo.zx.zxokhttpfinal.base;

import android.support.v4.app.Fragment;

import com.demo.zx.zxokhttpfinal.http.MyHttpCycleContext;

import cn.finalteam.okhttpfinal.HttpTaskHandler;

/**
 * Created by zx on 2016/1/26.
 */
public class BaseFragment extends Fragment implements MyHttpCycleContext {
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
