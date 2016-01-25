package com.demo.zx.zxokhttpfinal.ui.widget.dialogfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.GameListAdapter;
import com.demo.zx.zxokhttpfinal.model.GameInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zx on 2016/1/20.
 */
public class DownLoadListDialogFragment extends DialogFragment {
    private static final String ARG = "arg";
    @Bind(R.id.lv_game)
    ListView mLvGame;


    public static DownLoadListDialogFragment newInstance(List<GameInfo> gameList) {
        Bundle args = new Bundle();
        args.putSerializable(ARG, (Serializable) gameList);
        DownLoadListDialogFragment fragment = new DownLoadListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_download, container, false);
        ButterKnife.bind(this, rootView);
        List<GameInfo> gameList = (List<GameInfo>) getArguments().getSerializable(ARG);
        GameListAdapter gameListAdapter = new GameListAdapter(getActivity(), gameList, R.layout.adapter_game_list_item);
        mLvGame.setAdapter(gameListAdapter);
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
