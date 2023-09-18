import 'package:chewie/chewie.dart';
import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:video_player/video_player.dart';

import '../constants/colors.dart';

class VideoPlayerChieweWidget extends StatefulWidget {
  final String videoUrl;

  const VideoPlayerChieweWidget({Key? key, required this.videoUrl}) : super(key: key);

  @override
  _VideoPlayerChieweWidgetState createState() => _VideoPlayerChieweWidgetState();
}

class _VideoPlayerChieweWidgetState extends State<VideoPlayerChieweWidget> {
  final FijkPlayer player = FijkPlayer();


  @override
  void initState() {
    super.initState();
    player.setDataSource('https://youtu.be/A3ltMaM6noM', autoPlay: true);

  }

  @override
  void dispose() {
    player.release();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          elevation: 0,

          backgroundColor: Colors.white,
          title: Text(
              ""
          ),

          centerTitle: true,
          leading: IconButton(
            icon:  Icon(Icons.arrow_back,color: mainColor,),
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
          //       color: mainColor,
          //     ),
          //   )
          // ],
        ),
      body: Container(
        alignment: Alignment.center,
        child: FijkView(
          player: player,
        ),
      )
    );


  }
}