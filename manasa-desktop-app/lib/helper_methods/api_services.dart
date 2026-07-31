
import 'dart:convert';
import '../constants/strings.dart';
import 'package:http/http.dart' as http;


class ApiServices {

  Future registerStudent(
      String userName , String firstName , String fatherName , String grandFatherName
      ,String familyName , String mobile , String password , String email , schoolStageId , stateId) async{

    Uri uri = Uri.parse(url + "student/studentregister/") ;
    //TODO completeData
    var body = jsonEncode({
      "userName": userName,
      "fatherName": fatherName,
      "firstName": firstName,
      "password": password,
      "email" : email,
      "mobile" : mobile,
      "schoolStagesId" : schoolStageId ,
      "stateId" : stateId ,
      "code" : "g" ,
      "companyId" :companyId ,
      "branchId" : branchId ,
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",
      // "Authorization": "Bearer $token"
    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response;
    }
  }

  Future loginStudent(
      String userName , String password ,String macId) async{

    Uri uri = Uri.parse(url + "authenticationStudent/login/") ;
    //TODO completeData
    var body = jsonEncode({
      "userName": userName,
      "password": password,
      "companyId" :companyId ,
      "branchId" : branchId ,
      "deviceType":1,
      "macId" : macId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",
      // "Authorization": "Bearer $token"
    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future findInboxDetail(
     int id) async{

    Uri uri = Uri.parse(url + "inboxmessage/findmessage") ;
    //TODO completeData
    var body = jsonEncode({
      "id":id
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",
      // "Authorization": "Bearer $token"
    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future periodsByTeacher(String teacherId) async{

    Uri uri = Uri.parse(url + "periods/allPeriods") ;
    //TODO completeData
    var body = jsonEncode({
      "branchId" : branchId ,

    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future subjectsByTeacher(String teacherId, int schoolStagesId) async{

    Uri uri = Uri.parse(url + "lockup/materialListbystage") ;
    //TODO completeData
    var body = jsonEncode({

      "id": schoolStagesId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future examsById(String id) async{

    Uri uri = Uri.parse(url + "exams/findExams") ;
    //TODO completeData
    var body = jsonEncode({
      "id" : id ,
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future examsByFilters(List materialList , int examType , List chapters , teacherId , studentId) async {

    Uri uri = Uri.parse(url + "exams/findExamsForStudent") ;
    //TODO completeData
    var body = jsonEncode({
      "materialList" : List<dynamic>.from(materialList.map((x) => x.toString())),
      "examType" : examType ,
      "periodList" : List<dynamic>.from(chapters.map((x) => x.toString())),
      "teacherId" : teacherId ,
      "studentId" : studentId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future saveExams(companyId , branchId , examName , degree,
      selectedStage , selectedLesson  , timer ,teacherId ,
      selectedPeriod , examType , finalQuestionsForm ,examId , studentId , materialId , solutionTime) async{

    Uri uri = Uri.parse(url + "exams/saveExams") ;
    //TODO completeData
    var body = jsonEncode({
      "companyId": companyId,
      "branchId": branchId,
      "examName": examName,
      "degree": degree,
      "active": 1,
      "schoolStagesId": selectedStage,
      "lessonId": selectedLesson,
      // "monthId": selectedMonth,
      "timer": timer,
      "teacherId" : teacherId,
      "periodId" : selectedPeriod,
      "examType": examType,
      "questionsList":finalQuestionsForm ,
      "parent" : examId,
      "studentId" : studentId,
      "materialId" : materialId ,
      "solutionTime" : solutionTime
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
       return _response ;
    }
  }

  Future getLessonsByFilter(teacherId , materialId , schoolStageId, studentId) async{

    Uri uri = Uri.parse(url + "lesson/lessonList") ;


    var body = jsonEncode({
      "branchId": branchId,
      "companyId": companyId,
      "teacherId" : teacherId ,
      "materialId" : materialId ,
      "schoolStageId" : schoolStageId,
      "studentId": studentId,
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }
  Future getLessonsFreeByFilter(teacherId , materialId , schoolStageId, studentId) async{

    Uri uri = Uri.parse(url + "lesson/findAllLessonsFree") ;


    var body = jsonEncode({
      "branchId": branchId,
      "companyId": companyId,
      "teacherId" : teacherId ,
      "materialId" : materialId ,
      "schoolStageId" : schoolStageId,
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future getAllFeed() async{

    Uri uri = Uri.parse(url + "newrest/allNews") ;


    var body = jsonEncode({
      "newsType" : 0
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }
  Future getStudentNews(schoolStageId , teacherId) async{

    Uri uri = Uri.parse(url + "newrest/allNews") ;


    var body = jsonEncode({
      "schoolStageId" :  schoolStageId,
          "teacherId" : teacherId

    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future grades() async{

    Uri uri = Uri.parse(url + "lockup/levelList") ;


    var body = jsonEncode({
      "branchId": branchId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future scheduleBySchoolStageId(int schoolStageId) async{

    Uri uri = Uri.parse(url + "appointments/appointmentsByStudent") ;


    var body = jsonEncode({
      "branchId": branchId,
      "companyId": companyId,
      "schoolStageId" : schoolStageId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future findStudentExams(int studentId ,int schoolStageId) async{

    Uri uri = Uri.parse(url + "exams/findSolvedExamsForStudent") ;


    var body = jsonEncode({
      "branchId": branchId ,
      "companyId": companyId,
      "schoolStagesId" : schoolStageId ,
      "studentId" : studentId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future getOtherAppointmentsByStudentId(int teacherId ,int schoolStageId, int type) async{

    Uri uri = Uri.parse(url + "otherappointments/appointmentsByTeacher") ;


    var body = jsonEncode({
      "branchId": branchId ,
      "companyId": companyId,
      "teacherId" : teacherId ,
      "schoolStageId" : schoolStageId,
      // "appointmentsType" : type
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }
  Future getLinks(int teacherId ,int schoolStageId, int type) async{

    Uri uri = Uri.parse(url + "zoomlink/find") ;


    var body = jsonEncode({
      "branchId": branchId ,
      "companyId": companyId,
      "teacherId" : teacherId ,
      // "schoolStageId" : schoolStageId,
      // "appointmentsType" : type
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future findCityState(int stateId ) async{

    Uri uri = Uri.parse(url + "lockup/statesList") ;


    var body = jsonEncode({
      "id" : stateId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future findCities() async{
    Uri uri = Uri.parse(url + "lockup/governorateList") ;
    var body = jsonEncode({
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",
    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future examsByLesson(lessonId , studentId) async {

    Uri uri = Uri.parse(url + "exams/findExamsForStudent") ;
    //TODO completeData
    var body = jsonEncode({
      "lessonId" : lessonId ,
      "studentId" : studentId
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",

    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200){
      var _response = json.decode(utf8.decode(response.bodyBytes)) ;
      return _response ;
    }
  }

  Future<void> sendInbox({
    required int materialId,
    required int studentId,
    required int teacherId,
    required String subject,
    required String content,
     String? image,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {


      Uri uri = Uri.parse("${url}inboxmessage/save") ;

        var body = jsonEncode({
          "materialId": materialId,
          "subject":subject,
          "body":content,
          "sentDate": DateTime.now().toIso8601String(),
          "studentId": studentId,
          "teacherId": teacherId,
          "image": image,
          "createdBy": createdBy,
          "createdDate": DateTime.now().toIso8601String(),
          "companyId": companyId,
          "branchId":branchId,
          "value": 444
        });

        Map<String, String> headers = {
          "Content-Type": "application/json",

        };

        final response = await http.post(uri, body: body, headers: headers);
        if (response.statusCode == 200) {
          final _response = utf8.decode(response.bodyBytes);
          onSuccess(_response.toString());
        } else {
          throw "${response.reasonPhrase} : ${response.statusCode}";
        }

    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


  Future<void> sendReplyInbox({

     int ?companyId,
     int? branchId,
    required int studentId,
    required int teacherId,
    required String mBody,
    required int message,
     DateTime? sendDate,
    required String image,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {


      Uri uri = Uri.parse("${url}inboxreply/save") ;

      var body = jsonEncode({
        "companyId": companyId,
        "branchId":branchId,
        "body":mBody,
        "sentDate": DateTime.now().toIso8601String(),
        "studentId": studentId,
        "teacherId": teacherId,
        "image": image,
        "createdBy": createdBy,
        "message":message
      });

      Map<String, String> headers = {
        "Content-Type": "application/json",

      };

      final response = await http.post(uri, body: body, headers: headers);
      if (response.statusCode == 200) {
        final _response = utf8.decode(response.bodyBytes);
        onSuccess("تم الحفظ بنجاح");
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }

    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
  Future<void> sendScreenInfo({

    int ?companyId,
    int? branchId,
    required int studentId,
    required int teacherId,

    DateTime? sendDate,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {


      Uri uri = Uri.parse("${url}inboxreply/save") ;

      var body = jsonEncode({
        "companyId": companyId,
        "branchId":branchId,
        "sentDate": DateTime.now().toIso8601String(),
        "studentId": studentId,
        "teacherId": teacherId,
        "createdBy": createdBy,
        "companyId": companyId,
        "branchId":branchId,
      });

      Map<String, String> headers = {
        "Content-Type": "application/json",

      };

      final response = await http.post(uri, body: body, headers: headers);
      if (response.statusCode == 200) {
        final _response = utf8.decode(response.bodyBytes);
        onSuccess("تم الابلاغ عن المستخدم بنجاح");
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }

    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }



// Future sendInbox(companyId , branchId , examName , degree,
  //     selectedStage , selectedLesson  , timer ,teacherId ,
  //     selectedPeriod , examType , finalQuestionsForm ,examId , studentId , materialId , solutionTime) async{
  //
  //   Uri uri = Uri.parse(url + "exams/saveExams") ;
  //   //TODO completeData
  //   var body = jsonEncode({
  //     "companyId": companyId,
  //     "branchId": branchId,
  //     "examName": examName,
  //     "degree": degree,
  //     "active": 1,
  //     "schoolStagesId": selectedStage,
  //     "lessonId": selectedLesson,
  //     // "monthId": selectedMonth,
  //     "timer": timer,
  //     "teacherId" : teacherId,
  //     "periodId" : selectedPeriod,
  //     "examType": examType,
  //     "questionsList":finalQuestionsForm ,
  //     "parent" : examId,
  //     "studentId" : studentId,
  //     "materialId" : materialId ,
  //     "solutionTime" : solutionTime
  //   });
  //
  //   Map<String,String> headers = {
  //     "Content-Type": "application/json",
  //
  //   };
  //   var response = await http.post(uri , body: body , headers: headers );
  //
  //   if(response.statusCode == 200){
  //     var _response = json.decode(utf8.decode(response.bodyBytes)) ;
  //     return _response ;
  //   }
  // }


}