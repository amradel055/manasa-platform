import 'package:flutter/cupertino.dart';
import 'package:full_mark/models/teacher_model.dart';
import 'package:full_mark/repositories/teachers_repository.dart';

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
