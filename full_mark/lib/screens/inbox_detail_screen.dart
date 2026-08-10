import 'dart:io';

import 'package:flutter/material.dart';
import 'package:full_mark/models/inbox_question_detail_model.dart';
import 'package:full_mark/widgets/image_preview.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:full_mark/providers/screen_providers/inbox_provider.dart';
import 'package:full_mark/widgets/app_loading_overlay.dart';
import 'package:full_mark/widgets/expanded_card.dart';
import 'package:full_mark/widgets/image_widget.dart';

import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/material_model.dart';
import '../widgets/app_drawer.dart';

class InboxDetailScreen extends StatefulWidget {
  const InboxDetailScreen({Key? key, required this.id}) : super(key: key);
  final int ?id;
  @override
  State<InboxDetailScreen> createState() => _InboxScreen();
}

class _InboxScreen extends State<InboxDetailScreen> {

  final _scaffoldKey = GlobalKey<ScaffoldState>();
  late InboxProvider provider;


  @override
  void initState() {
    //
    context.read<InboxProvider>().getInboxDetail(context, widget.id!);
    provider = context.read<InboxProvider>();
    super.initState();

    // provider.getInboxDetail(context,widget.id!);
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Selector<InboxProvider, bool>(
      selector: (context, provider) => provider.isDetailLoading,
      builder: (context, isDetailLoading, child) {
        return AppLoadingOverlay(
          isLoading: isDetailLoading,
          child: Scaffold(
            backgroundColor: mainColor,
            key: _scaffoldKey,
            appBar: AppBar(
              elevation: 0,

              backgroundColor: mainColor,
              title: const Text( "Inbox Details ",style: TextStyle(fontSize: 23),),
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
            body:   SingleChildScrollView(
              physics: AlwaysScrollableScrollPhysics(),
              child:Container(
                decoration:  BoxDecoration(color:mainColor),
                width: size.width,
                height: size.height,
                child: Container(
                  decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(40),
                        topRight: Radius.circular(40),
                      )),

                  child: Column(
                    children: [
                      Center(
                        child: Padding(
                          padding: const EdgeInsets.all(20.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text("Multi Message Sender",style: subTitleUpStyle,),
                              Card(
                                color: greyColor,
                                elevation: 0,
                                shape: RoundedRectangleBorder(
                                  side: BorderSide(
                                    color: mainColor,
                                  ),
                                  borderRadius: const BorderRadius.all(Radius.circular(12)),
                                ),
                                child:  Column(
                                  children: [

                                    ListTile(
                                      // leading: Icon(Icons.arrow_drop_down_circle),
                                      title:  Text(provider.questionDetail!.materialName??"",style: TextStyle(fontSize: 25,fontWeight: FontWeight.bold),),
                                      subtitle: Text(
                                        DateFormat.yMMMEd().format(  context.read<InboxProvider>().questionDetail!.sentDate ?? DateTime.now())??"",
                                        style: TextStyle(color: Colors.black.withOpacity(0.6),fontWeight: FontWeight.bold),
                                      ),
                                      // subtitle: Text(
                                      //   context.read<InboxProvider>().questionDetail!.sentDate.toString(),
                                      //   style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                      // ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(3.0),
                                      child: Text(
                                        context.read<InboxProvider>().questionDetail!.subject ?? "",
                                        style: TextStyle(color: Colors.black.withOpacity(0.9),fontWeight: FontWeight.bold),
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(3.0),
                                      child: Text(
                                        context.read<InboxProvider>().questionDetail!.body ?? "",
                                        style: TextStyle(color: Colors.black.withOpacity(0.9)),
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Row(
                                        children: [
                                          provider.questionDetail!.image ==null?Text(""):
                                          GestureDetector(
                                              onTap: (){
                                                Navigator.push(context, MaterialPageRoute(builder: (_) => ImagePreviewWidget(path: uploadUrl+provider.questionDetail!.image!,tag: uploadUrl+provider.questionDetail!.image!)));

                                              },
                                              child: Card(
                                                  color: greyColor,

                                                  shape: RoundedRectangleBorder(
                                                    side: BorderSide(
                                                      color: Colors.black,
                                                    ),
                                                    borderRadius: const BorderRadius.all(Radius.circular(15)),
                                                  ),
                                                  child: Padding(
                                                    padding: const EdgeInsets.fromLTRB(5.0,1,5,1),
                                                    child: Row(
                                                      children: [
                                                        Icon(Icons.photo,color: Colors.grey[700],),
                                                        Text("Image",style: TextStyle(color: Colors.grey,fontWeight: FontWeight.bold),)
                                                      ],
                                                    ),
                                                  )),)
                                          ,  provider.questionDetail!.image ==null?Text(""):
                                          GestureDetector(
                                              onTap: (){
                                                Navigator.push(context, MaterialPageRoute(builder: (_) => ImagePreviewWidget(path: uploadUrl+provider.questionDetail!.image!,tag: uploadUrl+provider.questionDetail!.image!)));

                                              },
                                              child:Card(
                                                  color: greyColor,

                                                  shape: RoundedRectangleBorder(
                                                    side: BorderSide(
                                                      color: Colors.black,
                                                    ),
                                                    borderRadius: const BorderRadius.all(Radius.circular(15)),
                                                  ),
                                                  child: Padding(
                                                    padding: const EdgeInsets.fromLTRB(5.0,1,5,1),
                                                    child: Row(
                                                      children: [
                                                        Icon(Icons.video_camera_back,color: Colors.grey[700],),
                                                        Text("Video",style: TextStyle(color: Colors.grey,fontWeight: FontWeight.bold),)
                                                      ],
                                                    ),
                                                  )),)
                                          ,  provider.questionDetail!.image ==null?Text(""):
              GestureDetector(
                  onTap: (){
                    Navigator.push(context, MaterialPageRoute(builder: (_) => ImagePreviewWidget(path: uploadUrl+provider.questionDetail!.image!,tag: uploadUrl+ provider.questionDetail!.image!)));

                  },
                  child: Container(
                    width:size.width*.22,
                    child: Card(
                        color: greyColor,

                        shape: RoundedRectangleBorder(
                          side: BorderSide(
                            color: Colors.black,
                          ),
                          borderRadius: const BorderRadius.all(Radius.circular(15)),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.fromLTRB(5.0,1,5,1),
                          child: Row(
                            children: [
                              Icon(Icons.record_voice_over_outlined,color: Colors.grey[700],),
                              Text("Record",style: TextStyle(color: Colors.grey,fontWeight: FontWeight.bold),)
                            ],
                          ),
                        )),
                  )),

                                        ],
                                      ),
                                    )
                                  ,
                                    // Padding(
                                    //   padding: const EdgeInsets.all(16.0),
                                    //   child: ImageWidget(
                                    //     path: uploadUrl+provider.questionDetail!.image,
                                    //     width: 100,
                                    //     // height: 20,
                                    //   ),
                                    // ),
                                    // ButtonBar(
                                    //   alignment: MainAxisAlignment.start,
                                    //   children: [
                                    //     Padding(
                                    //       padding: const EdgeInsets.all(16.0),
                                    //       child: Text(
                                    //         provider.questionDetail!.studentName,
                                    //         style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                    //       ),
                                    //     ),
                                    //
                                    //   ],
                                    // ),
                                    // Text(
                                    //   provider.questionDetail!.teacherName,
                                    //   style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                    // ),

                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      )

,
                      provider.questionDetail!.inboxReplys.isEmpty?_buildBar(size,"لا يوجد ردود"):
                      _buildBar(size,"عدد الردود:${provider.questionDetail!.inboxReplys.length.toString()}"),
                      // Text("عدد الردود"),
                      // Text( provider.questionDetail!.inboxReplys.length.toString()),
                      provider.questionDetail!.inboxReplys.isEmpty?  Text("لا يوجد ردود"):
                      SizedBox(
                        height: size.height*.45,
                        child: ListView.separated(
                          itemCount:  context.read<InboxProvider>().questionDetail!.inboxReplys.length,
                          padding: const EdgeInsets.all(15.0),
                          separatorBuilder: (_, __) => const SizedBox(height: 10),
                          itemBuilder: (context, index) {
                            final question = context.read<InboxProvider>().questionDetail!.inboxReplys[index];
                            return Card(
                              clipBehavior: Clip.antiAlias,
                              color: greyColor,
                              elevation: 0,
                              shape: RoundedRectangleBorder(
                                side: BorderSide(
                                  color: mainColor,
                                ),
                                borderRadius: const BorderRadius.all(Radius.circular(12)),
                              ),
                              child: Column(

                                children: [
                                  ListTile(
                                    // leading: Icon(Icons.arrow_drop_down_circle),
                                    // title:  Text(question.body??""),
                                    title: Text(
                                      DateFormat.yMMMEd().format(   question.sentDate ?? DateTime.now())??"",


                                      style: TextStyle(color: Colors.black.withOpacity(0.6)),
                                    ),

                                  ),   Padding(
                                    padding: const EdgeInsets.fromLTRB(5.0,0,0,10),
                                    child: Text(
                                      question.body ?? "",
                                      style: TextStyle(color: Colors.black.withOpacity(0.6),fontSize: 20,fontWeight: FontWeight.bold
                                      ),
                                    ),
                                  ), question.image ==null?Text(""):
                                  GestureDetector(
                                      onTap: (){
                                        Navigator.push(context, MaterialPageRoute(builder: (_) => ImagePreviewWidget(path: uploadUrl+ question.image!,tag: uploadUrl+ question.image!)));

                                      },
                                      child: Container(
                                        width:size.width*.2,
                                        child: Card(
                                            color: greyColor,

                                            shape: RoundedRectangleBorder(
                                              side: BorderSide(
                                                color: Colors.black,
                                              ),
                                              borderRadius: const BorderRadius.all(Radius.circular(15)),
                                            ),
                                            child: Padding(
                                              padding: const EdgeInsets.fromLTRB(5.0,1,5,1),
                                              child: Row(
                                                children: [
                                                  Icon(Icons.photo,color: Colors.grey[700],),
                                                  Text("Image",style: TextStyle(color: Colors.grey,fontWeight: FontWeight.bold),)
                                                ],
                                              ),
                                            )),
                                      )),
                                  // question.image ==""?Text(""): Padding(
                                  //   padding: const EdgeInsets.all(5.0),
                                  //   child: ImageWidget(
                                  //     path: uploadUrl+question.image,
                                  //     width: size.width*.1,
                                  //   ),
                                  // ),


                                ],
                              ),
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
                        ),
                      )
                    ],
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

                      return Center(
                        child: SingleChildScrollView(
                          child: Dialog(
                            child: Padding(
                              padding: const EdgeInsets.all(10.0),
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  AspectRatio(
                                    aspectRatio: 1,
                                    child: Container(
                                      decoration: BoxDecoration(
                                          color: Colors.black12,
                                          borderRadius: BorderRadius.circular(10.0)
                                      ),
                                      child: StatefulBuilder(
                                        builder: (context, state) {
                                          if (context.read<InboxProvider>().replyImage == null) {
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
                                                                  context.read<InboxProvider>().replyImage = (await picker.pickImage(source: ImageSource.camera))?.path;
                                                                  state(() {});
                                                                  File imageFile = File( context.read<InboxProvider>().replyImage!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                  if (imageFile != null) {
                                                                    await context.read<InboxProvider>().uploadImage(imageFile);
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
                                                                  context.read<InboxProvider>().replyImage = (await picker.pickImage(source: ImageSource.gallery))?.path;
                                                                  state(() {});
                                                                  File imageFile = File( context.read<InboxProvider>().replyImage!);// احضر هنا ملف الصورة من الجهاز (مثال: await ImagePicker.pickImage(...))
                                                                  if (imageFile != null) {
                                                                    await context.read<InboxProvider>().uploadImage(imageFile);
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
                                                    path: context.read<InboxProvider>().replyImage!,
                                                    fit: BoxFit.cover,
                                                  ),
                                                ),
                                                Positioned(
                                                  top: 0,
                                                  right: 0,
                                                  child: IconButton(
                                                    icon: Icon(Icons.clear),
                                                    onPressed: () {
                                                      context.read<InboxProvider>().replyImage = null;
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
                                  Form(
                                    key: form,
                                    child: Column(
                                      children: [
                                        // Padding(
                                        //   padding: const EdgeInsets.all(8.0),
                                        //   child: TextFormField(
                                        //     controller: context.read<InboxProvider>().subjectReplyText,
                                        //     // onChanged: (value){
                                        //     //   provider.subjectText.text=value;
                                        //     // },
                                        //     decoration: InputDecoration(
                                        //       border: border,
                                        //       enabledBorder: border,
                                        //       errorBorder: border.copyWith(borderSide: const BorderSide(color: Colors.red)),
                                        //       focusedBorder: border,
                                        //       hintText: "Write message here...",
                                        //     ),
                                        //     validator: (value) {
                                        //       if (value == null || value.isEmpty) {
                                        //         return "Required";
                                        //       }
                                        //       return null;
                                        //     },
                                        //     maxLines: 1,
                                        //   ),
                                        // ),
                                        TextFormField(
                                          controller: context.read<InboxProvider>().bodyReplyText,
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
                                            hintText: "Write  body here...",
                                          ),
                                          validator: (value) {
                                            if (value == null || value.isEmpty) {
                                              return "Required";
                                            }
                                            return null;
                                          },
                                          maxLines: 4,
                                        ),
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
                                  const SizedBox(height: 10),
                                  ElevatedButton(
                                      onPressed: () {

                                        setState(() {
                                          // provider.uploadImage(provider.image);
                                          provider.sendInboxReplyRequest(context,provider.questionDetail!.id!);
                                          // Navigator.pop(context);
                                        });

                                        // if (form.currentState!.validate()) {


                                        // }
                                      },
                                      style: ElevatedButton.styleFrom(
                                          backgroundColor: mainColor
                                      ),
                                      child: Center(child: Text("Send"))
                                  )
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
                // height: size.height * 0.025,
                child: FittedBox(
                    child: Text(
                      title,
                      style: TextStyle(fontWeight:FontWeight.bold ,
                          color:Colors.white),
                    )))),
      ),
    );
  }

}
