// To parse this JSON data, do
//
//     final loginData = loginDataFromJson(jsonString);

import 'dart:convert';

import '../models/student_model.dart';

LoginData loginDataFromJson(String str) => LoginData.fromJson(json.decode(str));

String loginDataToJson(LoginData data) => json.encode(data.toJson());

class LoginData {
  LoginData({
    this.studentData,
    this.success,
    this.token,
    this.teacherList,
  });

  Student? studentData;
  bool? success;
  String? token;
  List<TeacherListElement>? teacherList;

  factory LoginData.fromJson(Map<String, dynamic> json) => LoginData(
    studentData: Student.fromJson(json["studentData"]),
    success: json["success"],
    teacherList: List<TeacherListElement>.from(json["teacherList"].map((x) => TeacherListElement.fromJson(x))),
    token: json["token"],
  );

  Map<String, dynamic> toJson() => {
    "studentData": studentData,
    "success":  success,
    "teacherList": List<dynamic>.from(teacherList!.map((x) => x.toJson())),
    "token": token,
  };



}

class TeacherListElement {
  TeacherListElement({
    this.id,
    this.markEdit,
    this.materialList,
    this.name,
  });

  int? id;
  bool? markEdit;
  List<Material>? materialList;
  String? name;

  factory TeacherListElement.fromJson(Map<String, dynamic> json) => TeacherListElement(
    id: json["id"],
    markEdit: json["markEdit"],
    materialList: List<Material>.from(json["materialList"].map((x) => Material.fromJson(x))),
    name: json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "markEdit": markEdit,
    "materialList": materialList == null ? null : List<dynamic>.from(materialList!.map((x) => x.toJson())),
    "name": name,
  };
}
class Material {
  Material({
    this.id,
    this.markEdit,
    this.name,
  });

  int? id;
  bool? markEdit;
  String? name;

  factory Material.fromJson(Map<String, dynamic> json) => Material(
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
