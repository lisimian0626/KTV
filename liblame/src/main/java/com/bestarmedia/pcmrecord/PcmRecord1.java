package com.bestarmedia.pcmrecord;

import android.util.Log;

public class PcmRecord1 {
    private Object mInstance;

    public PcmRecord1(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat,
                      int bufferSizeInBytes) {
        try {
            mInstance = Invoke.newInstance("android.media.PcmRecord",
                    new Object[]{audioSource, sampleRateInHz,
                            channelConfig, audioFormat, bufferSizeInBytes},
                    new Class[]{int.class, int.class,
                            int.class, int.class, int.class});
        } catch (Exception e) {
            e.printStackTrace();
            mInstance = null;
        }
    }

    public void startRecording() {
        if (mInstance == null) {
            Log.d("PcmRecord1", "startRecording mInstance is null");
            return;
        }
        Invoke.invokeMethod(mInstance, "startRecording", new Object[]{});
    }

    public int read(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        if (mInstance == null) {
            Log.d("PcmRecord1", "read mInstance is null");
            return 0;
        }
        Object obj = Invoke.invokeMethod(mInstance, "read", new Object[]{audioData, offsetInBytes, sizeInBytes},
                new Class[]{byte[].class, int.class, int.class});
        if (obj != null) {
            return (Integer) obj;
        }
        return 0;
    }

    public void release() {
        if (mInstance == null) {
            Log.d("PcmRecord1", "release mInstance is null");
            return;
        }
        Invoke.invokeMethod(mInstance, "release", new Object[]{});
    }
}
