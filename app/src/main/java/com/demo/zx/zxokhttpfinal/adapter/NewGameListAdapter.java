package com.demo.zx.zxokhttpfinal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.universal.CommonAdapter;
import com.demo.zx.zxokhttpfinal.adapter.universal.ViewHolder;
import com.demo.zx.zxokhttpfinal.http.model.GameInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zx on 2016/1/20.
 */
public class NewGameListAdapter extends CommonAdapter<GameInfo> {
    public NewGameListAdapter(Context context, List<GameInfo> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, GameInfo gameInfo) {
        holder.setText(R.id.tv_game_name,gameInfo.getName());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageView ivGameIcon = holder.getView(R.id.iv_game_icon);
        ImageLoader.getInstance().displayImage(gameInfo.getIconUrl(), ivGameIcon, options);
        holder.setText(R.id.tv_playing_man_number,String.valueOf(gameInfo.getCommentCount()));
        if(gameInfo.getOpenState() == 0 || gameInfo.getOpenState() == 21) {
            holder.setVisible(R.id.tv_game_socre, View.INVISIBLE);
        } else {
            holder.setText(R.id.tv_game_socre,gameInfo.getTotalSocreV() + "分");
            holder.setVisible(R.id.tv_game_socre, true);
        }
    }
}
