import 'dart:convert';
import 'package:full_mark/models/material_model.dart';
import 'package:full_mark/models/period_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/strings.dart';
import 'package:http/http.dart' as http;

class ActivateRequestRepository{

  Future<Map<String, String>> _authHeaders() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    String? token = preferences.getString(authTokenKey);
    return {
      "Content-Type": "application/json",
      if (token != null) "Authorization": "Bearer $token",
    };
  }

  // GET lookup/materials/by-stage?stageId= -- open to any authenticated role.
  // teacherId is no longer sent (kept in the signature for caller compatibility).
  Future<void> getMaterial(teacherId,int schoolStagesId,{
    required void Function(List<MaterialModel> materials) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}lookup/materials/by-stage?stageId=$schoolStagesId") ;

      final response = await http.get(uri, headers: headers);

      if(response.statusCode == 200){
        final decoded = json.decode(utf8.decode(response.bodyBytes)) ;
        if (decoded["success"] == true) {
          onSuccess(materialModelFromJson(decoded["payload"]));
        } else {
          throw decoded["error"]?["message"] ?? "حدث خطأ ما";
        }
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }

  // GET periods/by-material?materialId= -- open to any authenticated role.
  Future<void> getPeriods(int materialId,{
    required void Function(List<PeriodModel> periods) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}periods/by-material?materialId=$materialId") ;

      final response = await http.get(uri, headers: headers);

      if(response.statusCode == 200){
        final decoded = json.decode(utf8.decode(response.bodyBytes)) ;
        if (decoded["success"] == true) {
          onSuccess(periodModelFromJson(decoded["payload"]));
        } else {
          throw decoded["error"]?["message"] ?? "حدث خطأ ما";
        }
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }

  // POST students/period-orders (one request per selected period, matching
  // the original per-period loop) -- STUDENT-only; I just added this endpoint.
  // The student's identity and the request's teacher are both derived
  // server-side (from the JWT and from the period itself respectively), so
  // studentId/teacherId are no longer sent in the body.
  Future<void> savePeriodsOrder({
    required int materialId,
    required List<int> periods,
    required int studentId,
    required teacherId,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      var headers = await _authHeaders();
      Uri uri = Uri.parse("${url}students/period-orders") ;

      for(int period in periods) {
        var body = jsonEncode({
          "materialId": materialId,
          "periodId": period,
          "branchId":branchId,
        });

        final response = await http.post(uri, body: body, headers: headers);
        if (response.statusCode == 200 || response.statusCode == 201) {
          final decoded = json.decode(utf8.decode(response.bodyBytes));
          if (decoded["success"] != true) {
            throw decoded["error"]?["message"] ?? "حدث خطأ ما";
          }
        } else {
          throw "${response.reasonPhrase} : ${response.statusCode}";
        }
      }
      onSuccess("تم إرسال الطلب بنجاح");
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


}
