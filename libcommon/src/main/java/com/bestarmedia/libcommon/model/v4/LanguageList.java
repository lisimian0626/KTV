package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageList {
    @Expose
    @SerializedName("language")
    public List<Language> languages;
}
