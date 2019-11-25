package com.beidousat.karaoke.ui.fragment.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.DlgAccountPassword;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.dialog.setting.DlgMngPwd;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.ui.fragment.setting.FmSetting;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.BannerPlayer;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;


/**
 * Created by J Wong on 2015/12/16 16:13.
 */
public class FmService extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private BannerPlayer mBannerPlayer;
    private DlgMngPwd mMngPwdDialog;
    private PromptDialogSmall mPromptDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_service, null);
        mRootView.findViewById(R.id.wm_bill).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_sale).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_fire).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_wine).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_give).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_pr).setOnClickListener(this);
        mRootView.findViewById(R.id.wm_setting).setOnClickListener(this);
        mBannerPlayer = mRootView.findViewById(R.id.ad_banner);
        mBannerPlayer.loadAds("B1");
        mBannerPlayer.requestAds();
        SkinManager.getInstance().register(mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
    }

    private void tipMessage(int resId) {
        try {
            if (mPromptDialog == null || !mPromptDialog.isShowing()) {
                mPromptDialog = new PromptDialogSmall(getContext());
                mPromptDialog.setMessage(getString(resId));
                mPromptDialog.show();
            }
        } catch (Exception e) {
            Logger.d(getClass().getSimpleName(), "tipMessage ex:" + e.toString());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mBannerPlayer != null)
            if (hidden) {
                //do when hidden
                mBannerPlayer.stopPlayer();
            } else {
                //do when show
                mBannerPlayer.startPlayer();
            }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBannerPlayer.startPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBannerPlayer.stopPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wm_bill:
                if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                    FragmentUtil.addFragment(new FmBill(), false, false, false, false);
                } else {
                    tipMessage(R.string.is_purchase_not_enabled);
                }
                break;
            case R.id.wm_sale:
                FragmentUtil.addFragment(FmPromotionDetail.newInstance(), false, false, true, false);
                break;
            case R.id.wm_fire:
                FragmentUtil.addFragment(FmFireDiagram.newInstance(), false, false, true, false);
                break;
            case R.id.wm_wine:
                if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                    if (VodConfigData.getInstance().verifyPasswordEnterWine()) {
                        DlgAccountPassword orderSubmit = new DlgAccountPassword(getContext(), 0);
                        orderSubmit.setOnAccountPwdListener((account, pwd, isVerifyPwd) ->
                                FragmentUtil.addFragment(FmShop.newInstance(account, pwd, isVerifyPwd), false, false, false, false));
                        orderSubmit.show();
                    } else {
                        FragmentUtil.addFragment(FmShop.newInstance(null, null, true), false, false, false, false);
                    }
                } else {
                    tipMessage(R.string.is_purchase_not_enabled);
                }
                break;
            case R.id.wm_give:
                if (VodConfigData.getInstance().hasGift()) {
                    if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                        DlgAccountPassword dlgAccountPassword = new DlgAccountPassword(getActivity(), 1);
                        dlgAccountPassword.setOnAccountPwdListener((account, pwd, isVerifyPwd) ->
                                FragmentUtil.addFragment(FmGifts.newInstance(account, pwd), false, false, false, false));
                        dlgAccountPassword.show();
                    } else {
                        tipMessage(R.string.is_purchase_not_enabled);
                    }
                } else {//无赠送模块
                    tipMessage(R.string.erp_no_gift_tip);
                }
                break;
            case R.id.wm_pr://公关
                if (VodConfigData.getInstance().haveSlotCard()) {
                    if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                        FragmentUtil.addFragment(new FmRelationsCard(), false, false, false, false);
                    } else {
                        tipMessage(R.string.is_purchase_not_enabled);
                    }
                } else {
                    tipMessage(R.string.slot_card_not_enabled);
                }
                break;
            case R.id.wm_setting://设置
                if (mMngPwdDialog == null || !mMngPwdDialog.isShowing()) {
                    mMngPwdDialog = new DlgMngPwd(getContext());
                    mMngPwdDialog.setOnMngPwdListener(() -> FragmentUtil.addFragment(new FmSetting(), false, true, true, true));
                    mMngPwdDialog.show();
                }
                break;
        }
    }
}
