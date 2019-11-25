package com.bestarmedia.libcommon.transport;

import com.bestarmedia.libcommon.model.vod.Material;

import java.io.File;

/**
 * Created by J Wong on 2016/6/22.
 */
public interface FileUploadListener {

    void onUploadStart(File file);

    void onUploading(File file, float progress);

    void onUploadCompletion(File file, Material material);

    void onUploadFailure(File file, String error);

}
