// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Box.proto

package com.bestarmedia.proto.vod;

public final class BoxProto {
  private BoxProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BoxHeartbeat_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BoxHeartbeat_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BoxAuthRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BoxAuthRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BoxAuthResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BoxAuthResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BoxImageCutRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BoxImageCutRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MobileUser_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MobileUser_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_MobileMessageBroadcast_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_MobileMessageBroadcast_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ButtonStatusBroadcast_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ButtonStatusBroadcast_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Button_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Button_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_SongSimple_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_SongSimple_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_RoomSongList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_RoomSongList_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ExpiredSession_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ExpiredSession_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PayResultRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PayResultRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PayResultResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PayResultResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\tBox.proto\032\033google/protobuf/empty.proto" +
      "\032\nMime.proto\"4\n\014BoxHeartbeat\022$\n\004beat\030\001 \001" +
      "(\0132\026.google.protobuf.Empty\"\312\001\n\016BoxAuthRe" +
      "quest\022\024\n\014ktv_net_code\030\001 \001(\t\022\021\n\troom_code" +
      "\030\002 \001(\t\022\023\n\013hdmi_enable\030\003 \001(\005\022\021\n\tserial_no" +
      "\030\004 \001(\t\022\023\n\013anufacturer\030\005 \001(\t\022\022\n\nos_versio" +
      "n\030\006 \001(\005\022\030\n\020apk_version_code\030\007 \001(\005\022\030\n\020apk" +
      "_version_name\030\010 \001(\t\022\n\n\002ip\030\t \001(\t\"D\n\017BoxAu" +
      "thResponse\022\014\n\004code\030\001 \001(\005\"#\n\010CodeType\022\013\n\007" +
      "SUCCESS\020\000\022\n\n\006FAILED\020\001\"_\n\022BoxImageCutRequ" +
      "est\022\024\n\014ktv_net_code\030\001 \001(\t\022\021\n\troom_code\030\002" +
      " \001(\t\022\017\n\007user_id\030\003 \001(\t\022\017\n\007address\030\004 \001(\t\"\320" +
      "\001\n\nMobileUser\022\017\n\007user_id\030\001 \001(\005\022\014\n\004name\030\002" +
      " \001(\t\022\016\n\006avatar\030\003 \001(\t\022\013\n\003sex\030\004 \001(\005\022\024\n\014ktv" +
      "_net_code\030\005 \001(\t\022\020\n\010ktv_name\030\006 \001(\t\022\021\n\troo" +
      "m_code\030\007 \001(\t\022\021\n\troom_name\030\013 \001(\t\022\024\n\014room_" +
      "session\030\010 \001(\t\022\017\n\007address\030\t \001(\t\022\021\n\tphoneI" +
      "MEI\030\n \001(\t\"\215\001\n\026MobileMessageBroadcast\022\031\n\004" +
      "user\030\001 \001(\0132\013.MobileUser\022\021\n\tdirective\030\002 \001" +
      "(\005\022\023\n\004text\030\003 \001(\0132\005.Text\022\025\n\005image\030\004 \001(\0132\006" +
      ".Image\022\n\n\002id\030\005 \001(\t\022\r\n\005score\030\006 \001(\t\"K\n\025But" +
      "tonStatusBroadcast\022\027\n\006button\030\001 \001(\0132\007.But" +
      "ton\022\031\n\004user\030\002 \001(\0132\013.MobileUser\"\321\001\n\006Butto" +
      "n\022\025\n\ris_hdmi_black\030\001 \001(\010\022\025\n\ris_light_aut" +
      "o\030\002 \001(\010\022\017\n\007is_mute\030\003 \001(\010\022\020\n\010is_pause\030\004 \001" +
      "(\010\022\021\n\tis_record\030\005 \001(\010\022\022\n\nis_serving\030\006 \001(" +
      "\010\022\022\n\nlight_mode\030\007 \001(\005\022\021\n\torigin_on\030\010 \001(\010" +
      "\022\022\n\nscore_mode\030\t \001(\005\022\024\n\014service_mode\030\n \001" +
      "(\005\"y\n\nSongSimple\022\014\n\004uuid\030\001 \001(\t\022\017\n\007song_i" +
      "d\030\002 \001(\t\022\021\n\tsong_name\030\003 \001(\t\022\023\n\013singer_nam" +
      "e\030\004 \001(\t\022\022\n\nvideo_type\030\005 \001(\t\022\020\n\010is_grade\030" +
      "\006 \001(\005\"R\n\014RoomSongList\022\014\n\004type\030\001 \001(\005\022\031\n\004s" +
      "ong\030\002 \003(\0132\013.SongSimple\022\031\n\004user\030\003 \001(\0132\013.M" +
      "obileUser\"O\n\016ExpiredSession\022\024\n\014ktv_net_c" +
      "ode\030\001 \001(\t\022\021\n\troom_code\030\002 \001(\t\022\024\n\014room_ses" +
      "sion\030\003 \001(\t\"q\n\020PayResultRequest\022\024\n\014ktv_ne" +
      "t_code\030\001 \001(\t\022\021\n\troom_code\030\002 \001(\t\022\017\n\007user_" +
      "id\030\003 \001(\005\022\020\n\010order_no\030\004 \001(\t\022\021\n\ttotal_fee\030" +
      "\005 \001(\002\"r\n\021PayResultResponse\022\024\n\014ktv_net_co" +
      "de\030\001 \001(\t\022\021\n\troom_code\030\002 \001(\t\022\017\n\007user_id\030\003" +
      " \001(\005\022\020\n\010order_no\030\004 \001(\t\022\021\n\ttotal_fee\030\005 \001(" +
      "\002B\'\n\031com.bestarmedia.proto.vodB\010BoxProto" +
      "P\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
          com.bestarmedia.proto.node.MimeProto.getDescriptor(),
        }, assigner);
    internal_static_BoxHeartbeat_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_BoxHeartbeat_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BoxHeartbeat_descriptor,
        new String[] { "Beat", });
    internal_static_BoxAuthRequest_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_BoxAuthRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BoxAuthRequest_descriptor,
        new String[] { "KtvNetCode", "RoomCode", "HdmiEnable", "SerialNo", "Anufacturer", "OsVersion", "ApkVersionCode", "ApkVersionName", "Ip", });
    internal_static_BoxAuthResponse_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_BoxAuthResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BoxAuthResponse_descriptor,
        new String[] { "Code", });
    internal_static_BoxImageCutRequest_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_BoxImageCutRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BoxImageCutRequest_descriptor,
        new String[] { "KtvNetCode", "RoomCode", "UserId", "Address", });
    internal_static_MobileUser_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_MobileUser_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MobileUser_descriptor,
        new String[] { "UserId", "Name", "Avatar", "Sex", "KtvNetCode", "KtvName", "RoomCode", "RoomName", "RoomSession", "Address", "PhoneIMEI", });
    internal_static_MobileMessageBroadcast_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_MobileMessageBroadcast_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_MobileMessageBroadcast_descriptor,
        new String[] { "User", "Directive", "Text", "Image", "Id", "Score", });
    internal_static_ButtonStatusBroadcast_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_ButtonStatusBroadcast_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ButtonStatusBroadcast_descriptor,
        new String[] { "Button", "User", });
    internal_static_Button_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_Button_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Button_descriptor,
        new String[] { "IsHdmiBlack", "IsLightAuto", "IsMute", "IsPause", "IsRecord", "IsServing", "LightMode", "OriginOn", "ScoreMode", "ServiceMode", });
    internal_static_SongSimple_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_SongSimple_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_SongSimple_descriptor,
        new String[] { "Uuid", "SongId", "SongName", "SingerName", "VideoType", "IsGrade", });
    internal_static_RoomSongList_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_RoomSongList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_RoomSongList_descriptor,
        new String[] { "Type", "Song", "User", });
    internal_static_ExpiredSession_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_ExpiredSession_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ExpiredSession_descriptor,
        new String[] { "KtvNetCode", "RoomCode", "RoomSession", });
    internal_static_PayResultRequest_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_PayResultRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PayResultRequest_descriptor,
        new String[] { "KtvNetCode", "RoomCode", "UserId", "OrderNo", "TotalFee", });
    internal_static_PayResultResponse_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_PayResultResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PayResultResponse_descriptor,
        new String[] { "KtvNetCode", "RoomCode", "UserId", "OrderNo", "TotalFee", });
    com.google.protobuf.EmptyProto.getDescriptor();
    com.bestarmedia.proto.node.MimeProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
