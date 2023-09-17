
import 'package:desktop_screen/providers/auth_provider.dart';
import 'package:desktop_screen/screens/splash.dart';
import 'package:flutter/material.dart';
import 'package:flutter_windowmanager/flutter_windowmanager.dart';
import 'package:provider/provider.dart';


void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // WidgetsFlutterBinding.ensureInitialized();
  // Must add this line.
  // await windowManager.ensureInitialized();
  // if (kDebugMode) {
  //   WidgetsFlutterBinding.ensureInitialized();
  // await FlutterWindowManager.addFlags(FlutterWindowManager.FLAG_SECURE);
  //   print("Print only in debug mode");
  // }

// runApp(MyApp());
  runApp(MultiProvider(
    providers: [
      ChangeNotifierProvider(create: (_) => AuthProvider()),
      // ChangeNotifierProvider(create: (_) => DataHandler()),
      // ChangeNotifierProvider(create: (_) => LocalDatabase()),
      // ChangeNotifierProvider(create: (_) => ActivateRequestProvider()),
      // ChangeNotifierProvider(create: (_) => HomeProvider()),
      // ChangeNotifierProvider(create: (_) => InboxProvider()),
      // ChangeNotifierProvider(create: (_) => CommonQuestionsProvider()),
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
