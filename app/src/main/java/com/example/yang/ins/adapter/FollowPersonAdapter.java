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

public class FollowPersonAdapter extends BaseQuickAdapter<Person, BaseViewHolder> {
    public FollowPersonAdapter(int layoutResId, List<Person> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Person item) {
        helper.setText(R.id.follow_username, item.getName());
        helper.setText(R.id.follow_nickname, item.getNickname());
        Glide.with(mContext).load("http://ktchen.cn"+item.getSrc()).into((CircleImageView) helper.getView(R.id.follow_head));
        helper.addOnClickListener(R.id.follow_cancel);
        helper.addOnClickListener(R.id.follow_head);
        helper.addOnClickListener(R.id.follow_username);
        helper.addOnClickListener(R.id.follow_nickname);
    }
}
