// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

package com.bestarmedia.proto.node;

public interface VideoMessageRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:VideoMessageRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 用户ID 本地功能，没有则为0
   * </pre>
   *
   * <code>int32 user_id = 5;</code>
   */
  int getUserId();

  /**
   * <pre>
   * 用户姓名
   * </pre>
   *
   * <code>string name = 6;</code>
   */
  String getName();
  /**
   * <pre>
   * 用户姓名
   * </pre>
   *
   * <code>string name = 6;</code>
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <pre>
   * 用户头像
   * </pre>
   *
   * <code>string avatar = 7;</code>
   */
  String getAvatar();
  /**
   * <pre>
   * 用户头像
   * </pre>
   *
   * <code>string avatar = 7;</code>
   */
  com.google.protobuf.ByteString
      getAvatarBytes();

  /**
   * <pre>
   * 房间session
   * </pre>
   *
   * <code>string room_session = 4;</code>
   */
  String getRoomSession();
  /**
   * <pre>
   * 房间session
   * </pre>
   *
   * <code>string room_session = 4;</code>
   */
  com.google.protobuf.ByteString
      getRoomSessionBytes();

  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 3;</code>
   */
  String getKtvNetCode();
  /**
   * <pre>
   * KTV编号
   * </pre>
   *
   * <code>string ktv_net_code = 3;</code>
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
   * 视频消息体
   * </pre>
   *
   * <code>.Video video = 2;</code>
   */
  boolean hasVideo();
  /**
   * <pre>
   * 视频消息体
   * </pre>
   *
   * <code>.Video video = 2;</code>
   */
  Video getVideo();
  /**
   * <pre>
   * 视频消息体
   * </pre>
   *
   * <code>.Video video = 2;</code>
   */
  VideoOrBuilder getVideoOrBuilder();

  /**
   * <pre>
   *是否加入队列 0否 1是
   * </pre>
   *
   * <code>int32 push_to_queue = 8;</code>
   */
  int getPushToQueue();

  /**
   * <pre>
   *插入队列位置；默认0队尾，1队头
   * </pre>
   *
   * <code>int32 push_to_queue_position = 9;</code>
   */
  int getPushToQueuePosition();
}
