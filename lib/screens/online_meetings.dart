import 'dart:convert';
import 'dart:ui';
import 'package:desktop_screen/widgets/regular_dark_button.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../helper_methods/chat_methods.dart';
import '../models/login_data_model.dart';
import '../providers/auth_provider.dart';
import '../providers/data_handler_provider.dart';
import '../widgets/drop_down_list.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
// import 'conversionscreen.dart';










class OnlineMeetings extends StatefulWidget {
  const OnlineMeetings({Key? key}) : super(key: key);

  @override
  _OnlineMeetingsState createState() => _OnlineMeetingsState();
}

class _OnlineMeetingsState extends State<OnlineMeetings> {
  // ChatMethods chatMethods = ChatMethods();
  late LoginData student ;
  late List grades = [] ;
  late final List _grades = [] ;
  late List subjects = [] ;
  late final List _subjects = [] ;
  late List chats = [] ;
  late int? selectedGradeId ;
  String? gradeValue;
  String? subjectsDropValue;
  var selectedSubject;

  var schedule ;

  bool ?isLoading;
  // createChatRoomAndStartConv(String name1 , String name2 , materialId ,gradeId , subjectId) async {
  //   //Constants.myName= await HelperFunctions.getUserNameSharedPref();
  //   String chatRoomId = getChatRoomId(name1,name2);
  //   // List<String> users = [name1,name2];
  //   Map<String, dynamic> chatRoomMap = {
  //     // "users": users,
  //     "chatroomId": chatRoomId ,
  //     "materialId" : materialId ,
  //     "gradeId" : gradeId ,
  //     "subjectId" : subjectId
  //   };
  //   chatMethods.createChatRoom(chatRoomId,chatRoomMap);
  //   Navigator.push(context, MaterialPageRoute(builder:
  //       (context)=>Conv(chatRoomId)));
  // }

  //static SendPort? uiSendPort;



  @override
  void initState() {
    getGrades();
    getData();
    super.initState();
  }

  getGrades() async{
    await context.read<DataHandler>().getGrades().then((_){
      grades = context.read<DataHandler>().grades ;

      grades.forEach((element) {
        setState(() {
          _grades.add(element["name"]);
        });
      });
    });
  }

  // getData()async{
  //   // SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
  //   // var _selectedDoctor = jsonDecode(sharedPreferences.getString(selectedDoctor)!);
  //   // if(_selectedDoctor != null){
  //   //   await context.read<DataHandler>().getSubjectsByTeacher(_selectedDoctor["teacherId"].toString()).then((_){
  //   //     subjects = context.read<DataHandler>().subjects ;
  //   //     for(int i = 0 ; i < subjects.length ; i++){
  //   //       setState(() {
  //   //         _subjects.add(subjects[i]["name"]);
  //   //       });
  //   //     }
  //   //   });
  //   // }
  //   student = (await context.read<AuthProvider>().getData())!;
  //   await context.read<DataHandler>().getAvailableChats().then((_){
  //     chats = context.read<DataHandler>().meetings ;
  //   });
  // }
  getData(){
    context.read<DataHandler>().getLiks(0,context).then((value){
      schedule  = context.read<DataHandler>().studentsLinks;
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

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    // chats = context.watch<DataHandler>().meetings ;
    return Scaffold(
      backgroundColor: mainColor,
      appBar: AppBar(
        elevation: 0,

        backgroundColor: mainColor,
        title: const Text( "Zoom Meetings",style: TextStyle(fontSize: 23),),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),      body:isLoading==false ? Container(
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
              child: SizedBox(
                  height: size.height *0.1,
                  width: size.width *0.8,
                  child: schedule.isNotEmpty ?
                   Center(
                    child: Align(
                      alignment: AlignmentDirectional.bottomCenter,
                      child: regularDarkButton(
                          size,
                          mainColor!,
                          "Open Zoom Meeting",
                              () =>  launch(schedule[0]["link"])

                      ),
                    ),


                    // new InkWell(
                    //     child: new Text('Open Zoom Meeting'),
                    //     onTap: () => launch(schedule[0]["link"])
                    // ),
                  ): Center(child: SizedBox(child: Text("No Zoom Meetings Yet"),))
              ),
            ),
          ),
        ),
      ) : showMessage(size),
    );

  }
  getChatRoomId(String a, String b) {
    if (a.substring(0, 1).codeUnitAt(0) > b.substring(0, 1).codeUnitAt(0)) {
      return "$b\_$a";
    } else {
      return "$a\_$b";
    }


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

  // void onGardeChanged(newValue){
  //   setState(() {
  //     gradeValue = newValue;
  //     selectedGradeId = grades[grades.indexWhere((element) => element["name"] == newValue)]["id"];
  //   });
  // }
  // void onSubjectChanged(value){
  //   setState(() {
  //     subjectsDropValue = value ;
  //     selectedSubject = subjects[subjects.indexWhere((subject) => subject["name"] == value)];
  //   });
  // }


  // getChatsStream (Size size){
  //   final Stream<QuerySnapshot> _chatsStream = FirebaseFirestore.instance.collection('Zoom').snapshots();
  //   return StreamBuilder<QuerySnapshot>(
  //     stream: _chatsStream,
  //     builder: (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
  //       if (snapshot.hasError) {
  //         return Text('Something went wrong');
  //       }
  //
  //       if (snapshot.connectionState == ConnectionState.waiting) {
  //         return Text("Loading");
  //       }
  //
  //
  //
  //       // return Container(
  //       //   height: size.height,
  //       //   child: ListView(
  //       //     children: snapshot.data!.docs.map((DocumentSnapshot document){
  //       //       Map<String, dynamic> data = document.data()! as Map<String, dynamic>;
  //       //
  //       //       return  student.teacherList!.any((element) => element.id == data["doctorId"])
  //       //       // && student.teacherList!.any((element) => element.materialList!.any((element) => element.id == data["materialId"] ))
  //       //           ?
  //       //       GestureDetector(
  //       //         onTap: (){
  //       //           _launchURL(data["link"]);
  //       //         },
  //       //         child: Card(
  //       //           elevation: 8.0,
  //       //           color: Colors.grey,
  //       //           child: SizedBox(
  //       //               height: size.height * 0.1,
  //       //               child: ListTile(
  //       //                 title: Center(child: Text(data["zoomRoomId"] ,style: titleTextStyle,)),
  //       //                 // subtitle: Text(data['company']),
  //       //               )
  //       //           ),
  //       //         ),
  //       //       ) : const SizedBox();
  //       //     }).toList(),
  //       //   ),
  //       // );
  //     },
  //   );
  // }
  void _launchURL(_url) async =>
      await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';
}

