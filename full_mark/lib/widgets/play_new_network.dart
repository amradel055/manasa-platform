import 'package:appinio_video_player/appinio_video_player.dart';
import 'package:full_mark/constants/strings.dart';
import 'package:pod_player/pod_player.dart';
import 'package:flutter/material.dart';

class PlayNewVideoFromNetwork extends StatefulWidget {
  const PlayNewVideoFromNetwork({Key? key, required this.url}) : super(key: key);
  final String url ;
  @override
  State<PlayNewVideoFromNetwork> createState() => _PlayNewVideoFromNetworkState();
}

class _PlayNewVideoFromNetworkState extends State<PlayNewVideoFromNetwork> {
  late CustomVideoPlayerController _customVideoPlayerController;
  late VideoPlayerController videoPlayerController;

  // String videoUrl = widget.url!;

  @override
  void initState() {
    videoPlayerController = VideoPlayerController.network(VideoUrl+widget.url)
      ..initialize().then((value) => setState(() {}));
    _customVideoPlayerController = CustomVideoPlayerController(
      context: context,
      videoPlayerController: videoPlayerController,
    );
    // controller = PodPlayerController(
    //   playVideoFrom: PlayVideoFrom.youtube("youtube.com/watch?v=A3ltMaM6noM&ab_channel=Flutter"),
    // )..initialise();
    super.initState();
  }

  @override
  void dispose() {
    _customVideoPlayerController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: CustomVideoPlayer(
            customVideoPlayerController: _customVideoPlayerController
        ),
      ),
    );
  }
}
