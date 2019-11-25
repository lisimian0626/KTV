package com.beidousat.karaoke.widget.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.service.GiftChooseDialog;
import com.beidousat.karaoke.ui.dialog.service.GoodCountDialog;
import com.bestarmedia.libcommon.data.ComboAttrsHelper;
import com.bestarmedia.libcommon.data.ShopCartData;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtShop extends RecyclerView.Adapter<AdtShop.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Good> mData = new ArrayList();
    private ShopCartData mShopCartData;
    private String mCateId;
    private Activity activity;

    public AdtShop(Context context) {
        mShopCartData = new ShopCartData();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        activity = (Activity) context;
    }


    public void setData(String cateId, List<Good> data) {
        mCateId = cateId;
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvUnit, tvPrice, tvGift, tvOrderCount;
        private View tagShopCart;
        private RecyclerImageView ivCover;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_shop, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivCover = view.findViewById(R.id.riv_cover);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvOrderCount = view.findViewById(R.id.tv_count);
        viewHolder.tvUnit = view.findViewById(R.id.tv_unit);
        viewHolder.tvPrice = view.findViewById(R.id.tv_price);
        viewHolder.tvGift = view.findViewById(R.id.tv_gift);
        viewHolder.tagShopCart = view.findViewById(R.id.iv_shop);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Good good = mData.get(position);
        holder.ivCover.setVisibility(!TextUtils.isEmpty(good.Pic) ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(good.Pic)) {
            Glide.with(mContext).load(good.Pic).override(80, 80).centerCrop().skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.ic_good_default)
                    .error(R.drawable.ic_good_default).into(holder.ivCover);
        }

        holder.tvName.setText(good.Name);
        holder.tvUnit.setText("/" + good.Unit);
        holder.tvPrice.setText(mContext.getString(R.string.rmb_x, good.Price));
        final ShopCart contain = mShopCartData.getGoodById(good.GoodsID);
        if (contain != null) {
            holder.tvGift.setEnabled(contain.PointNum > 0);
            holder.tagShopCart.setVisibility(contain.PointNum > 0 ? View.VISIBLE : View.GONE);
            holder.tvOrderCount.setBackgroundResource(R.drawable.selector_green_btn);
            holder.tvOrderCount.setText(String.valueOf(contain.PointNum));
            holder.tvOrderCount.setTextColor(Color.WHITE);
        } else {
            holder.tvGift.setEnabled(false);
            holder.tagShopCart.setVisibility(View.GONE);
            holder.tvOrderCount.setBackgroundResource(R.drawable.selector_yellow_btn);
            holder.tvOrderCount.setText(R.string.order_count);
            holder.tvOrderCount.setTextColor(Color.parseColor("#8B4523"));
        }

        holder.tvGift.setVisibility("Y".equals(good.IsCombo) && contain != null && contain.PointNum > 0 ? View.VISIBLE : View.GONE);
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, "Y".equals(good.IsCombo) ? R.drawable.tag_handsel : 0, 0);

        holder.tvGift.setOnClickListener(v -> {

            GiftChooseDialog dialog = new GiftChooseDialog(contain, activity);
            dialog.setOnGiftChooseListener(goods -> {
                contain.comboGoods = goods;
                notifyItemChanged(position);
            });
            dialog.show();
        });

        holder.tvOrderCount.setOnClickListener(v -> {
            GoodCountDialog dialog = new GoodCountDialog(activity);
            dialog.setOnGoodCountListener(count -> {
                mShopCartData.addGood(good.toShopCart(mCateId, count, 0));
                notifyItemChanged(position);

                if ("Y".equals(good.IsCombo) && mShopCartData.getGoodById(good.GoodsID).PointNum > 0) {
                    ComboAttrsHelper comboAttrsHelper = new ComboAttrsHelper(mContext);
                    comboAttrsHelper.setOnComboListener((comboAttrs, goods) -> {
                        if (comboAttrs != null && comboAttrs.size() > 0 && goods != null && goods.size() > 0) {
                            GiftChooseDialog dialog1 = new GiftChooseDialog(mShopCartData.getGoodById(good.GoodsID), activity);
//                                        dialog.setComboAttrsGoodData(comboAttrs, goods);
                            dialog1.setOnGiftChooseListener(goods1 -> {
                                mShopCartData.getGoodById(good.GoodsID).comboGoods = goods1;
                                notifyItemChanged(position);
                            });
                            dialog1.show();
                        }
                    });
                    comboAttrsHelper.loadCombos(good.GoodsID, mShopCartData.getGoodById(good.GoodsID).PointNum);
                }
            });
            dialog.show();
        });
    }

}
