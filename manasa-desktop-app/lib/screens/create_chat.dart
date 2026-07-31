// import 'dart:ui';
// import 'package:flutter/foundation.dart';
// import 'package:flutter/material.dart';
// import '../constants/colors.dart';
// import '../constants/text_styles.dart';
// import '../helper_methods/chat_methods.dart';
// import '../models/login_data_model.dart';
// import '../providers/auth_provider.dart';
// import '../providers/data_handler_provider.dart';
// import 'package:provider/provider.dart';
//
//
//
//
//
//
//
//
//
//
// class CreateChat extends StatefulWidget {
//   const CreateChat({Key? key}) : super(key: key);
//
//   @override
//   _CreateChatState createState() => _CreateChatState();
// }
//
// class _CreateChatState extends State<CreateChat> {
//   // ChatMethods chatMethods = ChatMethods();
//   late LoginData student ;
//   late List grades = [] ;
//   late final List _grades = [] ;
//   late List subjects = [] ;
//   late final List _subjects = [] ;
//   late List chats = [] ;
//   late int? selectedGradeId ;
//   String? gradeValue;
//   String? subjectsDropValue;
//   var selectedSubject;
//   createChatRoomAndStartConv(String name1 , String name2 , materialId ,gradeId , subjectId) async {
//     //Constants.myName= await HelperFunctions.getUserNameSharedPref();
//     String chatRoomId = getChatRoomId(name1,name2);
//     // List<String> users = [name1,name2];
//     Map<String, dynamic> chatRoomMap = {
//       // "users": users,
//       "chatroomId": chatRoomId ,
//       "materialId" : materialId ,
//       "gradeId" : gradeId ,
//       "subjectId" : subjectId
//     };
//     chatMethods.createChatRoom(chatRoomId,chatRoomMap);
//     Navigator.push(context, MaterialPageRoute(builder:
//         (context)=>Conv(chatRoomId)));
//   }
//
//   //static SendPort? uiSendPort;
//
//
//
//   @override
//   void initState() {
//     getGrades();
//     getData();
//     super.initState();
//   }
//
//   getGrades() async{
//     await context.read<DataHandler>().getGrades().then((_){
//       grades = context.read<DataHandler>().grades ;
//
//       grades.forEach((element) {
//         setState(() {
//           _grades.add(element["name"]);
//         });
//       });
//     });
//   }
//
//   getData()async{
//
//     student = (await context.read<AuthProvider>().getData())!;
//     await context.read<DataHandler>().getAvailableChats().then((_){
//       chats = context.read<DataHandler>().chats ;
//     });
//   }
//
//
//   @override
//   Widget build(BuildContext context) {
//     Size size = MediaQuery.of(context).size ;
//     chats = context.watch<DataHandler>().chats ;
//     return Scaffold(
//       backgroundColor: mainColor,
//       appBar: AppBar(
//         elevation: 0,
//
//         backgroundColor: mainColor,
//         title: const Text( "Chats",style: TextStyle(fontSize: 23),),
//         centerTitle: true,
//         leading: IconButton(
//           icon: const Icon(Icons.arrow_back),
//           onPressed: () {
//             Navigator.pop(context);
//           },
//         ),
//       ),
//       // appBar: AppBar(
//       //   backgroundColor: appBarColor,
//       //   title: Text("Chat" , style: titleTextStyle,),
//       //   centerTitle: true,
//       // ),
//       body: Container(
//         decoration:  BoxDecoration(color:mainColor),
//         width: size.width,
//         height: size.height,
//         child: Container(
//           decoration: const BoxDecoration(
//               color: Colors.white,
//               borderRadius: BorderRadius.only(
//                 topLeft: Radius.circular(40),
//                 topRight: Radius.circular(40),
//               )),
//
//           child: SingleChildScrollView(
//               child: Padding(
//                 padding: const EdgeInsets.all(10.0),
//                 child: getChatsStream (size),
//               )
//           ),
//         ),
//       ),
//     );
//
//   }
//   getChatRoomId(String a, String b) {
//     if (a.substring(0, 1).codeUnitAt(0) > b.substring(0, 1).codeUnitAt(0)) {
//       return "$b\_$a";
//     } else {
//       return "$a\_$b";
//     }
//
//
//   }
//
//   void onGardeChanged(newValue){
//     setState(() {
//       gradeValue = newValue;
//       selectedGradeId = grades[grades.indexWhere((element) => element["name"] == newValue)]["id"];
//     });
//   }
//   void onSubjectChanged(value){
//     setState(() {
//       subjectsDropValue = value ;
//       selectedSubject = subjects[subjects.indexWhere((subject) => subject["name"] == value)];
//     });
//   }
//
//
//   getChatsStream (Size size){
//     final Stream<QuerySnapshot> _chatsStream = FirebaseFirestore.instance.collection('ChatRoom').snapshots();
//       return StreamBuilder<QuerySnapshot>(
//         stream: _chatsStream,
//         builder: (BuildContext context, AsyncSnapshot<QuerySnapshot> snapshot) {
//           if (snapshot.hasError) {
//             return Text('Something went wrong');
//           }
//
//           if (snapshot.connectionState == ConnectionState.waiting) {
//             return Text("Loading");
//           }
//
//
//
//           return Container(
//             height: size.height,
//             child: ListView(
//               children: snapshot.data!.docs.map((DocumentSnapshot document){
//                 Map<String, dynamic> data = document.data()! as Map<String, dynamic>;
//
//                 return  student.teacherList!.any((element) => element.id == data["doctorId"])
//                     // && student.teacherList!.any((element) => element.materialList!.any((element) => element.id == data["materialId"] ))
//                     ?
//                 GestureDetector(
//                   onTap: (){
//                     Navigator.push(context, MaterialPageRoute(builder: (context) =>  Conv(data["chatroomId"])));
//                   },
//                   child: Card(
//                     elevation: 8.0,
//                     color: Colors.grey,
//                     child: SizedBox(
//                       height: size.height * 0.1,
//                       child: ListTile(
//                         title: Center(child: Text(data["chatroomId"] ,style: titleTextStyle,)),
//                         // subtitle: Text(data['company']),
//                       )
//                     ),
//                   ),
//                 ) : const SizedBox();
//               }).toList(),
//             ),
//           );
//         },
//       );
//     }
//   }
//
//
//
//
//
//
//
//
