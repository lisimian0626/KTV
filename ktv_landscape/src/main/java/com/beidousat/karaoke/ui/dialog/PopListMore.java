package com.beidousat.karaoke.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.adapter.BaseRecyclerAdapter;
import com.bestarmedia.libwidget.util.DensityUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by J Wong on 2018/7/17.
 */

public class PopListMore extends PopupWindow {

    private Context mContext;
    private View contentView;
    private List<String> mTexts;
    private RecyclerView mRvItems;

    public PopListMore(Context context) {
        super(context);
        mContext = context;
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        contentView = inflater.inflate(R.layout.popup_list_common, null);
        this.setContentView(contentView);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置点击隐藏的属性
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        mRvItems = contentView.findViewById(R.id.rv_items);
//        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(mContext)
//                .color(Color.TRANSPARENT).size(10).margin(0, 0).build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvItems.setLayoutManager(layoutManager);
//        mRvItems.addItemDecoration(verDivider);

//        ColorDrawable dw = new ColorDrawable(0000000000);
//        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.popMenuMoreAnim);
    }

    public void setData(int resId) {
        setData(mContext.getResources().getStringArray(resId));
    }

    public void setData(String[] texts) {
        mTexts = Arrays.asList(texts);
    }

    public void setData(List<String> texts) {
        mTexts = texts;
        AdapterTabs adapterTabs = new AdapterTabs();
        adapterTabs.setData(mTexts);
        mRvItems.setAdapter(adapterTabs);
    }


    public void setBackground(int resId) {
        this.setBackgroundDrawable(mContext.getResources().getDrawable(resId));
    }

    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.mOnMenuClickListener = listener;
    }

    public void showAnchorLeft(View anchor) {

//        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        int popupWidth = getMeasuredWidth();
//        int popupHeight = view.getMeasuredHeight();
//        int[] location = new int[2];
//        add_ibtn.getLocationOnScreen(location);
//        pop.showAtLocation(add_ibtn, Gravity.NO_GRAVITY, location[0] - popupWidth, location[1] + add_ibtn.getHeight() / 2 - popupHeight / 2);

//        int popWidth = getContentView().getMeasuredWidth();
//        int popHeight = getContentView().getMeasuredHeight();
//        int anchorWidth = anchor.getMeasuredWidth();
        int anchorHeight = anchor.getMeasuredHeight();
        int popWidth = mTexts.size() * DensityUtil.dip2px(mContext, 50) + 12;
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Logger.d(getClass().getSimpleName(), "pop window getWidth():" + popWidth);
        this.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] - popWidth, location[1] + anchorHeight / 2 - 17);
    }

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }

    private class AdapterTabs extends BaseRecyclerAdapter<String> {

        private AdapterTabs() {
        }

        @Override
        protected int getItemViewLayoutId() {
            return R.layout.list_item_pop_menu_item;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseRecyclerAdapter.ViewHolder holder, final int position) {
            final String keyText = mData.get(position);
            View itemView = holder.getItemView();
            View view = itemView.findViewById(R.id.ll_tab);
            TextView tvKey = itemView.findViewById(android.R.id.title);
            tvKey.setText(keyText);
            view.setOnClickListener(view1 -> {
                if (mOnMenuClickListener != null)
                    mOnMenuClickListener.onMenuClick(position);
                dismiss();
            });
        }
    }

}
