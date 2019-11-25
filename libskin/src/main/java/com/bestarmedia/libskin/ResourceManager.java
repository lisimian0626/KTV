package com.bestarmedia.libskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bestarmedia.libskin.utils.L;

/**
 * Created by J Wong on 2017/1/17.
 */

public class ResourceManager {
    private static final String DEFTYPE_DRAWABLE = "drawable";
    private static final String DEFTYPE_COLOR = "color";
    private static final String DEFTYPE_STRING = "string";
    private Resources mResources;
    private String mPluginPackageName;
    private String mSuffix;


    public ResourceManager(Resources res, String pluginPackageName, String suffix) {
        mResources = res;
        mPluginPackageName = pluginPackageName;
        if (suffix == null) {
            suffix = "";
        }
        mSuffix = suffix;
    }

    public Drawable getDrawableByName(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getDrawable(mResources.getIdentifier(name, DEFTYPE_DRAWABLE, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            L.e("getDrawableByName ex:" + e.toString());
            return null;
        }
    }

    public int getColor(String name) throws Resources.NotFoundException {
        name = appendSuffix(name);
        return mResources.getColor(mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName));
    }

    public String getString(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getString(mResources.getIdentifier(name, DEFTYPE_STRING, mPluginPackageName));
        } catch (Exception e) {
            L.e("getString ex:" + e.toString());
//            e.printStackTrace();
        }
        return null;
    }

    public ColorStateList getColorStateList(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getColorStateList(mResources.getIdentifier(name, DEFTYPE_COLOR, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            L.e("getColorStateList ex:" + e.toString());
            return null;
        }

    }

    public ColorStateList getColorStateList2(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getColorStateList(mResources.getIdentifier(name, DEFTYPE_DRAWABLE, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            L.e("getColorStateList2 ex:" + e.toString());
            return null;
        }
    }

    private String appendSuffix(String name) {
        if (!TextUtils.isEmpty(mSuffix))
            return name += "_" + mSuffix;
        return name;
    }

}
