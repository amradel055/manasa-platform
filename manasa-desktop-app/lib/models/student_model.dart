// To parse this JSON data, do
//
//     final student = studentFromJson(jsonString);

import 'dart:convert';

Student studentFromJson(String str) => Student.fromJson(json.decode(str));

String studentToJson(Student data) => json.encode(data.toJson());

class Student {
  Student({
    this.branchId,
    this.code,
    this.email,
    this.familyName,
    this.fatherJob,
    this.fatherMobile,
    this.fatherName,
    this.firstName,
    this.gender,
    this.governorateId,
    this.grandFatherName,
    this.id,
    this.imagePath,
    this.macId,
    this.mobile,
    this.password,
    this.schoolStagesId,
    this.stateId,
    this.typeLogin,
    this.userName,
    this.teacherList
  });

  int? branchId;
  String? code;
  String? email;
  String? familyName;
  String? fatherJob;
  String? fatherMobile;
  String? fatherName;
  String? firstName;
  int? gender;
  int? governorateId;
  String? grandFatherName;
  int? id;
  String? imagePath;
  String? macId;
  String? mobile;
  String? password;
  int? schoolStagesId;
  int? stateId;
  int? typeLogin;
  String? userName;
  List? teacherList ;

  factory Student.fromJson(Map<String, dynamic> json) => Student(
    branchId: json["branchId"],
    code: json["code"],
    email: json["email"],
    familyName: json["familyName"],
    fatherJob: json["fatherJob"],
    fatherMobile: json["fatherMobile"],
    fatherName: json["fatherName"],
    firstName: json["firstName"],
    gender: json["gender"],
    governorateId: json["governorateId"],
    grandFatherName: json["grandFatherName"],
    id: json["id"],
    imagePath: json["imagePath"],
    macId: json["macId"],
    mobile: json["mobile"],
    password: json["password"],
    schoolStagesId: json["schoolStagesId"],
    stateId: json["stateId"],
    typeLogin: json["typeLogin"],
    userName: json["userName"],
    teacherList: json["teacherList"],
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "code": code ,
    "email": email ,
    "familyName": familyName,
    "fatherJob": fatherJob,
    "fatherMobile": fatherMobile,
    "fatherName": fatherName,
    "firstName": firstName,
    "gender": gender,
    "governorateId": governorateId ,
    "grandFatherName": grandFatherName,
    "id": id ,
    "imagePath": imagePath,
    "macId": macId ,
    "mobile": mobile,
    "password": password,
    "schoolStagesId": schoolStagesId ,
    "stateId": stateId ,
    "typeLogin": typeLogin,
    "userName": userName ,
    "teacherList" : teacherList
  };
}
