// To parse this JSON data, do
//
//     final inboxQuestionDetailModel = inboxQuestionDetailModelFromJson(jsonString);

import 'dart:convert';

InboxQuestionDetailModel inboxQuestionDetailModelFromJson(String str) => InboxQuestionDetailModel.fromJson(json.decode(str));

String inboxQuestionDetailModelToJson(InboxQuestionDetailModel data) => json.encode(data.toJson());

// Ported to match the new backend's InboxMessageResponse (see
// D:\Hiilt\backend-hiilt-manasa-service\src\main\java\com\igma\manasa\dto\InboxMessageResponse.java)
// merged with an injected "inboxReplys" list (built client-side by
// ApiServices().findInboxDetail() from a separate GET messages/{id}/replies
// call). Denormalized display strings the old backend used to send
// (materialName, studentName, teacherName, type) no longer exist server-side
// -- kept nullable so existing UI `??` fallbacks keep compiling, but they're
// never populated from the API anymore.
class InboxQuestionDetailModel {
  String? type;
  int? id;
  bool? markEdit;
  String? body;
  String? image;
  List<InboxReply> inboxReplys;
  int? materialId;
  String? materialName;
  DateTime? sentDate;
  int? studentId;
  String? studentName;
  String? subject;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  String? teacherId;
  String? teacherName;

  InboxQuestionDetailModel({
    this.type,
    this.id,
    this.markEdit,
    this.body,
    this.image,
    required this.inboxReplys,
    this.materialId,
    this.materialName,
    this.sentDate,
    this.studentId,
    this.studentName,
    this.subject,
    this.teacherId,
    this.teacherName,
  });

  factory InboxQuestionDetailModel.fromJson(Map<String, dynamic> json) => InboxQuestionDetailModel(
    id: json["id"],
    body: json["body"],
    image: json["image"],
    inboxReplys: json["inboxReplys"] == null
        ? []
        : List<InboxReply>.from((json["inboxReplys"] as List).map((x) => InboxReply.fromJson(x))),
    materialId: json["materialId"],
    sentDate: json["sentDate"] == null ? null : DateTime.tryParse(json["sentDate"]),
    studentId: json["studentProfileId"],
    subject: json["subject"],
    teacherId: json["teacherUserId"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "body": body,
    "image": image,
    "inboxReplys": List<dynamic>.from(inboxReplys.map((x) => x.toJson())),
    "materialId": materialId,
    "sentDate": sentDate?.toIso8601String(),
    "studentProfileId": studentId,
    "subject": subject,
    "teacherUserId": teacherId,
  };
}

// Matches InboxReplyResponse (id, messageId, senderUserId, sentDate, body,
// image, audio, video). markEdit no longer exists.
class InboxReply {
  int? id;
  bool? markEdit;
  String? body;
  String? image;
  int? message;
  DateTime? sentDate;
  // The reply's sender is now a user_service UUID (could be the teacher or
  // the student, whichever didn't send the original message).
  String? senderUserId;

  InboxReply({
    this.id,
    this.markEdit,
    this.body,
    this.image,
    this.message,
    this.sentDate,
    this.senderUserId,
  });

  factory InboxReply.fromJson(Map<String, dynamic> json) => InboxReply(
    id: json["id"],
    body: json["body"],
    image: json["image"],
    message: json["messageId"],
    sentDate: json["sentDate"] == null ? null : DateTime.tryParse(json["sentDate"]),
    senderUserId: json["senderUserId"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "body": body,
    "image": image,
    "messageId": message,
    "sentDate": sentDate?.toIso8601String(),
    "senderUserId": senderUserId,
  };
}
