
import 'dart:async';
import 'package:desktop_screen/providers/screen_providers/inbox_provider.dart';
import 'package:desktop_screen/screens/inbox_detail_screen.dart';
import 'package:desktop_screen/widgets/app_loading_overlay.dart';
import 'package:desktop_screen/widgets/image_widget.dart';
import 'package:path/path.dart' as path;
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:intl/intl.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';

// import 'package:audio_recorder/audio_recorder.dart';
import 'package:flutter_sound/flutter_sound.dart';
import 'package:video_player/video_player.dart';
import 'dart:io';

import '../constants/colors.dart';
import '../constants/text_styles.dart';
import '../models/material_model.dart';
import '../widgets/app_drawer.dart';

// import 'dart:html' as html;
// import 'dart:ui' as ui;

class InboxScreen extends StatefulWidget {
  const InboxScreen({Key? key}) : super(key: key);

  @override
  State<InboxScreen> createState() => _InboxScreen();
}

class _InboxScreen extends State<InboxScreen> {
  FlutterSoundRecorder ?_myRecorder;
  // final audioPlayer = AssetsAudioPlayer();
   String ?filePath;
  bool _play = false;
  String _recorderTxt = '00:00:00';
  final _scaffoldKey = GlobalKey<ScaffoldState>();
  late InboxProvider provider;
  // late html.VideoElement _preview;
  late VideoPlayerController _controller;

  @override
  void initState() {
    super.initState();
    provider = context.read<InboxProvider>();
    provider.getMaterials(context);
    startIt();
    _controller = VideoPlayerController.networkUrl(Uri.parse(
        provider.video??""))
      ..initialize().then((_) {
        // Ensure the first frame is shown after the video is initialized, even before the play button has been pressed.
        setState(() {});
      });


  }
  void startIt() async {
    filePath = '/sdcard/Download/temp.wav';
    _myRecorder = FlutterSoundRecorder();

    await _myRecorder!.openAudioSession(
        focus: AudioFocus.requestFocusAndStopOthers,
        category: SessionCategory.playAndRecord,
        mode: SessionMode.modeDefault,
        device: AudioDevice.speaker);
    await _myRecorder?.setSubscriptionDuration(Duration(milliseconds: 10));
    await initializeDateFormatting();

    await Permission.microphone.request();
    await Permission.storage.request();
    await Permission.manageExternalStorage.request();
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    ElevatedButton buildElevatedButton({IconData? icon, Color? iconColor, Function ?f}) {
      return ElevatedButton.icon(
        style: ElevatedButton.styleFrom(
          padding: EdgeInsets.all(5.0),
          side: BorderSide(
            color: Colors.orange,
            width: 3.0,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(15),
          ),
          primary: Colors.white,
          elevation: 10.0,
        ),
        onPressed:()=> f!,
        icon: Icon(
          icon,
          color: iconColor,
          size: 35.0,
        ),
        label: Text(''),
      );
    }
    Future<void> record() async {

      Directory dir = Directory(path.dirname(filePath!));
      if (!dir.existsSync()) {
        dir.createSync();
      }
      _myRecorder!.openAudioSession();
      await _myRecorder!.startRecorder(
        toFile: filePath,
        codec: Codec.pcm16WAV,
      );

      StreamSubscription _recorderSubscription = _myRecorder!.onProgress!.listen((e) {
        var date = DateTime.fromMillisecondsSinceEpoch(e.duration.inMilliseconds, isUtc: true);
        var txt = DateFormat('mm:ss:SS', 'en_GB').format(date);

        setState(() {
          _recorderTxt = txt.substring(0, 8);
        });
      });
      _recorderSubscription.cancel();
    }

    Future<String?> stopRecord() async {
      _myRecorder!.closeAudioSession();
      return await _myRecorder!.stopRecorder();
    }

    // Future<void> startPlaying() async {
    //   audioPlayer.open(
    //     Audio.file(filePath!),
    //     autoStart: true,
    //     showNotification: true,
    //   );
    // }
    //
    // Future<void> stopPlaying() async {
    //   audioPlayer.stop();
    // }
    return Selector<InboxProvider, bool>(
      selector: (context, provider) => provider.isLoading,
      builder: (context, isLoading, child) {
        return AppLoadingOverlay(
          isLoading: isLoading,
          child: Scaffold(
            backgroundColor: mainColor,
            key: _scaffoldKey,
            appBar: AppBar(
              elevation: 0,

              backgroundColor: mainColor,
              title: const Text( "Inbox",style: TextStyle(fontSize: 23),),
              centerTitle: true,
              leading: IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
              actions: [
                IconButton(
                  onPressed: () {
                    _scaffoldKey.currentState!.openDrawer();
                  },
                  icon: Icon(
                    Icons.menu,
                    color: Colors.white,
                  ),
                )
              ],
            ),
            drawer: const DrawerWidget(),
            body: Center(
              child: Container(
                decoration:  BoxDecoration(color:mainColor),
                width: size.width*.4,
                height: size.height*.9,
                child: Center(
                  child: Container(
                    decoration: const BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.all(
                         Radius.circular(40),

                        )),

                    child: Center(
                      child: Column(
                        children: [
                          Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: Selector<InboxProvider, MaterialModel?>(
                                selector: (_, provider) => provider.selectedMaterial,
                                builder: (context, selectedMaterial, _) {
                                  final materials = provider.materials;
                                  return Column(
                                     crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Text("please select material name",style: subTitleUpStyle,),
                                      ),
                                      DropdownButtonFormField<MaterialModel>(
                                        items: materials.map((e) {
                                          return DropdownMenuItem(child: Text(e.name!), value: e);
                                        }).toList(),
                                        onChanged: (value) {
                                          provider.selectMaterial(context, value);
                                        },
                                        value: selectedMaterial,
                                        hint: const Text("Choose material"),
                                        isExpanded: true,
                                        decoration: const InputDecoration(
                                          isDense: true,
                                          border: OutlineInputBorder(),
                                        ),
                                      ),
                                    ],
                                  );
                                }),
                          ),
                          Expanded(
                            child: Builder(
                                builder: (context) {
                                  if (provider.error != null) {
                                    return Center(
                                      child: Column(
                                        children: [
                                          Text("لا يوجد أسئله", textAlign: TextAlign.center),
                                          GestureDetector(
                                              onTap: () => provider.getQuestions(context),
                                              child: Text("اعاده التحميل", style: TextStyle(decoration: TextDecoration.underline)))
                                        ],
                                      ),
                                    );
                                  }
                                  if (provider.questions.isEmpty && !provider.isLoading) {
                                    return const Center(
                                      child: Text("There is no questions for this material",textAlign: TextAlign.center),
                                    );
                                  }
                                  return ListView.separated(
                                    itemCount: provider.questions.length,
                                    padding: const EdgeInsets.all(15.0),
                                    separatorBuilder: (_, __) => const SizedBox(height: 10),
                                    itemBuilder: (context, index) {
                                      final question = provider.questions[index];
                                      return GestureDetector(
                                        onTap: (){
                                          Navigator.push(context, MaterialPageRoute(builder: (_)=> InboxDetailScreen(id:question.id)));

                                        },
                                        child:
                                        Center(
                                          child: Card(
                                            elevation: 0,
                                            shape: RoundedRectangleBorder(
                                              side: BorderSide(
                                                color: mainColor,
                                              ),
                                              borderRadius: const BorderRadius.all(Radius.circular(12)),
                                            ),
                                            child:   Column(
                                              children: [
                                                ListTile(
                                                  // leading: Icon(Icons.arrow_drop_down_circle),
                                                  title:  Text(question.materialName??"",style: TextStyle(fontSize: 25,fontWeight: FontWeight.bold),),
                                                  subtitle: Text(
                                                      DateFormat.yMMMEd().format( question.sentDate!)??"",
                                                    style: TextStyle(color: Colors.black.withOpacity(0.6),fontWeight: FontWeight.bold),
                                                  ),
                                                ),
                                                Padding(
                                                  padding: const EdgeInsets.all(16.0),
                                                  child: Text(
                                                    question.subject??"",
                                                    style: TextStyle(color: Colors.black.withOpacity(0.8),fontWeight: FontWeight.bold),
                                                  ),
                                                ),

                                              ],
                                            ),
                                          ),
                                        )
                                        // Card(
                                        //   clipBehavior: Clip.antiAlias,
                                        //   child: Column(
                                        //     children: [
                                        //       ListTile(
                                        //         // leading: Icon(Icons.arrow_drop_down_circle),
                                        //         title:  Text(question.materialName??""),
                                        //         subtitle: Text(
                                        //           question.sentDate.toString()??"",
                                        //           style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                        //         ),
                                        //       ),
                                        //       Padding(
                                        //         padding: const EdgeInsets.all(16.0),
                                        //         child: Text(
                                        // question.subject??"",
                                        //           style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                        //         ),
                                        //       ),
                                        //
                                        //     ],
                                        //   ),
                                        // ),
                                      );
                                      //   ExpandedCard(
                                      //     trailing:Text(
                                      //       question.sentDate.toString()
                                      //         ),
                                      //   // leading: Text(question.sentDate.toString()??""),
                                      //   title: Text(question.subject??""),
                                      //   subtitle:Text(question.materialName??""),
                                      //
                                      //   // imagePath: question.image,
                                      //   children: [
                                      //     // Padding(
                                      //     //   padding: const EdgeInsets.all(10),
                                      //     //   child: Align(alignment: AlignmentDirectional.centerStart, child: Text(question.teacherAnswersDto!.text??"")),
                                      //     // ),
                                      //     // for(final x in question.teacherAnswersDto!.teacherAnswerDetailDtoList)
                                      //     //   Column(
                                      //     //     crossAxisAlignment: CrossAxisAlignment.start,
                                      //     //     children: [
                                      //     //       ImageWidget(path: x.image??"", width: double.infinity, height: 200, fit: BoxFit.cover,),
                                      //     //       Padding(
                                      //     //         padding: const EdgeInsetsDirectional.fromSTEB(10, 5, 10, 10),
                                      //     //         child: Text(x.description??""),
                                      //     //       ),
                                      //     //     ],
                                      //     //   ),
                                      //   ],
                                      // );
                                    },
                                  );
                                }
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
            floatingActionButton: FloatingActionButton(
              onPressed: () {
                showDialog(
                    context: context,
                    builder: (context) {
                      final border = OutlineInputBorder();
                      final form = GlobalKey<FormState>();

                      return SizedBox(
                        height: size.height*.3,
                        width: size.width*.5,
                        child: Dialog(
                          child: Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: SingleChildScrollView(
                              physics: AlwaysScrollableScrollPhysics(),
                              child: Column(
                                // mainAxisSize: MainAxisSize.min,
                                children: [
                                  Form(
                                    key: form,
                                    child: Column(
                                      children: [
                                        Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: TextFormField(
                                            controller: provider.subjectText,
                                            // onChanged: (value){
                                            //   provider.subjectText.text=value;
                                            // },
                                            decoration: InputDecoration(
                                              border: border,
                                              enabledBorder: border,
                                              errorBorder: border.copyWith(borderSide: const BorderSide(color: Colors.red)),
                                              focusedBorder: border,
                                              hintText: "Write subject here...",
                                            ),
                                            validator: (value) {
                                              if (value == null || value.isEmpty) {
                                                return "Required";
                                              }
                                              return null;
                                            },
                                            maxLines: 1,
                                          ),
                                        ),
                                        Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: TextFormField(
                                            controller: provider.bodyText,
                                            // onChanged: (value){
                                            //   setState(() {
                                            //
                                            //     provider.bodyText.text=value;
                                            //
                                            //   });
                                            // },
                                            decoration: InputDecoration(
                                              border: border,
                                              enabledBorder: border,
                                              errorBorder: border.copyWith(borderSide: const BorderSide(color: Colors.red)),
                                              focusedBorder: border,
                                              hintText: "Write your question here...",
                                            ),
                                            validator: (value) {
                                              if (value == null || value.isEmpty) {
                                                return "Required";
                                              }
                                              return null;
                                            },
                                            maxLines: 4,
                                          ),
                                        ),
                                        const SizedBox(height: 10),
                                        AspectRatio(
                                          aspectRatio: 1,
                                          child: Container(
                                            decoration: BoxDecoration(
                                                color: Colors.black12,
                                                borderRadius: BorderRadius.circular(10.0)
                                            ),
                                            child: StatefulBuilder(
                                              builder: (context, state) {
                                                if (provider.image == null) {
                                                  return GestureDetector(
                                                    behavior: HitTestBehavior.opaque,
                                                    onTap: () {
                                                      showModalBottomSheet(
                                                          context: context,
                                                          builder: (context) {
                                                            return Container(
                                                              padding: const EdgeInsets.all(20),
                                                              height: 100,
                                                              child: Row(
                                                                children: [
                                                                  Expanded(
                                                                    child: GestureDetector(
                                                                      onTap: () async {
                                                                        final picker = ImagePicker();
                                                                        provider.image = (await picker.pickImage(source: ImageSource.camera))?.path;
                                                                        state(() {});
                                                                        File imageFile = File( provider.image!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                        if (imageFile != null) {
                                                                          await provider.uploadImage(imageFile);
                                                                        }
                                                                        Navigator.pop(context);
                                                                      },
                                                                      child: Container(
                                                                        decoration: BoxDecoration(
                                                                            border: Border.all()
                                                                        ),
                                                                        child: Center(
                                                                          child: Icon(Icons.camera_alt_outlined),
                                                                        ),
                                                                      ),
                                                                    ),
                                                                  ),
                                                                  const SizedBox(width: 10,),
                                                                  Expanded(
                                                                    child: GestureDetector(
                                                                      onTap: () async {
                                                                        final picker = ImagePicker();
                                                                        // final picker = ImagePicker();
                                                                        provider.image = (await picker.pickImage(source: ImageSource.gallery))?.path;
                                                                        state(() {});
                                                                        File imageFile = File( provider.image!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                        if (imageFile != null) {
                                                                          await provider.uploadImage(imageFile);
                                                                        }
                                                                        // provider.image = (await picker.pickImage(source: ImageSource.gallery))?.path;
                                                                        // state(() {});
                                                                        Navigator.pop(context);
                                                                      },
                                                                      child: Container(
                                                                        decoration: BoxDecoration(
                                                                            border: Border.all()
                                                                        ),
                                                                        child: const Center(
                                                                          child: Icon(Icons.photo_outlined),
                                                                        ),
                                                                      ),
                                                                    ),
                                                                  ),
                                                                  // if(image != null)
                                                                  //   const SizedBox(width: 10,),
                                                                  // if(image != null)
                                                                  //   Expanded(
                                                                  //   child: GestureDetector(
                                                                  //     onTap: () async {
                                                                  //       image = null;
                                                                  //       state((){});
                                                                  //       Navigator.pop(context);
                                                                  //     },
                                                                  //     child: Container(
                                                                  //       decoration: BoxDecoration(
                                                                  //           border: Border.all()
                                                                  //       ),
                                                                  //       child: const Center(
                                                                  //         child: Icon(Icons.delete_forever_rounded),
                                                                  //       ),
                                                                  //     ),
                                                                  //   ),
                                                                  // ),
                                                                ],
                                                              ),
                                                            );
                                                          }
                                                      );
                                                    },
                                                    child: Center(
                                                      child: Column(
                                                        mainAxisSize: MainAxisSize.min,
                                                        children: [
                                                          Icon(Icons.add_a_photo_outlined),
                                                          const SizedBox(height: 10),
                                                          Text("Add image\n(optional)", textAlign: TextAlign.center,)
                                                        ],
                                                      ),
                                                    ),
                                                  );
                                                } else {
                                                  return Stack(
                                                    children: [
                                                      Positioned.fill(
                                                        child: ImageWidget(
                                                          path: provider.image!,
                                                          fit: BoxFit.cover,
                                                        ),
                                                      ),
                                                      Positioned(
                                                        top: 0,
                                                        right: 0,
                                                        child: IconButton(
                                                          icon: Icon(Icons.clear),
                                                          onPressed: () {
                                                            provider.image = null;
                                                            state(() {});
                                                          },
                                                        ),
                                                      )
                                                    ],
                                                  );
                                                }
                                              },
                                            ),
                                          ),
                                        ),
                                        const SizedBox(height: 10),
                                        AspectRatio(
                                          aspectRatio: 1,
                                          child: Container(
                                            decoration: BoxDecoration(
                                                color: Colors.black12,
                                                borderRadius: BorderRadius.circular(10.0)
                                            ),
                                            child: StatefulBuilder(
                                              builder: (context, state) {
                                                if (provider.video == null) {
                                                  return GestureDetector(
                                                    behavior: HitTestBehavior.opaque,
                                                    onTap: () {
                                                      showModalBottomSheet(
                                                          context: context,
                                                          builder: (context) {
                                                            return Container(
                                                              padding: const EdgeInsets.all(20),
                                                              height: 100,
                                                              child: Row(
                                                                children: [
                                                                  Expanded(
                                                                    child: GestureDetector(
                                                                      onTap: () async {
                                                                        final picker = ImagePicker();
                                                                        provider.video = (await picker.pickVideo(source: ImageSource.camera))?.path;
                                                                        state(() {});
                                                                        File imageFile = File( provider.video!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                        if (imageFile != null) {
                                                                          await provider.uploadVideo(imageFile);
                                                                        }
                                                                        Navigator.pop(context);
                                                                      },
                                                                      child: Container(
                                                                        decoration: BoxDecoration(
                                                                            border: Border.all()
                                                                        ),
                                                                        child: Center(
                                                                          child: Icon(Icons.video_camera_back),
                                                                        ),
                                                                      ),
                                                                    ),
                                                                  ),
                                                                  const SizedBox(width: 10,),
                                                                  Expanded(
                                                                    child: GestureDetector(
                                                                      onTap: () async {
                                                                        final picker = ImagePicker();
                                                                        // final picker = ImagePicker();
//                                                                   final XFile? galleryVideo =
//                                                                   await picker.pickVideo(source: ImageSource.gallery);
// // Capture a video.
//                                                                   final XFile? cameraVideo = await picker.pickVideo(source: ImageSource.camera)
                                                                        provider.video = (await picker.pickVideo(source: ImageSource.gallery))?.path;
                                                                        state(() {});
                                                                        File imageFile = File( provider.video!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                        if (imageFile != null) {
                                                                          await provider.uploadVideo(imageFile);
                                                                        }
                                                                        // provider.image = (await picker.pickImage(source: ImageSource.gallery))?.path;
                                                                        // state(() {});
                                                                        Navigator.pop(context);
                                                                      },
                                                                      child: Container(
                                                                        decoration: BoxDecoration(
                                                                            border: Border.all()
                                                                        ),
                                                                        child: const Center(
                                                                          child: Icon(Icons.photo_outlined),
                                                                        ),
                                                                      ),
                                                                    ),
                                                                  ),
                                                                  // if(image != null)
                                                                  //   const SizedBox(width: 10,),
                                                                  // if(image != null)
                                                                  //   Expanded(
                                                                  //   child: GestureDetector(
                                                                  //     onTap: () async {
                                                                  //       image = null;
                                                                  //       state((){});
                                                                  //       Navigator.pop(context);
                                                                  //     },
                                                                  //     child: Container(
                                                                  //       decoration: BoxDecoration(
                                                                  //           border: Border.all()
                                                                  //       ),
                                                                  //       child: const Center(
                                                                  //         child: Icon(Icons.delete_forever_rounded),
                                                                  //       ),
                                                                  //     ),
                                                                  //   ),
                                                                  // ),
                                                                ],
                                                              ),
                                                            );
                                                          }
                                                      );
                                                    },
                                                    child: Center(
                                                      child: Column(
                                                        mainAxisSize: MainAxisSize.min,
                                                        children: [
                                                          Icon(Icons.video_camera_back),
                                                          const SizedBox(height: 10),
                                                          Text("Add video\n(optional)", textAlign: TextAlign.center,)
                                                        ],
                                                      ),
                                                    ),
                                                  );
                                                } else {
                                                  return Card(
                                                    color: greyColor,
                                                    elevation: 0,
                                                    shape: RoundedRectangleBorder(
                                                      side: BorderSide(
                                                        color: mainColor,
                                                      ),
                                                      borderRadius: const BorderRadius.all(Radius.circular(12)),
                                                    ),
                                                    child: Stack(
                                                      children: [
                                                        // Center(
                                                        // child: _controller.value.isInitialized
                                                        //   ? AspectRatio(
                                                        //   aspectRatio: _controller.value.aspectRatio,
                                                        //   child: VideoPlayer(_controller),
                                                        // )
                                                        //       : Container(),
                                                        // )
                                                        //  ,
                                                        //         floatingActionButton: FloatingActionButton(
                                                        // onPressed: () {
                                                        // setState(() {
                                                        // _controller.value.isPlaying
                                                        // ? _controller.pause()
                                                        //     : _controller.play();
                                                        // });
                                                        // },
                                                        // child: Icon(
                                                        // _controller.value.isPlaying ? Icons.pause : Icons.play_arrow,
                                                        // ),
                                                        // )
                                                        Center(
                                                          child: Text(
                                                              "video uploaded"

                                                          ),
                                                        ),

                                                        Positioned(
                                                          top: 0,
                                                          right: 0,
                                                          child: IconButton(
                                                            icon: Icon(Icons.clear),
                                                            onPressed: () {
                                                              provider.video = null;
                                                              state(() {});
                                                            },
                                                          ),
                                                        )
                                                      ],
                                                    ),
                                                  );
                                                }
                                              },
                                            ),
                                          ),
                                        ),
                                        const SizedBox(height: 10),
                                        // Material(
                                        //   child: Container(
                                        //     padding: EdgeInsets.only(top: 0, bottom: 12, left: 5, right: 5),
                                        //     child: LongPressDraggable(
                                        //       child: Icon(
                                        //         Icons.mic,
                                        //         color: Colors.black54,
                                        //       ),
                                        //       onDragStarted: () async {
                                        //         stopWatchTimer.onExecute.add(StopWatchExecute.reset);
                                        //         stopWatchTimer.onExecute.add(StopWatchExecute.start);
                                        //         seconds = 0;
                                        //         minutes = 0;
                                        //         setState(() {
                                        //           showMic = true;
                                        //         });
                                        //         isRecording = true;
                                        //         bool result = await Record().hasPermission();
                                        //         if (result) {
                                        //           Directory tempDir = await getTemporaryDirectory();
                                        //           File filePath = File('${tempDir.path}/audio.mp3');
                                        //           audioPath = filePath.path;
                                        //           await Record().start(
                                        //             path: filePath.path, // required
                                        //             encoder: AudioEncoder.AAC, // by default
                                        //             bitRate: 128000, // by default
                                        //             samplingRate: 44100, // by default
                                        //           );
                                        //         }
                                        //       },
                                        //       onDragEnd: (a) async {
                                        //         if(a.offset.dx<width-width/5){
                                        //           await Record().stop();
                                        //           AudioPlayer audioPlayer = AudioPlayer(
                                        //             mode: PlayerMode.MEDIA_PLAYER,
                                        //           );
                                        //           int result = await audioPlayer.play(audioPath,
                                        //               isLocal: true, stayAwake: true);
                                        //           stopWatchTimer.onExecute.add(StopWatchExecute.stop);
                                        //           setState(() {
                                        //             showMic = false;
                                        //             isRecording = false;
                                        //           });
                                        //
                                        //         }else{
                                        //           setState(() {
                                        //             showMic = false;
                                        //             isRecording = false;
                                        //           });
                                        //           StaticDataAndFunctions.customToastMessage(
                                        //               "Record removed!");
                                        //         }
                                        //
                                        //       },
                                        //       feedback: Icon(
                                        //         Icons.delete,
                                        //         color: Colors.black54,
                                        //       ),
                                        //     ),
                                        //   ),
                                        //   color: Colors.white,
                                        // ),
                                        // Center(
                                        //   child: Column(
                                        //     mainAxisAlignment: MainAxisAlignment.start,
                                        //     children: <Widget>[
                                        //       Container(
                                        //         height: 200.0,
                                        //         decoration: BoxDecoration(
                                        //           gradient: LinearGradient(
                                        //             begin: Alignment.topCenter,
                                        //             end: Alignment.bottomCenter,
                                        //             colors: [Color.fromARGB(255, 2, 199, 226), Color.fromARGB(255, 6, 75, 210)],
                                        //           ),
                                        //           borderRadius: BorderRadius.vertical(
                                        //             bottom: Radius.elliptical(MediaQuery.of(context).size.width, 100.0),
                                        //           ),
                                        //         ),
                                        //         child: Center(
                                        //           child: Text(
                                        //             _recorderTxt,
                                        //             style: TextStyle(fontSize: 70),
                                        //           ),
                                        //         ),
                                        //       ),
                                        //       SizedBox(
                                        //         height: 20,
                                        //       ),
                                        //       Row(
                                        //         mainAxisAlignment: MainAxisAlignment.center,
                                        //         children: <Widget>[
                                        //           GestureDetector(
                                        //               onTap:(){
                                        //                 record();
                                        //               },
                                        //               child: Icon( Icons.mic,)
                                        //
                                        //             // Icon(
                                        //             //   icon: Icons.mic,
                                        //             //   // iconColor: Colors.red,
                                        //             //   // f: record,
                                        //             // ),
                                        //           ),
                                        //           SizedBox(
                                        //             width: 30,
                                        //           ),
                                        //           GestureDetector(
                                        //               onTap: (){
                                        //                 stopRecord();
                                        //               },
                                        //               child: Icon(Icons.stop)),
                                        //           // buildElevatedButton(
                                        //           //   icon: Icons.stop,
                                        //           //   iconColor: Colors.black,
                                        //           //   f: stopRecord,
                                        //           // ),
                                        //         ],
                                        //       ),
                                        //       SizedBox(
                                        //         height: 20,
                                        //       ),
                                        //       Row(
                                        //         mainAxisAlignment: MainAxisAlignment.center,
                                        //         children: <Widget>[
                                        //           GestureDetector(
                                        //               onTap:(){startPlaying();},
                                        //               child: Icon(Icons.play_arrow))
                                        //           // , buildElevatedButton(
                                        //           //    icon: Icons.play_arrow,
                                        //           //    iconColor: Colors.black,
                                        //           //    f: startPlaying,
                                        //           //  ),
                                        //           , SizedBox(
                                        //             width: 30,
                                        //           ),
                                        //           GestureDetector(
                                        //               onTap: (){
                                        //                 stopPlaying();
                                        //               },
                                        //               child: Icon(Icons.stop))
                                        //           // ,buildElevatedButton(
                                        //           //   icon: Icons.stop,
                                        //           //   iconColor: Colors.black,
                                        //           //   f: stopPlaying,
                                        //           // ),
                                        //         ],
                                        //       ),
                                        //       SizedBox(
                                        //         height: 20,
                                        //       ),
                                        //       ElevatedButton.icon(
                                        //         style: ElevatedButton.styleFrom(
                                        //           elevation: 10.0,
                                        //         ),
                                        //         onPressed: () {
                                        //           setState(() {
                                        //             _play = !_play;
                                        //           });
                                        //           if (_play) startPlaying();
                                        //           if (!_play) stopPlaying();
                                        //         },
                                        //         icon: _play
                                        //             ? Icon(
                                        //           Icons.stop,
                                        //         )
                                        //             : Icon(Icons.play_arrow),
                                        //         label: _play
                                        //             ? Text(
                                        //           "Stop Playing",
                                        //           style: TextStyle(
                                        //             fontSize: 25,
                                        //           ),
                                        //         )
                                        //             : Text(
                                        //           "Start Playing",
                                        //           style: TextStyle(
                                        //             fontSize: 25,
                                        //           ),
                                        //         ),
                                        //       ),
                                        //     ],
                                        //   ),
                                        // ),

                                        SizedBox(
                                          width: size.width*.5,
                                          child: ElevatedButton(
                                              onPressed: () {

                                                setState(() {
                                                  // provider.uploadImage(provider.image);
                                                  provider.sendInboxRequest(context);
                                                });

                                                // if (form.currentState!.validate()) {


                                                // }
                                              },
                                              style: ElevatedButton.styleFrom(
                                                  primary: mainColor
                                              ),
                                              child: Center(child: Text("Send"))
                                          ),
                                        )
                                      ],
                                    ),
                                  ),
                                  // Center(
                                  //   child: ElevatedButton(
                                  //     onPressed: () async {
                                  //
                                  //     },
                                  //     child: Text('اختر صورة للرفع'),
                                  //   ),
                                  // ),

                                ],
                              ),
                            ),
                          ),
                        ),
                      );
                    }
                );
              },
              child: const Icon(Icons.add),
              backgroundColor: Colors.blue,
            ),
          ),
        );
      },
    );
  }

}
