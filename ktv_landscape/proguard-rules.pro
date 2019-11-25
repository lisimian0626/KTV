# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose

-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keepattributes EnclosingMethod

#-libraryjars C:\Dev\Android\sdk\platforms\android-22\data\layoutlib.jar

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {  # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
 public static final android.os.Parcelable$Creator *;
}

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

################gson##################
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepattributes *Annotation*
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**


-keepattributes Signature

####################zxing#####################
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
# -dontwarn com.google.ads.**


 #eventbus
 -keepclassmembers class ** {
     public void onEvent*(**);
 }

 -keep public class * implements java.io.Serializable {*;}

 #model
 -keep class com.bestarmedia.libcommon.model.** { *; }
 #protoc
 -keep class com.bestarmedia.proto.** { *; }

 -keep class com.bestarmedia.libnetty.handler.** { *; }

 -keep class com.bestarmedia.libnetty.netty.ProtoHandlerInterface{*;}

 -keepattributes Signature,InnerClasses
 -keepclasseswithmembers class io.netty.** {
     *;
 }
 -dontwarn io.netty.**
 -dontwarn sun.**

 # Get rid of warnings about unreachable but unused classes referred to by Netty
 -dontwarn org.jboss.netty.**

 # Needed by commons logging
 -keep class org.apache.commons.logging.* {*;}

 #Some Factory that seemed to be pruned
 -keep class java.util.concurrent.atomic.AtomicReferenceFieldUpdater {*;}
 -keep class java.util.concurrent.atomic.AtomicReferenceFieldUpdaterImpl{*;}

 #Some important internal fields that where removed
 -keep class org.jboss.netty.channel.DefaultChannelPipeline{volatile <fields>;}

 #A Factory which has a static factory implementation selector which is pruned
 -keep class org.jboss.netty.util.internal.QueueFactory{static <fields>;}

 #Some fields whose names need to be maintained because they are accessed using inflection
 -keepclassmembernames class org.jboss.netty.util.internal.**{*;}

 -keep class com.beidousat.score.** { *; }

 -keep class com.beidousat.karaoke.widget.** { *; }
 -keep class com.beidousat.libwidget.** { *; }

-dontwarn com.squareup.okhttp.**

-dontwarn okio.**

-keep public class com.wwengine.hw.WWHandWrite {
    *;
}

#-libraryjars cxclasses.jar
#-libraryjars core-3.1.0.jar


#-libraryjars libs/cxclasses.jar
-dontwarn    android.media.*
-keep class  android.media.** { *;}
-dontwarn    android.os.*
-keep class  android.os.** { *;}

-dontwarn    android.view.*
-keep class  android.view.** { *;}

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep public class  com.beidousat.widget.handwritingjar.IHandwriting


# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# glide 的混淆代码
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 -keepattributes InnerClasses

 -keep class io.jsonwebtoken.** { *; }
 -keepnames class io.jsonwebtoken.* { *; }
 -keepnames interface io.jsonwebtoken.* { *; }

 -keep class org.bouncycastle.** { *; }
 -keepnames class org.bouncycastle.** { *; }
 -dontwarn org.bouncycastle.**

 -keep class com.serenegiant.**{*;}
 -keep class org.webrtc.**{*;}
 -keep class com.alivc.**{*;}

  # event bus
 -keepattributes *Annotation*
 -keepclassmembers class * {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }
 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

 -keep class com.squareup.wire.** { *; }
 -keep class com.opensource.svgaplayer.proto.** { *; }