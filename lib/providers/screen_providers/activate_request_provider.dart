import 'package:desktop_screen/helper_methods/show_toast.dart';
import 'package:desktop_screen/models/material_model.dart';
import 'package:desktop_screen/models/period_model.dart';
import 'package:desktop_screen/providers/screen_providers/home_provider.dart';
import 'package:desktop_screen/repositories/activate_request_repository.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../auth_provider.dart';

class ActivateRequestProvider with ChangeNotifier {
  final List<MaterialModel> materials = [];
  final List<PeriodModel> periods = [];
  MaterialModel? selectedMaterial;
  bool isLoading = true;
  String? error;
  List<PeriodModel> selectedPeriods = [];

  selectMaterial(MaterialModel? value) {
    selectedMaterial = value;
    getPeriods();
  }

  togglePeriodBox(PeriodModel period){
    if(selectedPeriods.contains(period)){
      selectedPeriods.remove(period);
    } else {
      selectedPeriods.add(period);
    }
    notifyListeners();
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
    selectedPeriods.clear();
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

  getPeriods() {
    isLoading = true;
    error = null;
    notifyListeners();
    ActivateRequestRepository().getPeriods(
      selectedMaterial!.id!,
      onSuccess: (data) {
        periods.clear();
        selectedPeriods.clear();
        periods.addAll(data);
      },
      onError: (e) => error = e,
      onComplete: (){
        isLoading = false;
        notifyListeners();
      }
    );
  }

  sendRequest(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final student = context.read<AuthProvider>().student!;
    isLoading = true;
    error = null;
    notifyListeners();
    ActivateRequestRepository().savePeriodsOrder(
        materialId: selectedMaterial!.id!,
        teacherId: teacherId,
        studentId: student.studentData!.id!,
        periods: selectedPeriods.map((e) => e.id).toList(),
        onSuccess: (data) {
          showToast(context, data);
        },
        onError: (e) {
          showToast(context, e);
        },
        onComplete: (){
          isLoading = false;
          notifyListeners();
        }
    );
  }
}
