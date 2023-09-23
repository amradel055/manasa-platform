import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:flutter/material.dart';
import 'package:sqflite/sqflite.dart';



class LocalDatabase with ChangeNotifier {


  late Database?  localDatabase ;
  List<Map> studentLocalSchedule = [] ;
  String? imgUrl ;
  String? finalImgUrl ;
  Future<Database> createDatabase()async{
  return localDatabase  = await openDatabase(
  "full_mark.db" ,
  version: 1 ,
  onCreate: (database , version){
  print("database created");
  database.execute("CREATE TABLE studentSchedule (id INTEGER PRIMARY KEY , teacherId INTEGER , schoolStageId INTEGER , day INTEGER , time VARCHAR , materialId INTEGER , teacherName VARCHAR)")
      .then((value){

  print("studentSchedule table created");
  }).catchError((error){
  print("$error when create studentSchedule table");
  });


  },
  onOpen: (database){
  print("database opened");
  //TODO get data from database
  getSchedule("studentSchedule", database).then((value){
    studentLocalSchedule = value ;
  print(value);
  notifyListeners();
  });
  }

  );
  }


  void insertToDatabase(String tableName , {int? teacherId  , int? schoolStageId  ,
  int? day  , String? time  , int? materialId , String? teacherName}){
  if(tableName == "studentSchedule"){
  localDatabase!.transaction((txn){
  return txn.rawInsert("INSERT INTO $tableName (teacherId, schoolStageId, day, time, materialId , teacherName) VALUES($teacherId , $schoolStageId , $day , '$time' , $materialId , '$teacherName')").then((value){
  print("$value insert to $tableName table done");
  }).catchError((error){
  print("$error when insert to $tableName table");
  });

  });}

  }

  Future<List<Map>> getSchedule(String tableName , Database database)async{
  return await  database.rawQuery("SELECT * FROM $tableName");
  }

  deleteFromDatabase(String tableName , int id){
  localDatabase!.rawDelete("DELETE FROM $tableName WHERE id = ?" , [id]);
  }

  // deleteAllFromDatabase(String tableName){
  //   localDatabase!.rawDelete("DELETE * FROM $tableName");
  // }

  Future getPhotoFromFirebase (String name)async{
    imgUrl = await FirebaseStorage.instance.ref().child('uploads/$name').getDownloadURL();
    notifyListeners();
  }

  Future getFinalPhotoFromFirebase (String name)async{
    finalImgUrl = await FirebaseStorage.instance.ref().child('uploads/$name').getDownloadURL();
    notifyListeners();
  }



}