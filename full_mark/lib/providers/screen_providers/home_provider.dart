import 'package:flutter/cupertino.dart';
import 'package:full_mark/models/teacher_model.dart';
import 'package:full_mark/repositories/teachers_repository.dart';

class HomeProvider with ChangeNotifier {
  List<TeacherModel> teachers = [];
  bool isLoading = true;
  String? error;
  // Teachers are now identified by a user_service UUID, not a legacy int id.
  late String selectedTeacherId;

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
