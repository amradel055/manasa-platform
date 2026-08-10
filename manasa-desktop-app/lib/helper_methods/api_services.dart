
import 'dart:convert';
import '../constants/strings.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';


class ApiServices {

  // Shared JWT header builder -- every method below reads the token persisted by
  // loginStudent() and sends it as a Bearer header. Omitted entirely when there's
  // no token yet (e.g. pre-login lookup calls like findCities/findCityState hit
  // public endpoints and shouldn't send a bogus "Bearer null").
  Future<Map<String, String>> _authHeaders() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    String? token = preferences.getString(authTokenKey);
    return {
      "Content-Type": "application/json",
      if (token != null) "Authorization": "Bearer $token",
    };
  }

  // POST students/register -- public (no auth), creates the STUDENT identity in
  // user-service and the manasa-service student_profile in one call. userName is
  // no longer sent (login is by email now, kept in the signature for caller
  // compatibility).
  Future registerStudent(
      String userName , String firstName , String fatherName , String grandFatherName
      ,String familyName , String mobile , String password , String email , schoolStageId , stateId) async{

    Uri uri = Uri.parse("${url}students/register");
    var body = jsonEncode({
      "email": email,
      "password": password,
      "fullName": [firstName, fatherName, familyName].where((s) => s.isNotEmpty).join(" "),
      "firstName": firstName,
      "fatherName": fatherName,
      "grandFatherName": grandFatherName,
      "familyName": familyName,
      "mobile": mobile,
      "schoolStagesId": schoolStageId,
      "stateId": stateId,
      "branchId": branchId,
    });

    Map<String,String> headers = {
      "Content-Type": "application/json",
    };
    var response = await http.post(uri , body: body , headers: headers );

    if(response.statusCode == 200 || response.statusCode == 201){
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // Logs in against user-service (POST /api/auth/login), then fetches the
  // student's own profile + enrolled-teachers list from manasa-service and
  // reshapes them into the legacy {success, token, studentData, teacherList}
  // envelope this app's LoginData/Student models already expect, so nothing
  // downstream of AuthProvider.signIn() needs to change. macId/device
  // tracking is dropped -- identity now comes entirely from the JWT.
  Future loginStudent(
      String userName , String password , String macId) async{

    final email = userName.contains("@") ? userName : "$userName@manasa.local";
    Uri loginUri = Uri.parse(authUrl + "login");
    var loginBody = jsonEncode({
      "email": email,
      "password": password,
    });
    http.Response loginResponse;
    try {
      loginResponse = await http.post(loginUri, body: loginBody, headers: {"Content-Type": "application/json"});
    } catch (e) {
      return {"msg": "تعذر الاتصال بالسيرفر"};
    }

    var loginDecoded = json.decode(utf8.decode(loginResponse.bodyBytes));
    if (loginResponse.statusCode != 200 || loginDecoded["success"] != true) {
      return {"msg": loginDecoded["error"]?["message"] ?? "اسم المستخدم أو كلمة السر غير صحيحة"};
    }

    var auth = loginDecoded["payload"];
    if (auth["role"] != "STUDENT") {
      return {"msg": "هذا التطبيق مخصص لحسابات الطلاب فقط"};
    }

    String token = auth["token"];
    SharedPreferences preferences = await SharedPreferences.getInstance();
    await preferences.setString(authTokenKey, token);

    Map<String, String> authHeaders = {
      "Content-Type": "application/json",
      "Authorization": "Bearer $token",
    };

    var meResponse = await http.get(Uri.parse(url + "students/me"), headers: authHeaders);
    var meDecoded = json.decode(utf8.decode(meResponse.bodyBytes));
    Map profile = meDecoded["payload"] ?? {};

    var teachersResponse = await http.get(Uri.parse(url + "students/me/teachers"), headers: authHeaders);
    var teachersDecoded = json.decode(utf8.decode(teachersResponse.bodyBytes));
    List teachersPayload = teachersDecoded["payload"] ?? [];

    var teacherList = teachersPayload.map((t) => {
      "id": t["teacherUserId"],
      "markEdit": true,
      "name": t["teacherName"],
      "materialList": (t["materials"] as List? ?? []).map((m) => {
        "id": m["id"],
        "markEdit": true,
        "name": m["name"],
      }).toList(),
    }).toList();

    var studentData = {
      "id": profile["id"],
      "branchId": profile["branchId"],
      "code": profile["code"],
      "familyName": profile["familyName"],
      "fatherJob": profile["fatherJob"],
      "fatherMobile": profile["fatherMobile"],
      "fatherName": profile["fatherName"],
      "firstName": profile["firstName"],
      "gender": profile["gender"],
      "grandFatherName": profile["grandFatherName"],
      "imagePath": profile["imagePath"],
      "mobile": profile["mobile"],
      "schoolStagesId": profile["schoolStagesId"],
      "stateId": profile["stateId"],
      "email": auth["email"],
      "userName": auth["email"],
      "teacherList": teacherList,
    };

    return {
      "success": true,
      "token": token,
      "studentData": studentData,
      "teacherList": teacherList,
    };
  }

  // GET messages/$id merged with GET messages/$id/replies -- InboxMessageResponse
  // fields (subject, body, image, audio, video, sentDate, readDone,
  // studentProfileId, teacherUserId, materialId, branchId) plus an injected
  // "inboxReplys" list built from InboxReplyResponse, matching the shape
  // InboxQuestionDetailModel.fromJson expects.
  Future findInboxDetail(int id) async {
    var headers = await _authHeaders();

    var messageResponse = await http.get(Uri.parse("${url}messages/$id"), headers: headers);
    if (messageResponse.statusCode != 200) return null;
    var messageDecoded = json.decode(utf8.decode(messageResponse.bodyBytes));
    if (messageDecoded["success"] != true) return null;
    Map message = Map.from(messageDecoded["payload"] ?? {});

    List replies = [];
    var repliesResponse = await http.get(Uri.parse("${url}messages/$id/replies"), headers: headers);
    if (repliesResponse.statusCode == 200) {
      var repliesDecoded = json.decode(utf8.decode(repliesResponse.bodyBytes));
      if (repliesDecoded["success"] == true) {
        replies = repliesDecoded["payload"] ?? [];
      }
    }
    message["inboxReplys"] = replies;
    return message;
  }

  // NOT PORTED: no backend endpoint returns "all periods in a branch" for a
  // student -- the closest, GET periods, is TEACHER-only (scoped to the
  // authenticated teacher's own periods). Still calls the old (now-retired)
  // endpoint pending a product/backend decision.
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

  // GET lookup/materials/by-stage?stageId= -- open to any authenticated role.
  // teacherId is no longer sent (kept in the signature for caller compatibility).
  Future subjectsByTeacher(String teacherId, int schoolStagesId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lookup/materials/by-stage?stageId=$schoolStagesId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET exams/{id}/take -- STUDENT-only, hides correct answers server-side
  // (ExamService.getForTaking passes revealCorrect=false, so every
  // AnswerResponse.correct comes back false regardless of the real answer;
  // "reason" is still included in the response either way).
  Future examsById(String id) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}exams/$id/take");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET exams/available?materialId= -- STUDENT-only, derives the student from
  // the JWT. The old RPC took a materialList/examType/periodList combo; the new
  // endpoint only filters by a single materialId, so we use the first entry
  // (every caller in this app already passes a single-element list) and drop
  // examType/chapters/teacherId/studentId entirely -- they're no longer sent.
  // Note this only returns list-view ExamResponses (questions == null); fetch
  // exam detail separately via examsById() before starting an exam.
  Future examsByFilters(List materialList , int examType , List chapters , teacherId , studentId) async {
    var headers = await _authHeaders();
    var materialId = materialList.isNotEmpty ? materialList.first : null;
    Uri uri = Uri.parse("${url}exams/available?materialId=$materialId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // Same backend route as examsByFilters -- the new API doesn't distinguish
  // "auto" exams from regular ones via a separate endpoint.
  Future autoExamsByFilters(List materialList , int examType , List chapters , teacherId , studentId) async {
    return examsByFilters(materialList, examType, chapters, teacherId, studentId);
  }

  // POST exams/{id}/attempts -- STUDENT-only. Server computes the score itself
  // from real answer-correctness data (the client never gets to claim which
  // answer was "correct"). `selections` is questionId -> the answerId the
  // student picked (ExamAttemptRequest.selectedAnswerByQuestionId). Returns
  // ExamAttemptResult {attemptId, score, maxDegree}. 409s if this exam was
  // already attempted (server enforces one attempt per student per exam).
  Future submitExamAttempt(int examId, Map<int, int> selections) async {
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}exams/$examId/attempts");
    var body = jsonEncode({
      "selectedAnswerByQuestionId": selections.map((k, v) => MapEntry(k.toString(), v)),
    });
    var response = await http.post(uri, body: body, headers: headers);
    if (response.statusCode == 200 || response.statusCode == 201) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET lessons/enrolled?materialId=&schoolStageId= -- STUDENT-only, derives the
  // student from the JWT. teacherId/studentId are no longer sent.
  Future getLessonsByFilter(teacherId , materialId , schoolStageId, studentId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lessons/enrolled?materialId=$materialId&schoolStageId=$schoolStageId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET lessons/free?materialId=&schoolStageId=&teacherUserId= -- open to any
  // role. teacherId is now sent as the teacherUserId query param.
  Future getLessonsFreeByFilter(teacherId , materialId , schoolStageId, studentId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lessons/free?materialId=$materialId&schoolStageId=$schoolStageId&teacherUserId=$teacherId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET news -- no filter args, the old newsType param has no server-side
  // equivalent.
  Future getAllFeed() async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}news");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET news?teacherUserId= -- schoolStageId isn't a supported server-side
  // filter anymore, so it's dropped (filter client-side on the result if that
  // distinction matters).
  Future getStudentNews(schoolStageId , teacherId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}news?teacherUserId=$teacherId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET lookup/school-stages?branchId=
  Future grades() async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lookup/school-stages?branchId=$branchId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET appointments/by-branch?branchId=&schoolStageId= -- open to any role.
  Future scheduleBySchoolStageId(int schoolStageId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}appointments/by-branch?branchId=$branchId&schoolStageId=$schoolStageId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // Added (not part of the legacy RPC surface) so DataHandler.scheduleBySchoolStageId()
  // can resolve AppointmentResponse.lessonLocationId (now a bare int id) back into
  // a display name -- the old backend returned a nested lessonLocation:{name}
  // object that no longer exists. GET lookup/lesson-locations?branchId= is open
  // to any role.
  Future lessonLocations() async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lookup/lesson-locations?branchId=$branchId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET exams/solved -- STUDENT-only, derives the student from the JWT. Both
  // params are no longer sent. Note: this only returns list-view ExamResponses
  // (questions == null) -- there is no backend endpoint that returns a solved
  // exam's per-question detail with correct answers revealed for student review
  // (GET exams/{id}/take 409s once already attempted, and GET exams/{id} detail
  // is TEACHER-only), so solved_exam_screen.dart's review UI has no working data
  // source post-migration. See report for details.
  Future findStudentExams(int studentId ,int schoolStageId) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}exams/solved");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET other-appointments/by-branch?branchId= -- open to any role; does NOT
  // support filtering by teacherId or type server-side, so both are dropped
  // (filter client-side on the returned list if the `type` distinction matters).
  Future getOtherAppointmentsByStudentId(teacherId ,int schoolStageId, int type) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}other-appointments/by-branch?branchId=$branchId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET zoom-links/by-branch?branchId=&schoolStageId= -- open to any role.
  Future getLinks(teacherId ,int schoolStageId, int type) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}zoom-links/by-branch?branchId=$branchId&schoolStageId=$schoolStageId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET lookup/states?governorateId= -- despite the "stateId" param name (kept
  // for caller compatibility), signup_screen.dart actually passes the selected
  // governorate's id here (see getCityState(selectedGovernorate["id"]) in
  // data_handler_provider.dart), which is exactly what this endpoint expects.
  Future findCityState(int stateId ) async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lookup/states?governorateId=$stateId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET lookup/governorates
  Future findCities() async{
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}lookup/governorates");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // GET exams/available?lessonId= -- STUDENT-only, derives the student from
  // the JWT. studentId is no longer sent (kept in the signature for caller
  // compatibility).
  Future examsByLesson(lessonId , studentId) async {
    var headers = await _authHeaders();
    Uri uri = Uri.parse("${url}exams/available?lessonId=$lessonId");
    var response = await http.get(uri, headers: headers);
    if (response.statusCode == 200) {
      var decoded = json.decode(utf8.decode(response.bodyBytes));
      if (decoded["success"] == true) return decoded["payload"];
    }
    return null;
  }

  // POST messages -- I just added STUDENT-role support to this endpoint. The
  // student's identity is derived from the JWT server-side, so studentId is no
  // longer sent; teacherUserId is required now since the recipient teacher must
  // be specified explicitly (InboxMessageRequest). Response is InboxMessageResponse.
  Future<void> sendInbox({
    required int materialId,
    required int studentId,
    required teacherId,
    required String subject,
    required String content,
     String? image,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}messages") ;

        var body = jsonEncode({
          "subject":subject,
          "body":content,
          "image": image,
          "materialId": materialId,
          "branchId":branchId,
          "teacherUserId": teacherId,
        });

        final response = await http.post(uri, body: body, headers: headers);
        if (response.statusCode == 200 || response.statusCode == 201) {
          final decoded = json.decode(utf8.decode(response.bodyBytes));
          if (decoded["success"] == true) {
            onSuccess("تم الحفظ بنجاح");
          } else {
            onError(decoded["error"]?["message"] ?? "حدث خطأ ما");
          }
        } else {
          throw "${response.reasonPhrase} : ${response.statusCode}";
        }

    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


  // POST messages/$message/replies -- any authenticated role. Body is
  // InboxReplyRequest {body, image, audio, video}. Response is InboxReplyResponse.
  Future<void> sendReplyInbox({

     int ?companyId,
     int? branchId,
    required int studentId,
    required teacherId,
    required String mBody,
    required int message,
     DateTime? sendDate,
    required String image,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}messages/$message/replies") ;

      var body = jsonEncode({
        "body": mBody,
        "image": image,
      });

      final response = await http.post(uri, body: body, headers: headers);
      if (response.statusCode == 200 || response.statusCode == 201) {
        final decoded = json.decode(utf8.decode(response.bodyBytes));
        if (decoded["success"] == true) {
          onSuccess("تم الحفظ بنجاح");
        } else {
          onError(decoded["error"]?["message"] ?? "حدث خطأ ما");
        }
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }

    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }

  // NOT PORTED: no backend equivalent for this at all (looks like a legacy
  // screen-recording/violation report feature). Don't invent a fake endpoint --
  // this is a no-op until there's a real destination for it.
  Future<void> sendScreenInfo({

    int ?companyId,
    int? branchId,
    required int studentId,
    required teacherId,

    DateTime? sendDate,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {
    if(onComplete != null) onComplete();
  }



}
