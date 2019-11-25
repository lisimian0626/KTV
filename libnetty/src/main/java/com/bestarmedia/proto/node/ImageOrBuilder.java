// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Mime.proto

package com.bestarmedia.proto.node;

public interface ImageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Image)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 图片 url
   * </pre>
   *
   * <code>string url = 1;</code>
   */
  String getUrl();
  /**
   * <pre>
   * 图片 url
   * </pre>
   *
   * <code>string url = 1;</code>
   */
  com.google.protobuf.ByteString
      getUrlBytes();

  /**
   * <pre>
   * 图片宽，默认屏幕宽度1/3=426
   * </pre>
   *
   * <code>int32 width = 2;</code>
   */
  int getWidth();

  /**
   * <pre>
   * 图片高，默认屏幕高度1/3=240
   * </pre>
   *
   * <code>int32 height = 3;</code>
   */
  int getHeight();

  /**
   * <pre>
   * 开始位置X坐标，默认0
   * </pre>
   *
   * <code>int32 start_x = 4;</code>
   */
  int getStartX();

  /**
   * <pre>
   * 开始位置Y坐标 默认0
   * </pre>
   *
   * <code>int32 start_y = 5;</code>
   */
  int getStartY();

  /**
   * <pre>
   * 结束位置X坐标，默认426（居中显示）
   * </pre>
   *
   * <code>int32 end_x = 6;</code>
   */
  int getEndX();

  /**
   * <pre>
   * 结束位置Y坐标 默认240（居中显示）
   * </pre>
   *
   * <code>int32 end_y = 7;</code>
   */
  int getEndY();

  /**
   * <pre>
   * 播放效果 1直接显示 2滑入滑出，暂时不需要（直接显示开始坐标和结束坐标一致）
   * </pre>
   *
   * <code>int32 play_effect = 8;</code>
   */
  int getPlayEffect();

  /**
   * <pre>
   * 停留时长，默认10秒
   * </pre>
   *
   * <code>int32 stay_time = 9;</code>
   */
  int getStayTime();

  /**
   * <pre>
   * 0 店家本地 1 政府管理
   * </pre>
   *
   * <code>int32 source = 10;</code>
   */
  int getSource();

  /**
   * <pre>
   * 文本对象
   * </pre>
   *
   * <code>.Text text = 19;</code>
   */
  boolean hasText();
  /**
   * <pre>
   * 文本对象
   * </pre>
   *
   * <code>.Text text = 19;</code>
   */
  com.bestarmedia.proto.node.Text getText();
  /**
   * <pre>
   * 文本对象
   * </pre>
   *
   * <code>.Text text = 19;</code>
   */
  com.bestarmedia.proto.node.TextOrBuilder getTextOrBuilder();
}