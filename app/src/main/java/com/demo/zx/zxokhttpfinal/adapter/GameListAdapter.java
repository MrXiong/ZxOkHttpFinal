package com.demo.zx.zxokhttpfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.universal.CommonAdapter;
import com.demo.zx.zxokhttpfinal.adapter.universal.ViewHolder;
import com.demo.zx.zxokhttpfinal.base.Constants;
import com.demo.zx.zxokhttpfinal.model.GameDownloadInfo;
import com.demo.zx.zxokhttpfinal.model.GameInfo;
import com.demo.zx.zxokhttpfinal.ui.activity.DownloadManangerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.finalteam.okhttpfinal.dm.DownloadManager;
import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.coder.Base64Coder;

/**
 * Created by zx on 2016/1/20.
 */
public class GameListAdapter extends CommonAdapter<GameInfo> {
    public GameListAdapter(Context context, List<GameInfo> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, final GameInfo gameInfo) {
        holder.setText(R.id.tv_game_name,gameInfo.getGameName());
        if ( DownloadManager.getInstance(mContext).hasTask(gameInfo.getUrl()) ) {
            holder.setEnabled(R.id.btn_download, false);
            holder.setText(R.id.btn_download, "已在队列");
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageView ivGameIcon = holder.getView(R.id.iv_game_icon);
        ImageLoader.getInstance().displayImage(gameInfo.getCoverUrl(), ivGameIcon, options);
        holder.setOnClickListener(R.id.btn_download, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = gameInfo.getUrl();
                if(!DownloadManager.getInstance(mContext).hasTask(url)) {
                    DownloadManager.getInstance(mContext).addTask(url, null);

                    holder.setEnabled(R.id.btn_download, false);
                    holder.setText(R.id.btn_download, "已在队列");

                    GameDownloadInfo info = new GameDownloadInfo();
                    info.setAppName(gameInfo.getGameName());
                    info.setLogo(gameInfo.getCoverUrl());
                    info.setPackageName(gameInfo.getPackageName());
                    String key = String.format(Constants.GAME_DOWNLOAD_INFO, Base64Coder.encodeToString(url.getBytes(), Base64Coder.DEFAULT));
                    AppCacheUtils.getInstance(mContext).put(key, info);

                }
            }
        });
    }
}
