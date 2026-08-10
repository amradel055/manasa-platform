import 'package:flutter/material.dart';
class LessonViewerPage extends StatefulWidget {
  const LessonViewerPage( {Key? key, required this.url}) : super(key: key);
  final String url;
  @override
  _LessonViewerPageState createState() => _LessonViewerPageState();
}

class _LessonViewerPageState extends State<LessonViewerPage> {


  @override
  void initState() {

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Video  ",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
      ),
      body:
          Container(
        child:  Center(

            child: Text("Video player unavailable")
           ),

      ),
    );
  }
}