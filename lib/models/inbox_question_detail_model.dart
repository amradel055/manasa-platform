// To parse this JSON data, do
//
//     final inboxQuestionDetailModel = inboxQuestionDetailModelFromJson(jsonString);

import 'dart:convert';

InboxQuestionDetailModel inboxQuestionDetailModelFromJson(String str) => InboxQuestionDetailModel.fromJson(json.decode(str));

String inboxQuestionDetailModelToJson(InboxQuestionDetailModel data) => json.encode(data.toJson());

class InboxQuestionDetailModel {
  String type;
  int id;
  bool markEdit;
  String body;
  String image;
  List<InboxReply> inboxReplys;
  int materialId;
  String materialName;
  DateTime sentDate;
  int studentId;
  String studentName;
  String subject;
  int teacherId;
  String teacherName;

  InboxQuestionDetailModel({
    required this.type,
    required this.id,
    required this.markEdit,
    required this.body,
    required this.image,
    required this.inboxReplys,
    required this.materialId,
    required this.materialName,
    required this.sentDate,
    required this.studentId,
    required this.studentName,
    required this.subject,
    required this.teacherId,
    required this.teacherName,
  });

  factory InboxQuestionDetailModel.fromJson(Map<String, dynamic> json) => InboxQuestionDetailModel(
    type: json["type"],
    id: json["id"],
    markEdit: json["markEdit"],
    body: json["body"],
    image: json["image"],
    inboxReplys: List<InboxReply>.from(json["inboxReplys"].map((x) => InboxReply.fromJson(x))),
    materialId: json["materialId"],
    materialName: json["materialName"],
    sentDate: DateTime.parse(json["sentDate"]),
    studentId: json["studentId"],
    studentName: json["studentName"],
    subject: json["subject"],
    teacherId: json["teacherId"],
    teacherName: json["teacherName"],
  );

  Map<String, dynamic> toJson() => {
    "type": type,
    "id": id,
    "markEdit": markEdit,
    "body": body,
    "image": image,
    "inboxReplys": List<dynamic>.from(inboxReplys.map((x) => x.toJson())),
    "materialId": materialId,
    "materialName": materialName,
    "sentDate": sentDate.toIso8601String(),
    "studentId": studentId,
    "studentName": studentName,
    "subject": subject,
    "teacherId": teacherId,
    "teacherName": teacherName,
  };
}

class InboxReply {
  int id;
  bool markEdit;
  String body;
  String image;
  int message;
  DateTime sentDate;

  InboxReply({
    required this.id,
    required this.markEdit,
    required this.body,
    required this.image,
    required this.message,
    required this.sentDate,
  });

  factory InboxReply.fromJson(Map<String, dynamic> json) => InboxReply(
    id: json["id"],
    markEdit: json["markEdit"],
    body: json["body"],
    image: json["image"],
    message: json["message"],
    sentDate: DateTime.parse(json["sentDate"]),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "markEdit": markEdit,
    "body": body,
    "image": image,
    "message": message,
    "sentDate": sentDate.toIso8601String(),
  };
}
