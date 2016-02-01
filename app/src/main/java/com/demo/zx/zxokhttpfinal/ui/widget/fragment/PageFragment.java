package com.demo.zx.zxokhttpfinal.ui.widget.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.NewGameListAdapter;
import com.demo.zx.zxokhttpfinal.adapter.universal.CommonAdapter;
import com.demo.zx.zxokhttpfinal.base.BaseFragment;
import com.demo.zx.zxokhttpfinal.base.UniversalActivity;
import com.demo.zx.zxokhttpfinal.http.Api;
import com.demo.zx.zxokhttpfinal.http.MyBaseHttpRequestCallback;
import com.demo.zx.zxokhttpfinal.http.model.GameInfo;
import com.demo.zx.zxokhttpfinal.http.model.NewGameResponse;
import com.demo.zx.zxokhttpfinal.ui.widget.swipeview.SwipeRefreshLayoutDirection;
import com.demo.zx.zxokhttpfinal.utils.AnimationUtils;
import com.demo.zx.zxokhttpfinal.utils.LocalDisplay;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.header.StoreHousePath;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PageFragment extends BaseFragment {
    public static final String ARG_PARAM = "param";
    @Bind(R.id.store_house_ptr_frame)
    PtrFrameLayout pullToRefresh;
    @Bind(R.id.gv_game)
    GridView gvGame;
    private int mPage = 1;
    List<GameInfo> mGameList;
    private StoreHouseHeader header;
    private NewGameListAdapter mNewGameListAdapter;
    private RequestParams mParams;


    public static PageFragment newInstance(int position) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, root);
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(getContext(), mGameList, R.layout.adapter_new_game_list_item);
        gvGame.setAdapter(mNewGameListAdapter);
        mParams = new RequestParams(this);
        mParams.put("limit", 6);
        mParams.put("page", mPage);
        int position = FragmentPagerItem.getPosition(getArguments());
        initUltraPull();
            initData();
        return root;
    }

    private void initData() {
        pullToRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                //return checkCanRefresh();
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                requestData();
            }
        });
    }

    private void initUltraPull() {
        header = new StoreHouseHeader(getContext());
        header.setPadding(0, LocalDisplay.dp2px(15), 0, 0);
        header.setTextColor(Color.RED);
        header.initWithString("LOADING",20);
        //下拉刷新
        pullToRefresh.setPullToRefresh(true);
        pullToRefresh.setLoadingMinTime(1000);
        pullToRefresh.setDurationToClose(50);
        pullToRefresh.setDurationToCloseHeader(1000);
        pullToRefresh.setHeaderView(header);
        pullToRefresh.addPtrUIHandler(header);
        //加载更多
        pullToRefresh.setLoadingMinTime(1000);

    }
    private void requestData() {
        mParams.put("page", mPage);
        HttpRequest.post(Api.NEW_GAME, mParams, new MyBaseHttpRequestCallback<NewGameResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                //请求之前
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //请求之后
                if(pullToRefresh.isRefreshing()) {
                    pullToRefresh.refreshComplete();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                //请求错误
                Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogicSuccess(NewGameResponse newGameResponse) {
                List<GameInfo> data = newGameResponse.getData();
                if (data != null) {
                    mGameList.clear();
                    mGameList.addAll(data);
                    mNewGameListAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLogicFailure(NewGameResponse newGameResponse) {
                super.onLogicFailure(newGameResponse);
                //请求成功，逻辑错误
                String msg = newGameResponse.getMsg();
                if (StringUtils.isEmpty(msg)) {
                    msg = "网络异常";
                }
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

        });
    }
    private boolean checkCanRefresh() {
        if (mNewGameListAdapter.getCount() == 0 || gvGame == null) {
            return true;
        }
        if (gvGame.getChildCount() == 0) {
            return true;
        }
        return gvGame.getFirstVisiblePosition() == 0 && gvGame.getChildAt(0).getTop() == 0;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
