// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

public interface BarrageResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:BarrageResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 5;</code>
   */
  String getKtvNetCode();
  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 5;</code>
   */
  com.google.protobuf.ByteString
      getKtvNetCodeBytes();

  /**
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 1;</code>
   */
  String getRoomCode();
  /**
   * <pre>
   * 房间号
   * </pre>
   *
   * <code>string room_code = 1;</code>
   */
  com.google.protobuf.ByteString
      getRoomCodeBytes();

  /**
   * <pre>
   * 0失败 1成功
   * </pre>
   *
   * <code>int32 code = 2;</code>
   */
  int getCode();

  /**
   * <pre>
   * 用户ID
   * </pre>
   *
   * <code>int32 user_id = 3;</code>
   */
  int getUserId();

  /**
   * <pre>
   * 用户SOCKET地址 没有则为空
   * </pre>
   *
   * <code>string address = 4;</code>
   */
  String getAddress();
  /**
   * <pre>
   * 用户SOCKET地址 没有则为空
   * </pre>
   *
   * <code>string address = 4;</code>
   */
  com.google.protobuf.ByteString
      getAddressBytes();
}