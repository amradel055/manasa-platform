import 'package:desktop_screen/models/common_question_model.dart';
import 'package:desktop_screen/providers/auth_provider.dart';
import 'package:desktop_screen/repositories/questions_repository.dart';
import 'package:flutter/cupertino.dart';
import 'package:provider/provider.dart';

import '../../models/material_model.dart';
import '../../repositories/activate_request_repository.dart';
import 'home_provider.dart';

class CommonQuestionsProvider with ChangeNotifier {

  final List<CommonQuestionModel> questions = [];
  final List<MaterialModel> materials = [];
  MaterialModel? selectedMaterial;
  bool isLoading = true;
  String? error;

  selectMaterial(BuildContext context, MaterialModel? value) {
    selectedMaterial = value;
    getQuestions(context);
  }

  getQuestions(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final schoolStagesId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    isLoading = true;
    error = null;
    notifyListeners();
    QuestionsRepository().getCommonQuestions(
      teacherId: teacherId,
      materialId: selectedMaterial!.id!,
      schoolId: schoolStagesId,
      onSuccess: (data) {
        questions.clear();
        questions.addAll(data);
      },
      onError: (e) => error = e,
      onComplete: () {
        isLoading = false;
        notifyListeners();
      }
    );
  }


  getMaterials(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final schoolStagesId = context.read<AuthProvider>().student!.studentData!.schoolStagesId!;
    if(isLoading == false) {
      isLoading = true;
      error = null;
      notifyListeners();
    }
    materials.clear();
    selectedMaterial = null;
    ActivateRequestRepository().getMaterial(
        teacherId,
        schoolStagesId,
        onSuccess: (data) => materials.addAll(data),
        onError: (e) => error = e,
        onComplete: (){
          isLoading = false;
          notifyListeners();
        }
    );
  }


}
