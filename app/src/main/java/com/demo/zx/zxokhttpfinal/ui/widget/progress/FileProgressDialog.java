package com.demo.zx.zxokhttpfinal.ui.widget.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.demo.zx.zxokhttpfinal.R;

/**
 * Created by zx on 2016/1/21.
 */
public class FileProgressDialog extends ProgressDialog {
    public FileProgressDialog(Context context) {
        super(context);
        setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        setCancelable(true);// 设置是否可以通过点击Back键取消
        setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        setIcon(R.mipmap.ic_launcher);// 设置提示的title的图标，默认是没有的
        setTitle("提示");
        setMax(100);
    }

}
