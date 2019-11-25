package com.beidousat.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J Wong on 2016/11/9.
 */

public class NativeScoreRunner {

    private boolean mIsRunning;

    public static NativeScoreRunner mNativeScoreRunner;
//    private OnKeyInfoListener mOnKeyInfoListener;

    public static NativeScoreRunner getInstance() {
        if (mNativeScoreRunner == null) {
            mNativeScoreRunner = new NativeScoreRunner();
        }
        return mNativeScoreRunner;
    }

//    public void setOnKeyInfoListener(OnKeyInfoListener listener) {
//        this.mOnKeyInfoListener = listener;
//    }

    public float getScore() {
        return NdkJniUtil.getScore();
    }

    public void setNotes(ArrayList<NoteInfo> notes) {
        NdkJniUtil.setNotes(notes);
//        if (mOnKeyInfoListener != null)
//            mOnKeyInfoListener.onOriginNotes(notes);
    }

//    private long mPreTime;

    public synchronized void start() {
        if (mIsRunning) {
            return;
        }
        mTasks.clear();
        mIsRunning = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                while (mIsRunning) {
                    if (mTasks.size() > 0) {
                        Task task = mTasks.remove(0);
                        if (mMode == 2) {
                            NdkJniUtil.getAnalyzeResult(task.getData(), task.getPosition(), task.getData().length);
                        } else {
                            NdkJniUtil.getAnalyzeResultEasy(task.getData(), task.getPosition(), task.getData().length);
                        }
//                        KeyInfo[] infos = mMode == 2 ? NdkJniUtil.getAnalyzeResult(task.getData(), task.getPosition(), task.getData().length)
//                                : NdkJniUtil.getAnalyzeResultEasy(task.getData(), task.getPosition(), task.getData().length);
//                        if (mOnKeyInfoListener != null && infos.length > 0) {
//                            mOnKeyInfoListener.onKeyInfoCallback(infos, 0);
//                        }
                    }
                }
                super.run();
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public void stop() {
        mTasks.clear();
        mIsRunning = false;
    }

    private int mMode;

    public void setScoreMode(int mode) {
        mMode = mode;
    }

    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());

//    private long mPreAddTaskTime;

    public void addData(double[] doubles, int position) {
//        long cur = System.currentTimeMillis();
//        Log.d("NativeScoreRunner", "use addData time:" + (cur - mPreAddTaskTime));
//        mPreAddTaskTime = cur;
        mTasks.add(new Task(doubles, position));
    }


    private class Task {
        private double[] datas;
        private int position;

        public Task(double[] datas, int position) {
            this.datas = datas.clone();
            this.position = position;
        }

        public double[] getData() {
            return datas;
        }

        public int getPosition() {
            return position;
        }
    }
}
