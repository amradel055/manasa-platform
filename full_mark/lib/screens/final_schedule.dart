import 'dart:convert';

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

class FinalSchedule extends StatefulWidget {
  const FinalSchedule({Key? key}) : super(key: key);

  @override
  _FinalScheduleState createState() => _FinalScheduleState();
}

class _FinalScheduleState extends State<FinalSchedule> {
  var schedule ;
  String? fullPath ;
  int? selectedDoctorId ;
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  void initState() {
    // TODO: implement initState
   getTeacherInfo();
    super.initState();
  }

  getData(){
    context.read<DataHandler>().getStudentOtherSchedules(1, context).then((value){
      schedule  = context.read<DataHandler>().studentOtherSchedules;
      context.read<LocalDatabase>().getFinalPhotoFromFirebase(schedule[0]["img"]).then((value){
        setState(() {
          fullPath = context.read<LocalDatabase>().finalImgUrl ;
        });
      });
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
    fullPath = context.watch<LocalDatabase>().finalImgUrl ;
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        backgroundColor: appBarColor,
        title: Text("Final Schedule" , style: titleTextStyle,),
        centerTitle: true,
        leading: IconButton(
          onPressed: (){
            Navigator.pop(context);
          },
          icon: Icon(Icons.arrow_back , color: yellow,),
        ),
        actions: [
          IconButton(
            onPressed: (){
              _scaffoldKey.currentState!.openDrawer();
            },
            icon: Icon(Icons.menu , color: yellow,),
          )
        ],
      ),
      drawer: const DrawerWidget(),
      body: selectedDoctorId != null ? SizedBox(
        width: size.width,
        height: size.height,
        child: SingleChildScrollView(padding: EdgeInsets.all(size.width*0.03),
          physics: const AlwaysScrollableScrollPhysics(),
          child: SizedBox(
              height: size.height *0.5,
              width: size.width *0.8,
              child: fullPath != null ? Image.network(fullPath! , fit: BoxFit.fill,
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
                    );},) : SizedBox()
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
