import 'package:desktop_screen/models/teacher_model.dart';
import 'package:desktop_screen/repositories/teachers_repository.dart';
import 'package:flutter/cupertino.dart';


class HomeProvider with ChangeNotifier {
  List<TeacherModel> teachers = [];
  bool isLoading = true;
  String? error;
  late int selectedTeacherId;

  getTeachers() {
    if(isLoading == false) {
      isLoading = true;
      error = null;
      teachers.clear();
      notifyListeners();
    }
    TeacherRepository().getTeachers(
      onSuccess: (data) => teachers.addAll(data),
      onError: (e) => error = e,
      onComplete: () {
        isLoading = false;
        notifyListeners();
      }
    );
  }
}
