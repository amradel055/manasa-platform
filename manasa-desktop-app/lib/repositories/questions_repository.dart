import 'dart:convert';
import 'package:desktop_screen/models/common_question_model.dart';
import 'package:desktop_screen/models/inbox_question_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/strings.dart';
import 'package:http/http.dart' as http;

class QuestionsRepository{

  Future<Map<String, String>> _authHeaders() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    String? token = preferences.getString(authTokenKey);
    return {
      "Content-Type": "application/json",
      if (token != null) "Authorization": "Bearer $token",
    };
  }

  // GET common-questions/by-material?materialId= -- open to any authenticated
  // role, returns FAQs for the material regardless of which teacher posted
  // them. teacherId/schoolId are no longer sent (not supported server-side
  // filters for this endpoint).
  Future<void> getCommonQuestions({
    required teacherId,
    required int materialId,
    required int schoolId,
    required void Function(List<CommonQuestionModel> questions) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}common-questions/by-material?materialId=$materialId") ;

      final response = await http.get(uri, headers: headers);

      if(response.statusCode == 200){
        final decoded = json.decode(utf8.decode(response.bodyBytes)) ;
        if (decoded["success"] == true) {
          onSuccess(commonQuestionModelFromJson(decoded["payload"]));
        } else {
          throw decoded["error"]?["message"] ?? "حدث خطأ ما";
        }
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


  // GET messages?materialId= -- any authenticated role; the backend
  // auto-detects a STUDENT caller and filters to their own messages, so
  // studentId is no longer sent. teacherId is likewise dropped (not a
  // supported filter for this endpoint -- a student's inbox already only
  // ever contains messages with their own enrolled teachers).
  Future<void> getInbox({
    required teacherId,
    required int materialId,
    required int studentId,
    required void Function(List<InboxQuestionModel> questions) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}messages?materialId=$materialId") ;

      final response = await http.get(uri, headers: headers);

      if(response.statusCode == 200){
        final decoded = json.decode(utf8.decode(response.bodyBytes)) ;
        if (decoded["success"] == true) {
          onSuccess(inboxQuestionModelFromJson(decoded["payload"]));
        } else {
          throw decoded["error"]?["message"] ?? "حدث خطأ ما";
        }
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
}
