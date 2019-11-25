package com.beidousat.karaoke.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.RemarksDialog;
import com.beidousat.karaoke.ui.dialog.service.GiftChooseDialog;
import com.beidousat.karaoke.ui.dialog.service.GoodCountDialog;
import com.bestarmedia.libcommon.data.ComboAttrsHelper;
import com.bestarmedia.libcommon.data.ShopCartData;
import com.bestarmedia.libcommon.model.erp.ComboAttr;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2016/5/3.
 */
public class AdtShopCart extends RecyclerView.Adapter<AdtShopCart.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ShopCart> mData = new ArrayList<>();
    private ShopCartData mShopCartData;

    public AdtShopCart(Context context) {
        mShopCartData = new ShopCartData();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<ShopCart> data) {
        this.mData = data;
        callbackPriceChanged();
    }

    public List<ShopCart> getData() {
        return mData;
    }

    public void removeDataByGoodId(String goodID) {
        try {
            mShopCartData.removeGood(goodID);
            for (int i = mData.size() - 1; i >= 0; i--) {
                ShopCart cart = mData.get(i);
                if (cart.GoodsID.equalsIgnoreCase(goodID)) {
                    mData.remove(i);
                }
            }
            notifyDataSetChanged();
            callbackPriceChanged();
        } catch (Exception e) {
            Logger.w(getClass().getSimpleName(), "remove Good ex:" + e.toString());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvCount, tvUnit, tvPrice, tvRemarks, tvGifts, tvGifts1, tvChooseGift;
        private CheckBox cbSelect;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_shopcart, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvName = view.findViewById(R.id.tv_name);
        viewHolder.tvCount = view.findViewById(R.id.tv_count);
        viewHolder.tvUnit = view.findViewById(R.id.tv_unit);
        viewHolder.tvPrice = view.findViewById(R.id.tv_price);
        viewHolder.tvRemarks = view.findViewById(R.id.tv_remarks);
        viewHolder.tvChooseGift = view.findViewById(R.id.tv_choose_gift);
        viewHolder.tvGifts = view.findViewById(R.id.tv_gifts);
        viewHolder.tvGifts1 = view.findViewById(R.id.tv_gift1);
        viewHolder.cbSelect = view.findViewById(R.id.cb_select);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ShopCart good = mData.get(position);
        holder.tvName.setText(good.Name);
        holder.tvUnit.setText(good.Unit);
        holder.tvPrice.setText(mContext.getString(R.string.rmb_x, good.PointNum * good.Price));
        holder.tvCount.setText(String.valueOf(good.PointNum));
        holder.tvChooseGift.setEnabled(good.PointNum > 0);
        holder.tvChooseGift.setVisibility("Y".equals(good.IsCombo) ? View.VISIBLE : View.INVISIBLE);
        holder.cbSelect.setChecked(good.isSelect);
        if (good.comboGoods != null && good.comboGoods.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, List<Good>> stringListEntry : good.comboGoods.entrySet()) {
                List<Good> goods = stringListEntry.getValue();
                if (goods != null && goods.size() > 0) {
                    for (Good gift : goods) {
                        if (gift.count > 0)
                            builder.append(gift.Name).append(" x").append(gift.count).append("\n");
                    }
                }
            }
            holder.tvGifts.setText(builder.toString());
            holder.tvGifts.setVisibility(View.VISIBLE);
            holder.tvGifts1.setVisibility(View.VISIBLE);
        } else {
            holder.tvGifts.setText("");
            holder.tvGifts.setVisibility(View.GONE);
            holder.tvGifts1.setVisibility(View.GONE);
        }

        holder.tvRemarks.setText(good.Taste == null ? mContext.getString(R.string.remarks) : good.Taste);

        holder.tvRemarks.setOnClickListener(v -> {
            if (mRemarksDialog == null || !mRemarksDialog.isShowing()) {
                mRemarksDialog = new RemarksDialog(VodApplication.getVodApplication().getStackTopActivity());
                mRemarksDialog.setTitle(mContext.getString(R.string.remarks));
                mRemarksDialog.setOnRemarkChooseListener(taste -> {
                    mShopCartData.getGoodList().get(position).Taste = taste.Name;
                    notifyItemChanged(position);
                });
                mRemarksDialog.show();
            }
        });

        holder.tvChooseGift.setOnClickListener(v -> {
            if (mGiftChooseDialog == null || !mGiftChooseDialog.isShowing()) {
                mGiftChooseDialog = new GiftChooseDialog(good, mContext);
                mGiftChooseDialog.setOnGiftChooseListener(goods -> {
                    good.comboGoods = goods;
                    notifyItemChanged(position);
                });
                mGiftChooseDialog.show();
            }
        });
        holder.tvCount.setOnClickListener(v -> {
            if (mGoodCountDialog == null || !mGoodCountDialog.isShowing()) {
                mGoodCountDialog = new GoodCountDialog(mContext);
                mGoodCountDialog.setOnGoodCountListener(count -> {
                    good.PointNum = count;
                    if (good.comboGoods != null)
                        good.comboGoods.clear();
                    mShopCartData.addGood(good);
                    notifyItemChanged(position);
                    callbackPriceChanged();
                    if ("Y".equals(good.IsCombo) && mShopCartData.getGoodById(good.GoodsID).PointNum > 0) {//弹出赠品选择对话框
                        ComboAttrsHelper comboAttrsHelper = new ComboAttrsHelper(mContext);
                        comboAttrsHelper.setOnComboListener((List<ComboAttr> comboAttrs, List<Good> goods) -> {
                            if (comboAttrs != null && comboAttrs.size() > 0 && goods != null && goods.size() > 0) {
                                GiftChooseDialog dialog = new GiftChooseDialog(mShopCartData.getGoodById(good.GoodsID), mContext);
//                                        dialog.setComboAttrsGoodData(comboAttrs, goods);
                                dialog.setOnGiftChooseListener(goods1 -> {
                                    mShopCartData.getGoodById(good.GoodsID).comboGoods = goods1;
                                    notifyItemChanged(position);
                                });
                                dialog.show();
                            }
                        });
                        comboAttrsHelper.loadCombos(good.GoodsID, mShopCartData.getGoodById(good.GoodsID).PointNum);
                    }

                });
                mGoodCountDialog.show();
            }

        });
        holder.cbSelect.setOnClickListener(v -> {
            good.isSelect = ((CheckBox) v).isChecked();
            mShopCartData.addGood(good);
            notifyItemChanged(position);
            callbackPriceChanged();
        });
    }

    private GoodCountDialog mGoodCountDialog;
    private GiftChooseDialog mGiftChooseDialog;
    private RemarksDialog mRemarksDialog;

    private void callbackPriceChanged() {
        double totalPrice = 0;
        for (ShopCart good : mData) {
            if (good.isSelect) {
                totalPrice = totalPrice + good.PointNum * good.Price;
            }
        }
        if (mOnShopCartPriceChangedListener != null) {
            mOnShopCartPriceChangedListener.onPriceChanged(totalPrice);
        }

    }

    private OnShopCartPriceChangedListener mOnShopCartPriceChangedListener;

    public void setOnShopCartPriceChangedListener(OnShopCartPriceChangedListener l) {
        mOnShopCartPriceChangedListener = l;
    }

    public interface OnShopCartPriceChangedListener {
        void onPriceChanged(double totalPrice);

        void onGiftClickListener(int position, List<Good> goods, int max);

        void onRemarkClickListener(int position, String[] remarks);
    }
}
