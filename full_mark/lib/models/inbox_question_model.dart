import 'dart:convert';

List<InboxQuestionModel> inboxQuestionModelFromJson(dynamic str) => List<InboxQuestionModel>.from(str.map((x) => InboxQuestionModel.fromJson(x)));

String inboxQuestionModelToJson(List<InboxQuestionModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

// Ported to match the new backend's InboxMessageResponse (list view -- see
// D:\Hiilt\backend-hiilt-manasa-service\src\main\java\com\igma\manasa\dto\InboxMessageResponse.java).
// materialName/teacherName no longer exist server-side (only materialId/
// teacherUserId) -- kept nullable so existing `??` fallbacks keep compiling,
// but never populated from the API.
class InboxQuestionModel {
  InboxQuestionModel({
    required this.subject,
    required this.materialName,
    required this.sentDate,
    required this.id,
    required this.image,
    required this.teacherName,

  });

  final String? materialName;
  final DateTime ?sentDate;

  final int ?id;
  final String? image;
  final String ?teacherName;
  final String? subject;

  factory InboxQuestionModel.fromJson(Map<String, dynamic> json) => InboxQuestionModel(

    id: json["id"],
    materialName: null,
    image: json["image"],
    teacherName: null,
    sentDate: json["sentDate"] == null ? null : DateTime.tryParse(json["sentDate"]),

    subject: json["subject"],
  );

  Map<String, dynamic> toJson() => {
    "subject": subject,
    "image": image,
    "sentDate": sentDate?.toIso8601String(),
    "id": id,

  };
}
