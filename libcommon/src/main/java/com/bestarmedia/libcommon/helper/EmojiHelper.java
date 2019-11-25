package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.EmojiListener;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.emoji.BarrageV4;
import com.bestarmedia.libcommon.model.vod.emoji.EmojiDetailV4;
import com.bestarmedia.libcommon.model.vod.emoji.EmojiV4;


/**
 * Created by J Wong on 2017/5/15.
 */
public class EmojiHelper implements HttpRequestListener {
    private static String Tag = EmojiHelper.class.getSimpleName();
    private static EmojiHelper mEmojiHelper;
    private Context mContext;
    private EmojiListener emojiListener;

    public static EmojiHelper getInstance(Context context) {
        if (mEmojiHelper == null) {
            mEmojiHelper = new EmojiHelper(context);
        }
        return mEmojiHelper;
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        this.emojiListener = emojiListener;
    }

    public void getEmoji() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.EMOJI);
        request.addParam("per_page", String.valueOf(100));
        request.addParam("current_page", String.valueOf(1));
        request.setHttpRequestListener(this);
        request.setConvert2Class(EmojiV4.class);
        request.get();
    }

    public void getEmojiDetail(String emojiId) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.EMOJI_DETAIL);
        request.addParam("emoji_id", emojiId);
        request.addParam("per_page", String.valueOf(100));
        request.addParam("current_page", String.valueOf(1));
        request.setHttpRequestListener(this);
        request.setConvert2Class(EmojiDetailV4.class);
        request.get();
    }

    public void getBarrage() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.BARRAGE);
        request.addParam("per_page", String.valueOf(100));
        request.addParam("current_page", String.valueOf(1));
        request.setHttpRequestListener(this);
        request.setConvert2Class(BarrageV4.class);
        request.get();
    }

    public EmojiHelper(Context context) {
        this.mContext = context;
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.EMOJI.equalsIgnoreCase(method)) {
            if (object instanceof EmojiV4) {
                EmojiV4 emojiV4 = (EmojiV4) object;
                if (emojiV4.emojiList != null && emojiV4.emojiList.data != null && emojiV4.emojiList.data.size() > 0) {
                    if (emojiListener != null) {
                        emojiListener.onEmoji(emojiV4.emojiList.data);
                    }
                }
            } else if (object instanceof BaseModelV4)
                Toast.makeText(mContext, ((BaseModelV4) object).tips, Toast.LENGTH_SHORT).show();
        } else if (RequestMethod.V4.EMOJI_DETAIL.equalsIgnoreCase(method)) {
            if (object instanceof EmojiDetailV4) {
                EmojiDetailV4 emojiDetailV4 = (EmojiDetailV4) object;
                if (emojiDetailV4.emojiDetailList != null && emojiDetailV4.emojiDetailList.data != null && emojiDetailV4.emojiDetailList.data.size() > 0) {
                    if (emojiListener != null) {
                        emojiListener.onEmojiDetial(emojiDetailV4.emojiDetailList.data);
                    }
                }
            } else if (object instanceof BaseModelV4) {
                Toast.makeText(mContext, ((BaseModelV4) object).tips, Toast.LENGTH_SHORT).show();
            }
        } else if (RequestMethod.V4.BARRAGE.equalsIgnoreCase(method)) {
            if (object instanceof BarrageV4) {
                BarrageV4 barrageV4 = (BarrageV4) object;
                if (barrageV4.barrageist != null && barrageV4.barrageist.data != null && barrageV4.barrageist.data.size() > 0) {
                    if (emojiListener != null) {
                        emojiListener.onBarrage(barrageV4.barrageist.data);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        Log.e(Tag, object.toString());
        if (object instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) object;
            Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String method, String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

}
