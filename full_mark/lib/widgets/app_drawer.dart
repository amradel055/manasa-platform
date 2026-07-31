


import 'package:flutter/material.dart';
import 'package:full_mark/screens/auto_exam_request_screen.dart';
import 'package:full_mark/screens/online_meetings.dart';
import 'package:full_mark/screens/splash.dart';
import '../constants/colors.dart';

import '../constants/text_styles.dart';
import '../models/login_data_model.dart';
import '../providers/auth_provider.dart';
import '../screens/activate_request_screen.dart';
import '../screens/all_sessions.dart';
import '../screens/common_questions_screen.dart';
import '../screens/create_chat.dart';
import '../screens/create_exam.dart';
import '../screens/exams_history.dart';
import '../screens/final_schedule.dart';
import '../screens/home_screen.dart';
import '../screens/inbox_screen.dart';
import '../screens/login_screen.dart';
import '../screens/re_schedule_screen.dart';
import 'package:provider/provider.dart';


class DrawerWidget extends StatefulWidget {
  const DrawerWidget({Key? key}) : super(key: key);

  @override
  _DrawerState createState() => _DrawerState();
}

class _DrawerState extends State<DrawerWidget> with TickerProviderStateMixin {
  late String userName ;
  late AnimationController controller;
  late Animation<Offset> animation;
  late AnimationController _controller;
  late Animation<double> _animation;
  late LoginData? student ;
   List doctorsList = [] ;
  @override
  void initState() {
    // TODO: implement initState
    student = context.read<AuthProvider>().student;
    super.initState();
     controller = AnimationController(
        duration: const Duration(milliseconds: 600), vsync: this);
    _controller = AnimationController(
        duration: const Duration(milliseconds: 600), vsync: this);
     animation =  Tween<Offset>(begin: const Offset(0, 0), end : const Offset(0, 0.05)).animate(controller);
    _animation =  Tween<double>(begin: -1, end : 1).animate(_controller);
    controller.forward();
    _controller.forward();

  }



  @override
  void dispose() {
    controller.dispose();
    _controller.dispose();
    super.dispose();
  }

  Widget createTextWidget (Size size , String name){
    return  SizedBox(
      height: size.height *0.046,
      child: FittedBox(
          child: Text(name , style: appDrawerFont,)),
    );
  }

  @override
  Widget build(BuildContext context) {

    Size size = MediaQuery.of(context).size ;

    return Drawer(
      elevation: 16,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
            topRight: Radius.circular(40),
            bottomRight: Radius.circular(40)),
      ),
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        scrollDirection: Axis.vertical,
        child: Container(
          height: size.height,

          decoration:  BoxDecoration(
              color: appDrawerUpColor,
              borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(40),
                topRight: Radius.circular(40),
              )          ),
          child: FadeTransition(
            opacity: _animation,
            child: SlideTransition(
              position: animation,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    decoration:  BoxDecoration(
                        // color:appDrawerUpColor,

                        borderRadius: BorderRadius.only(
                          bottomRight: Radius.circular(40),
                          topRight: Radius.circular(40),
                        )          ),
                    child: Column(
                      children: [
                        Center(
                          child: Padding(
                            padding: const EdgeInsets.all(20),
                            child: Container(
                                decoration: BoxDecoration(
                                    color:mainColor,
                                    borderRadius: BorderRadius.circular(90)
                                ),
                                child: Icon(Icons.person_rounded , size: size.width * 0.2,)),
                          ),
                        ),
                        Center(child:
                        SizedBox(height: size.height *0.055, child: FittedBox(child: Text( student != null ? student!.studentData!.firstName!.toUpperCase() : "Guest" , textAlign: TextAlign.center,)))) ,
                        SizedBox(height: size.height * 0.02,) ,

                        // const Divider(thickness: 1.5,),
                      ],
                    ),
                  ),


                  Container(
                    color: appDrawerColor,
                    height: size.height*.7,
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(30,10,0,0),
                      child: Column(
                        children: [
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.home_filled,color: Colors.white,),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Home")
                              ],
                            ),
                            onTap: (){
                              Navigator.popUntil(context, (route) => route.isFirst);
                            },
                          ),

                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.check,color: Colors.white,),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Activate request")

                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (_)=>const ActivateRequestScreen()));
                            },
                          ),

                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.move_to_inbox,color: Colors.white,),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Inbox")

                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (_)=>const InboxScreen()));
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.question_mark_rounded,color: Colors.white,),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Common questions")

                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (_)=>const CommonQuestionsScreen()));
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.person,color: Colors.white,),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Account")

                              ],
                            ),
                            onTap: (){
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.schedule,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Reparation")
                                ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const ReSchedule()));
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.play_circle_outline,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Sessions")
                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const AllSession()));
                            },
                          ),
                          // SizedBox(height: size.height * 0.01,) ,
                          // GestureDetector(
                          //   child: Row(
                          //     children: [
                          //       const Icon(Icons.info_outline),
                          //       SizedBox(width: size.width * 0.1,) ,
                          //       createTextWidget(size, "Final")
                          //     ],
                          //   ),
                          //   onTap: (){
                          //     Navigator.push(context, MaterialPageRoute(builder: (context) => const FinalSchedule()));
                          //
                          //   },
                          // ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.school_sharp,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Exams")
                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const CreateExam()));
                            },

                          ),SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.school_sharp,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Auto Exams")
                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const AutoExamRequestScreen()));
                            },

                          ),
                          SizedBox(height: size.height * 0.01,) ,

                          // GestureDetector(
                          //   child: Row(
                          //     children: [
                          //       const Icon(Icons.history),
                          //       SizedBox(width: size.width * 0.1,) ,
                          //       createTextWidget(size, "Previous Exams")
                          //     ],
                          //   ),
                          //   onTap: (){
                          //     Navigator.push(context, MaterialPageRoute(builder: (context) => const ExamsHistory()));
                          //   },
                          // ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.chat,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Online Chats")
                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const CreateChat()));
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.videocam_rounded,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Online Meetings")
                              ],
                            ),
                            onTap: (){
                              Navigator.push(context, MaterialPageRoute(builder: (context) => const OnlineMeetings()));
                            },
                          ),
                          SizedBox(height: size.height * 0.01,) ,
                          GestureDetector(
                            child: Row(
                              children: [
                                const Icon(Icons.logout,color: Colors.white),
                                SizedBox(width: size.width * 0.1,) ,
                                createTextWidget(size, "Log out")
                              ],
                            ),
                            onTap: ()async{
                              Navigator.pop(context);
                              await context.read<AuthProvider>().logOut();
                                Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const SplashScreen()));
                            },
                          ),
                        ],
                      ),
                    ),
                  ),

                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  // _launchURL(url) async {
  //   if (await canLaunch(url)) {
  //     await launch(url, forceWebView: true ,enableJavaScript: true);
  //   } else {
  //     throw 'Could not launch $url';
  //   }
  // }

}
