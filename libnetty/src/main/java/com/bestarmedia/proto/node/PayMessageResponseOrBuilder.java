// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

public interface PayMessageResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PayMessageResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string ktv_net_code = 1;</code>
   */
  String getKtvNetCode();
  /**
   * <code>string ktv_net_code = 1;</code>
   */
  com.google.protobuf.ByteString
      getKtvNetCodeBytes();

  /**
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 2;</code>
   */
  String getRoomCode();
  /**
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 2;</code>
   */
  com.google.protobuf.ByteString
      getRoomCodeBytes();

  /**
   * <pre>
   * 0未支付 1已支付
   * </pre>
   *
   * <code>int32 pay_status = 3;</code>
   */
  int getPayStatus();
}
