import 'dart:async';

import 'package:flutter/material.dart';
import '../constants/colors.dart';
import '../constants/text_styles.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '../helper_methods/chat_methods.dart';
import '../helper_methods/notifications.dart';
import '../models/login_data_model.dart';
import '../providers/auth_provider.dart';
import '../screens/home_screen.dart';
import 'package:provider/provider.dart';
 class Conv extends StatefulWidget {
   final String chatRoomId;
   // final int materialId;
   // final int teacherId;
   // final int gradeId;
   Conv(this.chatRoomId );



   @override
   _ConvState createState() => _ConvState();
 }
 
 class _ConvState extends State<Conv> {
    ChatMethods chatMethods = ChatMethods();
    FirebaseFirestore firestore = FirebaseFirestore.instance;
    TextEditingController messageController = TextEditingController();
    Stream<QuerySnapshot>? chatMessageStream;

    late LoginData student;
    bool ended = false ;
    Widget chatMessageList(Size size){
      return StreamBuilder<dynamic>(
          stream: chatMessageStream,

          builder:  (context, snapshot){
            return snapshot.hasData ? ListView.builder(
                reverse: true,
                itemCount:snapshot.data!.docs.length,
                itemBuilder: (context,index){
                String sender =  snapshot.data!.docs[index]!.data()["sender"].toString();
                  return MessageTile(
                      snapshot.data.docs[index].data()["message"],
                      snapshot.data!.docs[index]!.data()["sender"] == student.studentData!.firstName ,
                    sender

                  );

                      }
            ) :  const SizedBox(
            );
          }
      );


    }

  


   getData() async
   {

     await context.read<AuthProvider>().getData().then((_student){
       if( _student != null && _student.success != null ){
         setState(() {
           student = _student ;
         });
       }
       else {

       }
     });
   }


   sendMessage() async{

     if(messageController.text.isNotEmpty){
       Map<String,dynamic> messageMap = {
         "message" : messageController.text,
         "sender" : student.studentData!.firstName,
         "time" : DateTime.now().microsecondsSinceEpoch,
       };
       await chatMethods.addConvMessages(widget.chatRoomId,messageMap);

           messageController.text="";


       // Notifications().sendAndRetrieveMessage("New Message", messageController.text, "typeController.text" , "idController.text" , "test" , image: "imageController.text").then((_){
       //   print("done");
       // });

     }

   }

   getMessages()async{

   await  chatMethods.getConvMessages(widget.chatRoomId).then((val){
       if(val != null) {
         setState(() {
           chatMessageStream = val;
         });
       } else {
         Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>  const HomeScreen()));
       }
     });

   }

   late Timer timer ;
   @override
   void initState() {
      WidgetsBinding.instance.addPostFrameCallback((_) async{
      await  getData() ;
      await  getMessages();
      });

      super.initState();
   }

   @override
  void dispose() {
    // TODO: implement dispose
     timer.cancel();
    super.dispose();
  }

   @override
   Widget build(BuildContext context) {
     Size size = MediaQuery.of(context).size ;
     chatMessageStream != null ?   chatMessageStream!.listen((event) {
       print("eveeeent : ${event.size}");
       if(event.size == 0 ){

           timer = Timer(Duration(seconds: 2) , (){
             Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const HomeScreen()));
           });

       }
     }) : null ;
     return Scaffold(
       backgroundColor: mainColor,
       appBar: AppBar(
         elevation: 0,

         backgroundColor: mainColor,
         title: const Text( "Chats",style: TextStyle(fontSize: 23),),
         centerTitle: true,
         leading: IconButton(
           icon: const Icon(Icons.arrow_back),
           onPressed: () {
             Navigator.pop(context);
           },
         ),
         // actions: [
         //   IconButton(
         //     onPressed: () {
         //       _scaffoldKey.currentState!.openDrawer();
         //     },
         //     icon: Icon(
         //       Icons.menu,
         //       color: Colors.white,
         //     ),
         //   )
         // ],
       ),
       // appBar: AppBar(
       //
       //   backgroundColor: appBarColor,
       //   title: Text("Chat" , style: titleTextStyle,textAlign: TextAlign.end,),
       //   centerTitle: true,
       //   // actions: [
       //   //   IconButton(onPressed: ()async{
       //   //     await ChatMethods().deleteConv(widget.chatRoomId).then((_){
       //   //       Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) =>const  HomeScreen()));
       //   //     });
       //   //   }, icon: const Icon(Icons.delete_forever))
       //   // ],
       //   //bottom: PreferredSize(child: ElevatedButton(onPressed: (){chatMethods.deleteConv(widget.chatRoomId);}, child: Text("END")), preferredSize: Size(5,5)),
       // ),
       body: Container(
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
             height: size.height,
             width: size.width,
             child: Stack(
               children: [
                 Padding(
                   padding: const EdgeInsets.fromLTRB(0,0,0,80),
                   child: chatMessageList(size),
                 ),
             ended == false ? Container(
                   alignment: Alignment.bottomCenter,
                   child: Container(
                     color: appBarColor,
                     padding: EdgeInsets.symmetric(horizontal: size.width*0.03,vertical:size.height*0.01 ),
                     child: Row(
                       children: [
                         Expanded(
                           child: TextField(
                             style: TextStyle(
                               color: yellow
                             ),
                             controller: messageController,
                             decoration: InputDecoration(
                               hintText: "Message...",
                               hintStyle:  TextStyle(
                                   color: yellow
                               ), fillColor: Colors.blue
                             ),

                           ),
                         ),
                         GestureDetector(
                           onTap: (){

                             sendMessage();
                           },
                           child: Container(
                               width: size.width*0.1,
                               decoration: BoxDecoration(
                                 gradient: const LinearGradient(
                                     colors: [
                                       Color(0x36FFFFFF),
                                       Color(0x0FFFFFFF)
                                     ]
                                 ),
                                 borderRadius: BorderRadius.circular(40),
                               ),
                               padding: const EdgeInsets.all(8.0),
                               child: Image.asset('assets/images/send.png')),
                         ),
                       ],
                     ),),
                 ) : SizedBox(),

               ],

             ),
           ),
         ),
       ) ,

     );
   }
 }


class MessageTile extends StatelessWidget {

  final bool isSenderIsMe;

   final String message;

  final String sender;
   MessageTile(this.message, this.isSenderIsMe, this.sender);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;

     return // isSenderIsMe?
    Container(

      padding:
      EdgeInsets.only(left: isSenderIsMe ? 0 : 24 ,top: 10 , right: isSenderIsMe ? 24:0 , bottom: 10),
      width:  size.width,
      alignment: isSenderIsMe ? Alignment.centerRight : Alignment.bottomLeft,

      child: Container(

        decoration: BoxDecoration(
          color: isSenderIsMe ?  Colors.blue : Colors.grey,
          borderRadius: BorderRadius.circular(30),
        ),
        padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
        child: Column(
          crossAxisAlignment: isSenderIsMe ?  CrossAxisAlignment.end :CrossAxisAlignment.start,
          children: [
            Text(message,style: TextStyle(
              color: isSenderIsMe ?  Colors.white : Colors.black,
              fontSize: 15,

            ),
            ),
            Text(
              sender ,
              style: TextStyle(
              color:  Colors.white,
              fontSize: 10,
            ),
            ),
          ],
        ),
      ),
    );//:Container(child: ShowNotification());
  }
}
// Widget ShowNotification(){
//   void showNotification(){
//     var android=AndroidNotificationDetails("channelId","channelName","channelDescription",priority: Priority.high);
//     var ios=IOSNotificationDetails();
//     var platform=new NotificationDetails(android: android,iOS: ios);
//     flutterLocalNotificationsPlugin.show(0, "title", "body", platform,payload: "send");
//   }
//
//
// }

 
 