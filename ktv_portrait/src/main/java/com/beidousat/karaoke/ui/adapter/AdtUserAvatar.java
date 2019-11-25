package com.beidousat.karaoke.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.im.UserBase;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J Wong on 2015/12/17 08:54.
 */
public class AdtUserAvatar extends RecyclerView.Adapter<AdtUserAvatar.ViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<UserBase> mData = new ArrayList<>();
    private final static int mLWH = 60;
    private final static int mSWH = 50;

    public AdtUserAvatar(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setData(List<UserBase> data) {
        this.mData = data;
    }

    private int mFocusTab;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mItem;
        private RecyclerImageView cirAvatar;

        public ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.list_item_avatar, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mItem = view.findViewById(R.id.list_item);
        viewHolder.cirAvatar = view.findViewById(R.id.iv_avatar);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int wh = position == mFocusTab ? mLWH : mSWH;
        if (position == getItemCount() - 1) {
            holder.cirAvatar.setImageResource(R.drawable.ic_add_user);
//            holder.cirAvatar.setBorderWidth(0);
            holder.mItem.setPadding(0, 0, 0, 0);
            holder.cirAvatar.setBackgroundResource(0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mSWH, mSWH);
            holder.cirAvatar.setLayoutParams(params);
        } else {
            final UserBase userBase = mData.get(position);
            GlideUtils.LoadCornersImage(mContext, userBase.avatar, R.drawable.ic_avatar,
                    R.drawable.ic_avatar, 25, true, holder.cirAvatar);
            holder.mItem.setPadding(0, position == mFocusTab ? 0 : mLWH - mSWH, 0, 0);
            holder.cirAvatar.setBackgroundResource(position == mFocusTab ? R.drawable.ic_user_avater_selected_bg : 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, wh);
            holder.cirAvatar.setLayoutParams(params);
        }

        holder.cirAvatar.setOnClickListener(v -> {
            if (mOnAvatarClickListener != null)
                if (position == getItemCount() - 1) {
                    mOnAvatarClickListener.onAvatarAdd();
                } else {
                    int preFocus = mFocusTab;
                    mFocusTab = position;
                    mOnAvatarClickListener.onAvatarClick(position, mData.get(position));
                    notifyItemChanged(preFocus);
                    notifyItemChanged(mFocusTab);
                }
        });
    }

    private OnAvatarClickListener mOnAvatarClickListener;

    public void setOnAvatarClickListener(OnAvatarClickListener listener) {
        this.mOnAvatarClickListener = listener;
    }


    public interface OnAvatarClickListener {

        void onAvatarClick(int position, UserBase userBase);

        void onAvatarAdd();
    }
}
