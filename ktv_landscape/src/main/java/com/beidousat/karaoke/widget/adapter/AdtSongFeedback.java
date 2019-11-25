//package com.beidousat.karaoke.widget.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.beidousat.karaoke.R;
//import com.bestarmedia.libcommon.model.vod.SongFeedback;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by J Wong on 2016/7/28.
// */
//public class AdtSongFeedback extends BaseAdapter {
//
//    private List<SongFeedback> mData = new ArrayList<SongFeedback>();
//    private Context context;
//
//    private int mFocusPs = 0;
//    private int mFocusWhich = 0;
//
//    private OnFocusChangedListener mOnFocusChangedListener;
//
//
//    public interface OnFocusChangedListener {
//        void onInputFocusChanged(int[] focus, SongFeedback feedback);
//    }
//
//    public AdtSongFeedback(Context context) {
//        this.context = context;
//    }
//
//    public int addData(SongFeedback feedback) {
//        mData.add(feedback);
//        setFocus(mData.size() - 1, 0);
//        notifyDataSetChanged();
//
//        return mData.size();
//    }
//
//    public void inputText(String text) {
//        if (mFocusWhich == 0)
//            mData.get(mFocusPs).song_name = text;
//        else
//            mData.get(mFocusPs).singer_name = text;
//
//        notifyDataSetChanged();
//    }
//
//    public void setOnFocusChangedListener(OnFocusChangedListener listener) {
//        mOnFocusChangedListener = listener;
//    }
//
//    public List<SongFeedback> getData() {
//        return mData;
//    }
//
//    public int[] getFocus() {
//        return new int[]{mFocusPs, mFocusWhich};
//    }
//
//    private void setFocus(int ps, int which) {
//        mFocusPs = ps;
//        mFocusWhich = which;
//        if (mOnFocusChangedListener != null) {
//            mOnFocusChangedListener.onInputFocusChanged(new int[]{mFocusPs, mFocusWhich}, mData.get(mFocusPs));
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return mData.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_song_feed_back, null);
//            holder = new ViewHolder();
//            holder.mEtSongName = convertView.findViewById(R.id.et_song_name);
//            holder.mEtSinger = convertView.findViewById(R.id.et_singer);
//            holder.mEtBug = convertView.findViewById(R.id.et_bug);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        if (position == mFocusPs) {
//            if (mFocusWhich == 0) {
//                holder.mEtSongName.setSelected(true);
//                holder.mEtSinger.setSelected(false);
//            } else {
//                holder.mEtSongName.setSelected(false);
//                holder.mEtSinger.setSelected(true);
//            }
//        } else {
//            holder.mEtSongName.setSelected(false);
//            holder.mEtSinger.setSelected(false);
//        }
//
//        final SongFeedback feedback = mData.get(position);
//        holder.mEtSongName.setText(feedback.song_name == null ? "" : feedback.song_name);
//        holder.mEtSinger.setText(feedback.singer_name == null ? "" : feedback.singer_name);
//        holder.mEtBug.setText(feedback.problem_description == null ? "" : feedback.problem_description);
//
//        holder.mEtSongName.setOnClickListener(v -> {
//            setFocus(position, 0);
//            notifyDataSetChanged();
//        });
////屏蔽
////        holder.mEtBug.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                DlgSongBugType dlgSongBugType = new DlgSongBugType(Main.mMainActivity);
////                dlgSongBugType.setSelectedText(feedback.problem_description);
////                dlgSongBugType.setOnTypeSelectedListener(new DlgSongBugType.OnTypeSelectedListener() {
////                    @Override
////                    public void onTypeSelected(String text) {
////                        feedback.problem_description = text;
////                        notifyDataSetChanged();
////                    }
////                });
////                dlgSongBugType.show();
////            }
////        });
//
//        holder.mEtSinger.setOnClickListener(v -> {
//            setFocus(position, 1);
//            notifyDataSetChanged();
//        });
//
//        return convertView;
//    }
//
//
//    private static class ViewHolder {
//        private TextView mEtSongName, mEtSinger, mEtBug;
//    }
//}
