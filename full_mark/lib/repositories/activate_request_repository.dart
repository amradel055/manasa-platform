import 'dart:convert';
import 'package:full_mark/models/material_model.dart';
import 'package:full_mark/models/period_model.dart';
import 'package:full_mark/models/teacher_model.dart';

import '../constants/strings.dart';
import 'package:http/http.dart' as http;

class ActivateRequestRepository{


  Future<void> getMaterial(int teacherId,int schoolStagesId,{
    required void Function(List<MaterialModel> materials) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}student/findMaterials") ;

      var body = jsonEncode({
        "teacherId": teacherId,
        "stageId": schoolStagesId,
      });

      Map<String,String> headers = {
        "Content-Type": "application/json"
      };

      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){
        final _response = json.decode(utf8.decode(response.bodyBytes)) ;
        onSuccess(materialModelFromJson(_response));
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }

  Future<void> getPeriods(int materialId,{
    required void Function(List<PeriodModel> periods) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      Uri uri = Uri.parse("${url}periods/findPeriod") ;

      var body = jsonEncode({
        "materialId": materialId,
      });

      Map<String,String> headers = {
        "Content-Type": "application/json",

      };
      final response = await http.post(uri , body: body , headers: headers );

      if(response.statusCode == 200){
        final _response = json.decode(utf8.decode(response.bodyBytes)) ;
        onSuccess(periodModelFromJson(_response));
      } else {
        throw "${response.reasonPhrase} : ${response.statusCode}";
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }

  Future<void> savePeriodsOrder({
    required int materialId,
    required List<int> periods,
    required int studentId,
    required int teacherId,
    required void Function(String message) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {


      Uri uri = Uri.parse("${url}studentPeriodsOrders/saveStudentPeriodsOrder") ;

      for(int period in   periods) {
        var body = jsonEncode({
          "materialId": materialId,
          "periodId": period,
          "studentId": studentId,
          "teacherId": teacherId,
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
      }
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }


}