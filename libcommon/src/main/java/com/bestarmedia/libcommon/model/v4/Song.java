package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.SerializedName;

public class Song extends SongSimple {
    //拼音首字母
    @SerializedName("song_initial")
    public String songInitial;
    //歌曲类型id
    @SerializedName("song_type_id")
    public int songTypeId;
    //歌曲类型名称
    @SerializedName("song_type_name")
    public String songTypeName;
    //语种id
    @SerializedName("language_id")
    public int languageId;
    //语种名称
    @SerializedName("language_name")
    public String languageName;
    //媒体文件时长
    @SerializedName("duration")
    public int duration;
    //均衡音量值0-100
    @SerializedName("volume")
    public int volume;
    //原伴唱音轨、声道信息
    @SerializedName("audio_track")
    public int audioTrack;
    //状态，1正常使用，其他不正常
    @SerializedName("status")
    public int status;
    //客户端播放地址（客户端根据此字段判断是否需要下载）
    @SerializedName(value = "media_url")
    public String mediaUrl;
    //云端绝对地址（云下载使用此地址从云上下载）
    @SerializedName(value = "media_file_path")
    public String mediaFilePath;
    //文件MD5
    @SerializedName(value = "media_file_md5")
    public String mediaFileMd5;
    //本地文件相对路径
    @SerializedName(value = "local_media_file_path")
    public String localMediaFilePath;
    //评分坐标文件url（客户端根据此读取）
    @SerializedName(value = "coordinate_file_url")
    public String coordinateFileUrl;
    //评分坐标云端文件url（用于从云端下载）
    @SerializedName(value = "coordinate_file_path")
    public String coordinateFilePath;
    //评分坐标本地存放目录
    @SerializedName(value = "local_coordinate_file_path")
    public String localCoordinateFilePath;
    //评分标准文件url（客户端根据此读取）
    @SerializedName(value = "score_standard_file_url")
    public String scoreStandardFileUrl;
    //评分标准云端url（用于从云端下载）
    @SerializedName(value = "score_standard_file_path")
    public String scoreStandardFilePath;
    //评分标准本地存放目录
    @SerializedName(value = "local_score_standard_file_path")
    public String localScoreStandardFilePath;
    //作词者
    @SerializedName(value = "lyricist")
    public String lyricist;
    //作词者id
    @SerializedName(value = "lyricist_mid")
    public String lyricistMid;
    //作曲者
    @SerializedName(value = "composer")
    public String composer;
    //作曲者id
    @SerializedName(value = "composer_mid")
    public String composerMid;
    //
    @SerializedName(value = "litigant")
    public String litigant;

    @SerializedName(value = "litigant_mid")
    public String litigantMid;

    @SerializedName(value = "producer")
    public String producer;

    @SerializedName(value = "producer_mid")
    public String producerMid;

    @SerializedName(value = "publisher")
    public String publisher;

    @SerializedName(value = "publisher_mid")
    public String publisherMid;

}
