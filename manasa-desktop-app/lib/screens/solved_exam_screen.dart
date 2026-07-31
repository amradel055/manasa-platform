
import 'package:desktop_screen/widgets/regular_dark_button.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import '../constants/colors.dart';
import '../constants/strings.dart';
import '../constants/text_styles.dart';
import '../models/exam_model.dart';




class SolvedExam extends StatefulWidget {
  const SolvedExam(this.exam , {Key? key}) : super(key: key);
  final Exam exam ;
  @override
  _SolvedExamState createState() => _SolvedExamState();
}

class _SolvedExamState extends State<SolvedExam> {
  Exam? exam ;
  int index = 0 ;
  @override
  void initState() {
    // TODO: implement initState
    exam = widget.exam ;
    super.initState();

  }





  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size ;
    return Scaffold(
        appBar: AppBar(
          elevation: 0,

          backgroundColor: mainColor,
          title:  Text(widget.exam.examName!,style: TextStyle(fontSize: 23),),
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
        Container(
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
                child: _buildBody(size)))

    );
  }

  Widget _buildBody(Size size){
    return SizedBox(
        height: size.height,
        child: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          child: Container(
            // height: size.height,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: SizedBox(
                    // height: size.height *0.04,
                    child: FittedBox(
                      child: _buildBar(size,"Question ${index+1}/${exam!.questionsList!.length.toString()}" ,

                      ),
                    ),
                  ),
                ),
                Column(
                  children: [
                    Column(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              exam!.questionsList![index].questionName != null ?  Row(
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: SizedBox(height: size.height *0.04,child: FittedBox(child: Text(exam!.questionsList![index].questionName! , style: TextStyle(fontWeight: FontWeight.bold , fontSize: 20)))),
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
                            padding: const EdgeInsets.fromLTRB(30,8,30,8),
                            child: Container(
                              decoration:  BoxDecoration(
                                  color: answer.selection! == 1 && answer.correct == 1 ? Colors.grey[400] : answer.selection! == 1 && answer.correct != 1 ? Colors.red : Colors.grey[400],
                                  borderRadius: BorderRadius.all(
                                    Radius.circular(10),

                                  )),
                              // color: answer.selection! == 1 && answer.correct == 1 ? Colors.green[100] : answer.selection! == 1 && answer.correct != 1 ? Colors.red : Colors.white70,
                              child: Row(
                                children: [
                                  Radio(
                                    activeColor: Colors.white,
                                      value: answer.selection!,
                                      groupValue: 1 ,
                                      onChanged: (int? selection){
                                        null ;
                                      }),
                                  Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      answer.answer != null ?  Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: SizedBox(
                                            height: size.height *0.04,
                                            child: FittedBox(
                                              child: Text(answer.answer! , style: const TextStyle(fontWeight: FontWeight.bold , fontSize: 20)
                                              ),
                                            ),
                                          )) : const SizedBox(),
                                      answer.img != null ? Container(height: size.height * 0.2 , width: size.width *0.7 ,child: Image.network(uploadUrl + answer.img! , fit: BoxFit.fill,)) : const SizedBox(),
                                      answer.reason != null ?  Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: Text(answer.reason! , style: const TextStyle(fontWeight: FontWeight.bold , fontSize: 20)
                                          )) : const SizedBox(),
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
                        Navigator.pop(context)
                      }
                    }

                ),
              ),

            ],
          ),

          // Center(
          //         child: Padding(
          //           padding: const EdgeInsets.fromLTRB(0 , 10 , 0 , 30),
          //           child: (
          //               onPressed: (){
          //                 if( index < exam!.questionsList!.length - 1){
          //                   setState(() {
          //                     index++ ;
          //                   });
          //                 }else {
          //                   Navigator.pop(context);
          //                 }
          //               },
          //               style: ButtonStyle(
          //                 backgroundColor: buttonColor,
          //               ),
          //               child: index < exam!.questionsList!.length  -1  ?   Text("next"  , style: TextStyle(color: yellow),) :  Text("done"  , style: TextStyle(color: yellow),) ),
          //         ),
  // ),
              ],
            ),
          ),
        )
    );
  }

  _buildBar(Size size,  String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8,8,8,0),
      child: Container(
        width: size.width * 0.35,
        height: size.height * 0.06,
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

}
