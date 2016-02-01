package com.demo.zx.zxokhttpfinal.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.adapter.NewGameListAdapter;
import com.demo.zx.zxokhttpfinal.adapter.SuperPtrFrameLayoutAdapter;
import com.demo.zx.zxokhttpfinal.http.Api;
import com.demo.zx.zxokhttpfinal.http.MyBaseHttpRequestCallback;
import com.demo.zx.zxokhttpfinal.http.model.GameInfo;
import com.demo.zx.zxokhttpfinal.http.model.NewGameResponse;
import com.demo.zx.zxokhttpfinal.http.model.UploadResponse;
import com.demo.zx.zxokhttpfinal.ui.activity.DownloadManangerActivity;
import com.demo.zx.zxokhttpfinal.ui.activity.MaterialRefreshLayoutActivity;
import com.demo.zx.zxokhttpfinal.ui.activity.TestActivity;
import com.demo.zx.zxokhttpfinal.ui.activity.UltraPullActivity;
import com.demo.zx.zxokhttpfinal.ui.widget.dialogfragment.DownLoadListDialogFragment;
import com.demo.zx.zxokhttpfinal.ui.widget.dialogfragment.UniversalDialogFragment;
import com.demo.zx.zxokhttpfinal.ui.widget.progress.FileProgressDialog;
import com.demo.zx.zxokhttpfinal.ui.widget.swipeview.SwipeRefreshLayout;
import com.demo.zx.zxokhttpfinal.ui.widget.swipeview.SwipeRefreshLayoutDirection;
import com.demo.zx.zxokhttpfinal.utils.AnimationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.FileDownloadCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.okhttpfinal.StringHttpRequestCallback;
import cn.finalteam.toolsfinal.JsonFormatUtils;
import cn.finalteam.toolsfinal.StringUtils;
import us.feras.mdv.MarkdownView;

public class UniversalActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_game)
    RecyclerView mRvGame;
    //NewGameListAdapter mNewGameListAdapter;
    List<GameInfo> mGameList;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    private int mPage = 1;
    //private NewGameListAdapter mNewGameListAdapter;

    private RequestParams mParams;
    private UniversalDialogFragment mUniversalDialogFragment;
    private String mResult;
    private List<com.demo.zx.zxokhttpfinal.model.GameInfo> mLocalGameList = new ArrayList<>();
    private SuperPtrFrameLayoutAdapter mSuperPtrFrameLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal);
        ButterKnife.bind(this);
        initNewGameList();

        mLocalGameList.add(new com.demo.zx.zxokhttpfinal.model.GameInfo("冒险与挖矿", "com.speedsoftware.rootexplorer", "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk", "http://anzhuo.webdown.paojiao.cn/game/05f/caf/ff63b7c3bc79c350437b96cce8/icon2.png"));
        mLocalGameList.add(new com.demo.zx.zxokhttpfinal.model.GameInfo("二战风云", "com.wistone.war2victory", "http://anzhuo.webdown.paojiao.cn/game/6cb/ab7/cb92a447231d2865d6536b1b32/com.wistone.war2victory_20150909103114_2.8.1_build_16302_wst.apk", "http://anzhuo.webdown.paojiao.cn/game/6cb/ab7/cb92a447231d2865d6536b1b32/icon2.png"));
        mLocalGameList.add(new com.demo.zx.zxokhttpfinal.model.GameInfo("开心消鱼儿", "com.cnnzzse.kxxye", "http://apk.r1.market.hiapk.com/data/upload/apkres/2015/9_23/17/com.cnnzzse.kxxye_050348.apk", "http://img.r1.market.hiapk.com/data/upload/2015/09_23/17/72_72_20150923050916_4223.png"));
        mLocalGameList.add(new com.demo.zx.zxokhttpfinal.model.GameInfo("开心猜成语", "com.nerser.ccser", "http://apk.r1.market.hiapk.com/data/upload/apkres/2015/9_11/14/com.nerser.ccser_025042.apk", "http://img.r1.market.hiapk.com/data/upload/2015/09_11/14/72_72_20150911025051_7870.png"));
    }

    private void initNewGameList() {
        mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
        mSwipeLayout.setOnRefreshListener(this);

        setTitle("游戏列表");
        mGameList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRvGame.setLayoutManager(gridLayoutManager);


       // mNewGameListAdapter = new NewGameListAdapter(this, mGameList, R.layout.adapter_new_game_list_item);
        mSuperPtrFrameLayoutAdapter = new SuperPtrFrameLayoutAdapter();
        mSuperPtrFrameLayoutAdapter.setList(mGameList);
        mRvGame.setAdapter(mSuperPtrFrameLayoutAdapter);
        checkScroll();
        mParams = new RequestParams(this);
        mParams.put("limit", 9);
        mParams.put("page", mPage);
        //首次自动加载
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
                onRefresh(SwipeRefreshLayoutDirection.TOP);
            }
        },1000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_universal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_string :
                HttpRequestCallbackString();
                break;
            case R.id.action_json :
                HttpRequestCallbackJson();
                break;
            case R.id.action_download_list :
                downloadList();
                break;
            case R.id.action_upload :
                upload();
                break;
            case R.id.action_download_easy :
                download();
                break;
            case R.id.action_download_manager :
                startActivity(new Intent(this, DownloadManangerActivity.class));
                break;
            case R.id.action_other :
                startActivity(new Intent(this, UltraPullActivity.class));
                break;
            case R.id.action_fragment_tabHost :
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.action_material_refreshLayou :
                startActivity(new Intent(this, MaterialRefreshLayoutActivity.class));
                break;
            case R.id.action_super_ptrFrameLayout :
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void download() {
        final FileProgressDialog dialog = new FileProgressDialog(this);
        dialog.show();
        String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
        HttpRequest.download(url, new File("/sdcard/rootexplorer_140220.apk"), new FileDownloadCallback() {
            @Override public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                dialog.setProgress(progress);
                //String speed = FileUtils.generateFileSize(networkSpeed);
            }

            @Override public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override public void onDone() {
                super.onDone();
                Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void upload() {
        final FileProgressDialog dialog = new FileProgressDialog(this);
        dialog.show();
        File file = new File("/sdcard/DCIM/GalleryFinal/IMG20151201200821.jpg");
        String userId = "3097424";
        RequestParams params = new RequestParams(this);
        params.put("file", file);
        params.put("userId", userId);
        params.put("token", "NTCrWFKFCn1r8iaV3K0fLz2gX9LZS1SR");
        params.put("udid", "f0ba33e4de8a657d");
        params.put("sign", "39abfa9af6f6e3c8776b01ae612bc14c");
        params.put("version", "2.1.0");
        params.put("mac", "8c:3a:e3:5e:68:e0");
        params.put("appId", "paojiao_aiyouyou20");
        params.put("imei", "359250051610200");
        params.put("model", "Nexus 5");
        params.put("cid", "paojiao");
        String fileuploadUri = "http://uploader.paojiao.cn/avatarAppUploader?userId=" + userId;

        HttpRequest.post(fileuploadUri, params, new BaseHttpRequestCallback<UploadResponse>() {
            @Override
            public void onSuccess(UploadResponse uploadResponse) {
                super.onSuccess(uploadResponse);
                Toast.makeText(getBaseContext(), "上传成功：" + uploadResponse.getData(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Toast.makeText(getBaseContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress, long networkSpeed, boolean done) {
                dialog.setProgress(progress);
            }
        });
    }

    private void downloadList() {
        DownLoadListDialogFragment fragment = DownLoadListDialogFragment.newInstance(mLocalGameList);
        fragment.setCancelable(true);
        fragment.show(getSupportFragmentManager(), "DownLoadListDialogFragment");
    }

    private void HttpRequestCallbackString() {
        HttpRequest.post(Api.NEW_GAME, mParams, new StringHttpRequestCallback(){
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                mResult = JsonFormatUtils.formatJson(s);
                mUniversalDialogFragment = UniversalDialogFragment.newInstance(mResult);
                mUniversalDialogFragment.setCancelable(true);
                mUniversalDialogFragment.show(getSupportFragmentManager(),"UniversalDialogFragment");
            }
        });
    }

    private void HttpRequestCallbackJson() {
        HttpRequest.post(Api.NEW_GAME, mParams, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                mResult = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                mUniversalDialogFragment = UniversalDialogFragment.newInstance(mResult);
                mUniversalDialogFragment.setCancelable(true);
                mUniversalDialogFragment.show(getSupportFragmentManager(),"UniversalDialogFragment");
            }
        });
    }
    private void requestData(final SwipeRefreshLayoutDirection direction) {
        //mGvGame.setLayoutAnimation(AnimationUtils.getUniversalAnimation(this));
        if(direction == SwipeRefreshLayoutDirection.TOP) {
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
                mSwipeLayout.setRefreshing(false);
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
                    if(direction == SwipeRefreshLayoutDirection.TOP) {
                        mGameList.clear();
                    }
                        mGameList.addAll(data);
                    mSuperPtrFrameLayoutAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getBaseContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }

           /*     if(data != null && data.size() > 0) {
                    mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.BOTH);
                } else {
                    mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.TOP);
                }*/

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
                        //加载更多功能的代码
                        requestData(SwipeRefreshLayoutDirection.BOTTOM);
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
    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        requestData(direction);
    }
}

