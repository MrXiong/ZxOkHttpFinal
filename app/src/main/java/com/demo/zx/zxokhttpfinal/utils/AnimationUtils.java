package com.demo.zx.zxokhttpfinal.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;

import com.demo.zx.zxokhttpfinal.R;

/**
 * Created by zx on 2016/1/25.
 */
public class AnimationUtils {
    public static LayoutAnimationController getUniversalAnimation(Context context){
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.anim_item);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;

    }
}
