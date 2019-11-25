package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libskin.SkinManager;
import com.bestarmedia.libwidget.util.DensityUtil;
import com.bestarmedia.libcommon.util.ListUtil;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.adapter.BaseRecyclerAdapter;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import java.util.List;


/**
 * Created by J Wong on 2015/10/15 13:35.
 */
public class WidgetTopTabs extends RelativeLayout {

    private View mRootView;

    private RecyclerView mHlvLeft, mHlvRight;

    private OnTabClickListener mLeftOnItemClickListener, mRightOnItemClickListener;

    private AdapterTabs mAdapterTabsLeft, mAdapterTabsRight;

    public WidgetTopTabs(Context context) {
        super(context);
        initView();
    }

    public WidgetTopTabs(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();

        readAttr(attrs);
    }

    public WidgetTopTabs(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();

        readAttr(attrs);
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_top_tabs, this);
        mHlvLeft = mRootView.findViewById(R.id.hlv_left);
        mHlvRight = mRootView.findViewById(R.id.hlv_right);

        init();
    }

    private void readAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetTopTabs);
            float leftWeight = a.getFloat(R.styleable.WidgetTopTabs_leftTabWeight, 3);
            float rightWeight = a.getFloat(R.styleable.WidgetTopTabs_rightTabWeight, 1);
            Logger.d(getClass().getSimpleName(), "readAttr leftWeight:" + leftWeight + " rightWeight: " + rightWeight);
            setLeftTabWeight(leftWeight);
            setRightTabWeight(rightWeight);
        }
    }

    public void setLeftTabWeight(float weight) {
//        LayoutParams paramLeft = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, weight);
//        mHlvLeft.setLayoutParams(paramLeft);
    }

    public void setRightTabWeight(float weight) {
//        LayoutParams paramLeft = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, weight);
//        mHlvRight.setLayoutParams(paramLeft);
    }


    private void init() {

        int dividerW = DensityUtil.dip2px(getContext(), 8);

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(dividerW).margin(dividerW, dividerW).build();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHlvRight.setLayoutManager(layoutManager2);

        mHlvRight.addItemDecoration(verDivider);
        mAdapterTabsRight = new AdapterTabs(rightOnTabClickListener, false);
        mHlvRight.setAdapter(mAdapterTabsRight);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHlvLeft.setLayoutManager(layoutManager);

        mHlvLeft.addItemDecoration(verDivider);
        mAdapterTabsLeft = new AdapterTabs(leftOnTabClickListener, true);
        mHlvLeft.setAdapter(mAdapterTabsLeft);

    }

    private boolean mIsRightClickSelect = true;

    public void setRightClickSelect(boolean isClickSelect) {
        mIsRightClickSelect = isClickSelect;
    }

    public void setLeftTabShow(boolean show) {
        mHlvLeft.setVisibility(show ? VISIBLE : GONE);
    }

    public void setRightTabShow(boolean show) {
        mHlvRight.setVisibility(show ? VISIBLE : GONE);
    }

    public void setLeftTabs(int resId) {
        String[] texts = getResources().getStringArray(resId);
        setLeftTabs(texts);

    }


    public void setLeftTabs(String[] texts) {
        mAdapterTabsLeft.setData(ListUtil.array2List(texts));
        setLeftTabFocus(0);
        mAdapterTabsLeft.notifyDataSetChanged();
        mHlvLeft.setVisibility(texts != null && texts.length > 0 ? VISIBLE : GONE);
    }

    private OnTabClickListener leftOnTabClickListener = new OnTabClickListener() {
        @Override
        public void onTabClick(int position) {
//            if (mAdapterTabsLeft.getFocusedTab() != position) {
            setLeftTabFocus(position);
            if (mLeftOnItemClickListener != null)
                mLeftOnItemClickListener.onTabClick(position);
//            }
        }
    };
    private OnTabClickListener rightOnTabClickListener = new OnTabClickListener() {
        @Override
        public void onTabClick(int position) {
            if (!mIsRightClickSelect || mAdapterTabsRight.getFocusedTab() != position) {
                setRightTabFocus(position);
                if (mRightOnItemClickListener != null)
                    mRightOnItemClickListener.onTabClick(position);
            }
        }
    };


    public void setLeftTabFocus(int position) {
        mAdapterTabsLeft.setFocusedTab(position);
        mAdapterTabsLeft.notifyDataSetChanged();
    }

    public void setRightTabFocus(int position) {
        mAdapterTabsRight.setFocusedTab(position);
        mAdapterTabsRight.notifyDataSetChanged();
    }

    public void setRightTabs(int resId) {
        String[] texts = getResources().getStringArray(resId);
        setRightTabs(texts);
    }


    public void setRightTabs(String[] texts) {
        mAdapterTabsRight.setData(ListUtil.array2List(texts));
        mAdapterTabsRight.notifyDataSetChanged();
        mHlvRight.setVisibility(texts != null && texts.length > 0 ? VISIBLE : GONE);
    }

    public void setRightTabs(List<String> list) {
        mAdapterTabsRight.setData(list);
        mAdapterTabsRight.notifyDataSetChanged();
        mHlvRight.setVisibility(list != null && list.size() > 0 ? VISIBLE : GONE);
    }

    public void setLeftTabClickListener(OnTabClickListener listener) {
        this.mLeftOnItemClickListener = listener;
    }

    public void setRightTabClickListener(OnTabClickListener listener) {
        this.mRightOnItemClickListener = listener;
    }

    public interface OnTabClickListener {
        void onTabClick(int position);
    }


    private class AdapterTabs extends BaseRecyclerAdapter<String> {

        private int mFocusItemPs = -1;
        private OnTabClickListener mOnTabClickListener;
        private boolean mIsLeft;

        private AdapterTabs(OnTabClickListener listener, boolean isLeft) {
            this.mOnTabClickListener = listener;
            mIsLeft = isLeft;
        }

        private int getFocusedTab() {
            return mFocusItemPs;
        }

        private void setFocusedTab(int tab) {
            this.mFocusItemPs = tab;
        }

        @Override
        protected int getItemViewLayoutId() {
            return R.layout.list_item_top_tab;
        }

        @Override
        public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, final int position) {
            final String keyText = mData.get(position);
            View itemView = holder.getItemView();
            View view = itemView.findViewById(R.id.ll_tab);
            TextView tvKey = itemView.findViewById(android.R.id.title);
            ColorStateList dSongNameColor = SkinManager.getInstance().getResourceManager().getColorStateList2("selector_tab_text");
            if (dSongNameColor != null) {
                tvKey.setTextColor(dSongNameColor);
            }
            tvKey.setText(keyText);
            if (mIsLeft) {
                view.setSelected(mFocusItemPs == position);
            } else {
                if (mIsRightClickSelect)
                    view.setSelected(mFocusItemPs == position);
            }
            view.setOnClickListener(view1 -> {
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClick(position);
                }
                mFocusItemPs = position;
                notifyDataSetChanged();
            });
        }
    }

    public View getWidgetTopLastView() {
        return mHlvLeft.getChildAt(mAdapterTabsLeft.getItemCount() - 1);
    }
}
