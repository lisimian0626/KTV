// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

/**
 * <pre>
 * 火警控制请求
 * </pre>
 *
 * Protobuf type {@code FireAlarmRequest}
 */
public  final class FireAlarmRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:FireAlarmRequest)
    FireAlarmRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FireAlarmRequest.newBuilder() to construct.
  private FireAlarmRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FireAlarmRequest() {
    roomCode_ = "";
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private FireAlarmRequest(
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

            roomCode_ = s;
            break;
          }
          case 16: {

            op_ = input.readInt32();
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
    return com.bestarmedia.proto.node.NodeProto.internal_static_FireAlarmRequest_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.bestarmedia.proto.node.NodeProto.internal_static_FireAlarmRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            FireAlarmRequest.class, Builder.class);
  }

  public static final int ROOM_CODE_FIELD_NUMBER = 1;
  private volatile Object roomCode_;
  /**
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 1;</code>
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
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 1;</code>
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

  public static final int OP_FIELD_NUMBER = 2;
  private int op_;
  /**
   * <pre>
   * 1启动 2或者其它值 停止
   * </pre>
   *
   * <code>int32 op = 2;</code>
   */
  public int getOp() {
    return op_;
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
    if (!getRoomCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, roomCode_);
    }
    if (op_ != 0) {
      output.writeInt32(2, op_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getRoomCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, roomCode_);
    }
    if (op_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, op_);
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
    if (!(obj instanceof FireAlarmRequest)) {
      return super.equals(obj);
    }
    FireAlarmRequest other = (FireAlarmRequest) obj;

    if (!getRoomCode()
        .equals(other.getRoomCode())) return false;
    if (getOp()
        != other.getOp()) return false;
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
    hash = (37 * hash) + ROOM_CODE_FIELD_NUMBER;
    hash = (53 * hash) + getRoomCode().hashCode();
    hash = (37 * hash) + OP_FIELD_NUMBER;
    hash = (53 * hash) + getOp();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static FireAlarmRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FireAlarmRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FireAlarmRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FireAlarmRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FireAlarmRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static FireAlarmRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static FireAlarmRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static FireAlarmRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static FireAlarmRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static FireAlarmRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static FireAlarmRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static FireAlarmRequest parseFrom(
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
  public static Builder newBuilder(FireAlarmRequest prototype) {
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
   * 火警控制请求
   * </pre>
   *
   * Protobuf type {@code FireAlarmRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:FireAlarmRequest)
      com.bestarmedia.proto.node.FireAlarmRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.bestarmedia.proto.node.NodeProto.internal_static_FireAlarmRequest_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.bestarmedia.proto.node.NodeProto.internal_static_FireAlarmRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              FireAlarmRequest.class, Builder.class);
    }

    // Construct using com.bestarmedia.proto.node.FireAlarmRequest.newBuilder()
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
      roomCode_ = "";

      op_ = 0;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.bestarmedia.proto.node.NodeProto.internal_static_FireAlarmRequest_descriptor;
    }

    @Override
    public FireAlarmRequest getDefaultInstanceForType() {
      return FireAlarmRequest.getDefaultInstance();
    }

    @Override
    public FireAlarmRequest build() {
      FireAlarmRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public FireAlarmRequest buildPartial() {
      FireAlarmRequest result = new FireAlarmRequest(this);
      result.roomCode_ = roomCode_;
      result.op_ = op_;
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
      if (other instanceof FireAlarmRequest) {
        return mergeFrom((FireAlarmRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(FireAlarmRequest other) {
      if (other == FireAlarmRequest.getDefaultInstance()) return this;
      if (!other.getRoomCode().isEmpty()) {
        roomCode_ = other.roomCode_;
        onChanged();
      }
      if (other.getOp() != 0) {
        setOp(other.getOp());
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
      FireAlarmRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (FireAlarmRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object roomCode_ = "";
    /**
     * <pre>
     * 房间号
     * </pre>
     *
     * <code>string room_code = 1;</code>
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
     * <pre>
     * 房间号
     * </pre>
     *
     * <code>string room_code = 1;</code>
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
     * <pre>
     * 房间号
     * </pre>
     *
     * <code>string room_code = 1;</code>
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
     * <pre>
     * 房间号
     * </pre>
     *
     * <code>string room_code = 1;</code>
     */
    public Builder clearRoomCode() {
      
      roomCode_ = getDefaultInstance().getRoomCode();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 房间号
     * </pre>
     *
     * <code>string room_code = 1;</code>
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

    private int op_ ;
    /**
     * <pre>
     * 1启动 2或者其它值 停止
     * </pre>
     *
     * <code>int32 op = 2;</code>
     */
    public int getOp() {
      return op_;
    }
    /**
     * <pre>
     * 1启动 2或者其它值 停止
     * </pre>
     *
     * <code>int32 op = 2;</code>
     */
    public Builder setOp(int value) {
      
      op_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 1启动 2或者其它值 停止
     * </pre>
     *
     * <code>int32 op = 2;</code>
     */
    public Builder clearOp() {
      
      op_ = 0;
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


    // @@protoc_insertion_point(builder_scope:FireAlarmRequest)
  }

  // @@protoc_insertion_point(class_scope:FireAlarmRequest)
  private static final FireAlarmRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new FireAlarmRequest();
  }

  public static FireAlarmRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FireAlarmRequest>
      PARSER = new com.google.protobuf.AbstractParser<FireAlarmRequest>() {
    @Override
    public FireAlarmRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new FireAlarmRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<FireAlarmRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<FireAlarmRequest> getParserForType() {
    return PARSER;
  }

  @Override
  public FireAlarmRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

