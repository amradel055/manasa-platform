import 'package:flutter/material.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

class PlayNewVideoFromYoutube extends StatefulWidget {
  const PlayNewVideoFromYoutube({Key? key, required this.url}) : super(key: key);
  final String url;

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
    super.initState();
    final videoId = YoutubePlayer.convertUrlToId(widget.url);
    _controller = YoutubePlayerController(
      initialVideoId: videoId??"",
      flags: YoutubePlayerFlags(
        autoPlay: true,
        // mute: true,
      ),
    );
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
        onReady: () {
          _controller.addListener(listener);
        },
      ),
    );
  }
}
