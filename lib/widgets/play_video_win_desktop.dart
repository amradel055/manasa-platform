import 'package:dart_vlc/dart_vlc.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class VideoVlcPlayer extends StatefulWidget
{

  const VideoVlcPlayer({
    super.key,
  });

  @override
  VideoVlcPlayerState createState() => VideoVlcPlayerState();
}

class VideoVlcPlayerState extends State<VideoVlcPlayer>
{
  final player = Player(id: 69420);


  @override
  void initState()
  {   player.open(Playlist(medias: [Media.network("https://www.youtube.com/watch?v=GmfvuioLMrY&ab_channel=Hitesh%E2%80%A2%40alexmercerind")]));
    super.initState();
  }

  @override
  Widget build(BuildContext context)
  {
    return ListView(
      children: [
        ClipRRect(
          child:  Video(
            player: player,
            height: 1000.0,
            width: 700.0,
            scale: 1.0, // default
            showControls: true, // default
          ),
        ),
      ],
    );
  }

}