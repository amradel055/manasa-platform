
import 'dart:convert';

import '../constants/helper.dart';


Exam examFromJson(String str) => Exam.fromJson(json.decode(str));

String examToJson(Exam data) => json.encode(data.toJson());

class Exam {
  Exam({
    this.type,
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.active,
    this.degree,
    this.score,
    this.examName,
    this.lessonId,
    this.lessonName,
    this.periodId,
    this.questionsList,
    this.schoolStagesId,
    this.schoolStagesName,
    this.teacherId,
    this.timer,
    this.numberQuestions,
    this.materialId,
    this.examType ,
    this.solutionTime
  });

  String? type;
  int? branchId;
  int? createdBy;
  int? numberQuestions;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  int? active;
  double? degree;
  double? score;
  String? examName;
  int? lessonId;
  String? lessonName;
  int? periodId;
  List<Question>? questionsList;
  int? schoolStagesId;
  String? schoolStagesName;
  int? teacherId;
  int? timer ;
  int? materialId;
  int? examType;
  int? solutionTime ;
  factory Exam.fromJson(Map<String, dynamic> json) => Exam(
    type: json["type"],
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
      numberQuestions: json["numberQuestions"],
    markEdit: json["markEdit"],
    active: json["active"],
    degree: json["degree"],
    examName: json["examName"],
    lessonId: json["lessonId"],
    lessonName: json["lessonName"],
    periodId: json["periodId"],
    questionsList: List<Question>.from(json["questionsList"].map((x) => Question.fromJson(x))),
    schoolStagesId: json["schoolStagesId"],
    schoolStagesName: json["schoolStagesName"],
    teacherId: json["teacherId"],
    timer : json["timer"],
    materialId: json["materialId"],
    examType: json["examType"] ,
    score: json["score"] ,
    solutionTime: json["solutionTime"]
  );

  Map<String, dynamic> toJson() => {
    "type": type,
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate!.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "active": active,
    "numberQuestions": numberQuestions,
    "degree": degree,
    "examName": examName,
    "lessonId": lessonId,
    "lessonName": lessonName,
    "periodId": periodId,
    "questionsList": List<dynamic>.from(questionsList!.map((x) => x.toJson())),
    "schoolStagesId": schoolStagesId,
    "schoolStagesName": schoolStagesName,
    "teacherId": teacherId,
    "timer" :timer,
    "materialId" : materialId,
    "examType":  examType,
    "score": score ,
    "solutionTime" : solutionTime
  };
}


class Question {
  Question({
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.answersdtoList,
    this.examsId,
    this.questionDegree,
    this.questionName,
    this.questionType,
    this.img ,
    this.parent,
  });

  int? branchId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  List<Answer>? answersdtoList;
  int? examsId;
  double? questionDegree;
  String? questionName;
  int? questionType;
  String? img ;
  int? parent ;

  factory Question.fromJson(Map<String, dynamic> json) => Question(
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    answersdtoList: List<Answer>.from(json["answersdtoList"].map((x) => Answer.fromJson(x))),
    examsId: json["examsId"],
    questionDegree: json["questionDegree"],
    questionName: json["questionName"],
    questionType: json["questionType"],
    img: json["img"] ,
    parent : json["parent"]
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate!.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "answersdtoList": List<dynamic>.from(answersdtoList!.map((x) => x.toJson())),
    "examsId": examsId,
    "questionDegree": questionDegree,
    "questionName": questionName,
    "questionType": questionType,
    "img" : img ,
    "parent" : parent
  };
}



class Answer {
  Answer({
    this.branchId,
    this.createdBy,
    this.createdDate,
    this.id,
    this.index,
    this.markEdit,
    this.answer,
    this.correct,
    this.examId,
    this.questionId,
    this.selected ,
    this.selection,
    this.img ,
    this.parent ,
    this.reason
  });

  int? branchId;
  int? createdBy;
  DateTime? createdDate;
  int? id;
  int? index;
  bool? markEdit;
  String? answer;
  int? correct;
  int? examId;
  int? questionId;
  int? selection ;
  String? img ;
  answerSelection? selected = answerSelection.unSelected;
  int? parent ;
  String? reason ;

  factory Answer.fromJson(Map<String, dynamic> json) => Answer(
    branchId: json["branchId"],
    createdBy: json["createdBy"],
    createdDate: DateTime.parse(json["createdDate"]),
    id: json["id"],
    index: json["index"],
    markEdit: json["markEdit"],
    answer: json["answer"],
    correct: json["correct"],
    examId: json["examId"],
    questionId: json["questionId"],
    selection:json["selection"],
    selected: answerSelection.unSelected,
    img: json["img"],
    parent: json["parent"] ,
    reason: json["reason"]

  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "createdBy": createdBy,
    "createdDate": createdDate!.toIso8601String(),
    "id": id,
    "index": index,
    "markEdit": markEdit,
    "answer": answer,
    "correct": correct,
    "examId": examId,
    "questionId": questionId,
    "selection" : selection,
    "img" : img,
    "parent" : parent,
    "reason" : reason
  };
}

