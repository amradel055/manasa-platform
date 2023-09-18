import 'dart:convert';


import 'package:desktop_screen/models/common_question_model.dart';
import 'package:desktop_screen/models/inbox_question_detail_model.dart';
import 'package:desktop_screen/models/inbox_question_model.dart';

import '../constants/strings.dart';
import 'package:http/http.dart' as http;

class QuestionsRepository{


  Future<void> getCommonQuestions({
    required int teacherId,
    required int materialId,
    required int schoolId,
    required void Function(List<CommonQuestionModel> questions) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}commonquestions/findcommonquestions") ;

      var body = jsonEncode({
        "teacherId": teacherId,
        "materialId": materialId,
        "schoolId": schoolId,
      });

      Map<String,String> headers = {
        "Content-Type": "application/json",

      };
      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){
        final _response = json.decode(utf8.decode(response.bodyBytes)) ;
        onSuccess(commonQuestionModelFromJson(_response));
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


  Future<void> getInbox({
    required int teacherId,
    required int materialId,
    required int studentId,
    required void Function(List<InboxQuestionModel> questions) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}inboxmessage/findmessages") ;

      var body = jsonEncode({
        "teacherId": teacherId,
        "materialId": materialId,
        "studentId":studentId
      });

      Map<String,String> headers = {
        "Content-Type": "application/json",

      };
      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){
        final _response = json.decode(utf8.decode(response.bodyBytes)) ;
        onSuccess(inboxQuestionModelFromJson(_response));
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
  // Future getInboxDetail(
  //     int? id) async{
  //
  //   Uri uri = Uri.parse(url + "inboxmessage/findmessage") ;
  //   //TODO completeData
  //   var body = jsonEncode({
  //  id:id
  //   });
  //
  //   Map<String,String> headers = {
  //     "Content-Type": "application/json",
  //     // "Authorization": "Bearer $token"
  //   };
  //   var response = await http.post(uri , body: body , headers: headers );
  //
  //   if(response.statusCode == 200){
  //     var _response = json.decode(utf8.decode(response.bodyBytes)) ;
  //     return _response ;
  //   }
  // }

  Future<void> getInboxDetail({
    required int id,


    required void Function(InboxQuestionDetailModel  s) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}inboxmessage/findmessage") ;

      var body = jsonEncode({
        "id": id,

      });

      Map<String,String> headers = {
        "Content-Type": "application/json",

      };
      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){

        final _response  = json.decode(utf8.decode(response.bodyBytes)) ;
        // User.fromJson(json.decode(json['data'])),
        onSuccess(_response );

      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
}