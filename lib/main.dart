
import 'dart:io';

import 'package:dart_vlc/dart_vlc.dart';
import 'package:desktop_screen/providers/auth_provider.dart';
import 'package:desktop_screen/providers/data_handler_provider.dart';
import 'package:desktop_screen/providers/local_database.dart';
import 'package:desktop_screen/providers/screen_providers/activate_request_provider.dart';
import 'package:desktop_screen/providers/screen_providers/common_questions_provider.dart';
import 'package:desktop_screen/providers/screen_providers/home_provider.dart';
import 'package:desktop_screen/providers/screen_providers/inbox_provider.dart';
import 'package:desktop_screen/screens/splash.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_windowmanager/flutter_windowmanager.dart';
// import 'package:media_kit/media_kit.dart';                      // Provides [Player], [Media], [Playlist] etc.
// import 'package:media_kit_video/media_kit_video.dart';
import 'package:provider/provider.dart';
// import 'package:video_player_win/video_player_win_plugin.dart';
//
// Future<void> secureScreen() async {
//   await FlutterWindowManager.addFlags(FlutterWindowManager.FLAG_SECURE);
// }
void main() async {

  WidgetsFlutterBinding.ensureInitialized();
  // secureScreen();
  DartVLC.initialize();
  // await windowManager.ensureInitialized();
  // Necessary initialization for package:media_kit.
  // MediaKit.ensureInitialized();
  // if (!kIsWeb && Platform.isWindows) WindowsVideoPlayer.registerWith();

  // WidgetsFlutterBinding.ensureInitialized();
  // Must add this line.
  // await windowManager.ensureInitialized();
  // if (kDebugMode) {
  //   WidgetsFlutterBinding.ensureInitialized();
  // await FlutterWindowManager.addFlags(FlutterWindowManager.FLAG_SECURE);
  //   print("Print only in debug mode");
  // }
  // if (!kIsWeb && Platform.isWindows) WindowsVideoPlayer.registerWith();

// runApp(MyApp());
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
    child:
    MyApp(),
  ));
}

class MyApp extends StatelessWidget {
  MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Top 4',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const SplashScreen(),
    );
  }
}
