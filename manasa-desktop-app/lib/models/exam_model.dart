
import 'dart:convert';

import '../constants/helper.dart';


Exam examFromJson(String str) => Exam.fromJson(json.decode(str));

String examToJson(Exam data) => json.encode(data.toJson());

// Ported to match the new backend's ExamResponse (see
// D:\Hiilt\backend-hiilt-manasa-service\src\main\java\com\igma\manasa\dto\ExamResponse.java).
// Fields the old backend used to send that no longer exist server-side
// (createdDate, createdBy, index, markEdit, type, schoolStagesName,
// lessonName, numberQuestions) are kept as nullable so existing UI code that
// reads them with `??` fallbacks keeps compiling, but they are never
// populated from the API anymore -- do not rely on them being non-null.
// `questionsList` is null for list-view responses (GET exams/available,
// GET exams/solved) and only populated for a single exam's detail
// (GET exams/{id}/take).
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
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  String? teacherId;
  int? timer ;
  int? materialId;
  int? examType;
  int? solutionTime ;

  factory Exam.fromJson(Map<String, dynamic> json) => Exam(
    branchId: json["branchId"],
    id: json["id"],
    numberQuestions: json["numberQuestions"],
    active: json["active"],
    degree: (json["degree"] as num?)?.toDouble(),
    examName: json["examName"],
    lessonId: json["lessonId"],
    periodId: json["periodId"],
    questionsList: json["questions"] == null
        ? null
        : List<Question>.from((json["questions"] as List).map((x) => Question.fromJson(x))),
    schoolStagesId: json["schoolStagesId"],
    teacherId: json["teacherUserId"],
    timer : json["timer"],
    materialId: json["materialId"],
    examType: json["examType"] ,
    score: (json["score"] as num?)?.toDouble() ,
    solutionTime: json["solutionTime"]
  );

  Map<String, dynamic> toJson() => {
    "branchId": branchId,
    "id": id,
    "active": active,
    "numberQuestions": numberQuestions,
    "degree": degree,
    "examName": examName,
    "lessonId": lessonId,
    "periodId": periodId,
    "questions": questionsList == null ? null : List<dynamic>.from(questionsList!.map((x) => x.toJson())),
    "schoolStagesId": schoolStagesId,
    "teacherUserId": teacherId,
    "timer" :timer,
    "materialId" : materialId,
    "examType":  examType,
    "score": score ,
    "solutionTime" : solutionTime
  };
}


// Matches QuestionResponse (questionName, questionType, questionDegree, img,
// groupId, answers). branchId/createdBy/createdDate/index/markEdit/examsId/
// parent no longer exist server-side.
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
    this.groupId,
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
  int? groupId;

  factory Question.fromJson(Map<String, dynamic> json) => Question(
    id: json["id"],
    answersdtoList: json["answers"] == null
        ? []
        : List<Answer>.from((json["answers"] as List).map((x) => Answer.fromJson(x))),
    questionDegree: (json["questionDegree"] as num?)?.toDouble(),
    questionName: json["questionName"],
    questionType: json["questionType"],
    img: json["img"] ,
    groupId: json["groupId"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "answers": List<dynamic>.from((answersdtoList ?? []).map((x) => x.toJson())),
    "questionDegree": questionDegree,
    "questionName": questionName,
    "questionType": questionType,
    "img" : img ,
    "groupId": groupId,
  };
}



// Matches AnswerResponse (answer, correct (now a real bool), reason, img).
// branchId/createdBy/createdDate/index/markEdit/examId/questionId/selection/
// parent no longer exist server-side. Note: for GET exams/{id}/take
// specifically, `correct` always comes back false regardless of the real
// value (the backend never reveals it before an attempt is submitted) -- see
// ExamService.getForTaking server-side.
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
  bool? correct;
  int? examId;
  int? questionId;
  int? selection ;
  String? img ;
  answerSelection? selected = answerSelection.unSelected;
  int? parent ;
  String? reason ;

  factory Answer.fromJson(Map<String, dynamic> json) => Answer(
    id: json["id"],
    answer: json["answer"],
    correct: json["correct"] as bool?,
    selected: answerSelection.unSelected,
    img: json["img"],
    reason: json["reason"]
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "answer": answer,
    "correct": correct,
    "img" : img,
    "reason" : reason
  };
}
