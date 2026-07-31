import 'dart:convert';

List<QuestionModel> questionModelFromJson(dynamic str) => List<QuestionModel>.from(str.map((x) => QuestionModel.fromJson(x)));

String questionModelToJson(List<QuestionModel> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class QuestionModel {
  QuestionModel({
    required this.type,
    required this.branchId,
    required this.createdBy,
    required this.createdDate,
    required this.id,
    required this.index,
    required this.markEdit,
    required this.image,
    required this.materialId,
    required this.questionAnswerDto,
    required this.teacherId,
    required this.text,
  });

  final String type;
  final int branchId;
  final int createdBy;
  final DateTime createdDate;
  final int id;
  final int index;
  final bool markEdit;
  final String image;
  final int materialId;
  final QuestionAnswerDto questionAnswerDto;
  final int teacherId;
  final String text;

  factory QuestionModel.fromJson(Map<String, dynamic> json) => QuestionModel(
    type: json["type"],
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    image: json["image"],
    materialId: json["materialId"],
    questionAnswerDto: QuestionAnswerDto.fromJson(json["questionAnswerDTO"]),
    teacherId: json["teacherId"],
    text: json["text"],
  );

  Map<String, dynamic> toJson() => {
    "type": type,
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "image": image,
    "materialId": materialId,
    "questionAnswerDTO": questionAnswerDto.toJson(),
    "teacherId": teacherId,
    "text": text,
  };
}

class QuestionAnswerDto {
  QuestionAnswerDto({
    required this.branchId,
    required this.createdBy,
    required this.createdDate,
    required this.id,
    required this.index,
    required this.markEdit,
    required this.answerDetailDtoList,
    required this.image,
    required this.questionCommonId,
    required this.text,
  });

  final int branchId;
  final int createdBy;
  final DateTime createdDate;
  final int id;
  final int index;
  final bool markEdit;
  final List<AnswerDetailDtoList> answerDetailDtoList;
  final String image;
  final int questionCommonId;
  final String text;

  factory QuestionAnswerDto.fromJson(Map<String, dynamic> json) => QuestionAnswerDto(
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    answerDetailDtoList: List<AnswerDetailDtoList>.from(json["answerDetailDTOList"].map((x) => AnswerDetailDtoList.fromJson(x))),
    image: json["image"],
    questionCommonId: json["questionCommonId"],
    text: json["text"],
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "answerDetailDTOList": List<dynamic>.from(answerDetailDtoList.map((x) => x.toJson())),
    "image": image,
    "questionCommonId": questionCommonId,
    "text": text,
  };
}

class AnswerDetailDtoList {
  AnswerDetailDtoList({
    required this.branchId,
    required this.createdBy,
    required this.createdDate,
    required this.id,
    required this.index,
    required this.markEdit,
    required this.description,
    required this.image,
    required this.questionAnswerId,
  });

  final int branchId;
  final int createdBy;
  final DateTime createdDate;
  final int id;
  final int index;
  final bool markEdit;
  final String description;
  final String image;
  final int questionAnswerId;

  factory AnswerDetailDtoList.fromJson(Map<String, dynamic> json) => AnswerDetailDtoList(
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    description: json["description"],
    image: json["image"],
    questionAnswerId: json["questionAnswerId"],
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "description": description,
    "image": image,
    "questionAnswerId": questionAnswerId,
  };
}
