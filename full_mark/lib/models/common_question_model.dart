// To parse this JSON data, do
//
//     final commonQuestionModel = commonQuestionModelFromJson(jsonString);

import 'dart:convert';
List<CommonQuestionModel> commonQuestionModelFromJson(dynamic str) => List<CommonQuestionModel>.from(str.map((x) => CommonQuestionModel.fromJson(x)));

String commonQuestionModelToJson(List<CommonQuestionModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

// List<CommonQuestionModel> commonQuestionModelFromJson(String str) => List<CommonQuestionModel>.from(json.decode(str).map((x) => CommonQuestionModel.fromJson(x)));
//
// String commonQuestionModelToJson(List<CommonQuestionModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CommonQuestionModel {
  String? type;
  int? branchId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  String? image;
  int? materialId;
  QuestionAnswerDto? questionAnswerDto;
  int? schoolId;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  String? teacherId;
  String? text;

  CommonQuestionModel({
    this.type,
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.image,
    this.materialId,
    this.questionAnswerDto,
    this.schoolId,
    this.teacherId,
    this.text,
  });

  factory CommonQuestionModel.fromJson(Map<String, dynamic> json) => CommonQuestionModel(
    type: json["type"],
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: json["createdDate"] == null ? null : DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    image: json["image"],
    materialId: json["materialId"],
    questionAnswerDto: json["answer"] == null ? null : QuestionAnswerDto.fromJson(json["answer"]),
    teacherId: json["teacherUserId"],
    text: json["text"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "image": image,
    "materialId": materialId,
    "answer": questionAnswerDto?.toJson(),
    "teacherUserId": teacherId,
    "text": text,
  };
}

class QuestionAnswerDto {
  int? branchId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  List<dynamic>? answerDetailDtoList;
  String? image;
  int? questionCommonId;
  String? text;

  QuestionAnswerDto({
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.answerDetailDtoList,
    this.image,
    this.questionCommonId,
    this.text,
  });

  factory QuestionAnswerDto.fromJson(Map<String, dynamic> json) => QuestionAnswerDto(
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: json["createdDate"] == null ? null : DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    answerDetailDtoList: json["answerDetailDTOList"] == null ? [] : List<dynamic>.from(json["answerDetailDTOList"]!.map((x) => x)),
    image: json["image"],
    questionCommonId: json["questionCommonId"],
    text: json["text"],
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate?.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "answerDetailDTOList": answerDetailDtoList == null ? [] : List<dynamic>.from(answerDetailDtoList!.map((x) => x)),
    "image": image,
    "questionCommonId": questionCommonId,
    "text": text,
  };
}
