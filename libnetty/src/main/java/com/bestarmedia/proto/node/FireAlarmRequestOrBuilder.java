// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

public interface FireAlarmRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:FireAlarmRequest)
    com.google.protobuf.MessageOrBuilder {

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
   * 1启动 2或者其它值 停止
   * </pre>
   *
   * <code>int32 op = 2;</code>
   */
  int getOp();
}
