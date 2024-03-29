// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

/**
 * <pre>
 * 机顶盒截图请求
 * </pre>
 *
 * Protobuf type {@code BoxImageCutRequest}
 */
public  final class BoxImageCutRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:BoxImageCutRequest)
    BoxImageCutRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use BoxImageCutRequest.newBuilder() to construct.
  private BoxImageCutRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private BoxImageCutRequest() {
    ktvNetCode_ = "";
    roomCode_ = "";
    userId_ = "";
    address_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private BoxImageCutRequest(
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

            ktvNetCode_ = s;
            break;
          }
          case 18: {
            String s = input.readStringRequireUtf8();

            roomCode_ = s;
            break;
          }
          case 26: {
            String s = input.readStringRequireUtf8();

            userId_ = s;
            break;
          }
          case 34: {
            String s = input.readStringRequireUtf8();

            address_ = s;
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
    return com.bestarmedia.proto.vod.BoxProto.internal_static_BoxImageCutRequest_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.bestarmedia.proto.vod.BoxProto.internal_static_BoxImageCutRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            BoxImageCutRequest.class, Builder.class);
  }

  public static final int KTV_NET_CODE_FIELD_NUMBER = 1;
  private volatile Object ktvNetCode_;
  /**
   * <code>string ktv_net_code = 1;</code>
   */
  public String getKtvNetCode() {
    Object ref = ktvNetCode_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      ktvNetCode_ = s;
      return s;
    }
  }
  /**
   * <code>string ktv_net_code = 1;</code>
   */
  public com.google.protobuf.ByteString
      getKtvNetCodeBytes() {
    Object ref = ktvNetCode_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      ktvNetCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ROOM_CODE_FIELD_NUMBER = 2;
  private volatile Object roomCode_;
  /**
   * <code>string room_code = 2;</code>
   */
  public String getRoomCode() {
    Object ref = roomCode_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      roomCode_ = s;
      return s;
    }
  }
  /**
   * <code>string room_code = 2;</code>
   */
  public com.google.protobuf.ByteString
      getRoomCodeBytes() {
    Object ref = roomCode_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      roomCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int USER_ID_FIELD_NUMBER = 3;
  private volatile Object userId_;
  /**
   * <code>string user_id = 3;</code>
   */
  public String getUserId() {
    Object ref = userId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      userId_ = s;
      return s;
    }
  }
  /**
   * <code>string user_id = 3;</code>
   */
  public com.google.protobuf.ByteString
      getUserIdBytes() {
    Object ref = userId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      userId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ADDRESS_FIELD_NUMBER = 4;
  private volatile Object address_;
  /**
   * <code>string address = 4;</code>
   */
  public String getAddress() {
    Object ref = address_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      address_ = s;
      return s;
    }
  }
  /**
   * <code>string address = 4;</code>
   */
  public com.google.protobuf.ByteString
      getAddressBytes() {
    Object ref = address_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      address_ = b;
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
    if (!getKtvNetCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, ktvNetCode_);
    }
    if (!getRoomCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, roomCode_);
    }
    if (!getUserIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, userId_);
    }
    if (!getAddressBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, address_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getKtvNetCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, ktvNetCode_);
    }
    if (!getRoomCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, roomCode_);
    }
    if (!getUserIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, userId_);
    }
    if (!getAddressBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, address_);
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
    if (!(obj instanceof BoxImageCutRequest)) {
      return super.equals(obj);
    }
    BoxImageCutRequest other = (BoxImageCutRequest) obj;

    if (!getKtvNetCode()
        .equals(other.getKtvNetCode())) return false;
    if (!getRoomCode()
        .equals(other.getRoomCode())) return false;
    if (!getUserId()
        .equals(other.getUserId())) return false;
    if (!getAddress()
        .equals(other.getAddress())) return false;
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
    hash = (37 * hash) + KTV_NET_CODE_FIELD_NUMBER;
    hash = (53 * hash) + getKtvNetCode().hashCode();
    hash = (37 * hash) + ROOM_CODE_FIELD_NUMBER;
    hash = (53 * hash) + getRoomCode().hashCode();
    hash = (37 * hash) + USER_ID_FIELD_NUMBER;
    hash = (53 * hash) + getUserId().hashCode();
    hash = (37 * hash) + ADDRESS_FIELD_NUMBER;
    hash = (53 * hash) + getAddress().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static BoxImageCutRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BoxImageCutRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BoxImageCutRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BoxImageCutRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BoxImageCutRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static BoxImageCutRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static BoxImageCutRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BoxImageCutRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static BoxImageCutRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static BoxImageCutRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static BoxImageCutRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static BoxImageCutRequest parseFrom(
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
  public static Builder newBuilder(BoxImageCutRequest prototype) {
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
   * 机顶盒截图请求
   * </pre>
   *
   * Protobuf type {@code BoxImageCutRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:BoxImageCutRequest)
      com.bestarmedia.proto.vod.BoxImageCutRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.bestarmedia.proto.vod.BoxProto.internal_static_BoxImageCutRequest_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.bestarmedia.proto.vod.BoxProto.internal_static_BoxImageCutRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              BoxImageCutRequest.class, Builder.class);
    }

    // Construct using com.bestarmedia.proto.vod.BoxImageCutRequest.newBuilder()
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
      ktvNetCode_ = "";

      roomCode_ = "";

      userId_ = "";

      address_ = "";

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.bestarmedia.proto.vod.BoxProto.internal_static_BoxImageCutRequest_descriptor;
    }

    @Override
    public BoxImageCutRequest getDefaultInstanceForType() {
      return BoxImageCutRequest.getDefaultInstance();
    }

    @Override
    public BoxImageCutRequest build() {
      BoxImageCutRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public BoxImageCutRequest buildPartial() {
      BoxImageCutRequest result = new BoxImageCutRequest(this);
      result.ktvNetCode_ = ktvNetCode_;
      result.roomCode_ = roomCode_;
      result.userId_ = userId_;
      result.address_ = address_;
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
      if (other instanceof BoxImageCutRequest) {
        return mergeFrom((BoxImageCutRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(BoxImageCutRequest other) {
      if (other == BoxImageCutRequest.getDefaultInstance()) return this;
      if (!other.getKtvNetCode().isEmpty()) {
        ktvNetCode_ = other.ktvNetCode_;
        onChanged();
      }
      if (!other.getRoomCode().isEmpty()) {
        roomCode_ = other.roomCode_;
        onChanged();
      }
      if (!other.getUserId().isEmpty()) {
        userId_ = other.userId_;
        onChanged();
      }
      if (!other.getAddress().isEmpty()) {
        address_ = other.address_;
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
      BoxImageCutRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (BoxImageCutRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object ktvNetCode_ = "";
    /**
     * <code>string ktv_net_code = 1;</code>
     */
    public String getKtvNetCode() {
      Object ref = ktvNetCode_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        ktvNetCode_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string ktv_net_code = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKtvNetCodeBytes() {
      Object ref = ktvNetCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        ktvNetCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ktv_net_code = 1;</code>
     */
    public Builder setKtvNetCode(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ktvNetCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ktv_net_code = 1;</code>
     */
    public Builder clearKtvNetCode() {
      
      ktvNetCode_ = getDefaultInstance().getKtvNetCode();
      onChanged();
      return this;
    }
    /**
     * <code>string ktv_net_code = 1;</code>
     */
    public Builder setKtvNetCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ktvNetCode_ = value;
      onChanged();
      return this;
    }

    private Object roomCode_ = "";
    /**
     * <code>string room_code = 2;</code>
     */
    public String getRoomCode() {
      Object ref = roomCode_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        roomCode_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string room_code = 2;</code>
     */
    public com.google.protobuf.ByteString
        getRoomCodeBytes() {
      Object ref = roomCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        roomCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string room_code = 2;</code>
     */
    public Builder setRoomCode(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      roomCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string room_code = 2;</code>
     */
    public Builder clearRoomCode() {
      
      roomCode_ = getDefaultInstance().getRoomCode();
      onChanged();
      return this;
    }
    /**
     * <code>string room_code = 2;</code>
     */
    public Builder setRoomCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      roomCode_ = value;
      onChanged();
      return this;
    }

    private Object userId_ = "";
    /**
     * <code>string user_id = 3;</code>
     */
    public String getUserId() {
      Object ref = userId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        userId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string user_id = 3;</code>
     */
    public com.google.protobuf.ByteString
        getUserIdBytes() {
      Object ref = userId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        userId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string user_id = 3;</code>
     */
    public Builder setUserId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      userId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string user_id = 3;</code>
     */
    public Builder clearUserId() {
      
      userId_ = getDefaultInstance().getUserId();
      onChanged();
      return this;
    }
    /**
     * <code>string user_id = 3;</code>
     */
    public Builder setUserIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      userId_ = value;
      onChanged();
      return this;
    }

    private Object address_ = "";
    /**
     * <code>string address = 4;</code>
     */
    public String getAddress() {
      Object ref = address_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        address_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <code>string address = 4;</code>
     */
    public com.google.protobuf.ByteString
        getAddressBytes() {
      Object ref = address_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        address_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string address = 4;</code>
     */
    public Builder setAddress(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      address_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string address = 4;</code>
     */
    public Builder clearAddress() {
      
      address_ = getDefaultInstance().getAddress();
      onChanged();
      return this;
    }
    /**
     * <code>string address = 4;</code>
     */
    public Builder setAddressBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      address_ = value;
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


    // @@protoc_insertion_point(builder_scope:BoxImageCutRequest)
  }

  // @@protoc_insertion_point(class_scope:BoxImageCutRequest)
  private static final BoxImageCutRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new BoxImageCutRequest();
  }

  public static BoxImageCutRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BoxImageCutRequest>
      PARSER = new com.google.protobuf.AbstractParser<BoxImageCutRequest>() {
    @Override
    public BoxImageCutRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new BoxImageCutRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<BoxImageCutRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<BoxImageCutRequest> getParserForType() {
    return PARSER;
  }

  @Override
  public BoxImageCutRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

