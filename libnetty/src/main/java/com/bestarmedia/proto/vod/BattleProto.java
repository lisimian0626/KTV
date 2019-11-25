// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Battle.proto

package com.bestarmedia.proto.vod;

public final class BattleProto {
  private BattleProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BattleUser_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BattleUser_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BattleContract_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BattleContract_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ScoreToOpponent_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ScoreToOpponent_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\014Battle.proto\"\273\001\n\nBattleUser\022\024\n\014ktv_net" +
      "_code\030\001 \001(\t\022\020\n\010ktv_name\030\002 \001(\t\022\021\n\troom_co" +
      "de\030\003 \001(\t\022\021\n\troom_name\030\004 \001(\t\022\024\n\014room_sess" +
      "ion\030\005 \001(\t\022\017\n\007user_id\030\006 \001(\005\022\014\n\004name\030\007 \001(\t" +
      "\022\016\n\006avatar\030\010 \001(\t\022\013\n\003sex\030\t \001(\005\022\r\n\005score\030\n" +
      " \001(\002\"\233\002\n\016BattleContract\022\n\n\002id\030\001 \001(\005\022\016\n\006s" +
      "logan\030\002 \001(\t\022\017\n\007song_id\030\003 \001(\005\022\021\n\tsong_nam" +
      "e\030\004 \001(\t\022\023\n\013singer_name\030\005 \001(\t\022\035\n\010declarer" +
      "\030\006 \001(\0132\013.BattleUser\022\037\n\nchallenger\030\007 \001(\0132" +
      "\013.BattleUser\022\022\n\ncreated_at\030\010 \001(\t\022\021\n\tacce" +
      "pt_at\030\t \001(\t\022\022\n\nupdated_at\030\n \001(\t\022\016\n\006statu" +
      "s\030\013 \001(\005\022\021\n\texpire_at\030\014 \001(\t\022\026\n\016remaining_" +
      "time\030\r \001(\005\"\211\001\n\017ScoreToOpponent\022!\n\014curren" +
      "t_user\030\001 \001(\0132\013.BattleUser\022\035\n\025opponent_kt" +
      "v_net_code\030\002 \001(\t\022\032\n\022opponent_room_code\030\003" +
      " \001(\t\022\014\n\004text\030\004 \001(\t\022\n\n\002id\030\005 \001(\005B*\n\031com.be" +
      "starmedia.proto.vodB\013BattleProtoP\001b\006prot" +
      "o3"
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
        }, assigner);
    internal_static_BattleUser_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_BattleUser_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BattleUser_descriptor,
        new String[] { "KtvNetCode", "KtvName", "RoomCode", "RoomName", "RoomSession", "UserId", "Name", "Avatar", "Sex", "Score", });
    internal_static_BattleContract_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_BattleContract_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BattleContract_descriptor,
        new String[] { "Id", "Slogan", "SongId", "SongName", "SingerName", "Declarer", "Challenger", "CreatedAt", "AcceptAt", "UpdatedAt", "Status", "ExpireAt", "RemainingTime", });
    internal_static_ScoreToOpponent_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_ScoreToOpponent_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ScoreToOpponent_descriptor,
        new String[] { "CurrentUser", "OpponentKtvNetCode", "OpponentRoomCode", "Text", "Id", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
