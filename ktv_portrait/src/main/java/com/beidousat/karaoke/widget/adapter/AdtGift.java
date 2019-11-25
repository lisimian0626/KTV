package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.service.GiftChooseDialog;
import com.beidousat.karaoke.ui.dialog.service.GoodCountDialog;
import com.bestarmedia.libcommon.data.ComboAttrsHelper;
import com.bestarmedia.libcommon.data.GiftData;
import com.bestarmedia.libcommon.model.erp.ComboAttr;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtGift extends RecyclerView.Adapter<AdtGift.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Good> mData = new ArrayList<Good>();
    private GiftData mGiftData;
    private String mCateId;

    public AdtGift(Context context) {
        mGiftData = new GiftData();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(String cateId, List<Good> data) {
        mCateId = cateId;
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvUnit, tvPrice, tvGift, tvOrderCount;
        private ImageView tagShopCart;
        private RecyclerImageView ivCover;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_shop, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ivCover = (RecyclerImageView) view.findViewById(R.id.riv_cover);
        viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tvOrderCount = (TextView) view.findViewById(R.id.tv_count);
        viewHolder.tvUnit = (TextView) view.findViewById(R.id.tv_unit);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.tvGift = (TextView) view.findViewById(R.id.tv_gift);
        viewHolder.tagShopCart = (ImageView) view.findViewById(R.id.iv_shop);
        viewHolder.tagShopCart.setImageResource(R.drawable.service_good_in_handsel_tag);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        final ShopCart contain = mGiftData.getGoodById(good.GoodsID);
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
//        holder.tvGift.setVisibility("Y".equals(good.IsCombo) ? View.VISIBLE : View.GONE);
        holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, "Y".equals(good.IsCombo) ? R.drawable.tag_handsel : 0, 0);

        holder.tvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftChooseDialog dialog = new GiftChooseDialog(contain, mContext);
                dialog.setOnGiftChooseListener(new GiftChooseDialog.OnGiftChooseListener() {
                    @Override
                    public void onGiftChoose(Map<String, List<Good>> goods) {
                        contain.comboGoods = goods;
                        notifyItemChanged(position);
                    }
                });
                dialog.show();
            }
        });

        holder.tvOrderCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodCountDialog dialog = new GoodCountDialog(mContext);
                dialog.setOnGoodCountListener(new GoodCountDialog.OnGoodCountListener() {
                    @Override
                    public void onGoodCount(int count) {
                        mGiftData.addGood(good.toShopCart(mCateId, count, 1));
                        notifyItemChanged(position);

                        if ("Y".equals(good.IsCombo) && mGiftData.getGoodById(good.GoodsID).PointNum > 0) {
                            ComboAttrsHelper comboAttrsHelper = new ComboAttrsHelper(mContext);
                            comboAttrsHelper.setOnComboListener(new ComboAttrsHelper.OnComboListener() {
                                @Override
                                public void onComboAttrsGoods(List<ComboAttr> comboAttrs, List<Good> goods) {
                                    if (comboAttrs != null && comboAttrs.size() > 0 && goods != null && goods.size() > 0) {
                                        GiftChooseDialog dialog = new GiftChooseDialog(mGiftData.getGoodById(good.GoodsID), mContext);
//                                        dialog.setComboAttrsGoodData(comboAttrs, goods);
                                        dialog.setOnGiftChooseListener(new GiftChooseDialog.OnGiftChooseListener() {
                                            @Override
                                            public void onGiftChoose(Map<String, List<Good>> goods) {
                                                mGiftData.getGoodById(good.GoodsID).comboGoods = goods;
                                                notifyItemChanged(position);
                                            }
                                        });
                                        dialog.show();
                                    }
                                }
                            });
                            comboAttrsHelper.loadCombos(good.GoodsID, mGiftData.getGoodById(good.GoodsID).PointNum);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

}
