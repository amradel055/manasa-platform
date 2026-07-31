import 'package:desktop_screen/constants/colors.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../constants/text_styles.dart';



Widget finishExamCardWidget (String title,String subTitle, Size size ){
  return  Container(
    child: Column(
      children: [


        Padding(
          padding:  EdgeInsets.fromLTRB(size.width*.05, size.height*.01, size.width*.05, size.height*.005),
          child: Container(
            height: size.height*.2,
            width: size.width*.9,
            decoration:  BoxDecoration(borderRadius: BorderRadius.all(Radius.circular(15)),
              color: greyColor,
            ),


            child:Padding(
              padding: const EdgeInsets.fromLTRB(10, 10, 0, 10),
              child: Row(
                  children: [
                    Container(
                      height:  size.height * 0.18,
                      width: size.width * 0.1,
                      decoration: BoxDecoration(
                          borderRadius: const BorderRadius.all(Radius.circular(15)),
                          image: DecorationImage(
                              fit: BoxFit.cover,
                              image: NetworkImage(
                                  "https://previews.123rf.com/images/serezniy/serezniy1811/serezniy181132859/112616083-word-exam-made-of-letters-and-stationery-on-wooden-background.jpg")
                          )
                      ),

                    ),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(5, 0, 0, 0),
                      child: SizedBox(width: size.width*.27,
                        height: size.height*.1,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children:   [
                            SizedBox(
                              width: size.width,
                              height: size.height*.08,
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: [
                                  Column(
                                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                    children: [
                                      Text(title, style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.black,fontSize: 20 )),
                                      Text(subTitle, style:TextStyle(fontWeight: FontWeight.w400 , color: Colors.grey )),
                                    ],
                                  ),Container(
                                    height:size.height*.05,
                                    width: size.width*.1,
                                    decoration: BoxDecoration(
                                        color: Colors.green,
                                        borderRadius: BorderRadius.circular(size.height*.04)
                                      //more than 50% of width makes circle
                                    ),child:   Icon(Icons.check,color: Colors.white,),
                                  )
                                ,

                                ],
                              ),
                            ),
                            //

                          ],
                        ),
                      ),
                    ),
                  ]
              ),
            ),
          ),
        ),
      ],
    ),
  );
}