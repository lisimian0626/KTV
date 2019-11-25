package com.bestarmedia.libcommon.model.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileUploadDTO {
    @SerializedName("file")
    public String file;
    @SerializedName("folder")
    public String folder;
    @SerializedName("cut_size")
    public List<CutSize> cutSizeList;

    public FileUploadDTO(String file, String folder, List<CutSize> sizes) {
        this.file = file;
        this.folder = folder;
        this.cutSizeList = sizes;
    }

    public static class CutSize {
        @SerializedName("width")
        int width;
        @SerializedName("height")
        int height;

        public CutSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
