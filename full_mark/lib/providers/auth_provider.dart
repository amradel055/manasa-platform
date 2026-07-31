import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../constants/strings.dart';
import '../helper_methods/api_services.dart';
import '../models/login_data_model.dart';
import '../screens/home_screen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:device_info_plus/device_info_plus.dart';



class AuthProvider with ChangeNotifier {



  LoginData? student ;
  bool loading = false ;
  late String? imeiNo ;

  Future signUp(BuildContext context , String userName, String firstName, String fatherName, String grandFatherName
      , String familyName, String mobile,String  password, String email , schoolStageId , stateId) async {
    loading = true ;
    notifyListeners();
    await ApiServices().registerStudent(userName, firstName,
        fatherName, grandFatherName, familyName, mobile, password, email , schoolStageId , stateId).then((res){
          if(res["msgType"] == "success"){
             signIn(context, userName, password);
             }
          else {
            loading = false ;
            notifyListeners();
            ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("${res["msg"]}"
              , textAlign: TextAlign.center,) , backgroundColor: Colors.red,));
          }
    }).catchError((e){
      loading = false ;
      notifyListeners();
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("نأسف لوجود خطأ ما يرجي المحاوله مره اخري او الاتصال بالاداره" ,
            textAlign: TextAlign.center,),
            backgroundColor: Colors.red,));
    });
  }
  Future signIn(BuildContext context ,String userName , String  password) async {
    loading = true ;
    notifyListeners();
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
    //TODO add ios Id
    AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
    print('Running on ${androidInfo.id}'); // e.g. "Moto G (4)"

    await ApiServices().loginStudent(userName,password , androidInfo.id! ).then((res){
      if(res["msg"] == null) {
        student = LoginData.fromJson(res);
        notifyListeners();
        _saveData(student!) ;
        loading = false ;
        notifyListeners();
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const HomeScreen()));
      } else {
        loading = false ;
        notifyListeners();
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text("${res["msg"]}" ,
              textAlign: TextAlign.center,) ,
              backgroundColor: Colors.red,));
      }
    }).catchError((e){
      loading = false ;
      notifyListeners();
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text("نأسف لوجود خطأ ما يرجي المحاوله مره اخري او الاتصال بالاداره" ,
              textAlign: TextAlign.center,) ,
              backgroundColor: Colors.red,));

    });
  }

  _saveData(LoginData student)async {
   SharedPreferences preferences = await SharedPreferences.getInstance();
   preferences.setString(dataKey, jsonEncode(student));
 }

 Future<LoginData?> getData()async {
   SharedPreferences preferences = await SharedPreferences.getInstance();
   String? _student = preferences.getString(dataKey) ;
   if (_student != null){
   student  = LoginData.fromJson(jsonDecode(_student));
   notifyListeners();
   return student ;
   }
   else {
     return student ;
   }
 }

 Future logOut()async{
   SharedPreferences preferences = await SharedPreferences.getInstance();
   student = null ;

   preferences.remove(dataKey);
   preferences.remove(selectedDoctor);
   notifyListeners();

 }

}