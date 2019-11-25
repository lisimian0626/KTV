package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/5/17.
 */
public class AdtGiftChoose extends RecyclerView.Adapter<AdtGiftChoose.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<Good> mData = new ArrayList<>();
    private int mTotalCount;
    private boolean mMultiplyCount;//可选个数是否需要乘以购物车数量
    private ShopCart mShopCart;
    private String mComboId;
    private GiftsChooseListener mGiftsChooseListener;

    public AdtGiftChoose(Context context, String comboId, ShopCart shopCart) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mShopCart = shopCart;
        this.mComboId = comboId;
    }


    public List<Good> getData() {
        return mData;
    }


    /**
     * @param totalCount    套餐可选总量
     * @param data          商品列表
     * @param multiplyCount 个数是否需要乘以购物车数量，零度、维达 不需要，其他需要
     */
    public void setData(int totalCount, List<Good> data, boolean multiplyCount) {
        this.mTotalCount = totalCount;
        this.mMultiplyCount = multiplyCount;
        for (Good good : data) {
            good.count = good.select;
        }
        this.mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvCount, tvMax;
        private ImageView ivPlus, ivSub;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_gitf_choose, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvCount = view.findViewById(R.id.tv_count);
        viewHolder.ivSub = view.findViewById(R.id.iv_sub);
        viewHolder.ivPlus = view.findViewById(R.id.iv_plus);
        viewHolder.tvMax = view.findViewById(R.id.tv_max);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Good good = mData.get(position);
        holder.tvName.setText(good.Name);
        holder.ivSub.setEnabled(true);
        holder.ivPlus.setEnabled(true);
        int itemMax = (mMultiplyCount ? mData.get(position).Number * mShopCart.PointNum : mData.get(position).Number);
        holder.tvMax.setText(Html.fromHtml(context.getString(R.string.max_select, String.valueOf(itemMax))));
        List<Good> goods;
        if (mShopCart.comboGoods != null && (goods = mShopCart.comboGoods.get(mComboId)) != null && goods.size() > 0) {
            for (Good good1 : goods) {
                if (good.GoodsID.equals(good1.GoodsID)) {
                    good.count = good1.count;
                    break;
                }
            }
        }
        holder.tvCount.setText(String.valueOf(good.count));
        if (mGiftsChooseListener != null) {
            mGiftsChooseListener.onSelectAllGifts(getSelectCount() >= getMaxCount());
        }
        holder.ivSub.setOnClickListener(v -> {
            int count = mData.get(position).count;
            if (count > 0) {
                mData.get(position).count--;
                holder.tvCount.setText(String.valueOf(mData.get(position).count));
                if (mGiftsChooseListener != null) {
                    mGiftsChooseListener.onSelectListChanged(getSelGoods(), getMaxCount());
                }
            }
        });

        holder.ivPlus.setOnClickListener(v -> {
            int max = (mMultiplyCount ? mData.get(position).Number * mShopCart.PointNum : mData.get(position).Number);
            if (mData.get(position).count >= max) {//单品已到最大值
                if (mGiftsChooseListener != null) {
                    mGiftsChooseListener.onMaxCount(mData.get(position).count >= getMaxCount() || getSelectCount() >= getMaxCount() ?//单个大于等于总量时提示总数达最大值
                            context.getString(R.string.gift_count_max_tip, String.valueOf(getMaxCount())) : context.getString(R.string.gift_max_select, good.Name, String.valueOf(max)));
                }
            } else if (getSelectCount() >= getMaxCount()) {//整个套餐已达最大值
                if (mGiftsChooseListener != null) {
                    mGiftsChooseListener.onMaxCount(context.getString(R.string.gift_count_max_tip, String.valueOf(getMaxCount())));
                }
            } else {
                mData.get(position).count++;
                holder.tvCount.setText(String.valueOf(mData.get(position).count));
                if (mGiftsChooseListener != null) {
                    mGiftsChooseListener.onSelectListChanged(getSelGoods(), getMaxCount());
                }
            }
            if (mGiftsChooseListener != null) {
                mGiftsChooseListener.onSelectAllGifts(getSelectCount() >= getMaxCount());
            }
        });
    }

    private List<Good> getSelGoods() {
        List<Good> goods = getData();
        List<Good> selGoods = new ArrayList<>();
        for (Good good : goods) {
            if (good.count > 0) {
                selGoods.add(good);
            }
        }
        return selGoods;
    }

//    private boolean checkMaxCount() {
//        return getSelectCount() < getMaxCount();
//    }

    private int getMaxCount() {
        int maxCount = mTotalCount;
        if (VodConfigData.getInstance().isChangeGiftTotalCount()) {//王牌ERP:根据商品选择情况改变最大可选数量
            for (Good good : mData) {
                if (good.count > 0 && good.Number > 0 && good.Number < maxCount) {
                    //计算最大值，1同一组中包含买1送6和买1送4两种；只要选择了买1送4，赠送总量最多能选4个；
                    maxCount = good.Number;
                }
            }
        }
        return maxCount;
    }

    private int getSelectCount() {
        int count = 0;
        for (Good good : mData) {
            count = count + good.count;
        }
        return count;
    }


    public void setGiftsChooseListener(GiftsChooseListener listener) {
        this.mGiftsChooseListener = listener;
    }

    public interface GiftsChooseListener {
        void onMaxCount(String tip);

        void onSelectListChanged(List<Good> selectedGoods, int maxCount);

        void onSelectAllGifts(boolean isSelectAll);
    }
}
