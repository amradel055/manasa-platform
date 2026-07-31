// To parse this JSON data, do
//
//     final schedule = scheduleFromJson(jsonString);

import 'dart:convert';

List<Schedule> scheduleFromJson(String str) => List<Schedule>.from(json.decode(str).map((x) => Schedule.fromJson(x)));

String scheduleToJson(List<Schedule> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class Schedule {
  Schedule({
    this.type,
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.appointmentsType,
    this.day,
    this.dayText,
    this.lessonLocation,
    this.schoolStageId,
    this.teacherId,
    this.teacherName,
    this.time,
    this.remarks ,
    this.time2 , this.time3 , this.time4
  });

  String? type;
  int? branchId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  int? appointmentsType;
  int? day;
  String? dayText;
  LessonLocation? lessonLocation;
  int? schoolStageId;
  int? teacherId;
  String? teacherName;
  String? time;
  String? time2;
  String? time3;
  String? time4;
  String? remarks;
  factory Schedule.fromJson(Map<String, dynamic> json) => Schedule(
    type: json["type"],
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    appointmentsType: json["appointmentsType"],
    day: json["day"],
    dayText: json["dayText"],
    lessonLocation: LessonLocation.fromJson(json["lessonLocation"]),
    schoolStageId: json["schoolStageId"],
    teacherId: json["teacherId"],
    teacherName: json["teacherName"],
    time: json["time"],
    remarks: json["remarks"],
    time2: json["time2"],
    time3: json["time3"],
    time4: json["time4"],

  );

  Map<String, dynamic> toJson() => {
    "type": type,
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate!.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "appointmentsType": appointmentsType,
    "day": day,
    "dayText": dayText,
    "lessonLocation": lessonLocation!.toJson(),
    "schoolStageId": schoolStageId,
    "teacherId": teacherId,
    "teacherName": teacherName,
    "time": time,
    "remarks": remarks ,
    "time2": time2,
    "time3": time3,
    "time4": time4,
  };
}

class LessonLocation {
  LessonLocation({
    this.markEdit,
    this.name,
  });

  bool? markEdit;
  String? name;

  factory LessonLocation.fromJson(Map<String, dynamic> json) => LessonLocation(
    markEdit: json["markEdit"],
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "markEdit": markEdit,
    "name": name,
  };
}
