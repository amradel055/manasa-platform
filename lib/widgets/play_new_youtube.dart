import 'package:appinio_video_player/appinio_video_player.dart';
import 'package:flick_video_player/flick_video_player.dart';
import 'package:pod_player/pod_player.dart';
import 'package:flutter/material.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

class PlayNewVideoFromYoutube extends StatefulWidget {
  const PlayNewVideoFromYoutube({Key? key, required this.url}) : super(key: key);
  final String url ;
  @override
  State<PlayNewVideoFromYoutube> createState() => _PlayNewVideoFromYoutubeState();
}

class _PlayNewVideoFromYoutubeState extends State<PlayNewVideoFromYoutube> {
  late YoutubePlayerController _controller;

  late PlayerState _playerState;
  late YoutubeMetaData _videoMetaData;
  double _volume = 100;
  bool _muted = false;
  bool _isPlayerReady = false;
  // String videoUrl = widget.url!;

  @override
  void initState() {
    YoutubePlayerController _controller = YoutubePlayerController(
      initialVideoId: 'iLnmTe5Q2Qw',
      flags: YoutubePlayerFlags(
        autoPlay: true,
        mute: true,
      )
    )..addListener(listener);
    super.initState();
  }
  void listener() {
    if (_isPlayerReady && mounted && !_controller.value.isFullScreen) {
      setState(() {
        _playerState = _controller.value.playerState;
        _videoMetaData = _controller.metadata;
      });
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: YoutubePlayer(
        controller: _controller,
        showVideoProgressIndicator: true,
        // videoProgressIndicatorColor: Colors.amber,
        // progressColors: ProgressColors(
        //   playedColor: Colors.amber,
        //   handleColor: Colors.amberAccent,

      ),
    );
  }
}
