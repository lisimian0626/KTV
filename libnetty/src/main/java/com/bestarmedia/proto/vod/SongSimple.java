// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

/**
 * <pre>
 *已点已唱歌曲信息
 * </pre>
 *
 * Protobuf type {@code SongSimple}
 */
public  final class SongSimple extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:SongSimple)
    SongSimpleOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SongSimple.newBuilder() to construct.
  private SongSimple(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SongSimple() {
    uuid_ = "";
    songId_ = "";
    songName_ = "";
    singerName_ = "";
    videoType_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private SongSimple(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            String s = input.readStringRequireUtf8();

            uuid_ = s;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            songId_ = s;
            break;
          }
          case 26: {
            String s = input.readStringRequireUtf8();

            songName_ = s;
            break;
          }
          case 34: {
            String s = input.readStringRequireUtf8();

            singerName_ = s;
            break;
          }
          case 42: {
            String s = input.readStringRequireUtf8();

            videoType_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return BoxProto.internal_static_SongSimple_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return BoxProto.internal_static_SongSimple_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            SongSimple.class, Builder.class);
  }

  public static final int UUID_FIELD_NUMBER = 1;
  private volatile Object uuid_;
  /**
   * <pre>
   * 歌曲记录ID
   * </pre>
   *
   * <code>string uuid = 1;</code>
   */
  public String getUuid() {
    Object ref = uuid_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      uuid_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 歌曲记录ID
   * </pre>
   *
   * <code>string uuid = 1;</code>
   */
  public com.google.protobuf.ByteString
      getUuidBytes() {
    Object ref = uuid_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      uuid_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SONG_ID_FIELD_NUMBER = 2;
  private volatile Object songId_;
  /**
   * <pre>
   * 歌曲ID
   * </pre>
   *
   * <code>string song_id = 2;</code>
   */
  public String getSongId() {
    Object ref = songId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      songId_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 歌曲ID
   * </pre>
   *
   * <code>string song_id = 2;</code>
   */
  public com.google.protobuf.ByteString
      getSongIdBytes() {
    Object ref = songId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      songId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SONG_NAME_FIELD_NUMBER = 3;
  private volatile Object songName_;
  /**
   * <pre>
   * 歌曲名称
   * </pre>
   *
   * <code>string song_name = 3;</code>
   */
  public String getSongName() {
    Object ref = songName_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      songName_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 歌曲名称
   * </pre>
   *
   * <code>string song_name = 3;</code>
   */
  public com.google.protobuf.ByteString
      getSongNameBytes() {
    Object ref = songName_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      songName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SINGER_NAME_FIELD_NUMBER = 4;
  private volatile Object singerName_;
  /**
   * <pre>
   * 歌手
   * </pre>
   *
   * <code>string singer_name = 4;</code>
   */
  public String getSingerName() {
    Object ref = singerName_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      singerName_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 歌手
   * </pre>
   *
   * <code>string singer_name = 4;</code>
   */
  public com.google.protobuf.ByteString
      getSingerNameBytes() {
    Object ref = singerName_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      singerName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int VIDEO_TYPE_FIELD_NUMBER = 5;
  private volatile Object videoType_;
  /**
   * <pre>
   * 歌曲版本
   * </pre>
   *
   * <code>string video_type = 5;</code>
   */
  public String getVideoType() {
    Object ref = videoType_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      videoType_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 歌曲版本
   * </pre>
   *
   * <code>string video_type = 5;</code>
   */
  public com.google.protobuf.ByteString
      getVideoTypeBytes() {
    Object ref = videoType_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      videoType_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getUuidBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, uuid_);
    }
    if (!getSongIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, songId_);
    }
    if (!getSongNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, songName_);
    }
    if (!getSingerNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, singerName_);
    }
    if (!getVideoTypeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, videoType_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getUuidBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, uuid_);
    }
    if (!getSongIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, songId_);
    }
    if (!getSongNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, songName_);
    }
    if (!getSingerNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, singerName_);
    }
    if (!getVideoTypeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, videoType_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof SongSimple)) {
      return super.equals(obj);
    }
    SongSimple other = (SongSimple) obj;

    if (!getUuid()
        .equals(other.getUuid())) return false;
    if (!getSongId()
        .equals(other.getSongId())) return false;
    if (!getSongName()
        .equals(other.getSongName())) return false;
    if (!getSingerName()
        .equals(other.getSingerName())) return false;
    if (!getVideoType()
        .equals(other.getVideoType())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + UUID_FIELD_NUMBER;
    hash = (53 * hash) + getUuid().hashCode();
    hash = (37 * hash) + SONG_ID_FIELD_NUMBER;
    hash = (53 * hash) + getSongId().hashCode();
    hash = (37 * hash) + SONG_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getSongName().hashCode();
    hash = (37 * hash) + SINGER_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getSingerName().hashCode();
    hash = (37 * hash) + VIDEO_TYPE_FIELD_NUMBER;
    hash = (53 * hash) + getVideoType().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static SongSimple parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SongSimple parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SongSimple parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SongSimple parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SongSimple parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static SongSimple parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static SongSimple parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SongSimple parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static SongSimple parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static SongSimple parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static SongSimple parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static SongSimple parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(SongSimple prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   *已点已唱歌曲信息
   * </pre>
   *
   * Protobuf type {@code SongSimple}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:SongSimple)
      SongSimpleOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return BoxProto.internal_static_SongSimple_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return BoxProto.internal_static_SongSimple_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              SongSimple.class, Builder.class);
    }

    // Construct using com.bestarmedia.proto.vod.SongSimple.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      uuid_ = "";

      songId_ = "";

      songName_ = "";

      singerName_ = "";

      videoType_ = "";

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return BoxProto.internal_static_SongSimple_descriptor;
    }

    @Override
    public SongSimple getDefaultInstanceForType() {
      return SongSimple.getDefaultInstance();
    }

    @Override
    public SongSimple build() {
      SongSimple result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public SongSimple buildPartial() {
      SongSimple result = new SongSimple(this);
      result.uuid_ = uuid_;
      result.songId_ = songId_;
      result.songName_ = songName_;
      result.singerName_ = singerName_;
      result.videoType_ = videoType_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof SongSimple) {
        return mergeFrom((SongSimple)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(SongSimple other) {
      if (other == SongSimple.getDefaultInstance()) return this;
      if (!other.getUuid().isEmpty()) {
        uuid_ = other.uuid_;
        onChanged();
      }
      if (!other.getSongId().isEmpty()) {
        songId_ = other.songId_;
        onChanged();
      }
      if (!other.getSongName().isEmpty()) {
        songName_ = other.songName_;
        onChanged();
      }
      if (!other.getSingerName().isEmpty()) {
        singerName_ = other.singerName_;
        onChanged();
      }
      if (!other.getVideoType().isEmpty()) {
        videoType_ = other.videoType_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      SongSimple parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (SongSimple) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object uuid_ = "";
    /**
     * <pre>
     * 歌曲记录ID
     * </pre>
     *
     * <code>string uuid = 1;</code>
     */
    public String getUuid() {
      Object ref = uuid_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        uuid_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * 歌曲记录ID
     * </pre>
     *
     * <code>string uuid = 1;</code>
     */
    public com.google.protobuf.ByteString
        getUuidBytes() {
      Object ref = uuid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        uuid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 歌曲记录ID
     * </pre>
     *
     * <code>string uuid = 1;</code>
     */
    public Builder setUuid(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      uuid_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲记录ID
     * </pre>
     *
     * <code>string uuid = 1;</code>
     */
    public Builder clearUuid() {
      
      uuid_ = getDefaultInstance().getUuid();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲记录ID
     * </pre>
     *
     * <code>string uuid = 1;</code>
     */
    public Builder setUuidBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      uuid_ = value;
      onChanged();
      return this;
    }

    private Object songId_ = "";
    /**
     * <pre>
     * 歌曲ID
     * </pre>
     *
     * <code>string song_id = 2;</code>
     */
    public String getSongId() {
      Object ref = songId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        songId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * 歌曲ID
     * </pre>
     *
     * <code>string song_id = 2;</code>
     */
    public com.google.protobuf.ByteString
        getSongIdBytes() {
      Object ref = songId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        songId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 歌曲ID
     * </pre>
     *
     * <code>string song_id = 2;</code>
     */
    public Builder setSongId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      songId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲ID
     * </pre>
     *
     * <code>string song_id = 2;</code>
     */
    public Builder clearSongId() {
      
      songId_ = getDefaultInstance().getSongId();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲ID
     * </pre>
     *
     * <code>string song_id = 2;</code>
     */
    public Builder setSongIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      songId_ = value;
      onChanged();
      return this;
    }

    private Object songName_ = "";
    /**
     * <pre>
     * 歌曲名称
     * </pre>
     *
     * <code>string song_name = 3;</code>
     */
    public String getSongName() {
      Object ref = songName_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        songName_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * 歌曲名称
     * </pre>
     *
     * <code>string song_name = 3;</code>
     */
    public com.google.protobuf.ByteString
        getSongNameBytes() {
      Object ref = songName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        songName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 歌曲名称
     * </pre>
     *
     * <code>string song_name = 3;</code>
     */
    public Builder setSongName(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      songName_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲名称
     * </pre>
     *
     * <code>string song_name = 3;</code>
     */
    public Builder clearSongName() {
      
      songName_ = getDefaultInstance().getSongName();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲名称
     * </pre>
     *
     * <code>string song_name = 3;</code>
     */
    public Builder setSongNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      songName_ = value;
      onChanged();
      return this;
    }

    private Object singerName_ = "";
    /**
     * <pre>
     * 歌手
     * </pre>
     *
     * <code>string singer_name = 4;</code>
     */
    public String getSingerName() {
      Object ref = singerName_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        singerName_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * 歌手
     * </pre>
     *
     * <code>string singer_name = 4;</code>
     */
    public com.google.protobuf.ByteString
        getSingerNameBytes() {
      Object ref = singerName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        singerName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 歌手
     * </pre>
     *
     * <code>string singer_name = 4;</code>
     */
    public Builder setSingerName(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      singerName_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌手
     * </pre>
     *
     * <code>string singer_name = 4;</code>
     */
    public Builder clearSingerName() {
      
      singerName_ = getDefaultInstance().getSingerName();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌手
     * </pre>
     *
     * <code>string singer_name = 4;</code>
     */
    public Builder setSingerNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      singerName_ = value;
      onChanged();
      return this;
    }

    private Object videoType_ = "";
    /**
     * <pre>
     * 歌曲版本
     * </pre>
     *
     * <code>string video_type = 5;</code>
     */
    public String getVideoType() {
      Object ref = videoType_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        videoType_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * 歌曲版本
     * </pre>
     *
     * <code>string video_type = 5;</code>
     */
    public com.google.protobuf.ByteString
        getVideoTypeBytes() {
      Object ref = videoType_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        videoType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 歌曲版本
     * </pre>
     *
     * <code>string video_type = 5;</code>
     */
    public Builder setVideoType(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      videoType_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲版本
     * </pre>
     *
     * <code>string video_type = 5;</code>
     */
    public Builder clearVideoType() {
      
      videoType_ = getDefaultInstance().getVideoType();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 歌曲版本
     * </pre>
     *
     * <code>string video_type = 5;</code>
     */
    public Builder setVideoTypeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      videoType_ = value;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:SongSimple)
  }

  // @@protoc_insertion_point(class_scope:SongSimple)
  private static final SongSimple DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new SongSimple();
  }

  public static SongSimple getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SongSimple>
      PARSER = new com.google.protobuf.AbstractParser<SongSimple>() {
    @Override
    public SongSimple parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new SongSimple(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<SongSimple> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<SongSimple> getParserForType() {
    return PARSER;
  }

  @Override
  public SongSimple getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

