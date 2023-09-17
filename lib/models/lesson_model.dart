// To parse this JSON data, do
//
//     final lesson = lessonFromJson(jsonString);

import 'dart:convert';

List<Lesson> lessonFromJson(String str) => List<Lesson>.from(json.decode(str).map((x) => Lesson.fromJson(x)));

String lessonToJson(List<Lesson> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class Lesson {
  Lesson({
    this.type,
    this.branchId,
    this.companyId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.documents,
    this.images,
    this.lessonDescription,
    this.lessonName,
    this.lessonType,
    this.periodId,
    this.schoolStagesGuideId,
    this.schoolStagesName,
    this.teacherId,
    this.vimeoLink,
    this.lessonVoiceList,
    this.videoLinks,
    this.vimeoLinks,
    this.youtubeLinks,
    this.youtubeLink,
    this.image,
    this.voiceLink,
    this.thumb,
  });

  String? type;
  int? branchId;
  int? companyId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  List<dynamic>? documents;
  List<dynamic>? videoLinks;
  List<dynamic>? vimeoLinks;
  List<dynamic>? youtubeLinks;

  List<dynamic>? images;
  String? lessonDescription;
  String? lessonName;
  int? lessonType;
  int? periodId;
  SchoolStagesGuideId? schoolStagesGuideId;
  String? schoolStagesName;
  int? teacherId;
  String? vimeoLink;
  String? youtubeLink;
  String? image;
  String? voiceLink;
  String? thumb;
  List<String>? lessonVoiceList;

  factory Lesson.fromJson(Map<String, dynamic> json) => Lesson(
    type: json["type"],
    branchId: json["branchId"],
    companyId: json["companyId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    documents: List<dynamic>.from(json["documents"].map((x) => x)),
    images: List<dynamic>.from(json["images"].map((x) => x)),
    videoLinks: List<dynamic>.from(json["videoLinks"].map((x) => x)),
    youtubeLinks: List<dynamic>.from(json["youtubeLinks"].map((x) => x)),
    vimeoLinks: List<dynamic>.from(json["vimeoLinks"].map((x) => x)),


    lessonVoiceList: List<String>.from(json["lessonVoiceList"].map((x) => x)),
    lessonDescription: json["lessonDescription"],
    lessonName: json["lessonName"],
    lessonType: json["lessonType"],
    periodId: json["periodId"],
    schoolStagesGuideId: SchoolStagesGuideId.fromJson(json["schoolStagesGuideId"]),
    schoolStagesName: json["schoolStagesName"],
    teacherId: json["teacherId"],
    vimeoLink: json["vimeoLink"],
    youtubeLink: json["youtubeLink"],
    image: json["image"] == null ? null : json["image"],
    voiceLink: json["voiceLink"] == null ? null : json["voiceLink"],
    thumb: json["thumb"] == null ? null : json["thumb"],
  );

  Map<String, dynamic> toJson() => {
    "type": type,
    "branchId": branchId,
    "companyId": companyId,
    "createdBy": createdBy,
    "createdDate": createdDate!.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "documents": List<dynamic>.from(documents!.map((x) => x)),
    "lessonVoiceList": List<dynamic>.from(lessonVoiceList!.map((x) => x)),
    "images": List<dynamic>.from(images!.map((x) => x)),
    "vimeoLinks": List<dynamic>.from(vimeoLinks!.map((x) => x)),
    "youtubeLinks": List<dynamic>.from(youtubeLinks!.map((x) => x)),
    "videoLinks": List<dynamic>.from(videoLinks!.map((x) => x)),
    "lessonDescription": lessonDescription,
    "lessonName": lessonName,
    "lessonType": lessonType,
    "periodId": periodId,
    "schoolStagesGuideId": schoolStagesGuideId!.toJson(),
    "schoolStagesName": schoolStagesName,
    "teacherId": teacherId,
    "vimeoLink": vimeoLink,
    "youtubeLink": youtubeLink,
    "image": image == null ? null : image,
    "voiceLink": voiceLink == null ? null : voiceLink,
    "thumb": thumb == null ? null : thumb,
  };
}

class SchoolStagesGuideId {
  SchoolStagesGuideId({
    this.id,
    this.markEdit,
    this.name,
  });

  int? id;
  bool? markEdit;
  String? name;

  factory SchoolStagesGuideId.fromJson(Map<String, dynamic> json) => SchoolStagesGuideId(
    id: json["id"],
    markEdit: json["markEdit"],
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "markEdit": markEdit,
    "name": name,
  };
}
