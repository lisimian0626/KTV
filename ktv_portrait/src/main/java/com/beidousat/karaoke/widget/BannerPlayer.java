package com.beidousat.karaoke.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.song.FmBannerDetail;
import com.beidousat.karaoke.util.FragmentUtil;
import com.bestarmedia.libcommon.ad.AdGetterV4;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.banner.BannerBase;
import com.bestarmedia.libwidget.banner.OutlineContainer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/11 19:59.
 */
public class BannerPlayer extends BannerBase implements AdsRequestListenerV4 {

    private AdGetterV4 adGetterV4;
    private String mAdPosition;
    private int mPlaceholder;
    private MainAdapter mAdapter;
    private List<ADModel> mLsAds = new ArrayList<>();
    private List<Uri> mImageUrls = new ArrayList<>();

    public BannerPlayer(Context context) {
        super(context);
        init(context);
    }

    /**
     * @param context context
     * @param attrs   attrs
     */
    public BannerPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mAdapter = new MainAdapter(mContext);
        mPager.setAdapter(mAdapter);
        adGetterV4 = new AdGetterV4(mContext, this);

    }

    public void loadAds(String position) {
        mAdPosition = position;
        this.mPlaceholder = R.drawable.ad_banner_default;
    }


    public void requestAds() {
        if (!TextUtils.isEmpty(mAdPosition)) {
            adGetterV4.getADbyPos(mAdPosition);
        }
    }

    public void loadImage(String imgUrl, int placeholder) {
        loadImage(ServerFileUtil.getImageUrl(imgUrl), placeholder);
    }

    public void loadImage(Uri uri, int placeholder) {
        this.mPlaceholder = placeholder;
        mImageUrls.add(uri);
        mAdapter.setAds(mImageUrls);
    }

    @Override
    public void onAdsRequestFail() {
        this.post(() ->
                setBackgroundResource(mPlaceholder));
    }

    @Override
    public void onAdsRequestSuccess(final List<ADModel> adModelList) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (adModelList != null && adModelList.size() > 0) {
                    for (ADModel ad : adModelList) {
                        mLsAds.add(ad);
                        if (!TextUtils.isEmpty(ad.getAdContent())) {
                            try {
                                String[] urls = ad.getAdContent().split("\\|");
                                Uri uri = ServerFileUtil.getImageUrl(urls[0]);
                                mImageUrls.add(uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Logger.w(getClass().getSimpleName(), "解析图片路径出错" + "  " + e.toString());
                            }
                        }

                    }
                    mAdapter.setAds(mImageUrls);
                    setCurrentItem(mAdapter.getCount() - 1);
                }
            }
        });
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
                BannerPlayer.this.post(() ->
                        setCurrentItem(fItem));
            } catch (Exception e) {
                Logger.w("AdSupporterPlayer", "handleMessage ex:" + e.toString());
            }
        }
    }

    @Override
    public void toNextPage() {
        super.toNextPage();
        try {
            int count = mAdapter.getCount();
            int curItem = mPager.getCurrentItem();
            curItem = curItem + 1;
            if (curItem > count - 1) {
                curItem = 0;
            }
            final int fItem = curItem;
            BannerPlayer.this.post(() ->
                    setCurrentItem(fItem));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentItem(int curItem) {
        try {
            mPager.setCurrentItem(curItem);
            ADModel ad = mLsAds.get(curItem);
            new AdPlayRecorder(mContext).recordAdPlay(ad);
        } catch (Exception e) {
            Logger.w(getClass().getSimpleName(), "setCurrentItem ex:" + e.toString());
        }
    }

    private boolean mIsCorner = true;

    public void setCorner(boolean isCorner) {
        mIsCorner = isCorner;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    private class MainAdapter extends PagerAdapter {

        private List<Uri> mImgUrls = new ArrayList<>();
        private Context mContext;

        public MainAdapter(Context context) {
            mContext = context;
        }

        public void setAds(List<Uri> ulrs) {
            mImgUrls = ulrs;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mImgUrls.size();
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPager.setObjectForPosition(imageView, position);

            Uri imageUrl = mImgUrls.get(position);
            if (imageUrl == null) {
                imageView.setImageResource(mPlaceholder);
            } else {
                if (mIsCorner) {
                    Glide.with(mContext).load(imageUrl).override(400, 400).centerCrop().error(mPlaceholder)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(5))).into(imageView);
                } else {
                    Glide.with(mContext).load(imageUrl).override(400, 400).centerCrop().placeholder(mPlaceholder).into(imageView);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ADModel ad = mLsAds.get(position);
                        if (!TextUtils.isEmpty(ad.getAdContent())) {
                            FragmentUtil.addFragment(FmBannerDetail.newInstance(ad), false, false, true, false);
                        }
                    } catch (Exception e) {
                        Logger.w(getClass().getSimpleName(), "setOnClickListener ex:" + e.toString());
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
