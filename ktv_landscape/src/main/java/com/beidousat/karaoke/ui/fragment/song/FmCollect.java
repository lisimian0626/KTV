package com.beidousat.karaoke.ui.fragment.song;

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
import com.beidousat.karaoke.data.RoomUsers;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.ui.adapter.AdtUserAvatar;
import com.beidousat.karaoke.ui.dialog.DlgRoomQrCode;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmCollect extends BaseFragment implements OnPageScrollListener, OnPreviewSongListener, AdtUserAvatar.OnAvatarClickListener, WidgetPage.OnPageChangedListener, View.OnClickListener {

    private View mRootView;
    private WidgetSongPagerV4 mSongPager;
    private WidgetPage mWidgetPage;
    private RecyclerView mRvAvatar;
    private View llQrcode;
    private ImageView mIvQrCode;
    private TextView tvQrMsg;

    private ProgressBar mPgbQrCode;
    private Map<String, String> mParams;
    private AdtUserAvatar mAdtUserAvatar;
    private RoomUsers mRoomUsers;

    public static FmCollect newInstance() {
        FmCollect fragment = new FmCollect();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoomUsers = RoomUsers.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_collect, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        llQrcode = mRootView.findViewById(R.id.ll_qrode);
        mIvQrCode = mRootView.findViewById(R.id.iv_qrcode);
        mIvQrCode.setOnClickListener(this);
        mPgbQrCode = mRootView.findViewById(R.id.pgb_qrcode);
        tvQrMsg = mRootView.findViewById(R.id.tv_qr_msg);
        mRootView.findViewById(R.id.riv_close).setVisibility(View.GONE);
        mRootView.findViewById(R.id.riv_down_app).setVisibility(View.GONE);
        mRootView.findViewById(R.id.btn_disconnect).setVisibility(View.GONE);

        mRvAvatar = mRootView.findViewById(R.id.rv_avatar);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);

        mSongPager.setOnPagerScrollListener(this);

        mSongPager.setOnPreviewSongListener(this);

        VerticalDividerItemDecoration verDivider = new VerticalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8, 8).build();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvAvatar.setLayoutManager(layoutManager2);
        mRvAvatar.addItemDecoration(verDivider);

        mAdtUserAvatar = new AdtUserAvatar(getActivity());
        mAdtUserAvatar.setOnAvatarClickListener(this);

        mRvAvatar.setAdapter(mAdtUserAvatar);

        initUserCollect();

        EventBus.getDefault().register(this);

        return mRootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_qrcode) {
            requestQrCode();
        }
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

    private void requestUserCollect(String userId) {
        HttpRequestV4 request = initRequestV4(RequestMethod.V4.CLOUD_USER_FAVORITES);
        request.addParam("user_id", userId);
        request.addParam("per_page", String.valueOf(8));
        request.addParam("current_page", String.valueOf(1));
        request.setConvert2Class(SongSimplePage.class);
        mParams = request.getParams();
        request.get();
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

    public void initUserCollect() {
        Logger.d(getClass().getSimpleName(), "initUserCollect >>>>>>>>>>>>>>> ");
        if (mRoomUsers.getUsers() != null && mRoomUsers.getUsers().size() > 0) {
            Logger.d(getClass().getSimpleName(), "initUserCollect has person in room ");
            llQrcode.setVisibility(View.GONE);
            mRvAvatar.setVisibility(View.VISIBLE);
            mWidgetPage.setVisibility(View.VISIBLE);
            mAdtUserAvatar.setData(mRoomUsers.getUsers());
            mAdtUserAvatar.notifyDataSetChanged();
            requestUserCollect(mRoomUsers.getUsers().get(0).id + "");
        } else {
            Logger.d(getClass().getSimpleName(), "initUserCollect not person in room ");
            llQrcode.setVisibility(View.VISIBLE);
            mRvAvatar.setVisibility(View.GONE);
            mWidgetPage.setVisibility(View.GONE);
            requestQrCode();
        }
    }

    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(RequestMethod.V4.CLOUD_USER_FAVORITES, totalPage, firstPageSong, params);
    }

    @Override
    public void onSuccess(String method, Object object) {
        Logger.d(getClass().getSimpleName(), "onSuccess >>>>>>>>>> " + method);
        if (isAdded()) {
            if (RequestMethod.V4.CLOUD_USER_FAVORITES.equalsIgnoreCase(method)) {
                if (object instanceof SongSimplePage) {
                    SongSimplePage songSimplePage = (SongSimplePage) object;
                    if (songSimplePage.song != null && songSimplePage.song.data != null && songSimplePage.song.data.size() > 0) {
                        initSongPager(songSimplePage.song.last_page, songSimplePage.song.data, mParams);
                    } else {
                        initSongPager(0, null, mParams);
                    }
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (isAdded()) {
            if (RequestMethod.V4.CLOUD_USER_FAVORITES.equalsIgnoreCase(method)) {
                initSongPager(0, null, mParams);
            }
        }
    }

    @Override
    public void onAvatarClick(int position, UserBase userBase) {
        if (userBase != null)
            requestUserCollect(userBase.id + "");
    }

    @Override
    public void onAvatarAdd() {
        DlgRoomQrCode dialog = new DlgRoomQrCode(getActivity());
        dialog.show();
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mSongPager.runLayoutAnimation(isLeft);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }


    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        new PreviewDialog(getActivity(), song, String.valueOf(ps)).show();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.ROOM_USER_CHANGED) {
            initUserCollect();
        }
    }
}
