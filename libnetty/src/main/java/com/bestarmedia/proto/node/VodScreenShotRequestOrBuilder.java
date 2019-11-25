// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

public interface VodScreenShotRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:VodScreenShotRequest)
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
   * 请求来源 address
   * </pre>
   *
   * <code>string address = 3;</code>
   */
  String getAddress();
  /**
   * <pre>
   * 请求来源 address
   * </pre>
   *
   * <code>string address = 3;</code>
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <pre>
   * 请求平台 1北星云平台 2政府监管平台
   * </pre>
   *
   * <code>int32 platform = 4;</code>
   */
  int getPlatform();

  /**
   * <pre>
   * 上传接口，根据平台不同给机顶盒不同的上传地址,由服务器端定义，web前端传空
   * </pre>
   *
   * <code>string endpoint = 5;</code>
   */
  String getEndpoint();
  /**
   * <pre>
   * 上传接口，根据平台不同给机顶盒不同的上传地址,由服务器端定义，web前端传空
   * </pre>
   *
   * <code>string endpoint = 5;</code>
   */
  com.google.protobuf.ByteString
      getEndpointBytes();

  /**
   * <pre>
   * web 用户 ID
   * </pre>
   *
   * <code>int32 user_id = 6;</code>
   */
  int getUserId();
}