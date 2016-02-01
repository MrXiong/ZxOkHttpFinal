package com.demo.zx.zxokhttpfinal.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.NewGameListAdapter;
import com.demo.zx.zxokhttpfinal.adapter.SuperPtrFrameLayoutAdapter;
import com.demo.zx.zxokhttpfinal.base.BaseActivity;
import com.demo.zx.zxokhttpfinal.http.Api;
import com.demo.zx.zxokhttpfinal.http.MyBaseHttpRequestCallback;
import com.demo.zx.zxokhttpfinal.http.model.GameInfo;
import com.demo.zx.zxokhttpfinal.http.model.NewGameResponse;
import com.demo.zx.zxokhttpfinal.ui.widget.swipeview.SwipeRefreshLayoutDirection;
import com.demo.zx.zxokhttpfinal.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

public class MaterialRefreshLayoutActivity extends BaseActivity {

    @Bind(R.id.refresh)
    MaterialRefreshLayout mRefresh;
    @Bind(R.id.rv_game)
    RecyclerView mRvGame;
    private int mPage = 1;
    private RequestParams mParams;
    List<GameInfo> mGameList;
    //private NewGameListAdapter mNewGameListAdapter;
    private boolean mDirection;
    private SuperPtrFrameLayoutAdapter mSuperPtrFrameLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_refresh_layout);
        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvGame.setLayoutManager(gridLayoutManager);

        mParams = new RequestParams(this);
        mParams.put("limit", 6);
        mParams.put("page", mPage);
        mGameList = new ArrayList<>();
       // mNewGameListAdapter = new NewGameListAdapter(this, mGameList, R.layout.adapter_new_game_list_item);
        mSuperPtrFrameLayoutAdapter = new SuperPtrFrameLayoutAdapter();
        mSuperPtrFrameLayoutAdapter.setList(mGameList);
        mRvGame.setAdapter(mSuperPtrFrameLayoutAdapter);
        //首次自动加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.autoRefresh();
            }
        },1000);
        //checkScroll();
        mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mDirection = true;
                requestData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mDirection = false;
                requestData();
            }

            @Override
            public void onfinish() {

            }
        });
    }

    private void checkScroll() {
        mRvGame.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount -1) && isSlidingToLast) {
                        mDirection = false;
                        //加载更多功能的代码
                        requestData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //大于0表示，正在向右/下滚动
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0 ? true : false;
            }

        });
    }

    private boolean isBottom() {
        LinearLayoutManager lm= (LinearLayoutManager) mRvGame.getLayoutManager();
        if(lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop()==0 && lm.findFirstVisibleItemPosition()==0) {
            return true;
        }
        return false;
    }

    private void requestData() {
       // mGvGame.setLayoutAnimation(AnimationUtils.getUniversalAnimation(this));
        if(mDirection) {
            mPage = 1;
        } else {
            mPage ++;
        }
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
                if(mDirection) {
                    mRefresh.finishRefresh();
                } else {
                    mRefresh.finishRefreshLoadMore();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                //请求错误
                Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogicSuccess(NewGameResponse newGameResponse) {
                List<GameInfo> data = newGameResponse.getData();
                if (data != null) {
                    if(mDirection) {
                        mGameList.clear();
                    }
                    mGameList.addAll(data);
                    mSuperPtrFrameLayoutAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getBaseContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
