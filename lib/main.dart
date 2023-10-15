import 'dart:ui';
import 'package:android_alarm_manager_plus/android_alarm_manager_plus.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:flutter_windowmanager/flutter_windowmanager.dart';
import 'package:full_mark/providers/screen_providers/activate_request_provider.dart';
import 'package:full_mark/providers/screen_providers/common_questions_provider.dart';
import 'package:full_mark/providers/screen_providers/home_provider.dart';
import 'package:full_mark/providers/screen_providers/inbox_provider.dart';
import 'package:full_mark/screens/splash.dart';
// import 'package:screen_protector/screen_protector.dart';
import '../providers/auth_provider.dart';
import '../providers/data_handler_provider.dart';
import '../providers/local_database.dart';
import '../screens/home_screen.dart';
import '../screens/login_screen.dart';
import 'package:provider/provider.dart';
import 'package:upgrader/upgrader.dart';
import 'screens/login_screen.dart';
import 'package:flutter/foundation.dart';

final FlutterLocalNotificationsPlugin flutterLocalNotificationsPlugin = FlutterLocalNotificationsPlugin();

Future<void> _firebaseMessagingBackgroundHandler(RemoteMessage message) async {}
bool shouldUseFirestoreEmulator = false;

void main() async {
    WidgetsFlutterBinding.ensureInitialized();
    // WidgetsFlutterBinding.ensureInitialized();
    // Must add this line.
    // await windowManager.ensureInitialized();
  // if (kDebugMode) {
  //   WidgetsFlutterBinding.ensureInitialized();
    await FlutterWindowManager.addFlags(FlutterWindowManager.FLAG_SECURE);
  //   print("Print only in debug mode");
  // }

  IsolateNameServer.registerPortWithName(
    port.sendPort,
    isolateName,
  );
  AndroidAlarmManager.initialize();
  await Firebase.initializeApp();

  if (shouldUseFirestoreEmulator) {
    FirebaseFirestore.instance.useFirestoreEmulator('localhost', 8080);
  }
  // await ScreenProtector.protectDataLeakageOn();

  FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  await flutterLocalNotificationsPlugin.resolvePlatformSpecificImplementation<AndroidFlutterLocalNotificationsPlugin>()!.createNotificationChannel(channel);

  runApp(MultiProvider(
    providers: [
      ChangeNotifierProvider(create: (_) => AuthProvider()),
      ChangeNotifierProvider(create: (_) => DataHandler()),
      ChangeNotifierProvider(create: (_) => LocalDatabase()),
      ChangeNotifierProvider(create: (_) => ActivateRequestProvider()),
      ChangeNotifierProvider(create: (_) => HomeProvider()),
      ChangeNotifierProvider(create: (_) => InboxProvider()),
      ChangeNotifierProvider(create: (_) => CommonQuestionsProvider()),
    ],
    child: MyApp(),
  ));
}

class MyApp extends StatelessWidget {
  MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Full mark',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: UpgradeAlert(child: const SplashScreen()),
    );
  }
}
