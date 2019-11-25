// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Monitor.proto

package com.bestarmedia.proto.monitor;

public interface RoomListResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:RoomListResponse)
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
   * 客户端地址
   * </pre>
   *
   * <code>string address = 2;</code>
   */
  String getAddress();
  /**
   * <pre>
   * 客户端地址
   * </pre>
   *
   * <code>string address = 2;</code>
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <pre>
   * 前端用户id
   * </pre>
   *
   * <code>int32 user_id = 7;</code>
   */
  int getUserId();

  /**
   * <pre>
   * 房间总数量
   * </pre>
   *
   * <code>int32 amount = 3;</code>
   */
  int getAmount();

  /**
   * <pre>
   * 开房中数量
   * </pre>
   *
   * <code>int32 opening_amount = 4;</code>
   */
  int getOpeningAmount();

  /**
   * <pre>
   * 关房中数量
   * </pre>
   *
   * <code>int32 closing_amount = 5;</code>
   */
  int getClosingAmount();

  /**
   * <pre>
   * 房间列表
   * </pre>
   *
   * <code>repeated .Room room = 6;</code>
   */
  java.util.List<Room>
      getRoomList();
  /**
   * <pre>
   * 房间列表
   * </pre>
   *
   * <code>repeated .Room room = 6;</code>
   */
  Room getRoom(int index);
  /**
   * <pre>
   * 房间列表
   * </pre>
   *
   * <code>repeated .Room room = 6;</code>
   */
  int getRoomCount();
  /**
   * <pre>
   * 房间列表
   * </pre>
   *
   * <code>repeated .Room room = 6;</code>
   */
  java.util.List<? extends com.bestarmedia.proto.monitor.RoomOrBuilder> 
      getRoomOrBuilderList();
  /**
   * <pre>
   * 房间列表
   * </pre>
   *
   * <code>repeated .Room room = 6;</code>
   */
  com.bestarmedia.proto.monitor.RoomOrBuilder getRoomOrBuilder(
          int index);
}
