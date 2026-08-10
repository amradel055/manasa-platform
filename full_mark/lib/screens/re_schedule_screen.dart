import 'dart:convert';

import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../providers/data_handler_provider.dart';
import '../providers/local_database.dart';
import '../providers/screen_providers/home_provider.dart';
import '../widgets/app_drawer.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ReSchedule extends StatefulWidget {
  const ReSchedule({Key? key}) : super(key: key);

  @override
  _ReScheduleState createState() => _ReScheduleState();
}

class _ReScheduleState extends State<ReSchedule> {
  var schedule ;
  String? fullPath ;
  String? selectedDoctorId ;
  bool ?isLoading;

  final _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  void initState() {
    isLoading = true;
    // TODO: implement initState
    getTeacherInfo();
    getData();
    super.initState();
  }

  getData(){
    context.read<DataHandler>().getStudentOtherSchedules(0,context).then((value){
      schedule  = context.read<DataHandler>().studentOtherSchedules;
      setState(() {
        isLoading=false;
      });
      // setState(() {
      //   fullPath =schedule[0]["img"];
      // });
      // if(schedule != null && schedule.length > 0) {
      //   context.read<LocalDatabase>().getPhotoFromFirebase(schedule[0]["img"]).then((value){
      //   setState(() {
      //     fullPath = context.read<LocalDatabase>().imgUrl ;
      //   });
      // });
      // }
    });
  }

  getTeacherInfo() async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    // var _selectedDoctor = sharedPreferences.getString(selectedDoctor);
    var _selectedDoctor = context.read<HomeProvider>().selectedTeacherId;

    if(_selectedDoctor != null){
      // var selected = json.decode(_selectedDoctor);
      setState(() {
        selectedDoctorId = _selectedDoctor ;
        // selectedDoctorId = selected["teacherId"] ;
      });
      getData();
    }
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    // fullPath = context.watch<LocalDatabase>().imgUrl ;
    return Scaffold(
      backgroundColor: mainColor,
      key: _scaffoldKey,
      appBar: AppBar(
        elevation: 0,

        backgroundColor: mainColor,
        title: const Text( "Reparation",style: TextStyle(fontSize: 23),),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
        actions: [
          IconButton(
            onPressed: () {
              _scaffoldKey.currentState!.openDrawer();
            },
            icon: Icon(
              Icons.menu,
              color: Colors.white,
            ),
          )
        ],
      ),
      drawer: const DrawerWidget(),
      body: selectedDoctorId != null&&isLoading==false ? Container(
        decoration:  BoxDecoration(color:mainColor),
        width: size.width,
        height: size.height,
        child: Container(
          decoration: const BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(40),
                topRight: Radius.circular(40),
              )),

          child: SizedBox(
            width: size.width,
            height: size.height,
            child: SingleChildScrollView(padding: EdgeInsets.all(size.width*0.03),
              physics: const AlwaysScrollableScrollPhysics(),
              child: Container(
                  height: size.height *0.5,
                  width: size.width *0.8,
                  decoration:  BoxDecoration(
                      // color: Colors.white,
                      border: Border.all(
                        width: 2,
                        color: mainColor
                      ),
                      borderRadius: BorderRadius.all(
                        Radius.circular(20),

                      )),

                  child: schedule.isNotEmpty ? Image.network(    uploadUrl +schedule[0]["img"]! , fit: BoxFit.fill,
                      loadingBuilder: (BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
                        if (loadingProgress == null) {
                          return child;
                        }
                        return Center(
                          child: CircularProgressIndicator(
                            value: loadingProgress.expectedTotalBytes != null
                                ? loadingProgress.cumulativeBytesLoaded / loadingProgress.expectedTotalBytes!
                                : null,
                          ),
                        );}
                  ) : SizedBox()
              ),
            ),
          ),
        ),
      ) : showMessage(size),
    );
  }

  Widget showMessage(Size size){
    return SizedBox(
        height: size.height,
        child: Center(
          child: Text(
            "Please Select a Doctor" , style: subTitleStyle,
          ),
        )
    );
  }
}
