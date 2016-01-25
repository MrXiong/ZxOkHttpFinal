package com.demo.zx.zxokhttpfinal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.dm.DownloadInfo;
import cn.finalteam.okhttpfinal.dm.DownloadListener;
import cn.finalteam.okhttpfinal.dm.DownloadManager;
import cn.finalteam.toolsfinal.ApkUtils;
import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.coder.Base64Coder;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.universal.CommonAdapter;
import com.demo.zx.zxokhttpfinal.adapter.universal.ViewHolder;
import com.demo.zx.zxokhttpfinal.base.Constants;
import com.demo.zx.zxokhttpfinal.http.MyHttpCycleContext;
import com.demo.zx.zxokhttpfinal.model.GameDownloadInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/8 上午9:26
 */
public class DownloadManagerListAdapter extends CommonAdapter<DownloadInfo>{
    private View mLastShowBottomBar;
    private int mLastShowBottomBarPos = -1;
    protected List<DownloadInfo> mList;
    private Map<String, MyDLTaskListener> mDListenerMap;
    public DownloadManagerListAdapter(Context context, List<DownloadInfo> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mDListenerMap = new HashMap<>();
        this.mList = datas;
    }

    @Override
    public void convert(ViewHolder holder, DownloadInfo downloadInfo) {
        holder.setProgress(R.id.number_progress_bar, downloadInfo.getProgress());
        if ( downloadInfo.getTotalLength() > 0 ) {
            String downladScale = FileUtils.generateFileSize(downloadInfo.getDownloadLength()) + "/"
                    + FileUtils.generateFileSize(downloadInfo.getTotalLength());
            holder.setText(R.id.tv_download_scale, downladScale);
        }
        if ( downloadInfo.getState() == DownloadInfo.DOWNLOADING || downloadInfo.getState() == DownloadInfo.WAIT) {
            holder.setText(R.id.btn_operate, "暂停");
            if ( downloadInfo.getState() == DownloadInfo.WAIT ) {
                holder.setText(R.id.tv_download_state, "等待下载");
            } else {
                holder.setText(R.id.tv_download_state, "下载中");
            }
        } else if ( downloadInfo.getState() == DownloadInfo.COMPLETE ) {
            holder.setText(R.id.btn_operate, "安装");
            holder.setText(R.id.tv_download_state, "下载完成");
        } else {
            holder.setText(R.id.btn_operate, "继续");
            holder.setText(R.id.tv_download_state, "已暂停");
        }

        if (mLastShowBottomBarPos == holder.getPosition()) {
            holder.setVisible(R.id.ll_bottom_bar, true);
        } else {
            holder.setVisible(R.id.ll_bottom_bar, false);
        }

        String key = String.format(Constants.GAME_DOWNLOAD_INFO, Base64Coder.encodeToString(downloadInfo.getUrl().getBytes(), Base64Coder.DEFAULT));
        GameDownloadInfo gameDownloadInfo = (GameDownloadInfo) AppCacheUtils.getInstance(mContext).getObject(key);
        if ( gameDownloadInfo != null ) {
            holder.setText(R.id.tv_title, gameDownloadInfo.getAppName());
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageView ivIcon = holder.getView(R.id.iv_icon);
            ImageLoader.getInstance().displayImage(gameDownloadInfo.getLogo(), ivIcon, options);
        }
        //holder.setOnClickListener(new ItemClickListener(holder, holder.getPosition()));
        holder.setOnClickListener(R.id.tv_cancel, new CancelClickListener(downloadInfo));
        holder.setOnClickListener(R.id.btn_operate, new OperateButtonClickListener(downloadInfo, holder));
        holder.setOnClickListener(R.id.tv_game_detail, new GameDetailClickListener(downloadInfo));

        addDownloadListener(downloadInfo, holder);
    }



    /**
     * 下载进度 listener
     */
    private class MyDLTaskListener extends DownloadListener {
        private ViewHolder holder;
        public MyDLTaskListener(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            super.onProgress(downloadInfo);
            holder.setText(R.id.btn_operate, "暂停");
            holder.setText(R.id.tv_download_state, "下载中");
            holder.setProgress(R.id.number_progress_bar, downloadInfo.getProgress());
            String downladScale = FileUtils.generateFileSize(downloadInfo.getDownloadLength()) + "/"
                    + FileUtils.generateFileSize(downloadInfo.getTotalLength());
            holder.setText(R.id.tv_download_scale, downladScale);
            holder.setText(R.id.tv_download_speed, FileUtils.generateFileSize(downloadInfo.getNetworkSpeed()));
        }

        @Override
        public void onError(DownloadInfo downloadInfo) {
            super.onError(downloadInfo);
            holder.setText(R.id.btn_operate, "继续");
            holder.setText(R.id.tv_download_state, "已暂停");
            notifyDataSetChanged();
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            super.onFinish(downloadInfo);
            holder.setText(R.id.tv_download_state, "下载完成");
            holder.setText(R.id.btn_operate, "安装");
            notifyDataSetChanged();
        }
    }

    /**
     * 继续、停止、安装按钮事件
     */
    private class OperateButtonClickListener implements View.OnClickListener {

        private DownloadInfo info;
        private ViewHolder holder;

        public OperateButtonClickListener(DownloadInfo info, ViewHolder holder) {
            this.info = info;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {
            int state = info.getState();
            if ( state == DownloadInfo.DOWNLOADING || state == DownloadInfo.WAIT) {
                Logger.d("DownloadInfo.DOWNLOADING ");
                DownloadManager.getInstance(mContext).stopTask(info.getUrl());
                holder.setText(R.id.btn_operate, "继续");

            } else if ( state == DownloadInfo.COMPLETE ) {
                Logger.d("DownloadInfo.COMPLETE ");
                holder.setText(R.id.tv_download_state, "下载完成");
                holder.setText(R.id.btn_operate, "安装");
                ApkUtils.install(mContext, new File(info.getTargetPath()));
            } else {
                DownloadManager.getInstance(mContext).restartTask(info.getUrl());
                holder.setText(R.id.btn_operate, "暂停");
                if ( DownloadManager.getInstance(mContext).getDownloadingSize() >= 3 ) {
                    holder.setText(R.id.tv_download_state, "等待下载");
                } else {
                    holder.setText(R.id.tv_download_state, "下载中");
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 取消按钮
     */
    private class CancelClickListener implements View.OnClickListener {

        private DownloadInfo info;

        public CancelClickListener(DownloadInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View view) {
            mList.remove(info);
            mLastShowBottomBarPos = -1;
            notifyDataSetChanged();
            DownloadManager.getInstance(mContext).deleteTask(info.getUrl());
        }
    }

    private class GameDetailClickListener implements View.OnClickListener {

        private DownloadInfo info;

        public GameDetailClickListener(DownloadInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View view) {
        }
    }

    /**
     * Item click listener
     */
    private class ItemClickListener implements View.OnClickListener {

        private ViewHolder holder;
        private int position;

        public ItemClickListener(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void onClick(View view) {

            //隐藏上一个BottomBar
            LinearLayout llBottomBar = holder.getView(R.id.ll_bottom_bar);
            if (mLastShowBottomBar != null && mLastShowBottomBar != llBottomBar) {
                mLastShowBottomBar.setVisibility(View.GONE);
            }

            if (llBottomBar.getVisibility() == View.VISIBLE) {
                llBottomBar.setVisibility(View.GONE);
                mLastShowBottomBarPos = -1;
            } else {
                llBottomBar.setVisibility(View.VISIBLE);
                mLastShowBottomBarPos = position;
                mLastShowBottomBar = llBottomBar;
            }

            notifyDataSetChanged();
        }
    }

    /**
     * 添加下载回调
     * @param info
     * @param holder
     */
    private void addDownloadListener(DownloadInfo info, ViewHolder holder) {
        String key = info.getUrl();
        MyDLTaskListener dlListener = mDListenerMap.get(key);
        if (dlListener == null) {
            MyDLTaskListener listener = new MyDLTaskListener(holder);
            mDListenerMap.put(key, listener);
            DownloadManager.getInstance(mContext).addTaskListener(info.getUrl(), listener);
        }
    }
}
