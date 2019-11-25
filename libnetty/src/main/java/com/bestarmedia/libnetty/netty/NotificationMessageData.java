package com.bestarmedia.libnetty.netty;

import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.proto.node.ImageMessageRequest;
import com.bestarmedia.proto.node.TextMessageRequest;
import com.bestarmedia.proto.node.VideoMessageRequest;

import java.util.ArrayList;
import java.util.List;

public class NotificationMessageData {

    //文本通知队列
    private List<TextMessageRequest> textQueue = new ArrayList<>();
    //图片通知队列
    private List<ImageMessageRequest> imageQueue = new ArrayList<>();
    //视频通知队列(下一个视频插播)
    private List<VideoMessageRequest> videoQueueAfter = new ArrayList<>();
    //视频通知队列（立即插播）
    private List<VideoMessageRequest> videoQueueImmediately = new ArrayList<>();
    //视频通知队列(小窗口)
    private List<VideoMessageRequest> videoSmallQueue = new ArrayList<>();

    private static NotificationMessageData instance;

    public static NotificationMessageData getInstance() {
        if (instance == null) {
            instance = new NotificationMessageData();
        }
        return instance;
    }


    /**
     * 加入文本队列
     */
    public void addTextMessage(TextMessageRequest text, boolean isPriority) {
        if (isPriority) {
            textQueue.add(0, text);
        } else {
            textQueue.add(text);
        }
        Log.i("NotificationMessageData", "当前文本通知队列长度：" + textQueue.size());
        EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_TEXT_CHANGED, text);
    }

    /**
     * @return 当前文本通知队列
     */
    public List<TextMessageRequest> getTextQueue() {
        return textQueue;
    }

    /**
     * 加入图片队列
     */
    public void addImageMessage(ImageMessageRequest image, boolean isPriority) {
        if (isPriority) {
            imageQueue.add(0, image);
        } else {
            imageQueue.add(image);
        }
        Log.i("NotificationMessageData", "当前图片通知队列长度：" + imageQueue.size());
        EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_IMAGE_CHANGED, image);
    }

    /**
     * @return 当前文字通知队列
     */
    public List<ImageMessageRequest> getImageQueue() {
        return imageQueue;
    }

    /**
     * 加入视频队列
     */
    public void addVideoMessage(VideoMessageRequest video, boolean isPriority) {
        if (video.getVideo().getType() == 2) {
            if (isPriority) {
                videoSmallQueue.add(0, video);
            } else {
                videoSmallQueue.add(video);
            }
            Log.i("NotificationMessageData", "当前视频（小窗口）通知队列长度：" + videoSmallQueue.size());
            EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_VIDEO_SMALL_CHANGED, video);
        } else {
            if (video.getVideo().getPlayType() == 1) {
                if (isPriority) {
                    videoQueueImmediately.add(0, video);
                } else {
                    videoQueueImmediately.add(video);
                }
                Log.i("NotificationMessageData", "当前立即插播视频队列长度：" + videoQueueImmediately.size());
                EventBusUtil.postSticky(EventBusId.ImId.NOTIFICATION_VIDEO_CHANGED, video);
            } else {
                if (isPriority) {
                    videoQueueAfter.add(0, video);
                } else {
                    videoQueueAfter.add(video);
                }
                Log.i("NotificationMessageData", "当前排队插播视频队列长度：" + videoQueueAfter.size());
            }
        }
    }

    /**
     * @return 当前立即插播视频通知队列
     */
    public List<VideoMessageRequest> getVideoQueueImmediately() {
        return videoQueueImmediately;
    }

    /**
     * @return 当前视频通知队列
     */
    public List<VideoMessageRequest> getVideoQueueAfter() {
        return videoQueueAfter;
    }

    /**
     * @return 当前排队插播视频通知队列
     */
    public List<VideoMessageRequest> getVideoSmallQueue() {
        return videoSmallQueue;
    }
}
