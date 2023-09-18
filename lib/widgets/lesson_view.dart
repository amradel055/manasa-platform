import 'package:desktop_screen/widgets/play_video_desktop.dart';
import 'package:flutter/material.dart';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';

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

            child: (VideoPlayerChieweWidget(videoUrl: 'https://upload.wikimedia.org/wikipedia/commons/transcoded/c/c0/Big_Buck_Bunny_4K.webm/Big_Buck_Bunny_4K.webm.480p.webm',))
           ),

      ),
    );
  }
}