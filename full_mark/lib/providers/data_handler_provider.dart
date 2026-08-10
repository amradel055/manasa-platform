
import 'dart:convert';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:full_mark/providers/auth_provider.dart';
import 'package:full_mark/providers/screen_providers/home_provider.dart';
import '../constants/strings.dart';
import '../constants/helper.dart';
import '../helper_methods/api_services.dart';
import '../helper_methods/chat_methods.dart';
import '../models/exam_model.dart';
import '../models/lesson_model.dart';
import '../models/login_data_model.dart';
import '../models/schedule_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DataHandler with ChangeNotifier {
  late List periods ;
  late List subjects ;
  late Exam? exam ;
  late List<Exam> examsList = [] ;
  late List<Exam> autoExamsList = [] ;
  late List<Exam> FirstexamsList = [] ;
  late List<Exam> lessonExams = [] ;
  late List<Lesson> lessons = [];
  late List<Lesson> freeLessons = [];
  late List allFeed = [] ;
  late List news = [] ;
  late List grades = [] ;
  late List<Schedule> studentSchedule = [] ;
  List<QuerySnapshot> chats = [] ;
  List<QuerySnapshot> meetings = [] ;
  List<Exam> previousExams = [] ;
  List studentOtherSchedules = [];
  List studentsLinks = [];
  List states = [] ;
  List cities = [] ;
  Map  teacherSchedules = {} ;
  bool lessonLoading = false ;
  bool connected = true ;

  Future getPeriodsByTeacher(teacherId) async{
    await ApiServices().periodsByTeacher(teacherId).then((res){
      if (res != null){
        periods = res ;
        notifyListeners();
      }else {
        periods = [] ;
        notifyListeners();
      }
    });
  }

  Future getSubjectsByTeacher(teacherId, BuildContext context) async{
    final schoolStagesId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    await ApiServices().subjectsByTeacher(teacherId, schoolStagesId).then((res){
      if (res != null){
        subjects = res ;
        notifyListeners();
      }else {
        subjects = [] ;
        notifyListeners();
      }
    });
  }

  Future getExamById(examId) async{
    await ApiServices().examsById(examId).then((res){
      if (res != null){
        exam = Exam.fromJson(res);
        notifyListeners();
      }else {

      }
    });
  }

  Future getExamByFilter(materialList ,examType , chapters , BuildContext context) async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData studentData =  LoginData.fromJson(json.decode(prefs.getString(dataKey)!));
    await ApiServices().examsByFilters(materialList ,examType , chapters , teacherId , studentData.studentData!.id).then((res){
      if (res != null){
        if(examType == 1){
        examsList = List<Exam>.from(res.map((x) => Exam.fromJson(x)));
        // exam = Exam.fromJson(res);
        notifyListeners();}
        else {
          exam = Exam.fromJson(res[0]);
          notifyListeners();
        }
      }else {

      }
    });
  }
  Future getAutoExamByFilter(materialList ,examType , chapters , BuildContext context) async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData studentData =  LoginData.fromJson(json.decode(prefs.getString(dataKey)!));
    await ApiServices().autoExamsByFilters(materialList ,examType , chapters , teacherId , studentData.studentData!.id).then((res){
      if (res != null){
        if(examType == 0){
          autoExamsList = List<Exam>.from(res.map((x) => Exam.fromJson(x)));
          // exam = Exam.fromJson(res);
          notifyListeners();}
        else {
          exam = Exam.fromJson(res[0]);
          notifyListeners();
        }
      }else {

      }
    });
  }

  // Submits the student's answers via POST exams/{id}/attempts -- the server
  // computes the score itself (never trusts a client-claimed correctness).
  // Builds {questionId: selectedAnswerId} from each question's currently
  // `answerSelection.selected` answer; unanswered questions are simply
  // omitted from the map (server treats a missing selection as unanswered/0
  // score for that question, see ExamService.submitAttempt).
  Future saveExam(Exam exam, BuildContext context) async{
    Map<int, int> selections = {};
    for (final question in exam.questionsList ?? []) {
      if (question.id == null) continue;
      for (final answer in question.answersdtoList ?? []) {
        if (answer.selected == answerSelection.selected && answer.id != null) {
          selections[question.id!] = answer.id!;
          break;
        }
      }
    }
    await ApiServices().submitExamAttempt(exam.id!, selections).then((res){
      if (res != null)
      {
       print(res);
      }
      else {

      }
    });
  }

  Future getLessons (materialId, BuildContext context)async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final studentId = context.read<AuthProvider>().student!.studentData!.id!;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getLessonsByFilter(teacherId , materialId , _student.studentData!.schoolStagesId,studentId).then((res){
      if (res != null){
        lessons = List<Lesson>.from(res.map((x) => Lesson.fromJson(x)));
        // exam = Exam.fromJson(res);
        // notifyListeners();
      }else {

      }
    });
  }
  Future getFreeLessons (materialId, BuildContext context)async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final studentId = context.read<AuthProvider>().student!.studentData!.id!;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getLessonsFreeByFilter(teacherId , materialId , _student.studentData!.schoolStagesId,studentId).then((res){
      if (res != null){
        print(res);

        freeLessons = List<Lesson>.from(res.map((x) => Lesson.fromJson(x)));
        // exam = Exam.fromJson(res);
        // notifyListeners();
      }else {

      }
    });
  }

  Future getAllFeed () async{
    await ApiServices().getAllFeed().then((value){
     if (value != null ){
       allFeed = value ;
       saveLocalNews(value);
       notifyListeners();
     }
    });
  }

  Future getStudentNews (BuildContext context) async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getStudentNews(_student.studentData!.schoolStagesId, teacherId).then((value){
      if (value != null ){
        news = value ;
        notifyListeners();
      }
    });
  }

  Future getGrades () async{
    await ApiServices().grades().then((value){
      if (value != null ){
        grades = value ;
        notifyListeners();
      }
    });
  }

  // Legacy day (Saturday=6..Friday=5) -> Arabic display name. The old backend
  // used to send this pre-computed as "dayText"; AppointmentResponse only
  // sends the raw `day` int now, so it's computed client-side here (same
  // mapping used server-side in the teacher web app's Schedule.js dayList).
  static const Map<int, String> _dayNames = {
    6: "السبت", 7: "الاحد", 1: "الاتنين", 2: "الثلاثاء",
    3: "الاربعاء", 4: "الخميس", 5: "الجمعه",
  };

  /// AppointmentResponse only sends `lessonLocationId` (a bare int) and
  /// `teacherUserId` (a UUID) now -- the old nested lessonLocation.name and a
  /// teacherName string used to come pre-joined from the server. Resolve both
  /// client-side: lesson location names via ApiServices().lessonLocations(),
  /// teacher names via the teacherList already persisted at login (a student
  /// only ever sees schedules for teachers they're enrolled with, which is
  /// exactly that list).
  Future<Map<String, dynamic>> _buildTeacherSchedules(List<Schedule> schedules) async {
    Map<int, String> locationNames = {};
    try {
      var locations = await ApiServices().lessonLocations();
      if (locations != null) {
        for (final loc in locations) {
          locationNames[loc["id"]] = loc["name"] ?? "";
        }
      }
    } catch (_) {}

    Map<String, String> teacherNames = {};
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      String? raw = prefs.getString(dataKey);
      if (raw != null) {
        List teacherList = json.decode(raw)["teacherList"] ?? [];
        for (final t in teacherList) {
          teacherNames["${t["id"]}"] = t["name"] ?? "";
        }
      }
    } catch (_) {}

    Map<String, dynamic> result = {};
    for (int i = 0; i < schedules.length; i++) {
      final s = schedules[i];
      String teacherName = teacherNames[s.teacherId] ?? s.teacherId ?? "";
      String locationName = locationNames[s.lessonLocationId] ?? "";
      String dayText = _dayNames[s.day] ?? "";

      List time = s.time != null ? s.time!.split("T") : [] ;
      List times = time.length > 1 ?  time[1].split("+")[0].split(":") : [];
      String firstTime =times.isNotEmpty ? "${times[0]} : ${times[1]}" : "" ;

      List time2 = s.time2 != null ? s.time2!.split("T") : [] ;
      List times2 = time2.length > 1 ? time2[1].split("+")[0].split(":") : [];
      String secondTime =times2.isNotEmpty ? "${times2[0]} : ${times2[1]}" : "" ;

      List time3 = s.time3 != null ? s.time3!.split("T"): [] ;
      List times3 = time3.length > 1 ? time3[1].split("+")[0].split(":") : [];
      String thirdTime = times3.isNotEmpty ? "${times3[0]} : ${times3[1]}" : "" ;

      List time4 = s.time4 != null ?  s.time4!.split("T") : [] ;
      List times4 = time4.length > 1 ? time4[1].split("+")[0].split(":") : [];
      String fourthTime = times4.isNotEmpty ? "${times4[0]} : ${times4[1]}" : "";

      if (result.containsKey(teacherName)) {
        Map schedule = result[teacherName];
        List entries = schedule[locationName] ?? [];
        entries.add([dayText , [firstTime , secondTime , thirdTime , fourthTime] , s.remarks ]);
        schedule[locationName] = entries;
      } else {
        result[teacherName] = {
          locationName: [[dayText, [firstTime , secondTime , thirdTime , fourthTime], s.remarks]]
        };
      }
    }
    return result;
  }

  Future scheduleBySchoolStageId () async{
    await checkConnectivity() ;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().scheduleBySchoolStageId(_student.studentData!.schoolStagesId!).then((value) async {
      if ( value != null ){
        saveLocalSchedules(value);
        studentSchedule = List<Schedule>.from(value.map((x) => Schedule.fromJson(x)));
        notifyListeners();
        teacherSchedules = await _buildTeacherSchedules(studentSchedule);
        notifyListeners();
        return studentSchedule ;
      }
     });


  }

  Future getAvailableChats () async {
   await ChatMethods().getAllAvailableChats().then((value){
     chats.add(value);
   });
  }

  Future getAvailableZoom () async {
    await ChatMethods().getAllAvailableChats().then((value){
      meetings.add(value);
      notifyListeners();
    });
  }


  Future getPreviousExams() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().findStudentExams(_student.studentData!.id!, _student.studentData!.schoolStagesId!).then((value){
      if(value != null){
        previousExams = List<Exam>.from(value.map((x) => Exam.fromJson(x)));
        saveLocalExams(value);
        notifyListeners();
      }
    });
  }

  Future getStudentOtherSchedules(type, BuildContext context) async {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getOtherAppointmentsByStudentId(teacherId , _student.studentData!.schoolStagesId! ,type ).then((value){
      if(value != null){
        studentOtherSchedules = value ;
        notifyListeners();
      }
    });
  }
  Future getLiks(type, BuildContext context) async {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getLinks(teacherId , _student.studentData!.schoolStagesId! ,type ).then((value){
      if(value != null){
        studentsLinks = value ;
        notifyListeners();
      }
    });
  }

  Future getCities() async {
    await ApiServices().findCities().then((value){
      if(value != null){
        cities = value ;
      }
    });
  }

  Future getCityState(stateId) async{
    await ApiServices().findCityState(stateId).then((value){
      if(value != null){
        states = value ;
      }
    });
  }

  Future getLessonExam (lessonId)async {

    lessonLoading = true ;
    notifyListeners();
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().examsByLesson(lessonId , _student.studentData!.id!).then((res){
      if(res != null){
        lessonExams = List<Exam>.from(res.map((x) => Exam.fromJson(x)));
        lessonLoading = false ;
        notifyListeners();
      }
    });
  }

  clearLesson(){
    lessons = [] ;
    notifyListeners();
  }


   Future checkConnectivity()async{
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.mobile) {
      // I am connected to a mobile network.
      connected = true ;
      notifyListeners();
    } else if (connectivityResult == ConnectivityResult.wifi) {
      // I am connected to a wifi network.
        connected = true ;
      notifyListeners();
    } else if (connectivityResult == ConnectivityResult.none){
      connected = false ;
      notifyListeners();
    }


    return connected;

 }

  saveLocalSchedules(value)async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString("localSchedules", jsonEncode(value));
  }

  Future getLocalSchedules()async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var value = jsonDecode(prefs.getString("localSchedules")!);
      studentSchedule = List<Schedule>.from(value.map((x) => Schedule.fromJson(x)));
      notifyListeners();
      teacherSchedules = await _buildTeacherSchedules(studentSchedule);
      notifyListeners();
      return studentSchedule ;

  }


  saveLocalNews(value)async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString("localNews", jsonEncode(value));
  }

  getLocalNews()async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var value = jsonDecode(prefs.getString("localNews")!);
    allFeed = value ;
    notifyListeners();
  }

  saveLocalExams(value)async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString("localExams", jsonEncode(value));
  }

  getLocalExams()async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var value = jsonDecode(prefs.getString("localExams")!);
    previousExams = previousExams = List<Exam>.from(value.map((x) => Exam.fromJson(x))) ;
    notifyListeners();
  }
  }
