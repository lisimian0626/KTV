// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Monitor.proto

package com.bestarmedia.proto.monitor;

public interface BusinessRoomBroadcastOrBuilder extends
    // @@protoc_insertion_point(interface_extends:BusinessRoomBroadcast)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 1;</code>
   */
  String getKtvNetCode();
  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 1;</code>
   */
  com.google.protobuf.ByteString
      getKtvNetCodeBytes();

  /**
   * <pre>
   * 房间编号
   * </pre>
   *
   * <code>string room_code = 2;</code>
   */
  String getRoomCode();
  /**
   * <pre>
   * 房间编号
   * </pre>
   *
   * <code>string room_code = 2;</code>
   */
  com.google.protobuf.ByteString
      getRoomCodeBytes();

  /**
   * <pre>
   * 房间打卡时间
   * </pre>
   *
   * <code>string check_at = 3;</code>
   */
  String getCheckAt();
  /**
   * <pre>
   * 房间打卡时间
   * </pre>
   *
   * <code>string check_at = 3;</code>
   */
  com.google.protobuf.ByteString
      getCheckAtBytes();

  /**
   * <pre>
   * 开始时间
   * </pre>
   *
   * <code>string begin_at = 4;</code>
   */
  String getBeginAt();
  /**
   * <pre>
   * 开始时间
   * </pre>
   *
   * <code>string begin_at = 4;</code>
   */
  com.google.protobuf.ByteString
      getBeginAtBytes();

  /**
   * <pre>
   * 结束时间
   * </pre>
   *
   * <code>string end_at = 5;</code>
   */
  String getEndAt();
  /**
   * <pre>
   * 结束时间
   * </pre>
   *
   * <code>string end_at = 5;</code>
   */
  com.google.protobuf.ByteString
      getEndAtBytes();
}