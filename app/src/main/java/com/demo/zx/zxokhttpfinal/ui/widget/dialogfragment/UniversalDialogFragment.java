package com.demo.zx.zxokhttpfinal.ui.widget.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.demo.zx.zxokhttpfinal.R;

/**
 * Created by zx on 2016/1/20.
 */
public class UniversalDialogFragment extends DialogFragment {
    private static final String ARG = "arg";

    public static UniversalDialogFragment newInstance(String s) {

        Bundle args = new Bundle();
        args.putString(ARG, s);
        UniversalDialogFragment fragment = new UniversalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_universal, container, false);
        TextView tvResult = (TextView) rootView.findViewById(R.id.tv_result);
        tvResult.setText(getArguments().getString(ARG));
        return rootView;
    }
}
