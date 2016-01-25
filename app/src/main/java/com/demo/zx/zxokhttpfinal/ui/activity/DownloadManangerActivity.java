package com.demo.zx.zxokhttpfinal.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.DownloadManagerListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.dm.DownloadInfo;
import cn.finalteam.okhttpfinal.dm.DownloadManager;

public class DownloadManangerActivity extends AppCompatActivity {

    @Bind(R.id.lv_task_list)
    ListView mLvTaskList;
    private DownloadManagerListAdapter mDownloadManagerListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_mananger);
        ButterKnife.bind(this);
        setTitle("下载管理");
        List<DownloadInfo> list = DownloadManager.getInstance(this).getAllTask();
        mDownloadManagerListAdapter = new DownloadManagerListAdapter(this, list, R.layout.adapter_download_manager_list_item);
        mLvTaskList.setAdapter(mDownloadManagerListAdapter);
    }
}
