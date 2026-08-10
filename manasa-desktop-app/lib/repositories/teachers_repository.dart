import 'dart:convert';
import 'package:desktop_screen/models/teacher_model.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/strings.dart';

class TeacherRepository{

  // NOT calling a "browse all teachers in a branch" endpoint anymore -- that
  // legacy concept (POST student/findTeacher) doesn't exist server-side, and
  // wouldn't be the right model anyway: a student should only ever pick among
  // the teachers they're actually enrolled with. loginStudent() already fetches
  // exactly that list (GET students/me/teachers) and persists it as part of
  // the LoginData blob under dataKey, so this just reads it back out instead
  // of making a new network call.
  Future<void> getTeachers({
    required void Function(List<TeacherModel> teachers) onSuccess,
    required void Function(String error) onError,
    void Function()? onComplete,
  }) async {

    try {
      SharedPreferences preferences = await SharedPreferences.getInstance();
      String? raw = preferences.getString(dataKey);
      if (raw == null) {
        onSuccess([]);
        if (onComplete != null) onComplete();
        return;
      }
      Map decoded = json.decode(raw);
      List teacherList = decoded["teacherList"] ?? [];
      List<TeacherModel> teachers = teacherList.map((t) => TeacherModel(
        id: t["id"],
        name: t["name"],
        description: null,
        image: null,
        imageLabel: null,
      )).toList();
      onSuccess(teachers);
    } catch (e, s) {
      onError(e.toString());
    }
    if(onComplete != null) onComplete();
  }
}
