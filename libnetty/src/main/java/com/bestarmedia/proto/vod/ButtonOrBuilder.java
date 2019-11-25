// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

public interface ButtonOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Button)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 电视是否黑屏
   * </pre>
   *
   * <code>bool is_hdmi_black = 1;</code>
   */
  boolean getIsHdmiBlack();

  /**
   * <pre>
   * 智能灯光是否开启
   * </pre>
   *
   * <code>bool is_light_auto = 2;</code>
   */
  boolean getIsLightAuto();

  /**
   * <pre>
   * 是否静音
   * </pre>
   *
   * <code>bool is_mute = 3;</code>
   */
  boolean getIsMute();

  /**
   * <pre>
   * 是否暂停
   * </pre>
   *
   * <code>bool is_pause = 4;</code>
   */
  boolean getIsPause();

  /**
   * <pre>
   * 是否开启录音
   * </pre>
   *
   * <code>bool is_record = 5;</code>
   */
  boolean getIsRecord();

  /**
   * <pre>
   * 是否服务铃响应
   * </pre>
   *
   * <code>bool is_serving = 6;</code>
   */
  boolean getIsServing();

  /**
   * <pre>
   * 灯光模式
   * </pre>
   *
   * <code>int32 light_mode = 7;</code>
   */
  int getLightMode();

  /**
   * <pre>
   * 是否开启原唱
   * </pre>
   *
   * <code>bool origin_on = 8;</code>
   */
  boolean getOriginOn();

  /**
   * <pre>
   * 评分模式 0关闭 1娱乐 2专业
   * </pre>
   *
   * <code>int32 score_mode = 9;</code>
   */
  int getScoreMode();

  /**
   * <pre>
   * 服务铃模式 -1无呼叫 0服务员 1DJ 2清洁 3保安 4买单 5催单 6唛套 7加冰
   * </pre>
   *
   * <code>int32 service_mode = 10;</code>
   */
  int getServiceMode();
}
