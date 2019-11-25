package com.beidousat.karaoke.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.song.FmBannerDetail;
import com.beidousat.karaoke.ui.fragment.song.FmTopicDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.ad.HomeBannerGetter;
import com.bestarmedia.libcommon.ad.HomeBannerRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.RecommendInfo;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.banner.BannerBase;
import com.bestarmedia.libwidget.banner.OutlineContainer;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by J Wong on 2015/12/11 19:59.
 */
public class AdSupporterPlayer extends BannerBase implements View.OnClickListener, HomeBannerRequestListenerV4 {

    private MainAdapter mAdapter;
    private List<RecommendInfo> recommendInfoList = new ArrayList<>();
    private HomeBannerGetter homeBannerGetter;

    public AdSupporterPlayer(Context context) {
        super(context);
        if (homeBannerGetter == null) {
            homeBannerGetter = new HomeBannerGetter(context, this);
        }
    }

    public AdSupporterPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (homeBannerGetter == null) {
            homeBannerGetter = new HomeBannerGetter(context, this);
        }
    }


    public void loadAds() {
        requestAd();
    }

    @Override
    public void startPlayer() {
        if (mAdapter != null) {
            final int curItem = mPager.getCurrentItem();
            AdSupporterPlayer.this.post(() -> {
                try {
                    setCurrentItem(curItem);
                    View childAt = mPager.getChildAt(curItem);
                    childAt.setVisibility(VISIBLE);
                    childAt.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        super.startPlayer();
    }

    public void setCurrentItem(int curItem) {
        try {
            mPager.setCurrentItem(curItem);
            RecommendInfo ad = recommendInfoList.get(curItem);
            if (ad.getType() == 1) {
                new AdPlayRecorder(mContext).recordAdPlay(ad.toADModel());
            }
        } catch (Exception e) {
            Logger.w(getClass().getSimpleName(), "setCurrentItem ex:" + e.toString());
        }
    }

    @Override
    public void stopPlayer() {
        super.stopPlayer();
    }

    private void requestAd() {
        homeBannerGetter.getRecommend();
    }


    @Override
    public void toPrePage() {
        super.toPrePage();
        if (mAdapter != null) {
            try {
                int count = mAdapter.getCount();
                int curItem = mPager.getCurrentItem();
                curItem = curItem - 1;
                if (curItem < 0) {
                    curItem = count - 1;
                }
                final int fItem = curItem;
                AdSupporterPlayer.this.post(() -> setCurrentItem(fItem));
            } catch (Exception e) {
                Logger.w("AdSupporterPlayer", "handleMessage ex:" + e.toString());
            }
        }
    }

    @Override
    public void toNextPage() {
        super.toNextPage();
        if (mAdapter != null) {
            try {
                int count = mAdapter.getCount();
                int curItem = mPager.getCurrentItem();
                curItem = curItem + 1;
                if (curItem >= count) {
                    curItem = 0;
                }
                final int fItem = curItem;
                AdSupporterPlayer.this.post(() -> setCurrentItem(fItem));
            } catch (Exception e) {
                Logger.w("AdSupporterPlayer", "handleMessage ex:" + e.toString());
            }
        }
    }


    @Override
    public void onRequestSuccess(List<RecommendInfo> recommendInfos) {
        recommendInfoList = recommendInfos;
        if (recommendInfos != null && recommendInfos.size() > 0) {
            mAdapter = new MainAdapter(mContext);
            mPager.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mRivNext.setVisibility(VISIBLE);
            mRivPre.setVisibility(VISIBLE);

        } else {
            setBackgroundResource(R.drawable.ad_banner_default);
            mRivNext.setVisibility(GONE);
            mRivPre.setVisibility(GONE);
        }
    }

    @Override
    public void onRequestFail() {
        this.post(() -> {
            setBackgroundResource(R.drawable.ad_banner_default);
            mRivNext.setVisibility(GONE);
            mRivPre.setVisibility(GONE);
        });
    }


    private class MainAdapter extends PagerAdapter {

        private Context mContext;

        MainAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return recommendInfoList == null ? 0 : recommendInfoList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            RecyclerImageView imageView = new RecyclerImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPager.setObjectForPosition(imageView, position);
            final RecommendInfo ad = recommendInfoList.get(position);
            if (ad.getAdContent().contains("|")) {
                if (ad.getAdContent().equals("|")) {
                    imageView.setImageResource(R.drawable.ad_banner_default);
                } else {
                    try {
                        String[] imageUrls = ad.getAdContent().split("\\|");
                        //type=0未小编推荐，type=1为banner,如果是小编推荐，取第二个图，banner取第一个图
                        Uri imageUrl = ad.getType() == 0 ? ServerFileUtil.getImageUrl(imageUrls[1]) : ServerFileUtil.getImageUrl(imageUrls[0]);
                        Logger.d(getClass().getSimpleName(), "imageUrl:" + imageUrl);
                        if (imageUrl == null) {
                            imageView.setImageResource(R.drawable.ad_banner_default);
                        } else {
                            //设置图片圆角角度
                            RoundedCorners roundedCorners = new RoundedCorners(5);
                            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
                            Glide.with(mContext).load(imageUrl).error(R.drawable.ad_banner_default).apply(options).into(imageView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.w(getClass().getSimpleName(), "解析图片路径出错" + "  " + e.toString());
                    }
                }

            } else {
                imageView.setImageResource(R.drawable.ad_banner_default);
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ad.getAdContent().contains("|") && !ad.getAdContent().equals("|")) {
                        try {
                            if (ad.getType() == 0) {
                                FmTopicDetail detail = FmTopicDetail.newInstance(ad);
                                FragmentUtil.addFragment(detail, false, false, true, false);
                            } else if (ad.getType() == 1) {
                                FragmentUtil.addFragment(FmBannerDetail.newInstance(ad.toADModel()), false, false, true, false);
                            }
                        } catch (Exception e) {
                            Logger.w(getClass().getSimpleName(), "onClick ex:" + e.toString());
                        }
                    }
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object obj) {
            container.removeView(mPager.findViewFromObject(position));
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }
    }
}
