

import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:firebase_messaging/firebase_messaging.dart';

class Notifications {
  final FirebaseMessaging firebaseMessaging = FirebaseMessaging.instance;
  Future sendAndRetrieveMessage(String title , String body , String type , id , String topic ,{catId , image}) async {
    final String serverToken = 'AAAAC9zMuaE:APA91bHGYlgUuznJ9Ht_WPsF3GDkIjc7RLUBkDSONA7rTJWWtcAYIjYIl0BvbzlUS4yK8gfdANpTg5T0qSkbRKpE59ghx93NXsx98lEIdLX0alEf6g3vC3H-D6foDaZDwdXUyKoZ0L4_';

    await firebaseMessaging.requestPermission(
      sound: true, badge: true, alert: true,
    );

    await http.post(
      Uri.parse("https://fcm.googleapis.com/fcm/send")
      ,
      headers: <String, String>{
        'Content-Type': 'application/json',
        'Authorization': 'key=$serverToken',
      },
      body: jsonEncode(
        <String, dynamic>{
          "sound" : "isnt.mp3",
          "to" : "/topics/$topic",
          "notification": image == null ? {
            "title": title,
            "body": body,
            "sound" : "isnt.mp3",
          } :
          {
            "title": title,
            "body": body ,
            "image": image,
            "sound" : "isnt.mp3",

          },

          "data":   catId == null ?  {
           "type" : type ,
            "id" : "$id",
            "sound" : "isnt.mp3",
          } : {
            "type" : type ,
            "id" : "$id" ,
            "cat" : "$catId",
            "sound" : "isnt.mp3",
          }
        }
      ),
    );

    // final Completer<Map<String, dynamic>> completer =
    // Completer<Map<String, dynamic>>();
    //

    firebaseMessaging.setForegroundNotificationPresentationOptions(
      sound: true ,
      alert: true ,
      badge: true ,

    );


    // return completer.future;
  }
}
