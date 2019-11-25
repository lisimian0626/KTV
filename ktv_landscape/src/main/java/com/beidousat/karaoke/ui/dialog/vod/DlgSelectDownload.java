package com.beidousat.karaoke.ui.dialog.vod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.beidousat.karaoke.helper.VodCloudHelper;
import com.beidousat.karaoke.ui.dialog.BaseDialog;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.helper.QrCodeRequest;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.model.vod.RoomSongItem;
import com.bestarmedia.libcommon.transmission.RecordFileUploader;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.QrCodeUtil;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.liblame.AudioRecordFileUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;
import com.bestarmedia.libwidget.recycler.VerticalDividerItemDecoration2;
import com.bestarmedia.libwidget.util.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class DlgSelectDownload extends BaseDialog implements View.OnClickListener, RecordFileUploader.OnRecordFileUploadListener {

    private final String TAG = DlgSelectDownload.class.getSimpleName();

    private Context mContext;
    private TextView mTvRoomCode;
    private ImageView mIvQrCode;
    private ProgressBar mPgbQrCode;
    private RecyclerView mRvRoomUsers;
    private AdtUserCollect mAdtUserCollect;
    private RoomSongItem mSong;

    private View vProgress;
    private ProgressBar mProgressLoading;
    private TextView tvProgress;

    public DlgSelectDownload(Context context, RoomSongItem song) {
        super(context, R.style.MyDialog);
        mContext = context;
        mSong = song;
        init();
        EventBus.getDefault().register(this);

    }

    void init() {
        this.setContentView(R.layout.dlg_select_collect);
        LayoutParams lp = getWindow().getAttributes();
        lp.width = 980;
        lp.height = 450;
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

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.select_save_user);

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

        if (DeviceHelper.isMainVod(mContext)) {
            if (!AudioRecordFileUtil.isRecordFileExist(mSong.recordFile)) {
                PromptDialogSmall promptDialog = new PromptDialogSmall(mContext);
                promptDialog.setMessage(R.string.record_file_not_exist);
                promptDialog.setOkButton(true, mContext.getString(R.string.ok), v -> dismiss());
                promptDialog.show();
            }
        }
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

    @Subscribe
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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerImageView cirAvatar;
        public TextView tvName;
        public Button btnDownload;

        public ViewHolder(View view) {
            super(view);
        }
    }

    class AdtUserCollect extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<UserBase> mData = new ArrayList<>();

        public AdtUserCollect(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setData(List<UserBase> data) {
            this.mData = data;
        }

        public List<UserBase> getData() {
            return mData;
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
            viewHolder.btnDownload = view.findViewById(R.id.btn_collect);
            viewHolder.btnDownload.setText(getContext().getString(R.string.save));
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final UserBase userBase = mData.get(position);
            holder.tvName.setText(userBase.name);
            GlideUtils.LoadCornersImage(mContext, ServerFileUtil.getImageUrl(userBase.avatar).getPath(), R.drawable.star_default, R.drawable.star_default, 5, false, holder.cirAvatar);
            int resId = R.string.save;
            if (userBase.saveStatus == 1) {
                resId = R.string.saved;
            } else if (userBase.saveStatus == 2) {
                resId = R.string.saving;
            }

            holder.btnDownload.setEnabled(userBase.saveStatus == 0);

            holder.btnDownload.setText(getContext().getString(resId));

            holder.btnDownload.setOnClickListener(v -> {
                if (DeviceHelper.isMainVod(mContext))
                    VodCloudHelper.downloadRecord(mContext, userBase, mSong.recordFile, DlgSelectDownload.this);
                else {
                    //屏幕主副屏
//                        SocketOperationUtil.sendSaveRecord2Main(userBase, mSong.recordFile);
                }
                userBase.saveStatus = 2;
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onRecordFileUploadStart() {
        vProgress.setVisibility(View.VISIBLE);
        tvProgress.setText(R.string.saving);
    }

    @Override
    public void onRecordFileUploaded(RoomSongItem song, UserBase userBase, String fileUrl) {
        vProgress.setVisibility(View.GONE);

        if (userBase != null && mAdtUserCollect.getData() != null && mAdtUserCollect.getData().size() > 0) {
            int ps = 0;
            for (UserBase userBase1 : mAdtUserCollect.getData()) {
                if (userBase1.id == userBase.id) {
                    mAdtUserCollect.getData().get(ps).saveStatus = 0;
                    mAdtUserCollect.notifyDataSetChanged();
                }
                ps++;
            }
        }

        PromptDialogSmall dialog = new PromptDialogSmall(mContext);
        dialog.setMessage(R.string.save_record_success);
        dialog.show();
    }

    @Override
    public void onRecordFileUploadFail(RoomSongItem song, final UserBase userBase, String error) {
        vProgress.setVisibility(View.GONE);
        Activity activity = (Activity) mContext;
        activity.runOnUiThread(() -> {
            PromptDialogSmall dialog = new PromptDialogSmall(mContext);
            dialog.setMessage(R.string.save_record_fail);
            dialog.show();
            if (userBase != null && mAdtUserCollect.getData() != null && mAdtUserCollect.getData().size() > 0) {
                int ps = 0;
                for (UserBase userBase1 : mAdtUserCollect.getData()) {
                    if (userBase1.id == userBase.id) {
                        mAdtUserCollect.getData().get(ps).saveStatus = 0;
                        mAdtUserCollect.notifyDataSetChanged();
                    }
                    ps++;
                }
            }
        });
    }
}