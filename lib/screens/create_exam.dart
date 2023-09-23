
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:full_mark/providers/screen_providers/home_provider.dart';
import 'package:full_mark/widgets/exam_card.dart';
import 'package:full_mark/widgets/finished_exam_cart.dart';
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

import 'solved_exam_screen.dart';



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
  int? selectedDoctorId ;
  final List _chapters = [];
   List<Exam> exams = [];
   List<Exam> firstExams = [];
  final List _subjects = [];
  String? chaptersDropValue ;
  String? examsDropValue ;
  String? subjectsDropValue ;
   var selectedSubject;
  Exam? selectedExam;
   var selectedPopChapter ;
   List selectedAutoChapters = [] ;
  late List<Exam> previousExams = [];

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
  getPreData() async {
    await context.read<DataHandler>().getPreviousExams().then((value) {
      previousExams = context.read<DataHandler>().previousExams;

    });
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
        backgroundColor: mainColor,
        key: _scaffoldKey,
        appBar: AppBar(
          elevation: 0,

          backgroundColor: mainColor,
          title: const Text( "Exams",style: TextStyle(fontSize: 23),),
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
       body:
       selectedDoctorId != null ?
              Container(
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

                      child: _buildBody(size)))
            :
       Container(
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
  child:  showMessage(size)))
    );
  }
   Widget _buildBody(Size size){
    return SizedBox(
        height: size.height,
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
                  padding: const EdgeInsets.all(15.0),
                  child: appDropDown(
                      "Please Select Material" , size , _subjects , onSubjectChanged, subjectsDropValue),
                ),
                Divider(thickness: size.height *0.003,),

           //      ListTile(
           //        title:  SizedBox(child: Text("Create Exam" , style: subTitleStyle,)),
           //        leading :Radio(
           //          value: examType.createExam,
           //          groupValue: type,
           //          activeColor: black,
           //          onChanged: (examType? value){
           //            setState(() {
           //              type = value! ;
           //            });
           //            _controller.reverse();
           //          },
           //        ),
           //
           //      ),
           //      Padding(
           //        padding: const EdgeInsets.fromLTRB(20 , 5 , 0 , 10),
           //        child: Text("Select Chapters" , style: regularTextStyle,),
           //      ),
           //      SizedBox(height: size.height * 0.01,),
           //      for( int i = 0 ; i < chapters.length ; i ++)
           //        Padding(
           //          padding: const EdgeInsets.fromLTRB(25 , 1 , 0 , 1),
           //          child: Row(
           //            children: [
           //              Checkbox(
           //                  value: chapters[i]["value"],
           //                  activeColor: type == examType.createExam ? black : Colors.grey,
           //                  onChanged: (bool? _value){
           //                    if(type == examType.createExam){
           //                      setState(() {
           //                        chapters[i]["value"] = _value! ;
           //                      });
           //                    }
           //                  }),
           //              Text("${chapters[i]["name"]}" , style: subTitleStyle,)
           //            ],
           //          ),
           //        )
           //    ],
           //  ),
           //  Divider(thickness: size.height *0.003,),
           //  ListTile(
           //    title:  Text("Pop Exam" , style: subTitleStyle,),
           //    leading :Radio(
           //      value: examType.popExam,
           //      groupValue: type,
           //      activeColor: black,
           //      onChanged: (examType? value){
           //        setState(() {
           //          type = value! ;
           //        });
           //        if(popType == popExamType.chapter){
           //          _controller.forward(); }
           //      },
           //    ),
           //  ),
           // type == examType.popExam ?
           //  FadeTransition(
           //    opacity: _animation ,
           //    child: Padding(
           //      padding: const EdgeInsets.fromLTRB(30 , 0 , 0 , 0),
           //      child: Container(
           //          child: appDropDown("Choose Chapter", size, _chapters, onChanged , chaptersDropValue)),
           //    ),
           //  ) : const SizedBox(),
           //  type == examType.popExam ?
           //  FadeTransition(
           //    opacity: _animation ,
           //    child: Padding(
           //      padding: const EdgeInsets.fromLTRB(30 , 0 , 0 , 0),
           //      child: Container(
           //          child: appDropDown("Choose Exam", size, _exams, onExamChanged , examsDropValue)),
           //    ),
           //  ) : const SizedBox(),
           //  Padding(
           //    padding: const EdgeInsets.fromLTRB(25 , 0 , 0 , 30),
           //    child: OutlinedButton(
           //        onPressed: ()async{
           //              if(type == examType.createExam) {
           //                selectedAutoChapters.clear();
           //                for (int i = 0; i < chapters.length; i++) {
           //                  if (chapters[i]["value"] == true) {
           //                    setState(() {
           //                      selectedAutoChapters.add(chapters[i]["id"]);
           //                    });
           //                  }
           //                }
           //
           //                context.read<DataHandler>().getExamByFilter(
           //                    selectedSubject !=
           //                        null ? [selectedSubject["id"]] : [], 0,
           //                    selectedAutoChapters, context).then((_){
           //                      Exam exam = context.read<DataHandler>().exam!;
           //                      exam.questionsList!.isNotEmpty ?
           //                      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  DoExam(exam))) :
           //                          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("There is no exams for this chapter until now ..!")))
           //                      ;
           //                });
           //              }
           //              else {
           //           selectedExam != null ? Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  DoExam(selectedExam!))) : null
           //           ;
           //
           //              }
           //          // await context.read<DataHandler>().getExamById(13.toString()).then((_){
           //          //   Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const DoExam()));
           //          // });
           //        },
           //        style: ButtonStyle(
           //          backgroundColor: buttonColor,
           //        ),
           //        child:  Text("Next" , style: TextStyle(color: yellow),)),
           //  ),

                exams.isNotEmpty? SizedBox(
       height: size.height*.35,
       child: ListView.separated(
        itemCount: exams.length,
        padding: const EdgeInsets.all(0.0),
        separatorBuilder: (_, __) => const SizedBox(height: 10),
        itemBuilder: (context, index) {
        final ex = exams[index];
        return GestureDetector(
          onTap: (){
            Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  DoExam(exams[index]))) ;



            // context.read<DataHandler>().getExamByFilter(
            //       selectedSubject !=
            //           null ? [selectedSubject["id"]] : [], 0,
            //       selectedAutoChapters, context).then((_){
            //     Exam exam = context.read<DataHandler>().exam!;
            //     exam.questionsList!.isNotEmpty ?
            //     ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text("There is no exams for this chapter until now ..!")))
            //     ;
            //   });


            }
              // Navigator.push(context, MaterialPageRoute(builder: (_)=> InboxDetailScreen(id:question.id)));

          ,
          child:    examCardWidget(ex.examName??"",  ex.degree.toString(),ex.questionsList!.length.toString(),  ex.timer.toString(),  ex.lessonName??"",size,"" ),
          // Card(
          //   clipBehavior: Clip.antiAlias,
          //   child: Column(
          //     children: [
          //       ListTile(
          //         // leading: Icon(Icons.arrow_drop_down_circle),
          //         title:  Text(ex.examName??""),
          //         subtitle: Text(
          //           ex.degree.toString()??"",
          //           style: TextStyle(color: Colors.black.withOpacity(0.6)),
          //         ),
          //       ),
          //       // Padding(
          //       //   padding: const EdgeInsets.all(16.0),
          //       //   child: Text(
          //       //     ex.lessonName??"",
          //       //     style: TextStyle(color: Colors.black.withOpacity(0.6)),
          //       //   ),
          //       // ),
          //
          //     ],
          //   ),
          // ),
        );
        //   ExpandedCard(
        //     trailing:Text(
        //       question.sentDate.toString()
        //         ),
        //   // leading: Text(question.sentDate.toString()??""),
        //   title: Text(question.subject??""),
        //   subtitle:Text(question.materialName??""),
        //
        //   // imagePath: question.image,
        //   children: [
        //     // Padding(
        //     //   padding: const EdgeInsets.all(10),
        //     //   child: Align(alignment: AlignmentDirectional.centerStart, child: Text(question.teacherAnswersDto!.text??"")),
        //     // ),
        //     // for(final x in question.teacherAnswersDto!.teacherAnswerDetailDtoList)
        //     //   Column(
        //     //     crossAxisAlignment: CrossAxisAlignment.start,
        //     //     children: [
        //     //       ImageWidget(path: x.image??"", width: double.infinity, height: 200, fit: BoxFit.cover,),
        //     //       Padding(
        //     //         padding: const EdgeInsetsDirectional.fromSTEB(10, 5, 10, 10),
        //     //         child: Text(x.description??""),
        //     //       ),
        //     //     ],
        //     //   ),
        //   ],
        // );
        },
    ),
     ):SizedBox(),
                previousExams.isNotEmpty?   _buildBar(size,"Previous Exams"):SizedBox()
                ,
                previousExams.isNotEmpty?   SizedBox(
                    height: size.height*.25,
                    child: _buildPreBody(size)):SizedBox()
          ],
        ),
      ]  ));
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
   context.read<DataHandler>().getExamByFilter([selectedSubject["id"]], 1 , [], context).then((value){
     setState(() {
       exams = context.read<DataHandler>().examsList;
       exams.forEach((element) {_exams.add(element.examName); });
     });
   });
   getPreData();
  }
  onExamChanged(value){
    setState(() {
      examsDropValue = value ;
      selectedExam = exams[exams.indexWhere((exam) => exam.examName == value)];
    });
  }
  Widget _buildPreBody(Size size) {
    return Container(
      height: size.height,
      child: SingleChildScrollView(
        physics: AlwaysScrollableScrollPhysics(),
        child: Container(
          height: size.height * 0.25,
          child: ListView(

            children: previousExams.map((Exam exam) {
              return GestureDetector(
                onTap: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => SolvedExam(exam)));
                },
                child: finishExamCardWidget(   exam.examName??"","${exam.score} / ${exam.degree}",size),

                // Card(
                //   elevation: 8.0,
                //   color: Colors.grey,
                //   child: SizedBox(
                //       child: ListTile(
                //         title: Center(
                //             child: Row(
                //               mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                //               children: [
                //                 Text(
                //                   exam.examName!,
                //                   style: const TextStyle(
                //                       color: Colors.black,
                //                       fontSize: 22,
                //                       fontWeight: FontWeight.bold),
                //                 ),
                //                 // Text("${exam.score} / ${exam.degree}"),
                //               ],
                //             )),
                //         subtitle: Text(
                //           "${exam.score} / ${exam.degree}",
                //           style: titleTextStyle,
                //         ),
                //       )),
                // ),
              );
            }).toList(),
          ),
        ),
      ),
    );
  }
  _buildBar(Size size,  String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(25,8,8,0),
      child: Container(
        width: size.width * 0.29,
        height: size.height * 0.05,
        decoration: BoxDecoration(
          color: mainColor,
          borderRadius: const BorderRadius.all(Radius.circular(20)),),
        child: Center(
            child: SizedBox(
                height: size.height * 0.025,
                child: FittedBox(
                    child: Text(
                      title,
                      style: TextStyle(fontWeight:FontWeight.bold ,
                          color:Colors.white),
                    )))),
      ),
    );
  }

}
