package com.demo.zx.zxokhttpfinal.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.demo.zx.zxokhttpfinal.R;
import com.demo.zx.zxokhttpfinal.base.BaseActivity;
import com.demo.zx.zxokhttpfinal.ui.widget.fragment.PageFragment;
import com.demo.zx.zxokhttpfinal.ui.widget.fragment.PageRecyclerFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class UltraPullActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    public static final String TAG = UltraPullActivity.class.getSimpleName();
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private String mTitlePre;
    private StoreHouseHeader header;
    private FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra_pull);
        ButterKnife.bind(this);
        initSmartTab();
    }

    private void initSmartTab() {
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("tile1", PageFragment.class)
                .add("tile2", PageRecyclerFragment.class)
                .create());

        viewpager.setAdapter(adapter);

        viewpagertab.setViewPager(viewpager);
        viewpagertab.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {



    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
