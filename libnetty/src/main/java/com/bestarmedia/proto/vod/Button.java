// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

/**
 * Protobuf type {@code Button}
 */
public  final class Button extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Button)
    ButtonOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Button.newBuilder() to construct.
  private Button(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Button() {
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Button(
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
          case 8: {

            isHdmiBlack_ = input.readBool();
            break;
          }
          case 16: {

            isLightAuto_ = input.readBool();
            break;
          }
          case 24: {

            isMute_ = input.readBool();
            break;
          }
          case 32: {

            isPause_ = input.readBool();
            break;
          }
          case 40: {

            isRecord_ = input.readBool();
            break;
          }
          case 48: {

            isServing_ = input.readBool();
            break;
          }
          case 56: {

            lightMode_ = input.readInt32();
            break;
          }
          case 64: {

            originOn_ = input.readBool();
            break;
          }
          case 72: {

            scoreMode_ = input.readInt32();
            break;
          }
          case 80: {

            serviceMode_ = input.readInt32();
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
    return BoxProto.internal_static_Button_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return BoxProto.internal_static_Button_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            Button.class, Builder.class);
  }

  public static final int IS_HDMI_BLACK_FIELD_NUMBER = 1;
  private boolean isHdmiBlack_;
  /**
   * <pre>
   * 电视是否黑屏
   * </pre>
   *
   * <code>bool is_hdmi_black = 1;</code>
   */
  public boolean getIsHdmiBlack() {
    return isHdmiBlack_;
  }

  public static final int IS_LIGHT_AUTO_FIELD_NUMBER = 2;
  private boolean isLightAuto_;
  /**
   * <pre>
   * 智能灯光是否开启
   * </pre>
   *
   * <code>bool is_light_auto = 2;</code>
   */
  public boolean getIsLightAuto() {
    return isLightAuto_;
  }

  public static final int IS_MUTE_FIELD_NUMBER = 3;
  private boolean isMute_;
  /**
   * <pre>
   * 是否静音
   * </pre>
   *
   * <code>bool is_mute = 3;</code>
   */
  public boolean getIsMute() {
    return isMute_;
  }

  public static final int IS_PAUSE_FIELD_NUMBER = 4;
  private boolean isPause_;
  /**
   * <pre>
   * 是否暂停
   * </pre>
   *
   * <code>bool is_pause = 4;</code>
   */
  public boolean getIsPause() {
    return isPause_;
  }

  public static final int IS_RECORD_FIELD_NUMBER = 5;
  private boolean isRecord_;
  /**
   * <pre>
   * 是否开启录音
   * </pre>
   *
   * <code>bool is_record = 5;</code>
   */
  public boolean getIsRecord() {
    return isRecord_;
  }

  public static final int IS_SERVING_FIELD_NUMBER = 6;
  private boolean isServing_;
  /**
   * <pre>
   * 是否服务铃响应
   * </pre>
   *
   * <code>bool is_serving = 6;</code>
   */
  public boolean getIsServing() {
    return isServing_;
  }

  public static final int LIGHT_MODE_FIELD_NUMBER = 7;
  private int lightMode_;
  /**
   * <pre>
   * 灯光模式
   * </pre>
   *
   * <code>int32 light_mode = 7;</code>
   */
  public int getLightMode() {
    return lightMode_;
  }

  public static final int ORIGIN_ON_FIELD_NUMBER = 8;
  private boolean originOn_;
  /**
   * <pre>
   * 是否开启原唱
   * </pre>
   *
   * <code>bool origin_on = 8;</code>
   */
  public boolean getOriginOn() {
    return originOn_;
  }

  public static final int SCORE_MODE_FIELD_NUMBER = 9;
  private int scoreMode_;
  /**
   * <pre>
   * 评分模式 0关闭 1娱乐 2专业
   * </pre>
   *
   * <code>int32 score_mode = 9;</code>
   */
  public int getScoreMode() {
    return scoreMode_;
  }

  public static final int SERVICE_MODE_FIELD_NUMBER = 10;
  private int serviceMode_;
  /**
   * <pre>
   * 服务铃模式 -1无呼叫 0服务员 1DJ 2清洁 3保安 4买单 5催单 6唛套 7加冰
   * </pre>
   *
   * <code>int32 service_mode = 10;</code>
   */
  public int getServiceMode() {
    return serviceMode_;
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
    if (isHdmiBlack_ != false) {
      output.writeBool(1, isHdmiBlack_);
    }
    if (isLightAuto_ != false) {
      output.writeBool(2, isLightAuto_);
    }
    if (isMute_ != false) {
      output.writeBool(3, isMute_);
    }
    if (isPause_ != false) {
      output.writeBool(4, isPause_);
    }
    if (isRecord_ != false) {
      output.writeBool(5, isRecord_);
    }
    if (isServing_ != false) {
      output.writeBool(6, isServing_);
    }
    if (lightMode_ != 0) {
      output.writeInt32(7, lightMode_);
    }
    if (originOn_ != false) {
      output.writeBool(8, originOn_);
    }
    if (scoreMode_ != 0) {
      output.writeInt32(9, scoreMode_);
    }
    if (serviceMode_ != 0) {
      output.writeInt32(10, serviceMode_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (isHdmiBlack_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(1, isHdmiBlack_);
    }
    if (isLightAuto_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, isLightAuto_);
    }
    if (isMute_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, isMute_);
    }
    if (isPause_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(4, isPause_);
    }
    if (isRecord_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(5, isRecord_);
    }
    if (isServing_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(6, isServing_);
    }
    if (lightMode_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(7, lightMode_);
    }
    if (originOn_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(8, originOn_);
    }
    if (scoreMode_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(9, scoreMode_);
    }
    if (serviceMode_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(10, serviceMode_);
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
    if (!(obj instanceof Button)) {
      return super.equals(obj);
    }
    Button other = (Button) obj;

    if (getIsHdmiBlack()
        != other.getIsHdmiBlack()) return false;
    if (getIsLightAuto()
        != other.getIsLightAuto()) return false;
    if (getIsMute()
        != other.getIsMute()) return false;
    if (getIsPause()
        != other.getIsPause()) return false;
    if (getIsRecord()
        != other.getIsRecord()) return false;
    if (getIsServing()
        != other.getIsServing()) return false;
    if (getLightMode()
        != other.getLightMode()) return false;
    if (getOriginOn()
        != other.getOriginOn()) return false;
    if (getScoreMode()
        != other.getScoreMode()) return false;
    if (getServiceMode()
        != other.getServiceMode()) return false;
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
    hash = (37 * hash) + IS_HDMI_BLACK_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsHdmiBlack());
    hash = (37 * hash) + IS_LIGHT_AUTO_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsLightAuto());
    hash = (37 * hash) + IS_MUTE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsMute());
    hash = (37 * hash) + IS_PAUSE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsPause());
    hash = (37 * hash) + IS_RECORD_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsRecord());
    hash = (37 * hash) + IS_SERVING_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsServing());
    hash = (37 * hash) + LIGHT_MODE_FIELD_NUMBER;
    hash = (53 * hash) + getLightMode();
    hash = (37 * hash) + ORIGIN_ON_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getOriginOn());
    hash = (37 * hash) + SCORE_MODE_FIELD_NUMBER;
    hash = (53 * hash) + getScoreMode();
    hash = (37 * hash) + SERVICE_MODE_FIELD_NUMBER;
    hash = (53 * hash) + getServiceMode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static Button parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Button parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Button parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Button parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Button parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static Button parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static Button parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Button parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static Button parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static Button parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static Button parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static Button parseFrom(
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
  public static Builder newBuilder(Button prototype) {
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
   * Protobuf type {@code Button}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Button)
      com.bestarmedia.proto.vod.ButtonOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return BoxProto.internal_static_Button_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return BoxProto.internal_static_Button_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Button.class, Builder.class);
    }

    // Construct using com.bestarmedia.proto.vod.Button.newBuilder()
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
      isHdmiBlack_ = false;

      isLightAuto_ = false;

      isMute_ = false;

      isPause_ = false;

      isRecord_ = false;

      isServing_ = false;

      lightMode_ = 0;

      originOn_ = false;

      scoreMode_ = 0;

      serviceMode_ = 0;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return BoxProto.internal_static_Button_descriptor;
    }

    @Override
    public Button getDefaultInstanceForType() {
      return Button.getDefaultInstance();
    }

    @Override
    public Button build() {
      Button result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public Button buildPartial() {
      Button result = new Button(this);
      result.isHdmiBlack_ = isHdmiBlack_;
      result.isLightAuto_ = isLightAuto_;
      result.isMute_ = isMute_;
      result.isPause_ = isPause_;
      result.isRecord_ = isRecord_;
      result.isServing_ = isServing_;
      result.lightMode_ = lightMode_;
      result.originOn_ = originOn_;
      result.scoreMode_ = scoreMode_;
      result.serviceMode_ = serviceMode_;
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
      if (other instanceof Button) {
        return mergeFrom((Button)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(Button other) {
      if (other == Button.getDefaultInstance()) return this;
      if (other.getIsHdmiBlack() != false) {
        setIsHdmiBlack(other.getIsHdmiBlack());
      }
      if (other.getIsLightAuto() != false) {
        setIsLightAuto(other.getIsLightAuto());
      }
      if (other.getIsMute() != false) {
        setIsMute(other.getIsMute());
      }
      if (other.getIsPause() != false) {
        setIsPause(other.getIsPause());
      }
      if (other.getIsRecord() != false) {
        setIsRecord(other.getIsRecord());
      }
      if (other.getIsServing() != false) {
        setIsServing(other.getIsServing());
      }
      if (other.getLightMode() != 0) {
        setLightMode(other.getLightMode());
      }
      if (other.getOriginOn() != false) {
        setOriginOn(other.getOriginOn());
      }
      if (other.getScoreMode() != 0) {
        setScoreMode(other.getScoreMode());
      }
      if (other.getServiceMode() != 0) {
        setServiceMode(other.getServiceMode());
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
      Button parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (Button) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private boolean isHdmiBlack_ ;
    /**
     * <pre>
     * 电视是否黑屏
     * </pre>
     *
     * <code>bool is_hdmi_black = 1;</code>
     */
    public boolean getIsHdmiBlack() {
      return isHdmiBlack_;
    }
    /**
     * <pre>
     * 电视是否黑屏
     * </pre>
     *
     * <code>bool is_hdmi_black = 1;</code>
     */
    public Builder setIsHdmiBlack(boolean value) {
      
      isHdmiBlack_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 电视是否黑屏
     * </pre>
     *
     * <code>bool is_hdmi_black = 1;</code>
     */
    public Builder clearIsHdmiBlack() {
      
      isHdmiBlack_ = false;
      onChanged();
      return this;
    }

    private boolean isLightAuto_ ;
    /**
     * <pre>
     * 智能灯光是否开启
     * </pre>
     *
     * <code>bool is_light_auto = 2;</code>
     */
    public boolean getIsLightAuto() {
      return isLightAuto_;
    }
    /**
     * <pre>
     * 智能灯光是否开启
     * </pre>
     *
     * <code>bool is_light_auto = 2;</code>
     */
    public Builder setIsLightAuto(boolean value) {
      
      isLightAuto_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 智能灯光是否开启
     * </pre>
     *
     * <code>bool is_light_auto = 2;</code>
     */
    public Builder clearIsLightAuto() {
      
      isLightAuto_ = false;
      onChanged();
      return this;
    }

    private boolean isMute_ ;
    /**
     * <pre>
     * 是否静音
     * </pre>
     *
     * <code>bool is_mute = 3;</code>
     */
    public boolean getIsMute() {
      return isMute_;
    }
    /**
     * <pre>
     * 是否静音
     * </pre>
     *
     * <code>bool is_mute = 3;</code>
     */
    public Builder setIsMute(boolean value) {
      
      isMute_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 是否静音
     * </pre>
     *
     * <code>bool is_mute = 3;</code>
     */
    public Builder clearIsMute() {
      
      isMute_ = false;
      onChanged();
      return this;
    }

    private boolean isPause_ ;
    /**
     * <pre>
     * 是否暂停
     * </pre>
     *
     * <code>bool is_pause = 4;</code>
     */
    public boolean getIsPause() {
      return isPause_;
    }
    /**
     * <pre>
     * 是否暂停
     * </pre>
     *
     * <code>bool is_pause = 4;</code>
     */
    public Builder setIsPause(boolean value) {
      
      isPause_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 是否暂停
     * </pre>
     *
     * <code>bool is_pause = 4;</code>
     */
    public Builder clearIsPause() {
      
      isPause_ = false;
      onChanged();
      return this;
    }

    private boolean isRecord_ ;
    /**
     * <pre>
     * 是否开启录音
     * </pre>
     *
     * <code>bool is_record = 5;</code>
     */
    public boolean getIsRecord() {
      return isRecord_;
    }
    /**
     * <pre>
     * 是否开启录音
     * </pre>
     *
     * <code>bool is_record = 5;</code>
     */
    public Builder setIsRecord(boolean value) {
      
      isRecord_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 是否开启录音
     * </pre>
     *
     * <code>bool is_record = 5;</code>
     */
    public Builder clearIsRecord() {
      
      isRecord_ = false;
      onChanged();
      return this;
    }

    private boolean isServing_ ;
    /**
     * <pre>
     * 是否服务铃响应
     * </pre>
     *
     * <code>bool is_serving = 6;</code>
     */
    public boolean getIsServing() {
      return isServing_;
    }
    /**
     * <pre>
     * 是否服务铃响应
     * </pre>
     *
     * <code>bool is_serving = 6;</code>
     */
    public Builder setIsServing(boolean value) {
      
      isServing_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 是否服务铃响应
     * </pre>
     *
     * <code>bool is_serving = 6;</code>
     */
    public Builder clearIsServing() {
      
      isServing_ = false;
      onChanged();
      return this;
    }

    private int lightMode_ ;
    /**
     * <pre>
     * 灯光模式
     * </pre>
     *
     * <code>int32 light_mode = 7;</code>
     */
    public int getLightMode() {
      return lightMode_;
    }
    /**
     * <pre>
     * 灯光模式
     * </pre>
     *
     * <code>int32 light_mode = 7;</code>
     */
    public Builder setLightMode(int value) {
      
      lightMode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 灯光模式
     * </pre>
     *
     * <code>int32 light_mode = 7;</code>
     */
    public Builder clearLightMode() {
      
      lightMode_ = 0;
      onChanged();
      return this;
    }

    private boolean originOn_ ;
    /**
     * <pre>
     * 是否开启原唱
     * </pre>
     *
     * <code>bool origin_on = 8;</code>
     */
    public boolean getOriginOn() {
      return originOn_;
    }
    /**
     * <pre>
     * 是否开启原唱
     * </pre>
     *
     * <code>bool origin_on = 8;</code>
     */
    public Builder setOriginOn(boolean value) {
      
      originOn_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 是否开启原唱
     * </pre>
     *
     * <code>bool origin_on = 8;</code>
     */
    public Builder clearOriginOn() {
      
      originOn_ = false;
      onChanged();
      return this;
    }

    private int scoreMode_ ;
    /**
     * <pre>
     * 评分模式 0关闭 1娱乐 2专业
     * </pre>
     *
     * <code>int32 score_mode = 9;</code>
     */
    public int getScoreMode() {
      return scoreMode_;
    }
    /**
     * <pre>
     * 评分模式 0关闭 1娱乐 2专业
     * </pre>
     *
     * <code>int32 score_mode = 9;</code>
     */
    public Builder setScoreMode(int value) {
      
      scoreMode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 评分模式 0关闭 1娱乐 2专业
     * </pre>
     *
     * <code>int32 score_mode = 9;</code>
     */
    public Builder clearScoreMode() {
      
      scoreMode_ = 0;
      onChanged();
      return this;
    }

    private int serviceMode_ ;
    /**
     * <pre>
     * 服务铃模式 -1无呼叫 0服务员 1DJ 2清洁 3保安 4买单 5催单 6唛套 7加冰
     * </pre>
     *
     * <code>int32 service_mode = 10;</code>
     */
    public int getServiceMode() {
      return serviceMode_;
    }
    /**
     * <pre>
     * 服务铃模式 -1无呼叫 0服务员 1DJ 2清洁 3保安 4买单 5催单 6唛套 7加冰
     * </pre>
     *
     * <code>int32 service_mode = 10;</code>
     */
    public Builder setServiceMode(int value) {
      
      serviceMode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 服务铃模式 -1无呼叫 0服务员 1DJ 2清洁 3保安 4买单 5催单 6唛套 7加冰
     * </pre>
     *
     * <code>int32 service_mode = 10;</code>
     */
    public Builder clearServiceMode() {
      
      serviceMode_ = 0;
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


    // @@protoc_insertion_point(builder_scope:Button)
  }

  // @@protoc_insertion_point(class_scope:Button)
  private static final Button DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new Button();
  }

  public static Button getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Button>
      PARSER = new com.google.protobuf.AbstractParser<Button>() {
    @Override
    public Button parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Button(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Button> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<Button> getParserForType() {
    return PARSER;
  }

  @Override
  public Button getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

