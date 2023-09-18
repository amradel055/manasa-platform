import 'dart:convert';
import 'dart:io';

import 'package:desktop_screen/constants/strings.dart';
import 'package:desktop_screen/helper_methods/api_services.dart';
import 'package:desktop_screen/models/inbox_question_detail_model.dart';
import 'package:desktop_screen/providers/auth_provider.dart';
import 'package:desktop_screen/repositories/questions_repository.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:provider/provider.dart';


import '../../helper_methods/show_toast.dart';
import '../../models/inbox_question_model.dart';
import '../../models/material_model.dart';
import '../../repositories/activate_request_repository.dart';
import 'home_provider.dart';
import 'package:http/http.dart' as http;

class InboxProvider with ChangeNotifier {

  final List<InboxQuestionModel> questions = [];
   InboxQuestionDetailModel?questionDetail ;
  final List<MaterialModel> materials = [];
  MaterialModel? selectedMaterial;
  bool isLoading = true;
  bool isDetailLoading = true;
  String? error;
  TextEditingController subjectText=new TextEditingController();
  TextEditingController bodyText=new TextEditingController();
  TextEditingController subjectReplyText=new TextEditingController();
  TextEditingController bodyReplyText=new TextEditingController();
  String? image;
  String? video;
  String? replyImage;

   uploadImage(File imageFile) async {
    var stream = http.ByteStream(Stream.castFrom(imageFile.openRead()));
    var length = await imageFile.length();
    var uri = Uri.parse(uploads_thumb);
    var request = http.MultipartRequest('POST', uri);

    var multipartFile = http.MultipartFile('file', stream, length,
        filename: imageFile.path.split("/").last);
    request.files.add(multipartFile);

    var response = await request.send();

    if (response.statusCode == 200) {
      print("تم رفع الصورة بنجاح.");
      // اكمل هنا لمعالجة الاستجابة من السيرفر
    } else {
      print("فشل في رفع الصورة.");
    }}
  uploadVideo(File imageFile) async {
    var stream = http.ByteStream(Stream.castFrom(imageFile.openRead()));
    var length = await imageFile.length();
    var uri = Uri.parse(uploads_thumb);
    var request = http.MultipartRequest('POST', uri);

    var multipartFile = http.MultipartFile('file', stream, length,
        filename: imageFile.path.split("/").last);
    request.files.add(multipartFile);

    var response = await request.send();

    if (response.statusCode == 200) {
      print("تم رفع الفيديو بنجاح.");
      // اكمل هنا لمعالجة الاستجابة من السيرفر
    } else {
      print("فشل في رفع الفيديو.");
    }}

  selectMaterial(BuildContext context, MaterialModel? value) {

    // selectedMaterial = null;
    selectedMaterial = value;
    getQuestions(context);
  }

  getQuestions(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final student = context.read<AuthProvider>().student!;

    isLoading = true;
    error = null;
    notifyListeners();
    QuestionsRepository().getInbox(
        teacherId: teacherId,
        materialId: selectedMaterial!.id!,
        studentId:  student.studentData!.id!,
        onSuccess: (data) {
          questions.clear();
          questions.addAll(data);},
        onError: (e) => error = e,
        onComplete: () {
          isLoading = false;
          notifyListeners();
        }
    );
  }

  // getQuestionsDetails(BuildContext context,int id) {
  //
  //
  //   isLoading = true;
  //   error = null;
  //   // notifyListeners();
  //   QuestionsRepository().getInboxDetail(
  //       id: id,
  //
  //       onSuccess: (data) =>(){
  //         // LoginData.fromJson(res)
  //         questionDetail= InboxQuestionDetailModel.fromJson(data as Map<String, dynamic>  );
  //         print(questionDetail);
  //       } ,
  //       onError: (e) => error = e,
  //       onComplete: () {
  //         isLoading = false;
  //         notifyListeners();
  //       }
  //   );
  // }


  Future getInboxDetail(BuildContext context ,int id) async {
    isDetailLoading = true ;
    notifyListeners();

    await ApiServices().findInboxDetail(id ).then((res){
      if(res != null) {
        questionDetail = InboxQuestionDetailModel.fromJson(res);

        isDetailLoading = false ;
        notifyListeners();
      } else {
        isDetailLoading = false ;
        notifyListeners();
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text("${res["msg"]}" ,
              textAlign: TextAlign.center,) ,
              backgroundColor: Colors.red,));
      }
    }).catchError((e){
      isDetailLoading = false ;
      notifyListeners();
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text("نأسف لوجود خطأ ما يرجي المحاوله مره اخري او الاتصال بالاداره" ,
              textAlign: TextAlign.center,) ,
            backgroundColor: Colors.red,));

    });
  }
  // Future getQuestionsDetails(BuildContext context ,int id ) async {
  //   isLoading = true;
  //   error = null;
  //   notifyListeners();
  //
  //   await QuestionsRepository().getInboxDetail(
  //      id ).then((res){
  //     if(res != null) {
  //       isLoading = false ;
  //       questionDetail= InboxQuestionDetailModel.fromJson(res );
  //     } else {
  //       isLoading = false ;
  //       notifyListeners();
  //       ScaffoldMessenger.of(context).showSnackBar(
  //           SnackBar(content: Text("${res["msg"]}" ,
  //             textAlign: TextAlign.center,) ,
  //             backgroundColor: Colors.red,));
  //     }
  //   }).catchError((e){
  //     isLoading = false ;
  //     notifyListeners();
  //     ScaffoldMessenger.of(context).showSnackBar(
  //         const SnackBar(
  //           content: Text("نأسف لوجود خطأ ما يرجي المحاوله مره اخري او الاتصال بالاداره" ,
  //             textAlign: TextAlign.center,) ,
  //           backgroundColor: Colors.red,));
  //
  //   });
  // }

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
  sendInboxRequest(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final student = context.read<AuthProvider>().student!;
    isLoading = true;
    error = null;
    notifyListeners();
    ApiServices().sendInbox(
        materialId: selectedMaterial!.id!,
        teacherId: teacherId,
        studentId: student.studentData!.id!,
        // image: image!.split("/").last??"",
        image: image??"",
        subject:subjectText.text ,
        content: bodyText.text,
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
  sendInboxReplyRequest(BuildContext context,int message) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final student = context.read<AuthProvider>().student!;
    isLoading = true;
    error = null;
    notifyListeners();
    ApiServices().sendReplyInbox(
        teacherId: teacherId,
        studentId: student.studentData!.id!,
        image: replyImage!.split("/").last??"",
        message:message ,
        mBody: bodyReplyText.text,
        branchId: branchId,
        companyId: companyId,

        onSuccess: (data) {
          showToast(context, data);
        },
        onError: (e) {
          showToast(context, e);
        },
        onComplete: (){
          isLoading = false;
          Navigator.pop(context);
          sendInboxRequest(context);
          notifyListeners();
        }
    );
  }
  sendScreenInfoRequest(BuildContext context) {
    final teacherId = context.read<HomeProvider>().selectedTeacherId;
    final student = context.read<AuthProvider>().student!;
    isLoading = true;
    error = null;
    notifyListeners();
    ApiServices().sendScreenInfo(
        teacherId: teacherId,
        studentId: student.studentData!.id!,
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
