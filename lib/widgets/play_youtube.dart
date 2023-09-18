import 'package:pod_player/pod_player.dart';
import 'package:flutter/material.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

import '../constants/colors.dart';

class PlayVideoFromYoutube extends StatefulWidget {
  const PlayVideoFromYoutube({Key? key, required this.url}) : super(key: key);
  final String url;
  @override
  State<PlayVideoFromYoutube> createState() => _PlayVideoFromYoutubeState();
}

class _PlayVideoFromYoutubeState extends State<PlayVideoFromYoutube> {
  String video_url = "https://www.youtube.com/watch?v=QfkFPedJ6UQ";
 late YoutubePlayerController controller;
  List<String> videosList = <String>[
    "https://www.youtube.com/watch?v=BBAyRBTfsOU",
  ];

  @override
  void initState() {
    controller = YoutubePlayerController(
      initialVideoId: YoutubePlayer.convertUrlToId(video_url)!,
    )..addListener(_videoPlayerListner);
    controller.play();
    super.initState();
  }
  void _videoPlayerListner() {
    print(controller.value.playerState.toString());
  }
  @override
  void dispose() {
    controller.dispose();
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

      body:  GridView.builder(
        shrinkWrap: true,
        itemCount: videosList.length,
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          childAspectRatio: 16 / 9,
        ),
        itemBuilder: (BuildContext context, int index) {
          final YoutubePlayerController controller = YoutubePlayerController(
            initialVideoId: YoutubePlayer.convertUrlToId(
              videosList[index],
            ) ??
                "",
            flags: const YoutubePlayerFlags(autoPlay: true),
          );
          return YoutubePlayer(
            controller: controller,
            // showVideoProgressIndicator: true,
          );
        },
      ),
    );
  }
}
