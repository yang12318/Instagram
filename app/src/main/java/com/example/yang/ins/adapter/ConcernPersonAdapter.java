package com.example.yang.ins.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.ins.R;
import com.example.yang.ins.bean.Person;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by youxihouzainali on 2018/5/18.
 */

public class ConcernPersonAdapter extends BaseQuickAdapter<Person, BaseViewHolder> {
    public ConcernPersonAdapter(int layoutResId, List<Person> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Person item) {
        helper.setText(R.id.concern_username, item.getName());
        helper.setText(R.id.concern_nickname, item.getNickname());
        Glide.with(mContext).load(item.getSrc()).into((CircleImageView) helper.getView(R.id.concern_head));
        helper.addOnClickListener(R.id.concern_follow);
    }
}
