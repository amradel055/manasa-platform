import 'dart:convert';

List<InboxQuestionModel> inboxQuestionModelFromJson(dynamic str) => List<InboxQuestionModel>.from(str.map((x) => InboxQuestionModel.fromJson(x)));

String inboxQuestionModelToJson(List<InboxQuestionModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

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
    materialName: json["materialName"],
    image: json["image"],
    teacherName: json["teacherName"],
    sentDate: DateTime.parse(json["sentDate"]),

    subject: json["subject"],
  );

  Map<String, dynamic> toJson() => {
    "subject": subject,
    "materialName": materialName,
    "image": image,
    "teacherName": teacherName,
    "sentDate": sentDate!.toIso8601String(),
    "id": id,

  };
}

// class TeacherAnswersDto {
//   TeacherAnswersDto({
//     required this.id,
//     required this.image,
//     required this.teacherAnswerDetailDtoList,
//
//   });
//
//   final int branchId;
//   final int createdBy;
//   final DateTime createdDate;
//   final int id;
//   final int index;
//   final bool markEdit;
//   final String image;
//   final int studentQuestionId;
//   final List<TeacherAnswerDetailDtoList> teacherAnswerDetailDtoList;
//   final String text;
//
//   factory TeacherAnswersDto.fromJson(Map<String, dynamic> json) => TeacherAnswersDto(
//     branchId: json["branchId"],
//     createdBy: json["createdBy"],
//     createdDate: DateTime.parse(json["createdDate"]),
//     id: json["id"],
//     index: json["index"],
//     markEdit: json["markEdit"],
//     image: json["image"],
//     studentQuestionId: json["studentQuestionId"],
//     teacherAnswerDetailDtoList: List<TeacherAnswerDetailDtoList>.from(json["teacherAnswerDetailDTOList"].map((x) => TeacherAnswerDetailDtoList.fromJson(x))),
//     text: json["text"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "branchId": branchId,
//     "createdBy": createdBy,
//     "createdDate": createdDate.toIso8601String(),
//     "id": id,
//     "index": index,
//     "markEdit": markEdit,
//     "image": image,
//     "studentQuestionId": studentQuestionId,
//     "teacherAnswerDetailDTOList": List<dynamic>.from(teacherAnswerDetailDtoList.map((x) => x.toJson())),
//     "text": text,
//   };
// }

// class TeacherAnswerDetailDtoList {
//   TeacherAnswerDetailDtoList({
//     required this.branchId,
//     required this.createdBy,
//     required this.createdDate,
//     required this.id,
//     required this.index,
//     required this.markEdit,
//     required this.description,
//     required this.image,
//     required this.teacherAnswerId,
//   });
//
//   final int branchId;
//   final int createdBy;
//   final DateTime createdDate;
//   final int id;
//   final int index;
//   final bool markEdit;
//   final String description;
//   final String image;
//   final int teacherAnswerId;
//
//   factory TeacherAnswerDetailDtoList.fromJson(Map<String, dynamic> json) => TeacherAnswerDetailDtoList(
//     branchId: json["branchId"],
//     createdBy: json["createdBy"],
//     createdDate: DateTime.parse(json["createdDate"]),
//     id: json["id"],
//     index: json["index"],
//     markEdit: json["markEdit"],
//     description: json["description"],
//     image: json["image"],
//     teacherAnswerId: json["teacherAnswerId"],
//   );
//
//   Map<String, dynamic> toJson() => {
//     "branchId": branchId,
//     "createdBy": createdBy,
//     "createdDate": createdDate.toIso8601String(),
//     "id": id,
//     "index": index,
//     "markEdit": markEdit,
//     "description": description,
//     "image": image,
//     "teacherAnswerId": teacherAnswerId,
//   };
// }
