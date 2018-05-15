package com.example.yang.ins.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.yang.ins.bean.Review;

import java.util.List;

/**
 * Created by youxihouzainali on 2018/5/14.
 */

public class ReviewAdapter extends BaseQuickAdapter<Review, BaseViewHolder> {

    public ReviewAdapter(int layoutResId, List<Review> list) {
        super(layoutResId, list);
    }

    protected void convert(BaseViewHolder helper, Review item) {
        /*helper.setText(R.id.myReview_bookName, item.getBookName());
        helper.setText(R.id.myReview_pubTime, item.getPub_time());
        helper.setText(R.id.myReview_text, item.getText());
        helper.addOnClickListener(R.id.btn_review_delete);*/
    }
}
