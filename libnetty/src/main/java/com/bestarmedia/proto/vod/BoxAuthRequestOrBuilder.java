// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

public interface BoxAuthRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:BoxAuthRequest)
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
   * HDMI状态 0未开启 1已开启
   * </pre>
   *
   * <code>int32 hdmi_enable = 3;</code>
   */
  int getHdmiEnable();

  /**
   * <pre>
   * 机顶盒序列号
   * </pre>
   *
   * <code>string serial_no = 4;</code>
   */
  String getSerialNo();
  /**
   * <pre>
   * 机顶盒序列号
   * </pre>
   *
   * <code>string serial_no = 4;</code>
   */
  com.google.protobuf.ByteString
      getSerialNoBytes();

  /**
   * <pre>
   *厂商
   * </pre>
   *
   * <code>string anufacturer = 5;</code>
   */
  String getAnufacturer();
  /**
   * <pre>
   *厂商
   * </pre>
   *
   * <code>string anufacturer = 5;</code>
   */
  com.google.protobuf.ByteString
      getAnufacturerBytes();

  /**
   * <pre>
   *系统版本号
   * </pre>
   *
   * <code>int32 os_version = 6;</code>
   */
  int getOsVersion();

  /**
   * <pre>
   *APP版本号
   * </pre>
   *
   * <code>int32 apk_version_code = 7;</code>
   */
  int getApkVersionCode();

  /**
   * <pre>
   *APP版本名称
   * </pre>
   *
   * <code>string apk_version_name = 8;</code>
   */
  String getApkVersionName();
  /**
   * <pre>
   *APP版本名称
   * </pre>
   *
   * <code>string apk_version_name = 8;</code>
   */
  com.google.protobuf.ByteString
      getApkVersionNameBytes();

  /**
   * <pre>
   *设备IP
   * </pre>
   *
   * <code>string ip = 9;</code>
   */
  String getIp();
  /**
   * <pre>
   *设备IP
   * </pre>
   *
   * <code>string ip = 9;</code>
   */
  com.google.protobuf.ByteString
      getIpBytes();
}