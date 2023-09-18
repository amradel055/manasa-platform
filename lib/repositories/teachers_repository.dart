import 'dart:convert';

import 'package:desktop_screen/models/teacher_model.dart';

import '../constants/strings.dart';
import 'package:http/http.dart' as http;

class TeacherRepository{


  Future<void> getTeachers({
    required void Function(List<TeacherModel> teachers) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}student/findTeacher") ;

      var body = jsonEncode({
        "branchId": branchId,
      });

      Map<String,String> headers = {
        "Content-Type": "application/json",

      };
      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){
        final _response = json.decode(utf8.decode(response.bodyBytes)) ;
        onSuccess(teacherModelFromJson(_response));
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
}