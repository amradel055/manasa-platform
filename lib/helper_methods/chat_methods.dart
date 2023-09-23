import 'package:cloud_firestore/cloud_firestore.dart';

class ChatMethods {

  createChatRoom(String chatRoomId, chatRoomMap) {
    FirebaseFirestore.instance.collection("ChatRoom")
      .doc(chatRoomId).set(chatRoomMap).catchError((e){
    print(e.toString());
  });}

  addConvMessages(String chatRoomId, messageMap){
    FirebaseFirestore.instance.collection("ChatRoom").doc(chatRoomId).path != null ?
    FirebaseFirestore.instance.collection("ChatRoom")
        .doc(chatRoomId)
        .collection("chats")
        .add(messageMap).then((value){
          return value ;
    }).catchError((e){
      print(e.toString());
    }) : null;
  }

  Future deleteConv(String chatRoomId)async{
    QuerySnapshot chatsmessage = await  FirebaseFirestore.instance.collection("ChatRoom").doc(chatRoomId).collection("chats").get();
    await FirebaseFirestore.instance.runTransaction((Transaction myTransaction) async {
      for(int i = 0 ; i < chatsmessage.docs.length ; i++){
        await myTransaction.delete(chatsmessage.docs[i].reference);}
      }).then((_){
      FirebaseFirestore.instance.collection("ChatRoom").doc(chatRoomId).delete();
    });


 //   FirebaseFirestore.instance.collection("ChatRoom")
   //     .doc(chatRoomId).collection("cha")
  }
  Future getConvMessages(String chatRoomId,)async{
    return await FirebaseFirestore.instance.collection("ChatRoom")
        .doc(chatRoomId)
        .collection("chats")
        .orderBy("time",descending: true)
        .snapshots();
  }

  Future getAllAvailableChats()async{
    return await FirebaseFirestore.instance.collection("ChatRoom").get() ;
  }

  Future getAllAvailableZoom()async{
    return await FirebaseFirestore.instance.collection("Zoom").get() ;
  }

}