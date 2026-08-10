import 'dart:convert';

import 'package:desktop_screen/providers/screen_providers/home_provider.dart';
import 'package:desktop_screen/widgets/card_widget.dart';
import 'package:desktop_screen/widgets/loading_widget.dart';
import 'package:flutter/material.dart';

import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/lesson_model.dart';
import '../providers/data_handler_provider.dart';
import '../screens/lesson_screen.dart';
import '../widgets/app_drawer.dart';
import '../widgets/drop_down_list.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';



class AllSession extends StatefulWidget {
  const AllSession({Key? key}) : super(key: key);

  @override
  _AllSessionState createState() => _AllSessionState();
}

class _AllSessionState extends State<AllSession> with TickerProviderStateMixin  {

   String? selectedDoctorId ;
  late List subjects ;
  final List _subjects = [];
  var subjectsDropValue;
  var selectedSubject;
  List<Lesson> lessons = [] ;
  List<Lesson> freeLessons = [] ;
  bool isLoading = false ;
  final _scaffoldKey = GlobalKey<ScaffoldState>();
   int index = 0;

   late Animation<double> animation;
    AnimationController  ?_controller;
   ScrollController scrollController = ScrollController();

  getTeacherInfo() async{
    // SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    // var _selectedDoctor = sharedPreferences.getString(selectedDoctor);
    selectedDoctorId = context.read<HomeProvider>().selectedTeacherId;
    if(selectedDoctorId != null){
      setState(() {});
     getData();
    }
  }

  getData()async{
    if(selectedDoctorId != null){
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

  getLesson(materialId)async{
    await context.read<DataHandler>().getLessons(materialId, context).then((_){
      setState(() {
        lessons = context.read<DataHandler>().lessons ;
          isLoading = false ;

      });
    }).catchError((e){
      setState(() {
        isLoading = false ;
      });
    });
  }

   getFreeLesson(materialId)async{
     await context.read<DataHandler>().getFreeLessons(materialId, context).then((_){
       setState(() {
         freeLessons = context.read<DataHandler>().freeLessons ;
         isLoading = false ;

       });
     }).catchError((e){
       setState(() {
         isLoading = false ;
       });
     });
   }
  @override
  void initState() {
    // TODO: implement initState
    getTeacherInfo();
    super.initState();
    _controller = AnimationController(duration: const Duration(milliseconds: 250), vsync: this);
    animation = Tween<double>(begin: 0, end: 1).animate(_controller!);
    _controller!.forward();

  }

  @override
  void dispose() {
    // TODO: implement dispose
     clear();
    super.dispose();
  }

   clear () {
     Provider.of<DataHandler>(context , listen: false).clearLesson();
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
        title: const Text( "Sessions",style: TextStyle(fontSize: 23),),
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
      body: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        child: Center(
          child: Container(
            decoration:  BoxDecoration(color:mainColor),
            width: size.width*.5,
            height: size.height*.9,
            child: Container(
              decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.all(
               Radius.circular(40),

                  )),

              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children:  [
                  selectedDoctorId != null ?
                  _buildBody(size) :
                  showMessage(size)
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildBody(Size size){
    return Stack(
      children: [
        SizedBox(
            height: size.height*.88,
            child: SingleChildScrollView(
              physics: const AlwaysScrollableScrollPhysics(),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: appDropDown(
                        "" , size , _subjects , onSubjectChanged, subjectsDropValue),
                  ),
                  Divider(thickness: size.height *0.003,),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(25,0,0,0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        _buildBar(size, 0, "Lessons"),
                        // Container(
                        //   width: 1,
                        //   height: size.height *0.04,
                        //   color: Colors.grey,
                        // ),
                        // _buildBar(size, 1 , "Schedule"),
                        // Container(
                        //   width: 1,
                        //   height: size.height * 0.04,
                        //   color: Colors.grey,
                        // ),
                        _buildBar(size, 2, "Free Lessons"),
                      ],
                    ),
                  ),
                  index == 0 ?
                      Column(
                        children: [
                          for(Lesson lesson in lessons)
                            GestureDetector(
                                onTap: (){
                                  Navigator.push(context, MaterialPageRoute(builder: (context) => LessonScreen(lesson)));
                                },
                                child: cardWidget(lesson.lessonName!,lesson.lessonDescription!,lesson.createdDate!,size,lesson.image??"https://getsession.org/assets/images/logo-black.png" ,))

                        ],
                      ): Column(
                    children: [
                      for(Lesson lesson in freeLessons)
                        GestureDetector(
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => LessonScreen(lesson)));
                            },
                            child: cardWidget(lesson.lessonName!,lesson.lessonDescription!,lesson.createdDate!,size,lesson.image??"https://getsession.org/assets/images/logo-black.png" ,))

                    ],
                  )
                  // TextButton(
                    //     onPressed: (){
                    //     Navigator.push(context, MaterialPageRoute(builder: (context) => LessonScreen(lesson)));
                    //     },
                    //     child: SizedBox(height:size.height *0.035,child: FittedBox(child: Text(lesson.lessonName! , style: TextStyle(fontSize: size.width *0.04 , fontWeight: FontWeight.bold),))))
                ],
              ),
            )
        ),
        isLoading == true ?
        Positioned(
            top: size.height * 0.5,
            left: size.width * 0.3,
            right: size.width * 0.3,
            child: loadingWidget()) :
        const SizedBox(),
      ],
    );
  }

  Widget showMessage(Size size){
    return SizedBox(
        height: size.height*.9,
        child: Center(
          child: Text(
            "Please Select a Doctor" , style: subTitleStyle,
          ),
        )
    );
  }
   // Widget _buildAllBody(Size size) {
   //   return Container(
   //     decoration: const BoxDecoration(color: Colors.black38, borderRadius: BorderRadius.all(Radius.circular(10))),
   //     child: index == 0 ? _buildDoctorsList(size) : _buildAllFeed(size),
   //   );
   // }
  onSubjectChanged(value){

    setState(() {
      isLoading = true ;
      subjectsDropValue = value ;
      selectedSubject = subjects[subjects.indexWhere((subject) => subject["name"] == value)];
    });
    getLesson(selectedSubject["id"]);
    getFreeLesson(selectedSubject["id"]);

  }
   _buildBar(Size size, int _index, String title) {
     return Padding(
       padding: const EdgeInsets.fromLTRB(8,0,8,0),
       child: Container(
         width: size.width * 0.15,
         height: size.height * 0.06,
           decoration: BoxDecoration(
             color:_index == index ? mainColor:greyColor,
           borderRadius: const BorderRadius.all(Radius.circular(15)),),
         child: GestureDetector(
           onTap: () {
             scrollController.animateTo(0, duration: const Duration(milliseconds: 150), curve: Curves.bounceInOut);

             _controller!.reverse().then((_) {
               setState(() {
                 index = _index;
               });
               if(index==2){
                 context.read<DataHandler>().getAllFeed();
               }
               _controller!.forward();
             });
           },
           child: Center(
               child: SizedBox(
                   height: size.height * 0.025,
                   child: FittedBox(
                       child: Text(
                         title,
                         style: TextStyle(fontWeight: _index == index ? FontWeight.bold : FontWeight.normal,
                             color:_index == index ?Colors.white:Colors.black),
                       )))),
         ),
       ),
     );
   }

}
