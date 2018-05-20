package com.example.yang.ins.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.ins.bean.Info1;

import java.util.List;

/**
 * Created by yang on 2018/5/20.
 */

public class Info1Adapter extends BaseQuickAdapter<Info1, BaseViewHolder> {
    public Info1Adapter(int layoutResId, @Nullable List<Info1> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Info1 item) {

    }
}
