package com.beidousat.karaoke.ui.fragment.play;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.data.RoomUsers;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.adapter.AdtUserAvatar;
import com.beidousat.karaoke.ui.dialog.DlgRoomQrCode;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetMvPager;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.model.vod.MvSongsV4;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmMv extends BaseFragment implements OnPageScrollListener, AdtUserAvatar.OnAvatarClickListener, WidgetPage.OnPageChangedListener, View.OnClickListener {

    private View mRootView;
    private WidgetMvPager mSongPager;
    private RecyclerView mRvAvatar;
    private WidgetPage mWidgetPage;
    private ProgressBar mPgbQrCode;
    private AdtUserAvatar mAdtUserAvatar;
    private RoomUsers mRoomUsers;
    private View llQrcode;
    private TextView tvQrMsg;
    private ImageView mIvQrCode;

    public static FmMv newInstance() {
        return new FmMv();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_mv, null);
        mRoomUsers = RoomUsers.getInstance();
        llQrcode = mRootView.findViewById(R.id.ll_qrode);
        mIvQrCode = mRootView.findViewById(R.id.iv_qrcode);
        mIvQrCode.setOnClickListener(this);
        mPgbQrCode = mRootView.findViewById(R.id.pgb_qrcode);
        tvQrMsg = mRootView.findViewById(R.id.tv_qr_msg);
        mRootView.findViewById(R.id.riv_close).setVisibility(View.GONE);
        mRootView.findViewById(R.id.riv_down_app).setVisibility(View.GONE);

        mRootView.findViewById(R.id.btn_disconnect).setOnClickListener(this);
        mSongPager = mRootView.findViewById(R.id.songPager);

        mRvAvatar = mRootView.findViewById(R.id.rv_avatar);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mSongPager.setOnPagerScrollListener(this);

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8, 8).build();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvAvatar.setLayoutManager(layoutManager2);
        mRvAvatar.addItemDecoration(verDivider);

        mAdtUserAvatar = new AdtUserAvatar(getActivity());
        mAdtUserAvatar.setOnAvatarClickListener(this);

        mRvAvatar.setAdapter(mAdtUserAvatar);

        initUserMv();

        EventBus.getDefault().register(this);


        return mRootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qrcode:
                requestQrCode();
                break;
            case R.id.btn_disconnect:
                PromptDialogBig promptDialog = new PromptDialogBig(getContext());
                promptDialog.setMessage(R.string.disconnect_phone_tip);
                promptDialog.setOkButton(true, getString(R.string.disconnect), view -> {
                    VodApplication.getKaraokeController().resetBoxUUID(true);
                    mIvQrCode.setImageBitmap(null);
                    if (DeviceHelper.isMainVod(VodApplication.getVodApplicationContext())) {
                        requestQrCode();
                    } else {
                        mIvQrCode.postDelayed(() -> requestQrCode(), 5000);
                    }
                });
                promptDialog.setCancleButton(true, getString(R.string.cancel), null);
                promptDialog.show();
                break;
        }
    }

    private void requestQrCode() {
        if (isAdded()) {
            mPgbQrCode.setVisibility(View.VISIBLE);
            mIvQrCode.setClickable(false);
            QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
                @Override
                public void onQrCode(RoomQrCodeSimple code) {
                    if (isAdded()) {
                        mPgbQrCode.setVisibility(View.GONE);
                        if (code != null) {
                            Bitmap bitmap = QrCodeUtil.createQRCode(code.toString());
                            if (bitmap != null) {
                                mIvQrCode.setImageBitmap(bitmap);
                            }
                        }
                    }
                }

                @Override
                public void onQrCodeFail(String error) {
                    if (isAdded()) {
                        mPgbQrCode.setVisibility(View.GONE);
                        if (TextUtils.isEmpty(NodeRoomInfo.getInstance().getRoomSession())) {
                            tvQrMsg.setText(error);
                            tvQrMsg.setVisibility(View.VISIBLE);
                            mIvQrCode.setVisibility(View.GONE);
                            mIvQrCode.setClickable(false);
                        } else {
                            tvQrMsg.setVisibility(View.GONE);
                            mIvQrCode.setVisibility(View.VISIBLE);
                            mIvQrCode.setImageResource(R.drawable.ic_qrcode_disable);
                            mIvQrCode.setClickable(true);
                        }
                    }
                }
            });
            request.requestCode();
        }
    }

    public void initUserMv() {
        Logger.d(getClass().getSimpleName(), "initUserMv >>>>>>>>>>>>>>> ");
        if (mRoomUsers.getUsers() != null && mRoomUsers.getUsers().size() > 0) {
            Logger.d(getClass().getSimpleName(), "initUserMv has person in room ");
            llQrcode.setVisibility(View.GONE);
            mRvAvatar.setVisibility(View.VISIBLE);
            mWidgetPage.setVisibility(View.VISIBLE);
            mAdtUserAvatar.setData(mRoomUsers.getUsers());
            mAdtUserAvatar.notifyDataSetChanged();
            requestMvList(mRoomUsers.getUsers().get(0).id + "");
        } else {
            Logger.d(getClass().getSimpleName(), "initUserMv not person in room ");
            llQrcode.setVisibility(View.VISIBLE);
            mRvAvatar.setVisibility(View.GONE);
            mWidgetPage.setVisibility(View.GONE);
            requestQrCode();
        }
    }

    private void initSongPager(List<MvInfo> list) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(mSongPager.initPager(list, 8));
    }

    private void requestMvList(String userId) {
        HttpRequestV4 request = initRequestV4(RequestMethod.V4.CLOUD_USER_MV);
        request.addParam("user_id", userId);
        request.setConvert2Class(MvSongsV4.class);
        request.get();
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.CLOUD_USER_MV.equals(method)) {
                MvSongsV4 mvSongsV4;
                if (object instanceof MvSongsV4 && (mvSongsV4 = (MvSongsV4) object).song != null && mvSongsV4.song.size() > 0) {
                    initSongPager(mvSongsV4.song);
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onAvatarClick(int position, UserBase userBase) {
        if (userBase != null) {
            requestMvList(userBase.id + "");
        }
    }

    @Override
    public void onAvatarAdd() {
        DlgRoomQrCode dialog = new DlgRoomQrCode(getActivity());
        dialog.show();
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mSongPager.notifyCurrentPage();
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.ROOM_USER_CHANGED:
                Logger.d(getClass().getSimpleName(), "ROOM_USER_CHANGED >>>>>>>>>>>>>>> ");
                initUserMv();
                break;
            case EventBusId.Id.CHOOSE_SONG_CHANGED:
                mSongPager.notifyCurrentPage();
                break;
            default:
                break;
        }
    }
}
