// To parse this JSON data, do
//
//     final schedule = scheduleFromJson(jsonString);

import 'dart:convert';

List<Schedule> scheduleFromJson(String str) => List<Schedule>.from(json.decode(str).map((x) => Schedule.fromJson(x)));

String scheduleToJson(List<Schedule> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

// Ported to match the new backend's AppointmentResponse (see
// D:\Hiilt\backend-hiilt-manasa-service\src\main\java\com\igma\manasa\dto\AppointmentResponse.java).
// `lessonLocationId` is now a bare int id (the old nested lessonLocation:
// {name, markEdit} object no longer exists) -- `lessonLocation` is kept as a
// field for backward compat with existing UI code but is never populated
// from this JSON anymore; ApiServices.lessonLocations() + a client-side join
// is the way to resolve a display name now (see data_handler_provider.dart).
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
    this.lessonLocationId,
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
  int? lessonLocationId;
  int? schoolStageId;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  String? teacherId;
  String? teacherName;
  String? time;
  String? time2;
  String? time3;
  String? time4;
  String? remarks;
  factory Schedule.fromJson(Map<String, dynamic> json) => Schedule(
    branchId: json["branchId"],
    id: json["id"],
    appointmentsType: json["appointmentsType"],
    day: json["day"],
    lessonLocationId: json["lessonLocationId"],
    schoolStageId: json["schoolStageId"],
    teacherId: json["teacherUserId"],
    time: json["time"],
    remarks: json["remarks"],
    time2: json["time2"],
    time3: json["time3"],
    time4: json["time4"],

  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "id": id,
    "appointmentsType": appointmentsType,
    "day": day,
    "lessonLocationId": lessonLocationId,
    "schoolStageId": schoolStageId,
    "teacherUserId": teacherId,
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
