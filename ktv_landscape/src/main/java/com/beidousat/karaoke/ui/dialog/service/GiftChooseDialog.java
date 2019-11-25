package com.beidousat.karaoke.ui.dialog.service;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.widget.adapter.AdtGiftChoose;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.erp.ComboAttr;
import com.bestarmedia.libcommon.model.erp.ComboAttrsV4;
import com.bestarmedia.libcommon.model.erp.ComboItemsV4;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GiftChooseDialog extends BaseDialog implements View.OnClickListener, AdtGiftChoose.GiftsChooseListener, HttpRequestListener {

    private TextView mTvTitle;
    private RecyclerView mRecyclerView, mRvCombo;
    private TextView mBtnCancel, mBtnOk;
    private ShopCart mShopCart;
    private Map<String, List<Good>> comboGoods_default;
    private AdtGiftChoose mAdapter;
    private OnGiftChooseListener mOnGiftChooseListener;
    private ComboAttr mComboAttr;
    private Context mContext;
    private List<ComboAttr> mComboAttrs;

    public GiftChooseDialog(ShopCart shopCart, Context context) {
        super(context, R.style.MyDialog);
        mContext = context;
        mShopCart = shopCart;
        init();
    }

    private void saveDefault() {
        comboGoods_default = new HashMap<>();
        for (Map.Entry<String, List<Good>> entry : mShopCart.comboGoods.entrySet()) {
            comboGoods_default.put(entry.getKey(), entry.getValue());
        }
    }

    private void init() {
        this.setContentView(R.layout.dlg_gift_choose);
        if (getWindow() == null)
            return;
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 600;
        lp.height = 600;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        mTvTitle = findViewById(android.R.id.title);
        mRecyclerView = findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        if (mRecyclerView.getItemAnimator() != null) {
            ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mRvCombo = findViewById(R.id.rv_combo);
        mBtnCancel = findViewById(android.R.id.button1);
        mBtnCancel.setOnClickListener(this);
        mBtnOk = findViewById(android.R.id.button2);
        mBtnOk.setOnClickListener(this);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCombo.setLayoutManager(linearLayoutManager);
        saveDefault();
        requestAttr(mShopCart.PointNum);

    }

    public Dialog setTitle(String title) {
        mTvTitle.setText(Html.fromHtml(title));
        return this;
    }

    @Override
    public void dismiss() {
        setHaveGiftNoSelect();
        super.dismiss();
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_PURCHASE_COMBO.equalsIgnoreCase(method)) {
            if (object instanceof ComboAttrsV4) {
                ComboAttrsV4 comboAttrsV4 = (ComboAttrsV4) object;
                if (comboAttrsV4.combo != null && comboAttrsV4.combo.data != null && comboAttrsV4.combo.data.size() > 0) {
                    mComboAttrs = comboAttrsV4.combo.data;
                    setComboAttrs(mComboAttrs);
                    requestGoods(mComboAttrs.get(0));
                }
            }
        } else if (RequestMethod.NODE_PURCHASE_COMBO_ITEM.equalsIgnoreCase(method)) {
            if (object instanceof ComboItemsV4) {
                ComboItemsV4 comboItemsV4 = (ComboItemsV4) object;
                if (comboItemsV4.combo_item != null && comboItemsV4.combo_item.data != null && comboItemsV4.combo_item.data.size() > 0) {
                    //设置最大可选个数
                    int count = 0;
                    try {
                        if (mComboAttr.Number > 0) {
                            count = mComboAttr.Number;
                        } else {
                            count = (comboItemsV4.combo_item.data.get(0).Number);
                            if (VodConfigData.getInstance().isTotalGiftMultiplyCount()) {
                                count = mShopCart.PointNum * count;
                            }
                        }
                        setTitle(getContext().getString(R.string.gift_choose_x, String.valueOf(count)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setGoods(count, comboItemsV4.combo_item.data, VodConfigData.getInstance().isTotalGiftMultiplyCount());
                } else {
                    if (mComboAttr != null) {
                        mComboAttr.isSelectAllGifts = true;
                    }
                    List<Good> goods1 = new ArrayList<>();
                    setGoods(0, goods1, false);
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_PURCHASE_COMBO.equalsIgnoreCase(method)) {
            String error = "";
            if (obj instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) obj;
                error = baseModelV4.tips;
            } else if (obj instanceof String) {
                error = obj.toString();
            }
            if (!TextUtils.isEmpty(error)) {
                PromptDialogSmall dialog = new PromptDialogSmall(mContext);
                dialog.setMessage(error);
                dialog.show();
            }
        } else if (RequestMethod.NODE_PURCHASE_COMBO_ITEM.equalsIgnoreCase(method)) {
            if (mComboAttr != null)
                mComboAttr.isSelectAllGifts = true;
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    private void requestAttr(int quantity) {
        HttpRequestV4 request = initRequest(RequestMethod.NODE_PURCHASE_COMBO);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("goods_id", mShopCart.GoodsID);
        request.addParam("quantity", String.valueOf(quantity));
        request.setConvert2Class(ComboAttrsV4.class);
        request.get();
    }


    private void requestGoods(ComboAttr attr) {
        attr.isClicked = true;
        mComboAttr = attr;
        HttpRequestV4 request = initRequest(RequestMethod.NODE_PURCHASE_COMBO_ITEM);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("combo_id", attr.ComboID);
        request.setConvert2Class(ComboItemsV4.class);
        request.get();
    }


    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }


    public void setOnGiftChooseListener(OnGiftChooseListener listener) {
        this.mOnGiftChooseListener = listener;
    }

    /**
     * @param totalMaxCount 套餐可选总量
     * @param goods         商品列表
     * @param multiplyCount 个数是否需要乘以购物车数量，零度、维达 不需要，其他需要
     */
    private void setGoods(int totalMaxCount, List<Good> goods, boolean multiplyCount) {
        mAdapter = new AdtGiftChoose(getContext(), mComboAttr.ComboID, mShopCart);
        mAdapter.setGiftsChooseListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(totalMaxCount, goods, multiplyCount);
    }

    private void setComboAttrs(List<ComboAttr> comboAttrs) {
        AdapterComboAttr adapter = new AdapterComboAttr();
        mRvCombo.setAdapter(adapter);
        adapter.setData(comboAttrs);
    }

    private void setHaveGiftNoSelect() {
        boolean isALLClicked = true;
        if (mComboAttrs != null && mComboAttrs.size() > 0)
            for (ComboAttr comboAttr : mComboAttrs) {
                if (!comboAttr.isClicked) {//例送分类是否点击过，每点击过说明没有选商品
                    isALLClicked = false;
                }
                if (!comboAttr.isSelectAllGifts) {//例送是否选择子商品到最大值
                    isALLClicked = false;
                }
            }
        mShopCart.haveGiftsNoSelect = !isALLClicked;
    }

    private void setShopCartGift() {
        if (mShopCart.comboGoods != null && mShopCart.comboGoods.size() > 0) {
            Iterator iter = mShopCart.comboGoods.entrySet().iterator();
            List<Good> selGoods = new ArrayList<>();
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, List<Good>> entry = (Map.Entry) iter.next();
                List<Good> goods = entry.getValue();
                if (goods != null && goods.size() > 0)
                    selGoods.addAll(goods);
            }
            if (mOnGiftChooseListener != null)
                mOnGiftChooseListener.onGiftChoose(mShopCart.comboGoods);
        }
    }

    private void setShopCartGift(Map<String, List<Good>> goods) {
        if (mOnGiftChooseListener != null)
            mOnGiftChooseListener.onGiftChoose(goods);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                setShopCartGift(comboGoods_default);
                dismiss();
                break;
            case android.R.id.button2:
                setHaveGiftNoSelect();
                if (mShopCart.haveGiftsNoSelect) {//有未选择的例送商品
                    PromptDialogBig promptDialog = new PromptDialogBig(mContext);
                    promptDialog.setMessage(R.string.have_gift_no_select_close_tip);
                    promptDialog.setOkButton(true, mContext.getString(android.R.string.yes), v12 -> {
                        setShopCartGift();
                        dismiss();
                    });
                    promptDialog.setCancleButton(true, mContext.getString(android.R.string.no), null);
                    promptDialog.show();
                } else {
                    setShopCartGift();
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onSelectAllGifts(boolean isSelectAll) {
        if (mComboAttr != null) {
            mComboAttr.isSelectAllGifts = isSelectAll;
        }
    }

    @Override
    public void onMaxCount(String tip) {
        PromptDialogSmall dialog = new PromptDialogSmall(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(tip);
        dialog.show();
    }

    @Override
    public void onSelectListChanged(List<Good> selectedGoods, int maxCount) {
        if (mShopCart.comboGoods == null) {
            mShopCart.comboGoods = new HashMap<>();
        }
        mShopCart.comboGoods.put(mComboAttr.ComboID, selectedGoods);
        setTitle(getContext().getString(R.string.gift_choose_x, String.valueOf(maxCount)));
    }

    public interface OnGiftChooseListener {
        void onGiftChoose(Map<String, List<Good>> goods);
    }


    public class AdapterComboAttr extends RecyclerView.Adapter<AdapterComboAttr.ViewHolder> {

        private LayoutInflater mInflater;
        private List<ComboAttr> mData = new ArrayList<>();
        private int mFocusPs;

        private AdapterComboAttr() {
            mInflater = LayoutInflater.from(getContext());
        }

        private void setData(List<ComboAttr> data) {
            this.mData = data;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvKey;

            private ViewHolder(View view) {
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
            View view = mInflater.inflate(R.layout.list_item_combo_attr, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvKey = view.findViewById(R.id.tv_name);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            final ComboAttr comboAttr = mData.get(position);
            holder.tvKey.setText(comboAttr.Name);
            holder.tvKey.setSelected(mFocusPs == position);
            holder.tvKey.setOnClickListener(v -> {
                if (mComboAttr != null && !mComboAttr.isSelectAllGifts) {
                    PromptDialogBig promptDialog = new PromptDialogBig(mContext);
                    promptDialog.setMessage(R.string.have_gift_no_select_tip);
                    promptDialog.setOkButton(true, mContext.getString(android.R.string.yes), v1 -> {
                        int preF = mFocusPs;
                        mFocusPs = position;
                        notifyItemChanged(position);
                        notifyItemChanged(preF);
                        requestGoods(comboAttr);
                    });
                    promptDialog.setCancleButton(true, mContext.getString(android.R.string.no), null);
                    promptDialog.show();
                } else {
                    int preF = mFocusPs;
                    mFocusPs = position;
                    notifyItemChanged(position);
                    notifyItemChanged(preF);
                    requestGoods(comboAttr);
                }
            });
        }
    }
}
