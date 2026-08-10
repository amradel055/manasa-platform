
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:full_mark/providers/screen_providers/home_provider.dart';
import '../constants/colors.dart';
import '../constants/helper.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/exam_model.dart';
import '../providers/data_handler_provider.dart';
import '../screens/do_exam_screen.dart';
import '../widgets/app_drawer.dart';
import '../widgets/drop_down_list.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';



class CreateExam extends StatefulWidget {
  const CreateExam({Key? key}) : super(key: key);

  @override
  _CreateExamState createState() => _CreateExamState();
}

class _CreateExamState extends State<CreateExam> with TickerProviderStateMixin{
  late AnimationController _controller;
  late Animation<double> _animation;
  examType type = examType.createExam ;
  popExamType popType = popExamType.chapter ;

  List chapters = [];
  List subjects = [];
  List _exams = [] ;
  String? selectedDoctorId ;
  final List _chapters = [];
   List<Exam> exams = [];
  final List _subjects = [];
  String? chaptersDropValue ;
  String? examsDropValue ;
  String? subjectsDropValue ;
   var selectedSubject;
  Exam? selectedExam;
   var selectedPopChapter ;
   List selectedAutoChapters = [] ;
  final GlobalKey<ScaffoldState> _scaffoldKey =  GlobalKey<ScaffoldState>();
  @override
  void initState(){
    // TODO: implement initState
    getTeacherInfo() ;
    super.initState();
    _controller = AnimationController(
        duration: const Duration(milliseconds: 300), vsync: this);

    _animation =  Tween<double>(begin: -1, end : 1).animate(_controller);
  }


   getTeacherInfo() async{
    // SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    // var _selectedDoctor = sharedPreferences.getString(selectedDoctor);
     var _selectedDoctor = context.read<HomeProvider>().selectedTeacherId;

     if(_selectedDoctor != null){
      // var selected = json.decode(_selectedDoctor);
      setState(() {
        selectedDoctorId = _selectedDoctor ;
      });
      getData();
    }
  }

  getData()async{
    if(selectedDoctorId != null){
    await context.read<DataHandler>().getPeriodsByTeacher(selectedDoctorId.toString()).then((_){
      chapters = context.read<DataHandler>().periods ;
      for(int i = 0 ; i < chapters.length ; i++){
        setState(() {
          chapters[i]["value"] = false ;
          _chapters.add(chapters[i]["name"]);
        });
      }
    });
    await context.read<DataHandler>().getSubjectsByTeacher(selectedDoctorId.toString(), context).then((_){
      subjects = context.read<DataHandler>().subjects ;
      for(int i = 0 ; i < subjects.length ; i++){
        setState(() {
          _subjects.add(subjects[i]["name"]);
        });
      }
    });
    }
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        backgroundColor: appBarColor,
        title: Text("Exams" , style: titleTextStyle,),
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
       body:
       selectedDoctorId != null ?
              _buildBody(size)
            :
              showMessage(size)
    );
  }
   Widget _buildBody(Size size){
    return SizedBox(
        height: size.height,
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: appDropDown(
                        "Subject" , size , _subjects , onSubjectChanged, subjectsDropValue),
                  ),
                  Divider(thickness: size.height *0.003,),
                  ListTile(
                    title:  SizedBox(child: Text("Create Exam" , style: subTitleStyle,)),
                    leading :Radio(
                      value: examType.createExam,
                      groupValue: type,
                      activeColor: black,
                      onChanged: (examType? value){
                        setState(() {
                          type = value! ;
                        });
                        _controller.reverse();
                      },
                    ),

                  ),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(20 , 5 , 0 , 10),
                    child: Text("Select Chapters" , style: regularTextStyle,),
                  ),
                  SizedBox(height: size.height * 0.01,),
                  for( int i = 0 ; i < chapters.length ; i ++)
                    Padding(
                      padding: const EdgeInsets.fromLTRB(25 , 1 , 0 , 1),
                      child: Row(
                        children: [
                          Checkbox(
                              value: chapters[i]["value"],
                              activeColor: type == examType.createExam ? black : Colors.grey,
                              onChanged: (bool? _value){
                                if(type == examType.createExam){
                                  setState(() {
                                    chapters[i]["value"] = _value! ;
                                  });
                                }
                              }),
                          Text("${chapters[i]["name"]}" , style: subTitleStyle,)
                        ],
                      ),
                    )
                ],
              ),
              Divider(thickness: size.height *0.003,),
              ListTile(
                title:  Text("Pop Exam" , style: subTitleStyle,),
                leading :Radio(
                  value: examType.popExam,
                  groupValue: type,
                  activeColor: black,
                  onChanged: (examType? value){
                    setState(() {
                      type = value! ;
                    });
                    if(popType == popExamType.chapter){
                      _controller.forward(); }
                  },
                ),
              ),
             type == examType.popExam ?
              FadeTransition(
                opacity: _animation ,
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(30 , 0 , 0 , 0),
                  child: Container(
                      child: appDropDown("Choose Chapter", size, _chapters, onChanged , chaptersDropValue)),
                ),
              ) : const SizedBox(),
              type == examType.popExam ?
              FadeTransition(
                opacity: _animation ,
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(30 , 0 , 0 , 0),
                  child: Container(
                      child: appDropDown("Choose Exam", size, _exams, onExamChanged , examsDropValue)),
                ),
              ) : const SizedBox(),
              Padding(
                padding: const EdgeInsets.fromLTRB(25 , 0 , 0 , 30),
                child: OutlinedButton(
                    onPressed: ()async{
                          if(type == examType.createExam) {
                            selectedAutoChapters.clear();
                            for (int i = 0; i < chapters.length; i++) {
                              if (chapters[i]["value"] == true) {
                                setState(() {
                                  selectedAutoChapters.add(chapters[i]["id"]);
                                });
                              }
                            }

                            context.read<DataHandler>().getExamByFilter(
                                selectedSubject !=
                                    null ? [selectedSubject["id"]] : [], 0,
                                selectedAutoChapters, context).then((_){
                                  Exam exam = context.read<DataHandler>().exam!;
                                  exam.questionsList!.isNotEmpty ?
                                  Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  DoExam(exam))) :
                                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("There is no exams for this chapter until now ..!")))
                                  ;
                            });
                          }
                          else {
                       selectedExam != null ? Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  DoExam(selectedExam!))) : null
                       ;

                          }
                      // await context.read<DataHandler>().getExamById(13.toString()).then((_){
                      //   Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const DoExam()));
                      // });
                    },
                    style: ButtonStyle(
                      backgroundColor: buttonColor,
                    ),
                    child:  Text("Next" , style: TextStyle(color: yellow),)),
              ),
            ],
          ),
        )
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

  onChanged (value) {
    if(popType == popExamType.chapter ){
    setState(() {
      chaptersDropValue = value ;
      selectedPopChapter = chapters[chapters.indexWhere((element) => element["name"] == value)] ;
      print(selectedPopChapter);
    });
      context.read<DataHandler>().getExamByFilter([selectedSubject["id"]], 1 , [selectedPopChapter["id"]], context).then((value){
        setState(() {
          exams = context.read<DataHandler>().examsList;
          exams.forEach((element) {_exams.add(element.examName); });
        });
      });

  }}


  onSubjectChanged(value){
   setState(() {
     subjectsDropValue = value ;
     selectedSubject = subjects[subjects.indexWhere((subject) => subject["name"] == value)];
   });
  }
  onExamChanged(value){
    setState(() {
      examsDropValue = value ;
      selectedExam = exams[exams.indexWhere((exam) => exam.examName == value)];
    });
  }

}
