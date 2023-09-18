import 'dart:convert';
import 'dart:isolate';
import 'dart:math';
import 'dart:ui';

import 'package:android_alarm_manager_plus/android_alarm_manager_plus.dart';
import 'package:desktop_screen/models/teacher_model.dart';
import 'package:desktop_screen/providers/data_handler_provider.dart';
import 'package:desktop_screen/providers/local_database.dart';
import 'package:desktop_screen/providers/screen_providers/home_provider.dart';
import 'package:desktop_screen/screens/splash.dart';
import 'package:desktop_screen/screens/teacher_screen.dart';
// import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/animation.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:insta_image_viewer/insta_image_viewer.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';


import '../constants/colors.dart';
import '../constants/helper.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/login_data_model.dart';
import '../models/schedule_model.dart';
import '../providers/auth_provider.dart';

import 'package:sqflite/sqflite.dart';
import '../main.dart';


/// The name associated with the UI isolate's [SendPort].
const String isolateName = 'isolate';

/// A port used to communicate from a background isolate to the UI isolate.
final ReceivePort port = ReceivePort();

SendPort? uiSendPort;

Future<void> callback() async {
// print('Alarm fired!');

  int today = DateTime.now().weekday;
  Database? localDatabase = await LocalDatabase().createDatabase();
  List studentLocalSchedule = await localDatabase.rawQuery("SELECT * FROM studentSchedule");
  // for (int i = 0; i < studentLocalSchedule.length; i++) {
  //   if (today == studentLocalSchedule[i]["day"] - 1) {
  //     await flutterLocalNotificationsPlugin.show(i, "مواعيد", "${studentLocalSchedule[i]["teacherName"]}هناك حصة غدا للدكتور  ", notificationDetails,
  //         payload: "Custom_Sound");
  //   }
  // }

//
// This will be null if we're running in the background.
  uiSendPort ??= IsolateNameServer.lookupPortByName(isolateName);
  uiSendPort?.send(null);
}

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> with TickerProviderStateMixin {
  // List doctorsList = [] ;
  // final ScreenCaptureEvent screenCaptureEvent = ScreenCaptureEvent();
  // ScreenshotCallback screenshotCallback = ScreenshotCallback();
  // late ScreenshotCallback screenshotCallback;
  // final ScreenCaptureEvent screenListener = ScreenCaptureEvent();

  String text = "Ready..";
  String text2 = "Ready..";
  late LoginData? student;

  String? docValue;

  int index = 0;

  late Animation<double> animation;
  late AnimationController _controller;
  ScrollController scrollController = ScrollController();

  List allFeed = [];
  List news = [];
  var iosSecureScreenShotChannel = const MethodChannel('secureScreenshotChannel');

  List<Schedule> schedules = [];

  List<Map> studentLocalSchedule = [];

  Map teacherSchedules = {};
  bool connected = true;

  late HomeProvider homeProvider;

  period() async {
    DateTime time = DateTime.now().toLocal();
    await AndroidAlarmManager.periodic(
        const Duration(hours: 24),
        // Ensure we have a unique alarm ID.
        Random().nextInt((32)),
        await callback,
        exact: true,
        wakeup: true,
        rescheduleOnReboot: true,
        startAt: DateTime(time.year, time.month, time.day, 21, 0, 0, 0, 0));
  }

  List<String> topics = [];

  getNotifi() async {
    // final FirebaseMessaging _firebaseMessaging = FirebaseMessaging.instance;
    topics.add("all");
    topics.add("${student!.studentData!.schoolStagesId}-0-0");
    for (int i = 0; i < student!.teacherList!.length; i++) {
      topics.add("0-${student!.teacherList![i].id}-0");
      topics.add("${student!.studentData!.schoolStagesId}-${student!.teacherList![i].id}-0");
      for (int x = 0; x < student!.teacherList![i].materialList!.length; x++) {
        topics.add("${student!.studentData!.schoolStagesId}-${student!.teacherList![i].id}-${student!.teacherList![i].materialList![x].id}");
      }
    }
    for (int i = 0; i < topics.length; i++) {
      // _firebaseMessaging.subscribeToTopic(topics[i]);
    }
  }

  getData() async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    var _selectedDoctor = sharedPreferences.getString(selectedDoctor);
    if (_selectedDoctor != null) {
      var selected = json.decode(_selectedDoctor);
      setState(() {
        docValue = selected["teacherName"];
      });
    }
  }

  Future createDatabase() async {
    await context.read<LocalDatabase>().createDatabase().then((value) => value);
  }
  // @override
  // void dispose() {
  //   super.dispose();
  //   allowScreenshots();
  // }
  //
  // Future<void> preventScreenshots() async {
  //   await WindowManager.instance.o();
  // }
  //
  // Future<void> allowScreenshots() async {
  //   await WindowManager.instance.allowScreenshot();
  // }
  // listenScreenShot() {
  //
  //   screenCaptureEvent.addScreenShotListener((filePath) {
  //     Navigator.pop(context);
  //      context.read<AuthProvider>().logOut();
  //     Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const SplashScreen()));
  //
  //     context.read<InboxProvider>().sendScreenInfoRequest(context);
  //
  //     print("fresh screenshot!");
  //   });
  //   screenCaptureEvent.watch();
  // }
  // Future<void> initScreenshotCallback() async {
  //   // await screenshotCallback.checkPermission();
  //   // print('can take a PRINT? ${screenshotCallback.requestPermissions}');
  //   // await screenshotCallback.initialize();
  //   // screenshotCallback = ScreenshotCallback();
  //
  //   screenshotCallback.addListener(() {
  //     context.read<InboxProvider>().sendScreenInfoRequest(context);
  //     print('Take a PRINT');
  //   });
  // }
  // Future<void> initScreenshotCallback() async {
  //   screenshotCallback = ScreenshotCallback();
  //
  //   screenshotCallback.addListener(() {
  //     setState(() {
  //       text = "Screenshot callback Fired!";
  //     });
  //   });
  //
  //   screenshotCallback.addListener(() {
  //     print("We can add multiple listeners ");
  //   });
  // }


  @override
  void initState()  {
    // init();
    // ScreenProtector.protectDataLeakageWithColor(Colors.white);
    // screenListener.preventAndroidScreenShot(true);

      // listenScreenShot();
    // screenListener.addScreenRecordListener((recorded) {
    //   ///Recorded was your record status (bool)
    //   setState(() {
    //     text = recorded ? "Start Recording" : "Stop Recording";
    //   });
    // });

    // screenListener.addScreenShotListener((filePath)async {
    //   ///filePath only available for Android
    //   setState(() {
    //     text = "Screenshot stored on : $filePath";
    //   });
    //   await context.read<InboxProvider>().sendScreenInfoRequest(context);
    //   // await ScreenProtector.preventScreenshotOn();
    // });
    // screenListener.addScreenRecordListener((recorded) async {
    //   ///Recorded was your record status (bool)
    //   setState(() {
    //     text2 = recorded ? "Start Recording" : "Stop Recording";
    //   });
    //   await context.read<InboxProvider>().sendScreenInfoRequest(context);
    //
    // });
    ///You can add multiple listener ^-^
    // screenListener.addScreenRecordListener((recorded) {
    //   print("Hi i'm 2nd Screen Record listener");
    // });
    // screenListener.addScreenShotListener((filePath) {
    //   print("Wohooo i'm 2nd Screenshot listener");
    // });

    ///Start watch
    // screenListener.watch();

    homeProvider = context.read<HomeProvider>();
    homeProvider.getTeachers();
    context.read<DataHandler>().checkConnectivity().then((value) {
      connected = context.read<DataHandler>().connected;
    });

    period();
    getData();
    createDatabase().then((value) {
      context.read<DataHandler>().scheduleBySchoolStageId().then((value) {
        schedules = context.read<DataHandler>().studentSchedule;
        teacherSchedules = context.read<DataHandler>().teacherSchedules;
        studentLocalSchedule = context.read<LocalDatabase>().studentLocalSchedule.toList();
        if (schedules.length != studentLocalSchedule.length) {
          for (int i = 0; i < studentLocalSchedule.length; i++) {
            context.read<LocalDatabase>().deleteFromDatabase("studentSchedule", studentLocalSchedule[i]["id"]);
          }
          for (int i = 0; i < schedules.length; i++) {
            context.read<LocalDatabase>().insertToDatabase("studentSchedule",
                day: schedules[i].day,
                schoolStageId: schedules[i].schoolStageId,
                teacherId: schedules[i].teacherId,
                time: schedules[i].time.toString(),
                teacherName: schedules[i].teacherName);
          }
        }
      });
    });
    student = context.read<AuthProvider>().student!;
    context.read<DataHandler>().getAllFeed();
    teacherSchedules.clear();

    _controller = AnimationController(duration: const Duration(milliseconds: 250), vsync: this);
    animation = Tween<double>(begin: 0, end: 1).animate(_controller);
    getNotifi();
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      // RemoteMessage? initialMessage = await FirebaseMessaging.instance.getInitialMessage();
    });
    var initialzationSettingsAndroid = const AndroidInitializationSettings('@mipmap/ic_launcher');
    var initializationSettings = InitializationSettings(android: initialzationSettingsAndroid);
    // flutterLocalNotificationsPlugin.initialize(initializationSettings);

    // FirebaseMessaging.onMessage.listen((RemoteMessage message) async {
    //   RemoteNotification? notification = message.notification;
    //   AndroidNotification? android = message.notification!.android;
    //   if (notification != null && android != null) {
    //     flutterLocalNotificationsPlugin.show(
    //         notification.hashCode,
    //         notification.title,
    //         notification.body,
    //         NotificationDetails(
    //           android: AndroidNotificationDetails(
    //             channel.id,
    //             channel.name,
    //             channelDescription: channel.description,
    //             icon: android.smallIcon,
    //             importance: Importance.max,
    //             priority: Priority.max,
    //             playSound: true,
    //             sound: const RawResourceAndroidNotificationSound("isnt"),
    //           ),
    //         ));
    //   }
    // });
    super.initState();
    _controller.forward();
    AndroidAlarmManager.initialize();
    // port.listen((_) async => await _incrementCounter());
  }
  // void init() async {
  //   await initScreenshotCallback();
  // }
  getLocalData() {
    context.read<DataHandler>().getLocalSchedules();
    context.read<DataHandler>().getLocalNews();
  }

  selectTeacher(TeacherModel teacher){
    homeProvider.selectedTeacherId = teacher.id!;
    Navigator.push(context, MaterialPageRoute(builder: (context) => TeacherScreen(teacher)));
  }
  @override
  void dispose() {
    // screenCaptureEvent.dispose();
    // screenshotCallback.dispose();
    // screenListener.dispose();
    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    // if(doctorsList.isEmpty){
    //   for(final teacher in student!.studentData!.teacherList!){
    //     doctorsList.add(teacher["teacherName"]);
    //   }
    // }
    student = context.watch<AuthProvider>().student;
    allFeed = context.watch<DataHandler>().allFeed;
    news = context.watch<DataHandler>().news;
    schedules = context.watch<DataHandler>().studentSchedule;
    studentLocalSchedule = context.watch<LocalDatabase>().studentLocalSchedule;
    connected = context.watch<DataHandler>().connected;
    teacherSchedules = context.watch<DataHandler>().teacherSchedules;
    if (connected == false) {
      getLocalData();
    }
    return Scaffold(
      backgroundColor: mainColor,
      appBar: AppBar(
        elevation: 0,

        backgroundColor: mainColor,
        title: const Text("Doctors",style: TextStyle(fontSize: 23),),
        centerTitle: true,
        leading: IconButton(
          icon: const Icon(Icons.logout),
          onPressed: ()async {
            await context.read<AuthProvider>().logOut();
            Navigator.pushReplacement(context, MaterialPageRoute(builder: (_) => const SplashScreen()));
          },
        ),
      ),
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

          child: RefreshIndicator(
            onRefresh: () async {
              homeProvider.teachers.clear();
              homeProvider = context.read<HomeProvider>();
              homeProvider.getTeachers();
              // await Future.delayed(Duration(seconds: 2));
            },
            child: SizedBox(

              child: ListView(
                // crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // _buildDropDown(size ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      _buildBar(size, 0, "Top Doctors"),
                      // Container(
                      //   width: 1,
                      //   height: size.height *0.04,
                      //   color: Colors.grey,
                      // ),
                      // _buildBar(size, 1 , "Schedule"),
                      Container(
                        width: 1,
                        height: size.height * 0.04,
                        color: Colors.grey,
                      ),
                      _buildBar(size, 2, "News"),
                    ],
                  ),
                  Expanded(child: _buildBody(size))
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  _buildBar(Size size, int _index, String title) {
    return SizedBox(
      width: size.width * 0.25,
      height: size.height * 0.1,
      child: GestureDetector(
        onTap: () {
          scrollController.animateTo(0, duration: const Duration(milliseconds: 150), curve: Curves.bounceInOut);

          _controller.reverse().then((_) {
            setState(() {
              index = _index;
            });
            if(index==2){
              context.read<DataHandler>().getAllFeed();
            }
            _controller.forward();
          });
        },
        child: Center(
            child: SizedBox(
                height: size.height * 0.025,
                child: FittedBox(
                    child: Text(
                      title,
                      style: TextStyle(fontWeight: _index == index ? FontWeight.bold : FontWeight.normal),
                    )))),
      ),
    );
  }

  Widget _buildBody(Size size) {
    return Container(
      height: size.height,
      decoration: const BoxDecoration(color: Colors.black38, borderRadius: BorderRadius.all(Radius.circular(10))),
      child: index == 0 ? _buildDoctorsList(size) : _buildAllFeed(size),
    );
  }

  void onChanged(String? newValue) async {
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    int index = student!.studentData!.teacherList!.indexWhere((element) => element["teacherName"] == newValue);
    sharedPreferences.setString(selectedDoctor, json.encode(student!.studentData!.teacherList![index]));
    setState(() {
      docValue = newValue;
    });
    context.read<DataHandler>().getStudentNews(context);
  }

  _buildDropDown(Size size) {
    return Padding(
      padding: const EdgeInsets.all(4.0),
      child: Container(
        height: size.height * 0.075,
        width: size.width * 0.85,
        decoration: BoxDecoration(borderRadius: const BorderRadius.all(Radius.circular(10)), border: Border.all(color: appBarColor)),
        child: Padding(
          padding: const EdgeInsets.all(4.0),
          child: DropdownButton(
            value: docValue,
            hint: const Text("Choose Doctor"),
            underline: const SizedBox(),
            style: valueStyle,
            items: (student!.studentData!.teacherList ?? []).map<DropdownMenuItem<String>>((value) {
              return DropdownMenuItem<String>(
                value: value.toString(),
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: SizedBox(
                    height: size.height * 0.03,
                    child: FittedBox(
                      child: Text(
                        value["teacherName"],
                        style: regularTextStyle,
                      ),
                    ),
                  ),
                ),
              );
            }).toList(),
            isExpanded: true,
            onChanged: onChanged,
          ),
        ),
      ),
    );
  }


  // Widget buildTextWidget (Size size , Text text){
  //  return
  // }
  _buildSchedule(Size size) {
    return FadeTransition(
      opacity: animation,
      child: Container(
        width: size.width,
        decoration: BoxDecoration(borderRadius: BorderRadius.circular(50)),
        child: Column(
          children: [
            for (int i = 0; i < teacherSchedules.length; i++)
              for (int x = 0; x < teacherSchedules[teacherSchedules.keys.toList()[i]].length; x++)
                Card(
                  color: Colors.white,
                  elevation: 5,
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Column(
                          children: [
                            SizedBox(
                                height: size.height * 0.05,
                                child: FittedBox(
                                    child: Text(
                                  '${teacherSchedules.keys.toList()[i].toString()} ',
                                  style: subTitleStyle,
                                ))),
                            Padding(
                              padding: const EdgeInsets.fromLTRB(0, 5, 0, 0),
                              child: SizedBox(
                                  height: size.height * 0.045,
                                  child: FittedBox(
                                      child: Text(
                                    '(${teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()}) ',
                                    style: subTitleStyle,
                                  ))),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(4.0),
                        child: SizedBox(
                          width: size.width,
                          child: DataTable(
                            dataRowHeight: size.height * 0.2,
                            headingRowColor: row1color,
                            dataRowColor: headrow1color,
                            columns: [
                              DataColumn(
                                  label: SizedBox(
                                      height: size.height * 0.045,
                                      child: FittedBox(
                                          child: Text(
                                        "اليوم",
                                        style: columnFontStyle,
                                      )))),
                              DataColumn(
                                  label: SizedBox(
                                      height: size.height * 0.045,
                                      child: FittedBox(
                                          child: Text(
                                        "الساعة",
                                        style: columnFontStyle,
                                      )))),
                              DataColumn(
                                  label: SizedBox(
                                      height: size.height * 0.045,
                                      child: FittedBox(
                                          child: Text(
                                        "الملاحظات",
                                        style: columnFontStyle,
                                      )))),
                            ],
                            rows: [
                              for (int y = 0;
                                  y <
                                      teacherSchedules[teacherSchedules.keys.toList()[i].toString()]
                                              [teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()]
                                          .length;
                                  y++)
                                DataRow(cells: [
                                  DataCell(SizedBox(
                                      height: size.height * 0.045,
                                      child: FittedBox(
                                          child: Text(
                                        "${teacherSchedules[teacherSchedules.keys.toList()[i].toString()][teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][0] ?? ""}",
                                        style: rowsFontStyle,
                                      )))),
                                  DataCell(Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      for (int z = 0;
                                          z <
                                              teacherSchedules[teacherSchedules.keys.toList()[i].toString()]
                                                      [teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][1]
                                                  .length;
                                          z++)
                                        teacherSchedules[teacherSchedules.keys.toList()[i].toString()]
                                                    [teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][1][z] !=
                                                ""
                                            ? z == 0
                                                ? Padding(
                                                    padding: const EdgeInsets.all(7.0),
                                                    child: SizedBox(
                                                        height: size.height * 0.025,
                                                        child: FittedBox(
                                                            child: Text(
                                                          "${teacherSchedules[teacherSchedules.keys.toList()[i].toString()][teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][1][z]} ",
                                                          style: rowsFontStyle,
                                                        ))),
                                                  )
                                                : Column(
                                                    mainAxisAlignment: MainAxisAlignment.center,
                                                    children: [
                                                      Padding(
                                                        padding: const EdgeInsets.fromLTRB(0, 5, 0, 5),
                                                        child: SizedBox(
                                                            height: size.height * 0.024,
                                                            child: const FittedBox(
                                                                child: Text(
                                                              "OR",
                                                              style: TextStyle(color: Colors.black87, fontWeight: FontWeight.bold),
                                                            ))),
                                                      ),
                                                      SizedBox(
                                                          height: size.height * 0.025,
                                                          child: FittedBox(
                                                              child: Text(
                                                            "${teacherSchedules[teacherSchedules.keys.toList()[i].toString()][teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][1][z]} ",
                                                            style: rowsFontStyle,
                                                          ))),
                                                    ],
                                                  )
                                            : const SizedBox(),
                                    ],
                                  )),
                                  DataCell(SizedBox(
                                      height: size.height * 0.045,
                                      width: size.width * 0.2,
                                      child: FittedBox(
                                          child: Text(
                                        "${teacherSchedules[teacherSchedules.keys.toList()[i].toString()][teacherSchedules[teacherSchedules.keys.toList()[i].toString()].keys.toList()[x].toString()][y][2] ?? ""}",
                                        style: rowsFontStyle,
                                      )))),
                                ]),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
          ],
        ),
      ),
    );
  }

  _buildDoctorsList(Size size) {
    return FadeTransition(
        opacity: animation,
        child: Selector<HomeProvider, bool>(
          selector: (context, provider) => provider.isLoading,
          builder: (context, value, child) {
            if(value){
              return const Center(
                child: CircularProgressIndicator(),
              );
            }
            if(homeProvider.error != null){
              return Center(
                child: Column(
                  children: [
                    Text(homeProvider.error!,textAlign: TextAlign.center),
                    GestureDetector(
                      onTap: () => homeProvider.getTeachers(),
                      child: Text("Retry",style: TextStyle(decoration: TextDecoration.underline)))
                  ],
                ),
              );
            }
            final teachers = homeProvider.teachers;
            return SizedBox(
              height: size.height*.785,
              width: size.width,
              child: GridView.count(
                  shrinkWrap: true,
                  controller: ScrollController(keepScrollOffset: false),
                  crossAxisCount: 5,
                  padding: const EdgeInsets.all(15),
                  childAspectRatio: size.aspectRatio * .5,
                  mainAxisSpacing: 5,
                  crossAxisSpacing: 5,
                  children: List.generate(
                    teachers.length,
                    (i) {
                      final teacher = teachers[i];
                      return Card(
                              color: cardColor,
                              elevation: 10.0,
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20),),
                              child: InkWell(
                                child: Column(
                                  children: [
                                    Container(
                                      height: size.height * 0.3,
                                      decoration: BoxDecoration(
                                          borderRadius: const BorderRadius.all(
                                            Radius.circular(8.0),
                                          ),
                                          color:cardColor,
                                          image: DecorationImage(
                                              image:
                                              NetworkImage(
                                                (    teacher.image !=  null? (uploadUrl + teacher.image!) :"https://tekno-class.com/new-lms/wp-content/uploads/2021/09/teacher.png"),

                                              ),
                                              fit: BoxFit.fill)

                                      ),
                                    ),
                                    Align(
                                      alignment: Alignment.bottomRight,
                                      child: Padding(
                                        padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                                        child: SizedBox(
                                          height: size.height * 0.05,
                                          width: size.width,
                                          child: OutlinedButton(
                                              onPressed: () => selectTeacher(teacher),
                                              style: ButtonStyle(
                                                backgroundColor: buttonHomeColor,
                                              ),
                                              child: SizedBox(
                                                  height: size.height * 0.03,
                                                  child: FittedBox(
                                                      child: Text(
                                                    "See More",
                                                    style: TextStyle(color: Colors.white),
                                                  )))),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                                onTap: () => selectTeacher(teacher),
                              ),
                            );
                    },
                  )),
            );
          },
        ));
  }

  _buildAllFeed(Size size) {
    return FadeTransition(
      opacity: animation,
      child: RefreshIndicator(
        onRefresh: () {
         return context.read<DataHandler>().getAllFeed();


        },
        child: Stack(
          children: [
            ListView(
              shrinkWrap: true,
              // mainAxisSize: MainAxisSize.max,
              // crossAxisAlignment: CrossAxisAlignment.end,
              // mainAxisAlignment: MainAxisAlignment.start,
              children: [
                for (int i = 0; i < allFeed.length; i++)
                  Container(
                    width: size.width*.9,
                    decoration: BoxDecoration(borderRadius: BorderRadius.circular(50)),
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Card(
                        color: Colors.white,
                        elevation: 5,
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                        child: Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: Column(
                            children: [
                              SizedBox(
                                  height: size.height * 0.06,
                                  child: FittedBox(
                                      child: Text(
                                    allFeed[i]["address"],
                                    style: subTitleStyle,
                                  ))),
                              SizedBox(
                                  height: size.height * 0.025,
                                  child: FittedBox(
                                      child: Text(
                                    allFeed[i]["post"].toString(),
                                    style: regularTextStyle,
                                    textAlign: TextAlign.end,
                                  ))),
                              allFeed[i]["img"] != null
                                  ?
                              SizedBox(
                                height: size.height * 0.3,
                                     width: size.width,

                                child:

                                 InstaImageViewer(
                                    child: Image(
                                      image: Image.network(    uploadUrl + allFeed[i]["img"]).image,
                                    ),

                                ),
                                // ImagePreview(
                                //   assetName:  uploadUrl + allFeed[i]["img"],
                                // ),
                              )
                              // Image.network(
                              //         uploadUrl + allFeed[i]["img"],
                              //         height: size.height * 0.3,
                              //         width: size.width,
                              //         fit: BoxFit.fill,
                              //       )
                                  : const SizedBox()
                            ],
                          ),
                        ),
                      ),
                    ),
                  )
              ],
            ),
          ],
        ),
      ),
    );
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder properties) {
    super.debugFillProperties(properties);
    properties.add(DiagnosticsProperty<SendPort>('uiSendPort', uiSendPort));
  }
}

const AndroidNotificationChannel channel = AndroidNotificationChannel(
  'high_importance_channel', // id
  'High Importance Notifications', // title
  description: 'This channel is used for important notifications',
  // description
  importance: Importance.max,
  playSound: true,
  sound: RawResourceAndroidNotificationSound("isnt"),
  showBadge: true,
);

Future onDidReceiveLocalNotification(int id, String title, String body, String payload) async {
  // await flutterLocalNotificationsPlugin.show(0, "title", "body", notificationDetails, payload: "Custom_Sound");
}

NotificationDetails notificationDetails = NotificationDetails(
    android: AndroidNotificationDetails(
  "${channel.id}",
  "${channel.name}",
  channelDescription: "${channel.description}",
  importance: Importance.max,
  priority: Priority.max,
  playSound: true,
  sound: RawResourceAndroidNotificationSound("isnt"),
));
