package com.beidousat.karaoke.ui.fragment.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.ui.dialog.DlgClockFail;
import com.beidousat.karaoke.ui.dialog.DlgClockSuccess;
import com.beidousat.karaoke.ui.dialog.DlgSlotCard;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.adapter.AdtRelationsCard;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.ListRelationsCard;
import com.bestarmedia.libcommon.model.erp.RelationsCard;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2018/6/26.
 */

public class FmRelationsCard extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private AdtRelationsCard mAdapter;
    private PromptDialogSmall mPromptDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_relations_card, null);
        mRootView.findViewById(R.id.riv_pr_on).setOnClickListener(this);
        mRootView.findViewById(R.id.riv_pr_off).setOnClickListener(this);
        mRootView.findViewById(R.id.riv_pr_cancel).setOnClickListener(this);

        mRecyclerView = mRootView.findViewById(R.id.rv_persons);
        mRecyclerView.setHasFixedSize(true);
//        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager3);

        mAdapter = new AdtRelationsCard(VodApplication.getVodApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        requestCardPerson();

        return mRootView;
    }

    private void showDlgCard(int type) {
        if (VodConfigData.getInstance().getServerConfig() != null && VodConfigData.getInstance().haveSlotCard()) {
            if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                DlgSlotCard dlgCard = new DlgSlotCard(getContext(), type);
                dlgCard.setOnClockListener(new DlgSlotCard.OnClockListener() {
                    @Override
                    public void onClockSuccess(String title, String info) {
                        DlgClockSuccess dlgClockSuccess = new DlgClockSuccess(getActivity());
                        dlgClockSuccess.setTitle(title);
                        dlgClockSuccess.setTitleIcon(R.drawable.dlg_success_ic);
                        dlgClockSuccess.setAutoDismiss(true);
                        dlgClockSuccess.setMessage(info);
                        dlgClockSuccess.show();

                        dlgCard.dismiss();
                        requestCardPerson();
                    }

                    @Override
                    public void onClockFail(String title, String err) {
                        DlgClockFail dlgClockFail = new DlgClockFail(getContext());
                        dlgClockFail.setTitle(title);
                        dlgClockFail.setTitleIcon(R.drawable.dlg_fail_ic);
                        dlgClockFail.setMessage(err);
                        dlgClockFail.setAutoDismiss(true);
                        dlgClockFail.show();
                    }
                });
                dlgCard.show();
            } else {
                tipMessage(R.string.is_purchase_not_enabled);
            }
        } else {
            tipMessage(R.string.slot_card_not_enabled);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_pr_on:
                showDlgCard(1);
                break;
            case R.id.riv_pr_off:
                showDlgCard(2);
                break;
            case R.id.riv_pr_cancel:
                showDlgCard(3);
                break;
        }
    }

    public void tipMessage(int resId) {
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
    public void onSuccess(String method, Object object) {
        super.onSuccess(method, object);
        if (isAdded()) {
            if (RequestMethod.NODE_ERP_ROOM_CHECK_LIST.equalsIgnoreCase(method)) {
                ListRelationsCard listRelationsCard;
                if (object instanceof ListRelationsCard && (listRelationsCard = (ListRelationsCard) object).checklist != null
                        && listRelationsCard.checklist.data != null && listRelationsCard.checklist.data.size() > 0) {
                    mAdapter.setData(listRelationsCard.checklist.data);
                    mAdapter.notifyDataSetChanged();
                } else {
                    List<RelationsCard> list = new ArrayList<>();
                    mAdapter.setData(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void requestCardPerson() {
        HttpRequestV4 r = initRequestV4(RequestMethod.NODE_ERP_ROOM_CHECK_LIST);
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.setConvert2Class(ListRelationsCard.class);
        r.get();
    }
}
