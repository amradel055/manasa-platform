// To parse this JSON data, do
//
//     final lesson = lessonFromJson(jsonString);

import 'dart:convert';

List<Lesson> lessonFromJson(String str) => List<Lesson>.from(json.decode(str).map((x) => Lesson.fromJson(x)));

String lessonToJson(List<Lesson> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

// Ported to match the new backend's LessonResponse (see
// D:\Hiilt\backend-hiilt-manasa-service\src\main\java\com\igma\manasa\dto\LessonResponse.java).
// `videoLinks`/`vimeoLinks`/`youtubeLinks`/`images` map directly by name now.
// `voices` replaces the old `lessonVoiceList` key. `schoolStagesGuideId` is now
// a plain int id (was a nested {id,name,markEdit} object -- nothing in this app
// read `.name`/`.markEdit` off it, so the type change is safe). `documents`
// (lesson attachments) are NOT embedded in the response anymore -- they live
// behind a separate `GET /documents?lessonId=` call (MiscController) that
// isn't wired up here; defaults to [] so the existing `.documents!.isNotEmpty`
// UI code in lesson_screen.dart doesn't crash, it just shows nothing until
// that call is added. Legacy-only fields (type/companyId/createdBy/
// createdDate/index/markEdit/schoolStagesName/vimeoLink/youtubeLink -- the
// singular ones, not the *Links lists) no longer exist server-side and stay
// nullable/unused so existing `??` fallbacks keep compiling.
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
    this.materialId,
    this.monthId,
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
  int? materialId;
  int? monthId;
  int? schoolStagesGuideId;
  String? schoolStagesName;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  String? teacherId;
  String? vimeoLink;
  String? youtubeLink;
  String? image;
  String? voiceLink;
  String? thumb;
  List<String>? lessonVoiceList;

  factory Lesson.fromJson(Map<String, dynamic> json) => Lesson(
    branchId: json["branchId"],
    id: json["id"],
    documents: [],
    images: json["images"] == null ? [] : List<dynamic>.from(json["images"]),
    videoLinks: json["videoLinks"] == null ? [] : List<dynamic>.from(json["videoLinks"]),
    youtubeLinks: json["youtubeLinks"] == null ? [] : List<dynamic>.from(json["youtubeLinks"]),
    vimeoLinks: json["vimeoLinks"] == null ? [] : List<dynamic>.from(json["vimeoLinks"]),
    lessonVoiceList: json["voices"] == null ? [] : List<String>.from(json["voices"]),
    lessonDescription: json["lessonDescription"],
    lessonName: json["lessonName"],
    lessonType: json["lessonType"],
    periodId: json["periodId"],
    materialId: json["materialId"],
    monthId: json["monthId"],
    schoolStagesGuideId: json["schoolStagesGuideId"],
    teacherId: json["teacherUserId"],
    image: json["image"],
    voiceLink: json["voiceLink"],
    thumb: json["thumb"],
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "id": id,
    "images": images ?? [],
    "voices": lessonVoiceList ?? [],
    "vimeoLinks": vimeoLinks ?? [],
    "youtubeLinks": youtubeLinks ?? [],
    "videoLinks": videoLinks ?? [],
    "lessonDescription": lessonDescription,
    "lessonName": lessonName,
    "lessonType": lessonType,
    "periodId": periodId,
    "materialId": materialId,
    "monthId": monthId,
    "schoolStagesGuideId": schoolStagesGuideId,
    "teacherUserId": teacherId,
    "image": image,
    "voiceLink": voiceLink,
    "thumb": thumb,
  };
}
