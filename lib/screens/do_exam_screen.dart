import 'dart:async';

import 'package:desktop_screen/widgets/regular_dark_button.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import '../constants/colors.dart';
import '../constants/helper.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/exam_model.dart';
import '../providers/data_handler_provider.dart';
import '../screens/home_screen.dart';
import 'package:provider/provider.dart';



class DoExam extends StatefulWidget {
  const DoExam(this.exam , {Key? key}) : super(key: key);
  final Exam exam ;
  @override
  _DoExamState createState() => _DoExamState();
}

class _DoExamState extends State<DoExam> {
  int? timerPerMin;
  int seconds = 60 ;
  late Timer timer ;
   Exam? exam ;
   int index = 0 ;
  @override
  void initState() {
    // TODO: implement initState
    exam = widget.exam ;
    timerPerMin = widget.exam.timer != null  ? (widget.exam.timer! - 1)  : 9 ;
    WidgetsBinding.instance.addPostFrameCallback((_) async{
    await playTimer();
   });
    super.initState();

  }


  playTimer(){
    timer = Timer.periodic(const Duration(seconds: 1) , (_){
      if(timerPerMin! > 0 || seconds != 0){
        setState(() {
          seconds = seconds -1 ;
          if(seconds == 0 && timerPerMin != 0) {
            setState(() {
              timerPerMin =  timerPerMin! -1;
              seconds = 59 ;
            });
          }
        });
      }else {
        saveExam(exam!);
        setState(() {
          timer.cancel();
        });

      }
    });
  }


  @override
  void dispose() {
    // TODO: implement dispose
    setState(() {
      timer.cancel();
    });


    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    // exam = context.read<DataHandler>().exam ;
    // for(int i = 0 ; i < exam!.questionsList!.length ;i ++){
    //   for (int x = 0 ; x < exam!.questionsList![i].answersdtoList!.length ; x++ ){
    //     setState(() {
    //
    //     });
    //   }
    // }
    return Scaffold(
        backgroundColor: mainColor,
        appBar: AppBar(
          elevation: 0,

          backgroundColor: mainColor,
          title:  Text( widget.exam.examName! ,style: TextStyle(fontSize: 23),),
          centerTitle: true,
          leading: IconButton(
            icon: const Icon(Icons.arrow_back),
            onPressed: () {
              if(  index > 0){
                setState(() {
                  index-- ;
                });
              }else {
                Navigator.pop(context);
              }            },
          ),
        ),
        body:
        Container(  decoration:  BoxDecoration(color:mainColor),
            width: size.width,
            height: size.height,child: Container(decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(40),
                  topRight: Radius.circular(40),
                )),
                child: _buildBody(size)))

    );
  }

  Widget _buildBody(Size size){
    return SizedBox(
        height: size.height,
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisSize: MainAxisSize.min,
            children: [
             Center(
               child: Padding(
                 padding: const EdgeInsets.all(8.0),
                 child: Container(

                   width: size.width * 0.3,
                   height: size.height * 0.07,
                   decoration: BoxDecoration(
                     color: timerPerMin! < 2?Colors.red:mainColor,
                     borderRadius: const BorderRadius.all(Radius.circular(20)),),

                   child: Row(
                     mainAxisAlignment: MainAxisAlignment.center,
                     children: [
                       Text(timerPerMin.toString() , style: const TextStyle(color: Colors.white,fontSize: 20,fontWeight: FontWeight.bold),),
                       Text(":" , style: const TextStyle(color: Colors.white,fontSize: 20,fontWeight: FontWeight.bold),),
                       Text(seconds.toString() , style: const TextStyle(color: Colors.white,fontSize: 20,fontWeight: FontWeight.bold),)
                     ],
                   ),
                 ),
               ),
             ),
              Padding(
                padding: const EdgeInsets.fromLTRB(8,0,0,0),
                child: SizedBox(
                  // height: size.height *0.04,

                    child: _buildBar(size,"Question ${index+1}/${exam!.questionsList!.length.toString()}" ,
                    ),
                  ),
                ),

              Column(
                children: [
                  // for(Question question in exam!.questionsList!)

                    Column(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: [
                              exam!.questionsList![index].questionName != null ?  Row(
                                children: [
                                  // Text( (exam!.questionsList!.indexOf(question) + 1 )
                                  //       .toString() , style: valueStyle,),
                                  // Text("-" , style: valueStyle),
                                  Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: SizedBox(height: size.height *0.04,child: FittedBox(child: Text(exam!.questionsList![index].questionName! , style: TextStyle(fontWeight: FontWeight.bold)))),
                                  ),
                                ],
                              ) : const SizedBox(),
                              exam!.questionsList![index].img != null && exam!.questionsList![index].img != "" ?
                              Image.network(uploadUrl + exam!.questionsList![index].img!) :
                              const SizedBox()
                            ],
                          ),
                        ),
                        for(Answer answer in exam!.questionsList![index].answersdtoList!)
                          Padding(
                            padding: const EdgeInsets.fromLTRB(30,10,30,0),
                            child: Container(
                              decoration:  BoxDecoration(
                                  color: answer.selected! == answerSelection.selected ? mainColor: Colors.grey[300],
                                  borderRadius: BorderRadius.all(
                                 Radius.circular(10),

                                  )),

                              child: Row(
                                children: [
                                  Radio(
                                    activeColor: Colors.white,
                                      value: answerSelection.selected,
                                      groupValue: answer.selected,
                                      onChanged: (answerSelection? value){
                                        for(Answer answer in exam!.questionsList![index].answersdtoList!){
                                          setState(() {
                                            answer.selected = answerSelection.unSelected ;
                                          });
                                        }
                                        setState(() {
                                          answer.selected = value ;
                                          // _selection = value! ;
                                        });
                                      }),
                                  Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      answer.answer != null ?  Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: SizedBox(
                                          height: size.height *0.04,
                                          child: FittedBox(

                                            child: Text(answer.answer! , style:  TextStyle(fontWeight: FontWeight.bold,color: answer.selected! == answerSelection.selected ? Colors.white: mainColor, )
                                      ),
                                          ),
                                        )) : const SizedBox(),
                                      answer.img != null ? Container(height: size.height * 0.2 , width: size.width *0.65 ,child: Image.network(uploadUrl + answer.img! , fit: BoxFit.fill,)) : const SizedBox(),
                                    ],
                                  )
                                ],
                              ),
                            ),
                          ),
                      ],
                    ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  index !=0?  Align(
                    alignment: AlignmentDirectional.bottomCenter,
                    child: regularDarkButton(
                        size,
                        mainColor!,
                        "previous",
                            () => {
                          if( index ==0){
                            null
                          }else {
                            setState(() {
                              index-- ;
                            })
                          }
                        }

                    ),
                  ):SizedBox(),
                  Align(
                    alignment: AlignmentDirectional.bottomCenter,
                    child: regularDarkButton(
                        size,
                        mainColor!,
                        "next",
                            () => {
    if( index < exam!.questionsList!.length - 1){
    setState(() {
    index++ ;
    })
    }else {
    saveExam(exam!).then((value){
    Navigator.push(context, MaterialPageRoute(builder: (context) =>const HomeScreen()));
    })

    }})
                  ),

                ],
              ),

              // Center(
              //   child: Padding(
              //     padding: const EdgeInsets.fromLTRB(0 , 10 , 0 , 30),
              //     child: OutlinedButton(
              //         onPressed: (){
              //          if( index < exam!.questionsList!.length - 1){
              //            setState(() {
              //              index++ ;
              //            });
              //          }else {
              //            saveExam(exam!).then((value){
              //              Navigator.push(context, MaterialPageRoute(builder: (context) =>const  HomeScreen()));
              //            }) ;
              //
              //          }
              //         },
              //         style: ButtonStyle(
              //           backgroundColor: buttonColor,
              //         ),
              //         child: index < exam!.questionsList!.length  -1  ?   Text("next" ,   style: TextStyle(color: yellow),) :  Text("done"  , style: TextStyle(color: yellow),) ),
              //   ),
              // ),
            ],
          ),
        )
    );
  }

 Future saveExam(Exam exam) async {
    for(int i = 0 ; i < exam.questionsList!.length ; i++){
      setState(() {
        exam.questionsList![i].parent = exam.questionsList![i].id ;
        exam.questionsList![i].id = null ;
        exam.solutionTime = widget.exam.timer!= null ? (widget.exam.timer! - timerPerMin!) : 0;
      });
      for(Answer answer in  exam.questionsList![i].answersdtoList!){
        if(answer.selected == answerSelection.selected){
          setState(() {
            answer.selection = 1 ;
            answer.parent = answer.id;
            answer.id = null ;
          });
        } else {
          setState(() {
            answer.selection = 0 ;
            answer.parent = answer.id;
            answer.id = null ;
          });
        }
      }
    }
    context.read<DataHandler>().saveExam(exam, context);
    setState(() {
      timer.cancel();
    });
  }
  _buildBar(Size size,  String title) {
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

}
