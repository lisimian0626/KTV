package com.beidousat.karaoke.ui.dialog.play;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.vod.Pk;
import com.bestarmedia.libcommon.model.vod.PkRequestBody;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;
import com.bestarmedia.libwidget.util.GlideUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class DlgSelectPk extends BaseDialog implements View.OnClickListener {

    private final String TAG = DlgSelectPk.class.getSimpleName();

    private Activity mActivity;
    private TextView mTvRoomCode;
    private ImageView mIvQrCode;
    private ProgressBar mPgbQrCode;
    private RecyclerView mRvRoomUsers;
    private AdtUserCollect mAdtUserCollect;
    private Pk mPk;

    private View vProgress;
    private ProgressBar mProgressLoading;
    private TextView tvProgress;

    public DlgSelectPk(Pk pk, Activity context) {
        super(context, R.style.MyDialog);
        mActivity = context;
        mPk = pk;
        init();

//        EventBus.getDefault().register(this);

    }

    private void init() {
        this.setContentView(R.layout.dlg_select_collect);
        if (getWindow() == null)
            return;
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
        TextView tvTitle = findViewById(R.id.tv_title);

        vProgress = findViewById(R.id.ll_progress);
        mProgressLoading = findViewById(R.id.progress);
        tvProgress = findViewById(R.id.tv_progress);

        tvTitle.setText(R.string.select_pk);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(16).margin(16, 16)
                .build();

        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
                .color(Color.TRANSPARENT).size(60).margin(60, 60)
                .build();

        mRvRoomUsers.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvRoomUsers.addItemDecoration(horDivider);
        mRvRoomUsers.addItemDecoration(verDivider);

        mAdtUserCollect = new AdtUserCollect(getContext());
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

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.ROOM_USER_CHANGED:
                initUserCollect();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        EventBus.getDefault().unregister(this);
        super.dismiss();
    }

    public void request(Pk pk, UserBase userBase) {
        PkRequestBody requestBody = new PkRequestBody();
        requestBody.id = pk.id;
        requestBody.accept_ktv_net_code = VodConfigData.getInstance().getKtvNetCode();
        requestBody.accept_ktv_net_name = NodeRoomInfo.getInstance().getKtvName();
        requestBody.accept_room_code = VodConfigData.getInstance().getRoomCode();
        requestBody.accept_room_name = NodeRoomInfo.getInstance().getRoomName();
        requestBody.accept_room_session = NodeRoomInfo.getInstance().getRoomSession();
        requestBody.accept_user_id = userBase.id;
        requestBody.accept_user_name = userBase.name;
        requestBody.accept_user_avatar = userBase.avatar;

        HttpRequestV4 r = new HttpRequestV4(getContext(), RequestMethod.V4.CLOUD_SONG_DUEL_ACCEPT);
        r.setConvert2Class(Pk.class);
        r.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onStart(String method) {
                mActivity.runOnUiThread(() -> {
                    vProgress.setVisibility(View.VISIBLE);
                    tvProgress.setText(R.string.pk_accepting);
                });
            }

            @Override
            public void onSuccess(String method, Object object) {
                mActivity.runOnUiThread(() -> {
                    tvProgress.setText(R.string.pk_accept_success);
                    vProgress.setVisibility(View.GONE);
                    if (mOnPkListener != null) {
                        mOnPkListener.onPkStart(mPk);
                    }
                    dismiss();
                });
            }

            @Override
            public void onFailed(final String method, Object object) {
                String error = "";
                if (object instanceof BaseModelV4) {
                    BaseModelV4 baseModelV4 = (BaseModelV4) object;
                    error = baseModelV4.tips;
                } else if (object instanceof String) {
                    error = object.toString();
                }
                Logger.d(TAG, method + ":" + object);

                String finalError = error;
                mActivity.runOnUiThread(() -> {
                    vProgress.setVisibility(View.GONE);
                    try {
                        String reason = finalError;
                        if (!TextUtils.isEmpty(finalError) && finalError.contains("|")) {
                            String[] errors = finalError.split("\\|");
                            if (finalError != null && finalError.length() >= 3) {
                                reason = errors[2];
                            }
                        }
                        tvProgress.setText(getContext().getString(R.string.fire_fail, reason));
                        PromptDialog promptDialog = new PromptDialog(mActivity);
                        promptDialog.setMessage(getContext().getString(R.string.fire_fail, reason));
                        promptDialog.setPositiveButton(getContext().getString(R.string.ok), v -> dismiss());
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
        r.postJson(requestBody.toString());
    }

    private OnPkListener mOnPkListener;

    public void setOnPkListener(OnPkListener listener) {
        mOnPkListener = listener;
    }

    public interface OnPkListener {
        void onPkStart(Pk pk);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerImageView cirAvatar;
        public TextView tvName;
        public Button btnCollect;

        public ViewHolder(View view) {
            super(view);
        }
    }

    class AdtUserCollect extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<UserBase> mData = new ArrayList<UserBase>();

        public AdtUserCollect(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<UserBase> data) {
            this.mData = data;
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.list_item_room_user_avatar, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.tvName = view.findViewById(R.id.tv_nickname);
            viewHolder.cirAvatar = view.findViewById(R.id.iv_avatar);
            viewHolder.btnCollect = view.findViewById(R.id.btn_collect);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final UserBase userBase = mData.get(position);
            holder.tvName.setText(userBase.name);
            GlideUtils.LoadCornersImage(mContext, ServerFileUtil.getImageUrl(userBase.avatar).getPath(), R.drawable.star_default, R.drawable.star_default, 10, false, holder.cirAvatar);
//            Glide.with(mContext).load(ServerFileUtil.getImageUrl(userBase.avatar))
//                    .error(R.drawable.star_default).bitmapTransform(new CropCircleTransformation(mContext)).skipMemoryCache(true).into(holder.cirAvatar);
            holder.btnCollect.setOnClickListener(v -> request(mPk, userBase));
        }
    }
}