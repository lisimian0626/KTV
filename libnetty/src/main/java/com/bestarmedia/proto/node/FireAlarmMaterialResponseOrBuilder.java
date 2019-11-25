// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Local.proto

package com.bestarmedia.proto.node;

public interface FireAlarmMaterialResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:FireAlarmMaterialResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 横版图 base64
   * </pre>
   *
   * <code>string alarm_img_a = 1;</code>
   */
  String getAlarmImgA();
  /**
   * <pre>
   * 横版图 base64
   * </pre>
   *
   * <code>string alarm_img_a = 1;</code>
   */
  com.google.protobuf.ByteString
      getAlarmImgABytes();

  /**
   * <pre>
   * 竖版图 base 64
   * </pre>
   *
   * <code>string alarm_img_b = 2;</code>
   */
  String getAlarmImgB();
  /**
   * <pre>
   * 竖版图 base 64
   * </pre>
   *
   * <code>string alarm_img_b = 2;</code>
   */
  com.google.protobuf.ByteString
      getAlarmImgBBytes();

  /**
   * <pre>
   * 预警时间（分），默认为0
   * </pre>
   *
   * <code>int32 early_alarm_time = 6;</code>
   */
  int getEarlyAlarmTime();

  /**
   * <code>string address = 3;</code>
   */
  String getAddress();
  /**
   * <code>string address = 3;</code>
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <code>int32 user_id = 4;</code>
   */
  int getUserId();

  /**
   * <code>string ktv_net_code = 5;</code>
   */
  String getKtvNetCode();
  /**
   * <code>string ktv_net_code = 5;</code>
   */
  com.google.protobuf.ByteString
      getKtvNetCodeBytes();
}
