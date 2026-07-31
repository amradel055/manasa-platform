import 'dart:async';
import 'package:full_mark/widgets/play_new_network.dart';
import 'package:full_mark/widgets/play_new_youtube.dart';
import 'package:assets_audio_player/assets_audio_player.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:full_mark/widgets/attach.dart';
import 'package:full_mark/widgets/video_cart_widget.dart';
import 'package:full_mark/widgets/viemo_player.dart';
import 'package:headset_connection_event/headset_event.dart';
import 'package:pod_player/pod_player.dart';
import 'package:provider/provider.dart';
import 'package:full_mark/helper_methods/show_toast.dart';
import 'package:full_mark/models/exam_model.dart';
import 'package:full_mark/providers/data_handler_provider.dart';
import 'package:full_mark/screens/do_exam_screen.dart';
import 'package:full_mark/widgets/loading_widget.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../models/lesson_model.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';


class LessonScreen extends StatefulWidget {
  const LessonScreen(this.lesson, {Key? key}) : super(key: key);
  final Lesson lesson;

  @override
  _LessonScreenState createState() => _LessonScreenState();
}

class _LessonScreenState extends State<LessonScreen> {
  AssetsAudioPlayer assetsAudioPlayer = AssetsAudioPlayer();
  // late YoutubePlayerController _controller;

  List<AssetsAudioPlayer> assetsAudioPlayers = [];
  List<Audio> audios = [];
  String? currentPlay;
  bool? isRecording;
  List<dynamic> allVideoes = [] ;

  bool isPlaying = false;

  String currentTime = "0:0:0";

  String completeTime = "0:0:0";

  List<Exam> exams = [];

  bool loading = false;

  late Audio audio;

  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  HeadsetEvent headsetPlugin = HeadsetEvent();
  late VideoPlayerController videoController;
  late YoutubePlayerController ycontroller;
  late final PodPlayerController controller;

  // late String videoId;
  // videoId = YoutubePlayer.convertUrlToId("https://www.youtube.com/watch?v=BBAyRBTfsOU");
  @override
  void initState() {
    // allVideoes.addAll(widget.lesson.youtubeLinks! + widget.lesson.vimeoLinks! +widget.lesson.videoLinks!);
    // controller = PodPlayerController(
    //   playVideoFrom: PlayVideoFrom.network(
    //     'https://flutter.github.io/assets-for-api-docs/assets/videos/bee.mp4',
    //   ),
    // )..initialise();
    // for(int i;i<=widget.lesson.youtubeLink!.length;i++)
    //  ycontroller = YoutubePlayerController(
    //   initialVideoId:  YoutubePlayer.convertUrlToId(widget.lesson.youtubeLink??"https://www.youtube.com/watch?v=BBAyRBTfsOU")!,
    //   flags: YoutubePlayerFlags(
    //     autoPlay: true,
    //     mute: true,
    //   ),
    // );
    // videoController = VideoPlayerController.networkUrl(Uri.parse(
    //    "https://flutter.github.io/assets-for-api-docs/assets/videos/bee.mp4"))
    // ..initialize().then((_) {
    //     // Ensure the first frame is shown after the video is initialized, even before the play button has been pressed.
    //     setState(() {});
    //   });
    // TODO: implement initState
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      context.read<DataHandler>().getLessonExam(widget.lesson.id).then((value) {
        exams = context.read<DataHandler>().lessonExams;
      });
      setState(() {});
    });
    if (widget.lesson.voiceLink != null) {
      audio = Audio.network(uploadUrl + widget.lesson.voiceLink!);
    }
    for (final audio in widget.lesson.lessonVoiceList ?? []) {
      audios.add(Audio.network(uploadUrl + audio));
    }
    headsetPlugin.getCurrentState.then((value) {
      if(value == HeadsetState.CONNECT){
        assetsAudioPlayer.open(audio);
        isPlaying = true;
      }
    });

    /// Detect the moment headset is plugged or unplugged
    headsetPlugin.setListener((value) {
      if(value == HeadsetState.DISCONNECT){
        assetsAudioPlayer.pause();
        isPlaying = false;
        setState(() {});
      }
    });
    super.initState();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    assetsAudioPlayer.dispose();
    videoController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    loading = context.watch<DataHandler>().lessonLoading;
    return Scaffold(
        backgroundColor: Colors.white,
        key: _scaffoldKey,
        appBar: AppBar(
          elevation: 0,

          backgroundColor: Colors.white,
          title: Text(
            widget.lesson.lessonName!.toLowerCase(),
            style: TextStyle(fontSize: 20,color: mainColor),
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
        // drawer: const DrawerWidget(),

        body: Container(
          decoration:  BoxDecoration(color:Colors.white),
          width: size.width,
          height: size.height,
          child: Container(
            decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(40),
                  topRight: Radius.circular(40),
                )),

            child: Stack(
              children: [
                _BuildBody(size),
                loading == true ? loadingWidget() : const SizedBox(),
              ],
            ),
          ),
        ));
  }
  _BuildBody(Size size){
    return Padding(
      padding: const EdgeInsets.fromLTRB(12,5,12,5),
      child: SizedBox(
        height: size.height,
        child: SingleChildScrollView(
          physics: AlwaysScrollableScrollPhysics(),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              widget.lesson.images != null && widget.lesson.images!.isNotEmpty
                            ? Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Container(
                                    height: size.height * 0.25,
                                    width: size.width * 0.9,
                                    decoration: BoxDecoration(border: Border.all(color: Colors.grey)),
                                    child: CarouselSlider(
                                      options:
                                          CarouselOptions(height: size.height * 0.5, enableInfiniteScroll: false, pageSnapping: true, enlargeCenterPage: true),
                                      items: widget.lesson.images!.map((i) {
                                        return Builder(
                                          builder: (BuildContext context) {
                                            return GestureDetector(
                                              child: Container(
                                                width: MediaQuery.of(context).size.width,
                                                margin: EdgeInsets.symmetric(horizontal: 5.0),
                                                decoration: const BoxDecoration(color: Colors.transparent),
                                                child: Image.network(
                                                  uploadUrl + i,
                                                  fit: BoxFit.fill,
                                                  loadingBuilder: (BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
                                                    if (loadingProgress == null) {
                                                      return child;
                                                    }
                                                    return Center(
                                                      child: CircularProgressIndicator(
                                                        value: loadingProgress.expectedTotalBytes != null
                                                            ? loadingProgress.cumulativeBytesLoaded / loadingProgress.expectedTotalBytes!
                                                            : null,
                                                      ),
                                                    );
                                                  },
                                                ),
                                              ),
                                              onTap: () {
                                                showDialog(
                                                    context: context,
                                                    builder: (context) {
                                                      return CarouselSlider(
                                                        options: CarouselOptions(
                                                            height: size.height,
                                                            enableInfiniteScroll: false,
                                                            pageSnapping: true,
                                                            enlargeCenterPage: false,
                                                            viewportFraction: 1,
                                                            initialPage: widget.lesson.images!.indexOf(i)),
                                                        items: widget.lesson.images!.map((i) {
                                                          return Padding(
                                                            padding: EdgeInsets.only(bottom: size.height * .1),
                                                            child: InteractiveViewer(
                                                              panEnabled: false,
                                                              boundaryMargin: const EdgeInsets.all(100),
                                                              minScale: 0.5,
                                                              maxScale: 4,
                                                              child: Container(
                                                                width: size.width,
                                                                height: size.height,
                                                                margin: const EdgeInsets.symmetric(horizontal: 5.0),
                                                                child: Image.network(
                                                                  uploadUrl + i,
                                                                  fit: BoxFit.fill,
                                                                ),
                                                              ),
                                                            ),
                                                          );
                                                        }).toList(),
                                                      );
                                                    });
                                              },
                                            );
                                          },
                                        );
                                      }).toList(),
                                    )

                                    // Image.network(uploadUrl + widget.lesson.image! , fit: BoxFit.fitHeight,) ,
                                    ),
                              )
                            : const SizedBox(),

              widget.lesson.lessonVoiceList!.isNotEmpty
                  ? _buildBar(size,"Voice Notes"):SizedBox(),
              widget.lesson.lessonVoiceList!.isNotEmpty
                  ?  Padding(
                padding: const EdgeInsets.fromLTRB(10,10,0,0),
                child: SizedBox(
                    height: size.height * .12,
                    child:
                    ListView.builder(
                        itemCount: widget.lesson.lessonVoiceList!.length,
                        // itemCount: widget.lesson.lessonVoiceList!.length,
                        scrollDirection: Axis.horizontal,
                        itemBuilder: (context, index) {
                          return
                            Card(
                              color: greyColor,
                              child: Container(
                                  height: size.height * 0.12,
                                  width: size.width * 0.6,
                                  child: Container(
                                    height: size.height * 0.12,
                                    width: size.width * 0.75,

                                    decoration: BoxDecoration(
                                      color:greyColor,
                                      borderRadius: BorderRadius.all(Radius.circular(
                                          10.0)),),
                                    child: Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Column(
                                        children: [
                                          Expanded(
                                            child: SingleChildScrollView(
                                              child: Column(
                                                crossAxisAlignment: CrossAxisAlignment.center,
                                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                                children: [
                                                  for (final audio in audios)
                                                    Row(
                                                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                                                      children: [
                                                        IconButton(
                                                          onPressed: () {
                                                            setState(() {
                                                              assetsAudioPlayer.seekBy(Duration(seconds: -10));
                                                            });
                                                          },
                                                          icon: Icon(
                                                            Icons.fast_rewind,
                                                            size: size.width * 0.1,
                                                          ),
                                                        ),
                                                        IconButton(
                                                          onPressed: () async {
                                                            final state = await headsetPlugin.getCurrentState;
                                                            if (!isPlaying && state == HeadsetState.DISCONNECT) {
                                                              showToast(context, "Plug in your headset to listen");
                                                              return;
                                                            }
                                                            // setState(() async {
                                                            if (currentPlay == audio.path) {
                                                              await assetsAudioPlayer.playOrPause();
                                                              isPlaying = assetsAudioPlayer.isPlaying.value;
                                                            } else {
                                                              currentPlay = audio.path;
                                                              await assetsAudioPlayer.open(audio);
                                                              isPlaying = true;
                                                            }
                                                            // if(!isPlaying){
                                                            setState(() {});
                                                            // }
                                                            // });

                                                            // setState(() {
                                                            // isPlaying =  !assetsAudioPlayer.isPlaying.value;
                                                            // });
                                                          },
                                                          icon: Icon(
                                                            currentPlay == audio.path && isPlaying ? Icons.pause : Icons.play_circle_outline,
                                                            size: size.width * 0.1,
                                                          ),
                                                        ),
                                                        IconButton(
                                                          onPressed: () {
                                                            setState(() {
                                                              setState(() {
                                                                assetsAudioPlayer.seekBy(Duration(seconds: 10));
                                                              });
                                                            });
                                                          },
                                                          icon: Icon(
                                                            Icons.fast_forward,
                                                            size: size.width * 0.1,
                                                          ),
                                                        ),
                                                      ],
                                                    ),
                                                ],
                                              ),
                                            ),
                                          ),
                                          // assetsAudioPlayer.current.hasValue
                                          //     ? assetsAudioPlayer.builderCurrentPosition(builder: (context, pos) {
                                          //         return ProgressBar(
                                          //           progress: pos,
                                          //           total: assetsAudioPlayer.current.hasValue != null
                                          //               ? assetsAudioPlayer.current.value!.audio.duration
                                          //               : const Duration(milliseconds: 0),
                                          //           onSeek: (duration) {
                                          //             print('User selected a new time: $duration');
                                          //             setState(() {
                                          //               assetsAudioPlayer.seek(duration);
                                          //             });
                                          //           },
                                          //         );
                                          //       })
                                          //     : SizedBox(),
                                          // Row(
                                          //   mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                          //   children: [
                                          //     assetsAudioPlayer.builderCurrentPosition(builder: (context , pos){
                                          //
                                          //       return  Text(pos.toString());
                                          //     })
                                          //     ,
                                          //
                                          //     assetsAudioPlayer.builderCurrent(builder: (context , cu){
                                          //       return  Text(cu.audio.duration.toString());
                                          //     })
                                          //   ],
                                          // ),
                                        ],
                                      ),
                                    ),
                                  )

                                // AudioWidget.network(
                                //     play: isPlaying,
                                //
                                //     onPositionChanged: (duration , _duration){
                                //       setState(() {
                                //          currentTime = duration.inHours.toString() + " : " + duration.inMinutes.toString() + " : " + duration.inSeconds.toString() ;
                                //          completeTime = _duration.inHours.toString() + " : " + _duration.inMinutes.toString() + " : " + _duration.inSeconds.toString();
                                //       });
                                //     },
                                //     onFinished: (){
                                //       setState(() {
                                //         isPlaying = !isPlaying;
                                //       });
                                //     },
                                //
                                //
                                //
                                //     child: Container(
                                //       height: size.height * 0.3,
                                //       width: size.width *0.75,
                                //       color: Colors.yellow[100],
                                //       child: Padding(
                                //         padding: const EdgeInsets.all(8.0),
                                //         child: Column(
                                //           crossAxisAlignment: CrossAxisAlignment.center,
                                //           mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                //           children: [
                                //             Row(
                                //               mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                                //               children: [
                                //                 IconButton(
                                //                   onPressed: () {
                                //                     setState(() {
                                //                       // assetsAudioPlayer;
                                //                     });
                                //                   },
                                //                   icon:  Icon(Icons.fast_rewind, size: size.width *0.1,),
                                //                 ),
                                //                 IconButton(
                                //                   onPressed: () {
                                //                     setState(() {
                                //                       isPlaying = !isPlaying;
                                //                     });
                                //                   },
                                //                   icon:  Icon(isPlaying == false ?Icons.play_circle_outline : Icons.pause , size: size.width *0.1,),
                                //                 ),
                                //                 IconButton(
                                //                   onPressed: () {
                                //                     setState(() {
                                //                       // assetsAudioPlayer.currentPosition.listen((event) {
                                //                       //   setState(() {
                                //                       //     event = Duration(hours:event.inHours , minutes:event.inMinutes , seconds: (event.inSeconds+10));
                                //                       //
                                //                       //   });
                                //                       // });
                                //                     });
                                //                   },
                                //                   icon:  Icon(Icons.fast_forward , size: size.width *0.1,),
                                //                 ),
                                //               ],
                                //             ),
                                //             Row(
                                //               mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                //               children: [
                                //                 Text(currentTime.toString()),
                                //
                                //                 Text(completeTime.toString())
                                //               ],
                                //             ),
                                //
                                //           ],
                                //         ),
                                //       ),
                                //     ),
                                //     url:
                                //        uploadUrl + widget.lesson.voiceLink! ,
                                //
                                //    // "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"
                                // ),
                              ),
                            )
                          ;})
                ),
              )
                  :SizedBox(),
                widget.lesson.youtubeLinks!.isNotEmpty || widget.lesson.videoLinks!.isNotEmpty||
                    widget.lesson.vimeoLinks!.isNotEmpty

                    ?    _buildBar(size," Videos"):SizedBox(),
                widget.lesson.youtubeLinks!.isNotEmpty
                  ?  Column(
                    children: [
                      _buildBarTwo(size," Youtube"),
                      Padding(
                padding: const EdgeInsets.fromLTRB(0,10,0,0),
                child: SizedBox(
                        height: size.height * .15,
                        child:
                        ListView.builder(
                            itemCount: widget.lesson.youtubeLinks!.length,

                            scrollDirection: Axis.horizontal,
                            itemBuilder: (context, index) {
                              return
                                // _button(
                                //   'video part 1',
                                //   onPressed: () =>
                                //       // Navigator.of(context).pushNamed('/fromNetwork'),
                                //   Navigator.push(context,
                                //   MaterialPageRoute(builder: (context) => PlayVideoFromNetwork()))
                                // );
                              GestureDetector(
                                onTap: (){
                                    Navigator.push(context,
                                    MaterialPageRoute(builder: (context) => PlayNewVideoFromYoutube(url: 'kkk', )));
                                },
                                child:Stack(
                                  children: [
                                    videoCartWidget(
                                        widget.lesson.youtubeLinks![index]??"",size,"",context
                                    ),
                                    Center(
                                      child: _button(
                                        'Play',
                                        onPressed: () =>       Navigator.push(context,
                                            MaterialPageRoute(builder: (context) => PlayNewVideoFromYoutube(url:widget.lesson.youtubeLinks![index]??"" , ))),
                                      ),
                                    ),
                                  ],
                                )

                              );
                              })
                ),
              ),
                    ],
                  ):SizedBox(),

              widget.lesson.videoLinks!.isNotEmpty
                  ?  Column(
                    children: [
                      _buildBarTwo(size," network"),
                      Padding(
                padding: const EdgeInsets.fromLTRB(0,10,0,0),
                child: SizedBox(
                        height: size.height * .15,
                        child:
                        ListView.builder(
                            itemCount: widget.lesson.videoLinks!.length,

                            scrollDirection: Axis.horizontal,
                            itemBuilder: (context, index) {
                              return
                                // _button(
                                //   'video part 1',
                                //   onPressed: () =>
                                //       // Navigator.of(context).pushNamed('/fromNetwork'),
                                //   Navigator.push(context,
                                //   MaterialPageRoute(builder: (context) => PlayVideoFromNetwork()))
                                // );
                                Stack(
                                  children: [
                                    videoCartWidget(
                                        widget.lesson.videoLinks![index]??"",size,"",context
                                    ),
                                    Center(
                                      child: _button(
                                        'Play',
                                        onPressed: () =>       Navigator.push(context,
                                            MaterialPageRoute(builder: (context) => PlayNewVideoFromNetwork(url:widget.lesson.videoLinks![index]??"" , ))),
                                      ),
                                    ),
                                  ],
                                );
                            })
                ),
              ),
                    ],
                  ):SizedBox(),
              widget.lesson.vimeoLinks!.isNotEmpty
                  ?  Column(
                    children: [
                      _buildBarTwo(size,"vimeo"),
                      Padding(
                padding: const EdgeInsets.fromLTRB(0,10,0,0),
                child: SizedBox(
                        height: size.height * .15,
                        child:
                        ListView.builder(
                            itemCount: widget.lesson.vimeoLinks!.length,

                            scrollDirection: Axis.horizontal,
                            itemBuilder: (context, index) {
                              return
                                // _button(
                                //   'video part 1',
                                //   onPressed: () =>
                                //       // Navigator.of(context).pushNamed('/fromNetwork'),
                                //   Navigator.push(context,
                                //   MaterialPageRoute(builder: (context) => PlayVideoFromNetwork()))
                                // );
                                Stack(
                                  children: [
                                    videoCartWidget(
                                        widget.lesson.vimeoLinks![index]??"",size,"",context
                                    ),
                                    Center(
                                      child: _button(
                                        'Play',
                                        onPressed: () =>       Navigator.push(context,
                                            MaterialPageRoute(builder: (context) => PlayVideoFromVimeo(url:widget.lesson.vimeoLinks![index]??"" , ))),
                                      ),
                                    ),
                                  ],
                                );
                            })
                ),
              ),
                    ],
                  ):SizedBox(),

          //     Padding(
          //       padding: const EdgeInsets.fromLTRB(10,10,0,0),
          //       child: Container(
          //            decoration: BoxDecoration(
          //              color: greyColor,
          //              borderRadius: const BorderRadius.all(Radius.circular(30)),),
          //            child: Column(
          //              children: [
          //                ClipRRect(
          //                          borderRadius: BorderRadius.circular(30),
          //                    child: PodVideoPlayer(controller: controller)),
          //                // Container(
          //                //   height: 250,
          //                //   child: VimeoPlayer(
          //                //     videoId:  "845597890"
          //                //   ),
          //                // ),
          // //                YoutubePlayer(
          // //                  controller: ycontroller,
          // //                  showVideoProgressIndicator: true,
          // //                  // videoProgressIndicatorColor: Colors.amber,
          // //                  // progressColors: ProgressColors(
          // //                  //   playedColor: Colors.amber,
          // //                  //   handleColor: Colors.amberAccent,
          // //                  // ),
          // //                  //   onReady () {
          // //                  // _controller.addListener(listener);
          // //                  // },
          // //                ),
          // //                Container(
          // //
          // //   decoration: BoxDecoration(
          // //       borderRadius: const BorderRadius.all(Radius.circular(30)),),
          // //   child: Center(
          // //       child: videoController.value.isInitialized
          // //                 ?ClipRRect(
          // //         borderRadius: BorderRadius.circular(30),
          // //                   child: AspectRatio(
          // //
          // //         aspectRatio: videoController.value.aspectRatio,
          // //         child: VideoPlayer(videoController),
          // //       ),
          // //                 )
          // //                 : Container(),),
          // // ),
          //                // Positioned(
          //                //   bottom: 2,
          //                //   width: MediaQuery.of(context).size.width,
          //                //   child: videoController.value.isInitialized
          //                //       ? VCaption(videoController)
          //                //       : Container(),
          //                // ),
          //                // Positioned(
          //                //     bottom: 0,
          //                //     width: MediaQuery.of(context).size.width,
          //                //     child: VideoProgressIndicator(
          //                //       videoController,
          //                //       allowScrubbing: true,
          //                //       colors: VideoProgressColors(
          //                //           backgroundColor: Colors.blueGrey,
          //                //           bufferedColor: Colors.blueGrey,
          //                //           playedColor: Colors.blueAccent),
          //                //     )),
          //                // Positioned(
          //                //   bottom: 0,
          //                //   width: MediaQuery.of(context).size.width,
          //                //   child: Center(
          //                //     child: Row(
          //                //       mainAxisAlignment: MainAxisAlignment.center,
          //                //       children: [
          //                //         ElevatedButton(
          //                //             style: ButtonStyle(
          //                //                 backgroundColor: MaterialStateProperty.all<Color>(Colors.green),
          //                //                 fixedSize: MaterialStateProperty.all(Size(10, 10)),
          //                //                 shape: MaterialStateProperty.all(RoundedRectangleBorder(
          //                //                     borderRadius: BorderRadius.circular(200)))),
          //                //             onPressed: () {
          //                //               videoController.seekTo(Duration(
          //                //                   seconds: videoController.value.position.inSeconds + 10));
          //                //             },
          //                //             child: Text("+10")),
          //                //         ElevatedButton(
          //                //             style: ButtonStyle(
          //                //                 backgroundColor: MaterialStateProperty.all<Color>(Colors.white),
          //                //                 fixedSize: MaterialStateProperty.all(Size(25, 25)),
          //                //                 shape: MaterialStateProperty.all(RoundedRectangleBorder(
          //                //                     borderRadius: BorderRadius.circular(100)))),
          //                //             onPressed: () {
          //                //               videoController.value.isPlaying
          //                //                   ? videoController.pause()
          //                //                   : videoController.play();  },
          //                //             child: Icon(
          //                //               videoController.value.isPlaying ? Icons.pause : Icons.play_arrow,color: Colors.grey,
          //                //             )),
          //                //         ElevatedButton(
          //                //             style: ButtonStyle(
          //                //                 backgroundColor: MaterialStateProperty.all<Color>(Colors.red),
          //                //                 fixedSize: MaterialStateProperty.all(Size(25, 25)),
          //                //                 shape: MaterialStateProperty.all(RoundedRectangleBorder(
          //                //                     borderRadius: BorderRadius.circular(100)))),
          //                //             onPressed: () {
          //                //                videoController.pause();  },
          //                //             child: Icon(
          //                //           Icons.pause ,
          //                //             )),
          //                //       ],
          //                //     ),
          //                //   ),
          //                // ),
          //
          //              ],
          //            ),
          //          ),
          //     ),
              widget.lesson.documents!.isNotEmpty
                  ? _buildBar(size,"attachments"):SizedBox(),
              widget.lesson.documents!.isNotEmpty
                  ?  Padding(
                padding: const EdgeInsets.fromLTRB(10,10,0,0),
                child: SizedBox(
                    height: size.height * .16,
                    child:
                    ListView.builder(
                        itemCount:widget.lesson.documents!.length,
                        scrollDirection: Axis.horizontal,
                        itemBuilder: (context, index) {
                          return
                            AttachWidget(widget.lesson.documents![index], size, "",context)
                          ;})
                ),
              ):SizedBox(),
              _buildBar(size,"Exams"),

              exams.isNotEmpty
                  ? SizedBox(
                height: size.height * 0.3,
                width: size.width,
                child: SingleChildScrollView(
                  child: Column(
                      children: exams
                          .map((exam) => TextButton(
                          onPressed: () {
                            Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => DoExam(exam)));
                          },
                          child: Text(exam.examName!)))
                          .toList()),
                ),

              )
                  : Center(
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                "There is no exams for this lesson",
                style: TextStyle(fontWeight: FontWeight.w500 , color: mainColor ),
              ),
                    ),
                  ),




            ],
          ),
        ),
      ),
    );
  }
  _buildBar(Size size,  String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8,8,8,0),
      child: Container(
        width: size.width * 0.25,
        height: size.height * 0.05,
        decoration: BoxDecoration(
          color: mainColor,
          borderRadius: const BorderRadius.all(Radius.circular(20)),),
        child: Center(
            child: SizedBox(
                height: size.height * 0.025,
                child: FittedBox(
                    child: Text(
                      title,
                      style: TextStyle(fontWeight:FontWeight.bold ,
                          color:Colors.white),
                    )))),
      ),
    );
  }
  _buildBarTwo(Size size,  String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8,8,8,0),
      child: Container(
        width: size.width * 0.25,
        height: size.height * 0.05,
        decoration: BoxDecoration(
          color: greyColor,
          borderRadius: const BorderRadius.all(Radius.circular(20)),),
        child: Center(
            child: SizedBox(
                height: size.height * 0.025,
                child: FittedBox(
                    child: Text(
                      title,
                      style: TextStyle(fontWeight:FontWeight.bold ,
                          color:mainColor),
                    )))),
      ),
    );
  }

  // _BuildBody(Size size) {
  //   return SizedBox(
  //     height: size.height,
  //     child: SingleChildScrollView(
  //       child: Column(
  //         children: [
  //           widget.lesson.images != null && widget.lesson.images!.isNotEmpty
  //               ? Padding(
  //                   padding: const EdgeInsets.all(8.0),
  //                   child: Container(
  //                       height: size.height * 0.5,
  //                       width: size.width * 0.9,
  //                       decoration: BoxDecoration(border: Border.all(color: Colors.grey)),
  //                       child: CarouselSlider(
  //                         options:
  //                             CarouselOptions(height: size.height * 0.5, enableInfiniteScroll: false, pageSnapping: true, enlargeCenterPage: true),
  //                         items: widget.lesson.images!.map((i) {
  //                           return Builder(
  //                             builder: (BuildContext context) {
  //                               return GestureDetector(
  //                                 child: Container(
  //                                   width: MediaQuery.of(context).size.width,
  //                                   margin: EdgeInsets.symmetric(horizontal: 5.0),
  //                                   decoration: const BoxDecoration(color: Colors.transparent),
  //                                   child: Image.network(
  //                                     uploadUrl + i,
  //                                     fit: BoxFit.fill,
  //                                     loadingBuilder: (BuildContext context, Widget child, ImageChunkEvent? loadingProgress) {
  //                                       if (loadingProgress == null) {
  //                                         return child;
  //                                       }
  //                                       return Center(
  //                                         child: CircularProgressIndicator(
  //                                           value: loadingProgress.expectedTotalBytes != null
  //                                               ? loadingProgress.cumulativeBytesLoaded / loadingProgress.expectedTotalBytes!
  //                                               : null,
  //                                         ),
  //                                       );
  //                                     },
  //                                   ),
  //                                 ),
  //                                 onTap: () {
  //                                   showDialog(
  //                                       context: context,
  //                                       builder: (context) {
  //                                         return CarouselSlider(
  //                                           options: CarouselOptions(
  //                                               height: size.height,
  //                                               enableInfiniteScroll: false,
  //                                               pageSnapping: true,
  //                                               enlargeCenterPage: false,
  //                                               viewportFraction: 1,
  //                                               initialPage: widget.lesson.images!.indexOf(i)),
  //                                           items: widget.lesson.images!.map((i) {
  //                                             return Padding(
  //                                               padding: EdgeInsets.only(bottom: size.height * .1),
  //                                               child: InteractiveViewer(
  //                                                 panEnabled: false,
  //                                                 boundaryMargin: const EdgeInsets.all(100),
  //                                                 minScale: 0.5,
  //                                                 maxScale: 4,
  //                                                 child: Container(
  //                                                   width: size.width,
  //                                                   height: size.height,
  //                                                   margin: const EdgeInsets.symmetric(horizontal: 5.0),
  //                                                   child: Image.network(
  //                                                     uploadUrl + i,
  //                                                     fit: BoxFit.fill,
  //                                                   ),
  //                                                 ),
  //                                               ),
  //                                             );
  //                                           }).toList(),
  //                                         );
  //                                       });
  //                                 },
  //                               );
  //                             },
  //                           );
  //                         }).toList(),
  //                       )
  //
  //                       // Image.network(uploadUrl + widget.lesson.image! , fit: BoxFit.fitHeight,) ,
  //                       ),
  //                 )
  //               : const SizedBox(),
  //           widget.lesson.lessonVoiceList!.isNotEmpty
  //               ? Center(
  //                   child: Padding(
  //                     padding: const EdgeInsets.all(8.0),
  //                     child: Card(
  //                       color: Colors.green[100],
  //                       child: Container(
  //                           height: size.height * 0.2,
  //                           width: size.width * 0.9,
  //                           child: Container(
  //                             height: size.height * 0.3,
  //                             width: size.width * 0.75,
  //                             color: Colors.yellow[100],
  //                             child: Padding(
  //                               padding: const EdgeInsets.all(8.0),
  //                               child: Column(
  //                                 children: [
  //                                   Expanded(
  //                                     child: SingleChildScrollView(
  //                                       child: Column(
  //                                         crossAxisAlignment: CrossAxisAlignment.center,
  //                                         mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //                                         children: [
  //                                           for (final audio in audios)
  //                                             Row(
  //                                               mainAxisAlignment: MainAxisAlignment.spaceEvenly,
  //                                               children: [
  //                                                 IconButton(
  //                                                   onPressed: () {
  //                                                     setState(() {
  //                                                       assetsAudioPlayer.seekBy(Duration(seconds: -10));
  //                                                     });
  //                                                   },
  //                                                   icon: Icon(
  //                                                     Icons.fast_rewind,
  //                                                     size: size.width * 0.1,
  //                                                   ),
  //                                                 ),
  //                                                 IconButton(
  //                                                   onPressed: () async {
  //                                                     final state = await headsetPlugin.getCurrentState;
  //                                                     if (!isPlaying && state == HeadsetState.DISCONNECT) {
  //                                                       showToast(context, "Plug in your headset to listen");
  //                                                       return;
  //                                                     }
  //                                                     // setState(() async {
  //                                                     if (currentPlay == audio.path) {
  //                                                       await assetsAudioPlayer.playOrPause();
  //                                                       isPlaying = assetsAudioPlayer.isPlaying.value;
  //                                                     } else {
  //                                                       currentPlay = audio.path;
  //                                                       await assetsAudioPlayer.open(audio);
  //                                                       isPlaying = true;
  //                                                     }
  //                                                     // if(!isPlaying){
  //                                                     setState(() {});
  //                                                     // }
  //                                                     // });
  //
  //                                                     // setState(() {
  //                                                     // isPlaying =  !assetsAudioPlayer.isPlaying.value;
  //                                                     // });
  //                                                   },
  //                                                   icon: Icon(
  //                                                     currentPlay == audio.path && isPlaying ? Icons.pause : Icons.play_circle_outline,
  //                                                     size: size.width * 0.1,
  //                                                   ),
  //                                                 ),
  //                                                 IconButton(
  //                                                   onPressed: () {
  //                                                     setState(() {
  //                                                       setState(() {
  //                                                         assetsAudioPlayer.seekBy(Duration(seconds: 10));
  //                                                       });
  //                                                     });
  //                                                   },
  //                                                   icon: Icon(
  //                                                     Icons.fast_forward,
  //                                                     size: size.width * 0.1,
  //                                                   ),
  //                                                 ),
  //                                               ],
  //                                             ),
  //                                         ],
  //                                       ),
  //                                     ),
  //                                   ),
  //                                   // assetsAudioPlayer.current.hasValue
  //                                   //     ? assetsAudioPlayer.builderCurrentPosition(builder: (context, pos) {
  //                                   //         return ProgressBar(
  //                                   //           progress: pos,
  //                                   //           total: assetsAudioPlayer.current.hasValue != null
  //                                   //               ? assetsAudioPlayer.current.value!.audio.duration
  //                                   //               : const Duration(milliseconds: 0),
  //                                   //           onSeek: (duration) {
  //                                   //             print('User selected a new time: $duration');
  //                                   //             setState(() {
  //                                   //               assetsAudioPlayer.seek(duration);
  //                                   //             });
  //                                   //           },
  //                                   //         );
  //                                   //       })
  //                                   //     : SizedBox(),
  //                                   // Row(
  //                                   //   mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //                                   //   children: [
  //                                   //     assetsAudioPlayer.builderCurrentPosition(builder: (context , pos){
  //                                   //
  //                                   //       return  Text(pos.toString());
  //                                   //     })
  //                                   //     ,
  //                                   //
  //                                   //     assetsAudioPlayer.builderCurrent(builder: (context , cu){
  //                                   //       return  Text(cu.audio.duration.toString());
  //                                   //     })
  //                                   //   ],
  //                                   // ),
  //                                 ],
  //                               ),
  //                             ),
  //                           )
  //
  //                           // AudioWidget.network(
  //                           //     play: isPlaying,
  //                           //
  //                           //     onPositionChanged: (duration , _duration){
  //                           //       setState(() {
  //                           //          currentTime = duration.inHours.toString() + " : " + duration.inMinutes.toString() + " : " + duration.inSeconds.toString() ;
  //                           //          completeTime = _duration.inHours.toString() + " : " + _duration.inMinutes.toString() + " : " + _duration.inSeconds.toString();
  //                           //       });
  //                           //     },
  //                           //     onFinished: (){
  //                           //       setState(() {
  //                           //         isPlaying = !isPlaying;
  //                           //       });
  //                           //     },
  //                           //
  //                           //
  //                           //
  //                           //     child: Container(
  //                           //       height: size.height * 0.3,
  //                           //       width: size.width *0.75,
  //                           //       color: Colors.yellow[100],
  //                           //       child: Padding(
  //                           //         padding: const EdgeInsets.all(8.0),
  //                           //         child: Column(
  //                           //           crossAxisAlignment: CrossAxisAlignment.center,
  //                           //           mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //                           //           children: [
  //                           //             Row(
  //                           //               mainAxisAlignment: MainAxisAlignment.spaceEvenly,
  //                           //               children: [
  //                           //                 IconButton(
  //                           //                   onPressed: () {
  //                           //                     setState(() {
  //                           //                       // assetsAudioPlayer;
  //                           //                     });
  //                           //                   },
  //                           //                   icon:  Icon(Icons.fast_rewind, size: size.width *0.1,),
  //                           //                 ),
  //                           //                 IconButton(
  //                           //                   onPressed: () {
  //                           //                     setState(() {
  //                           //                       isPlaying = !isPlaying;
  //                           //                     });
  //                           //                   },
  //                           //                   icon:  Icon(isPlaying == false ?Icons.play_circle_outline : Icons.pause , size: size.width *0.1,),
  //                           //                 ),
  //                           //                 IconButton(
  //                           //                   onPressed: () {
  //                           //                     setState(() {
  //                           //                       // assetsAudioPlayer.currentPosition.listen((event) {
  //                           //                       //   setState(() {
  //                           //                       //     event = Duration(hours:event.inHours , minutes:event.inMinutes , seconds: (event.inSeconds+10));
  //                           //                       //
  //                           //                       //   });
  //                           //                       // });
  //                           //                     });
  //                           //                   },
  //                           //                   icon:  Icon(Icons.fast_forward , size: size.width *0.1,),
  //                           //                 ),
  //                           //               ],
  //                           //             ),
  //                           //             Row(
  //                           //               mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //                           //               children: [
  //                           //                 Text(currentTime.toString()),
  //                           //
  //                           //                 Text(completeTime.toString())
  //                           //               ],
  //                           //             ),
  //                           //
  //                           //           ],
  //                           //         ),
  //                           //       ),
  //                           //     ),
  //                           //     url:
  //                           //        uploadUrl + widget.lesson.voiceLink! ,
  //                           //
  //                           //    // "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"
  //                           // ),
  //                           ),
  //                     ),
  //                   ),
  //                 )
  //               : const SizedBox(),
  //           Padding(
  //             padding: const EdgeInsets.all(8.0),
  //             child: Text(widget.lesson.lessonDescription!),
  //           ),
  //           Divider(),
  //           Text(
  //             "lesson Exams",
  //             style: subTitleStyle,
  //           ),
  //           exams.isNotEmpty
  //               ? SizedBox(
  //                   height: size.height * 0.3,
  //                   width: size.width,
  //                   child: SingleChildScrollView(
  //                     child: Column(
  //                         children: exams
  //                             .map((exam) => TextButton(
  //                                 onPressed: () {
  //                                   Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => DoExam(exam)));
  //                                 },
  //                                 child: Text(exam.examName!)))
  //                             .toList()),
  //                   ),
  //                 )
  //               : Text(
  //                   "There is no exams for this lesson",
  //                   style: subTitleStyle,
  //                 ),
  //         ],
  //       ),
  //     ),
  //   );
  // }
  Widget _button(String text, {void Function()? onPressed}) {
    return Padding(
    padding: const EdgeInsets.fromLTRB(20,0,5,5),
    child: OutlinedButton(
    onPressed: onPressed ?? () {},
    child: Icon(
    Icons.play_arrow,
    // style:  TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
    ),
    ),
    );
    }
}
