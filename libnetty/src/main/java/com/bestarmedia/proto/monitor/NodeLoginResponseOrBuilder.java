// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Monitor.proto

package com.bestarmedia.proto.monitor;

public interface NodeLoginResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:NodeLoginResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 认证结果 1成功 0失败
   * </pre>
   *
   * <code>int32 code = 1;</code>
   */
  int getCode();

  /**
   * <pre>
   * 消息
   * </pre>
   *
   * <code>string message = 2;</code>
   */
  String getMessage();
  /**
   * <pre>
   * 消息
   * </pre>
   *
   * <code>string message = 2;</code>
   */
  com.google.protobuf.ByteString
      getMessageBytes();
}
