
import 'dart:convert';
// import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:desktop_screen/providers/auth_provider.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../constants/strings.dart';
import '../helper_methods/api_services.dart';
import '../helper_methods/chat_methods.dart';
import '../models/exam_model.dart';
import '../models/lesson_model.dart';
import '../models/login_data_model.dart';
import '../models/schedule_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'screen_providers/home_provider.dart';

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
  // List<QuerySnapshot> chats = [] ;
  // List<QuerySnapshot> meetings = [] ;
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
    final studentId = context.read<AuthProvider>().student!.studentData!.id!;
    //
    // SharedPreferences prefs = await SharedPreferences.getInstance();
    // LoginData studentData =  LoginData.fromJson(json.decode(prefs.getString(dataKey)!));
    await ApiServices().examsByFilters(materialList ,examType , chapters , teacherId , studentId).then((res){
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
  // Future getAutoExamByFilter(materialList ,examType , chapters , BuildContext context) async{
  //   final teacherId = context.read<HomeProvider>().selectedTeacherId;
  //   SharedPreferences prefs = await SharedPreferences.getInstance();
  //   LoginData studentData =  LoginData.fromJson(json.decode(prefs.getString(dataKey)!));
  //   await ApiServices().autoExamsByFilters(materialList ,examType , chapters , teacherId , studentData.studentData!.id).then((res){
  //     if (res != null){
  //       if(examType == 0){
  //         autoExamsList = List<Exam>.from(res.map((x) => Exam.fromJson(x)));
  //         // exam = Exam.fromJson(res);
  //         notifyListeners();}
  //       else {
  //         exam = Exam.fromJson(res[0]);
  //         notifyListeners();
  //       }
  //     }else {
  //
  //     }
  //   });
  // }

  Future saveExam(Exam exam, BuildContext context) async{
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    // SharedPreferences prefs = await SharedPreferences.getInstance();
    // LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    final studentId = context.read<AuthProvider>().student!.studentData!.id!;
    var _exam = exam.toJson() ;
    await ApiServices().saveExams(companyId , branchId , _exam["examName"] , _exam["degree"] , _exam["schoolStagesId"] ,
      _exam['lessonId'] , _exam['timer'] , teacherId , _exam['periodId'], _exam['examType'] , _exam['questionsList'] , _exam["id"] ,studentId , _exam["materialId"] , _exam["solutionTime"] ).then((res){
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
    final studentSchoolId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    // SharedPreferences prefs = await SharedPreferences.getInstance();
    // LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getLessonsByFilter(teacherId , materialId ,studentSchoolId,studentId).then((res){
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
    final studentSchoolId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    // SharedPreferences prefs = await SharedPreferences.getInstance();
    // LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().getLessonsFreeByFilter(teacherId , materialId ,studentSchoolId,studentId).then((res){
      if (res != null){
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

  Future scheduleBySchoolStageId () async{
    await checkConnectivity() ;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().scheduleBySchoolStageId(_student.studentData!.schoolStagesId!).then((value){
      if ( value != null ){
        saveLocalSchedules(value);
        studentSchedule = List<Schedule>.from(value.map((x) => Schedule.fromJson(x)));
        notifyListeners();
        teacherSchedules = {} ;
        for(int i = 0 ; i < studentSchedule.length ; i ++){
          List time = studentSchedule[i].time != null ? studentSchedule[i].time!.split("T") : [] ;
          List times = time.isNotEmpty ?  time[1].split("+")[0].split(":") : [];
          String? firstTime =times.isNotEmpty ? "${times[0]} : ${times[1]}" : "" ;

          List time2 = studentSchedule[i].time2 != null ? studentSchedule[i].time2!.split("T") : [] ;
          List times2 = time2.isNotEmpty ?time2[1].split("+")[0].split(":") : [];
          String? secondTime =times2.isNotEmpty ? "${times2[0]} : ${times2[1]}" : "" ;


          List time3 = studentSchedule[i].time3 != null ? studentSchedule[i].time3!.split("T"): [] ;
          List times3 = time3.isNotEmpty ? time3[1].split("+")[0].split(":") : [];
          String? thirdTime = times3.isNotEmpty ? "${times3[0]} : ${times3[1]}" : "" ;

          List time4 = studentSchedule[i].time4 != null ?  studentSchedule[i].time4!.split("T") : [] ;
          List times4 = time4.isNotEmpty ? time4[1].split("+")[0].split(":") : [];
          String? fourthTime = times4.isNotEmpty ? "${times4[0]} : ${times4[1]}" : "";

          if(teacherSchedules.containsKey(studentSchedule[i].teacherName))
          {
            Map _schedule = teacherSchedules["${studentSchedule[i].teacherName}"] ;
            if(_schedule.containsKey(studentSchedule[i].lessonLocation!.name))
            {

              List schedule = teacherSchedules["${studentSchedule[i].teacherName}"]["${studentSchedule[i].lessonLocation!.name}"] ;
              schedule.add([studentSchedule[i].dayText , [firstTime , secondTime , thirdTime , fourthTime] , studentSchedule[i].remarks ]);
              _schedule.putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
            }
            else {
              List schedule = [] ;
              schedule.add([studentSchedule[i].dayText , [firstTime , secondTime , thirdTime , fourthTime] , studentSchedule[i].remarks ]);
              _schedule.putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
            }
          } else {
             teacherSchedules["${studentSchedule[i].teacherName}"] = {} ;
            List schedule = [] ;
            schedule.add([studentSchedule[i].dayText, [firstTime , secondTime , thirdTime , fourthTime], studentSchedule[i].remarks]);
             teacherSchedules["${studentSchedule[i].teacherName}"].putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
          }
          // = {
          //   "${studentSchedule[i].lessonLocation!.name}" : {}
          // };
        }
        notifyListeners();
        return studentSchedule ;
      }
     });


  }

  Future getAvailableChats () async {
   // await ChatMethods().getAllAvailableChats().then((value){
   //   // chats.add(value);
   // });
  }

  Future getAvailableZoom () async {
    // await ChatMethods().getAllAvailableChats().then((value){
    //   // meetings.add(value);
    //   notifyListeners();
    // });
  }


  Future getPreviousExams(BuildContext context) async {
    final studentId = context.read<AuthProvider>().student!.studentData!.id!;
    final studentSchoolId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    // SharedPreferences prefs = await SharedPreferences.getInstance();
    // LoginData _student = LoginData.fromJson(json.decode(prefs.getString(dataKey)!)) ;
    await ApiServices().findStudentExams(studentId,studentSchoolId).then((value){
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
      teacherSchedules = {} ;
      for(int i = 0 ; i < studentSchedule.length ; i ++){
        List time = studentSchedule[i].time != null ? studentSchedule[i].time!.split("T") : [] ;
        List times = time.isNotEmpty ?  time[1].split("+")[0].split(":") : [];
        String? firstTime =times.isNotEmpty ? "${times[0]} : ${times[1]}" : "" ;

        List time2 = studentSchedule[i].time2 != null ? studentSchedule[i].time2!.split("T") : [] ;
        List times2 = time2.isNotEmpty ?time2[1].split("+")[0].split(":") : [];
        String? secondTime =times2.isNotEmpty ? "${times2[0]} : ${times2[1]}" : "" ;


        List time3 = studentSchedule[i].time3 != null ? studentSchedule[i].time3!.split("T"): [] ;
        List times3 = time3.isNotEmpty ? time3[1].split("+")[0].split(":") : [];
        String? thirdTime = times3.isNotEmpty ? "${times3[0]} : ${times3[1]}" : "" ;

        List time4 = studentSchedule[i].time4 != null ?  studentSchedule[i].time4!.split("T") : [] ;
        List times4 = time4.isNotEmpty ? time4[1].split("+")[0].split(":") : [];
        String? fourthTime = times4.isNotEmpty ? "${times4[0]} : ${times4[1]}" : "";

        if(teacherSchedules.containsKey(studentSchedule[i].teacherName))
        {
          Map _schedule = teacherSchedules["${studentSchedule[i].teacherName}"] ;
          if(_schedule.containsKey(studentSchedule[i].lessonLocation!.name))
          {

            List schedule = teacherSchedules["${studentSchedule[i].teacherName}"]["${studentSchedule[i].lessonLocation!.name}"] ;
            schedule.add([studentSchedule[i].dayText , [firstTime , secondTime , thirdTime , fourthTime] , studentSchedule[i].remarks ]);
            _schedule.putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
          }
          else {
            List schedule = [] ;
            schedule.add([studentSchedule[i].dayText , [firstTime , secondTime , thirdTime , fourthTime] , studentSchedule[i].remarks ]);
            _schedule.putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
          }
        } else {
          teacherSchedules["${studentSchedule[i].teacherName}"] = {} ;
          List schedule = [] ;
          schedule.add([studentSchedule[i].dayText, [firstTime , secondTime , thirdTime , fourthTime], studentSchedule[i].remarks]);
          teacherSchedules["${studentSchedule[i].teacherName}"].putIfAbsent(studentSchedule[i].lessonLocation!.name, () => schedule);
        }
        // = {
        //   "${studentSchedule[i].lessonLocation!.name}" : {}
        // };
      }
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
