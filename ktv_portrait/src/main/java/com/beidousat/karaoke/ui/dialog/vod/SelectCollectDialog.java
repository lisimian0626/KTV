package com.beidousat.karaoke.ui.dialog.vod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.data.RoomUsers;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.FavoritesSongRequestBody;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectCollectDialog extends BaseDialog implements View.OnClickListener {

    private final String TAG = SelectCollectDialog.class.getSimpleName();
    private TextView mTvRoomCode;
    private ImageView mIvQrCode;
    private ProgressBar mPgbQrCode;
    private RecyclerView mRvRoomUsers;

    private View vProgress;
    private ProgressBar mProgressLoading;
    private TextView tvProgress;


    private AdtUserCollect mAdtUserCollect;
    private SongSimple mSong;
    private Context mContext;

    public SelectCollectDialog(Context context, SongSimple song) {
        super(context, R.style.MyDialog);
        mSong = song;
        mContext = context;
        init();

        EventBus.getDefault().register(this);

    }

    private void init() {
        this.setContentView(R.layout.dlg_select_collect);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 601;
        lp.height = 650;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        mRvRoomUsers = findViewById(R.id.rv_user);
        findViewById(R.id.iv_close).setOnClickListener(this);
        mTvRoomCode = findViewById(R.id.tv_room_qrcode);
        mIvQrCode = findViewById(R.id.iv_qrcode);
        mIvQrCode.setOnClickListener(this);
        mPgbQrCode = findViewById(R.id.pgb_qrcode);

        vProgress = findViewById(R.id.ll_progress);
        mProgressLoading = findViewById(R.id.progress);
        tvProgress = findViewById(R.id.tv_progress);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(16).margin(16, 16)
                .build();

        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(60).margin(60, 60)
                .build();

        mRvRoomUsers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvRoomUsers.addItemDecoration(horDivider);
        mRvRoomUsers.addItemDecoration(verDivider);

        mAdtUserCollect = new AdtUserCollect();
        mRvRoomUsers.setAdapter(mAdtUserCollect);

        initUserCollect();

        requestQrCode();
    }

    private void requestQrCode() {
        mPgbQrCode.setVisibility(View.VISIBLE);
        mIvQrCode.setClickable(false);
        QrCodeRequest request = new QrCodeRequest(getContext(), new QrCodeRequest.QrCodeRequestListener() {
            @Override
            public void onQrCode(RoomQrCodeSimple code) {
                mPgbQrCode.setVisibility(View.GONE);
                if (code != null) {
                    Bitmap bitmap = QrCodeUtil.createQRCode(code.toString());
                    if (bitmap != null) {
                        mIvQrCode.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onQrCodeFail(String error) {
                mPgbQrCode.setVisibility(View.GONE);
                mIvQrCode.setImageResource(R.drawable.ic_qrcode_disable);
                mIvQrCode.setClickable(true);

            }
        });
        request.requestCode();
    }

    private void initUserCollect() {
        List<UserBase> userBases = RoomUsers.getInstance().getUsers();
        if (userBases != null && userBases.size() > 0) {
            mAdtUserCollect.setData(userBases);
            mAdtUserCollect.notifyDataSetChanged();
            findViewById(R.id.iv_empty).setVisibility(View.GONE);
        } else {
            findViewById(R.id.iv_empty).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_qrcode:
                requestQrCode();
                break;
        }
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.ROOM_USER_CHANGED) {
            initUserCollect();
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerImageView cirAvatar;
        private TextView tvName;
        private Button btnDownload;

        public ViewHolder(View view) {
            super(view);
        }
    }

    class AdtUserCollect extends RecyclerView.Adapter<ViewHolder> {

        private LayoutInflater mInflater;
        private List<UserBase> mData = new ArrayList<>();

        AdtUserCollect() {
            mInflater = LayoutInflater.from(mContext);
        }

        public void setData(List<UserBase> data) {
            this.mData = data;
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_room_user_avatar, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvName = view.findViewById(R.id.tv_nickname);
            viewHolder.cirAvatar = view.findViewById(R.id.iv_avatar);
            viewHolder.btnDownload = view.findViewById(R.id.btn_collect);
            viewHolder.btnDownload.setText(getContext().getString(R.string.collect));
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final UserBase userBase = mData.get(position);
            Activity mActivity = (Activity) mContext;
            holder.tvName.setText(userBase.name);
            //设置图片圆角角度
            RoundedCorners roundedCorners = new RoundedCorners(5);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(mContext).load(ServerFileUtil.getImageUrl(userBase.avatar))
                    .error(R.drawable.star_default).skipMemoryCache(false).apply(options).into(holder.cirAvatar);
            holder.btnDownload.setOnClickListener(v -> {
                vProgress.setVisibility(View.VISIBLE);
                tvProgress.setText(R.string.collecting);
                HttpRequestV4 r = new HttpRequestV4(mContext, RequestMethod.V4.CLOUD_FAVORITES_SONG);
                r.setHttpRequestListener(new HttpRequestListener() {
                    @Override
                    public void onStart(String method) {

                        mActivity.runOnUiThread(() -> {
                            vProgress.setVisibility(View.VISIBLE);
                            tvProgress.setText(R.string.collecting);
                        });
                    }

                    @Override
                    public void onSuccess(String method, Object object) {
                        mActivity.runOnUiThread(() -> {
                            tvProgress.setText(R.string.collect_success);
                            vProgress.postDelayed(() -> vProgress.setVisibility(View.GONE), 1500);
                        });
                    }

                    @Override
                    public void onFailed(final String method, final Object object) {
                        String error = "";
                        if (object instanceof BaseModelV4) {
                            BaseModelV4 baseModelV4 = (BaseModelV4) object;
                            error = baseModelV4.tips;
                        } else if (object instanceof String) {
                            error = object.toString();
                        }
                        Logger.d(TAG, method + ":" + error);
                        String finalError = error;
                        mActivity.runOnUiThread(() -> {
                            vProgress.setVisibility(View.GONE);
                            try {
                                String reason = finalError;
                                if (!TextUtils.isEmpty(finalError) && finalError.contains("|")) {
                                    String[] errors = finalError.split("\\|");
                                    if (finalError.length() >= 3) {
                                        reason = errors[2];
                                    }
                                }
                                tvProgress.setText(getContext().getString(R.string.collect_fail, reason));
                                PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
                                promptDialog.setMessage(getContext().getString(R.string.collect_fail, reason));
                                promptDialog.show();
                            } catch (Exception e) {
                                Logger.w(TAG, "onFailed " + method + " ex:" + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onError(String method, String error) {

                    }
                });
                FavoritesSongRequestBody body = new FavoritesSongRequestBody();
                body.user_id = userBase.id;
                body.song_id = mSong.id;
                body.ktv_net_code = VodConfigData.getInstance().getKtvNetCode();
                body.room_code = VodConfigData.getInstance().getRoomCode();
                body.simple_song_name = mSong.songName;
                body.singer_name = mSong.getSingerName();
                body.song_version = mSong.videoType;
                body.is_gradeLib = mSong.isScore;
                body.is_hd = mSong.isHd;
                r.postJson(body.toString());
            });
        }
    }
}
