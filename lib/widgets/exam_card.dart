import 'package:desktop_screen/constants/colors.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../constants/text_styles.dart';



Widget examCardWidget (String title,String subTitle,String points,String time,String material, Size size ,String image){
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
              padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
              child: Row(
                  children: [
                    Container(
                      height:  size.height * 0.2,
                      width: size.width * 0.1,
                      decoration: BoxDecoration(
                          borderRadius: const BorderRadius.all(Radius.circular(5)),
                          image: DecorationImage(
                              fit: BoxFit.fill,
                              image: NetworkImage(
                                  "https://static.wixstatic.com/media/0df54e_73d623d9312c4f5daade2ae91998dd23~mv2.png/v1/fill/w_450,h_450,al_c/0df54e_73d623d9312c4f5daade2ae91998dd23~mv2.png")
                          )
                      ),

                    ),
                    Padding(
                      padding:  EdgeInsets.fromLTRB(size.width*.03,5,0,5),
                      child: SizedBox(width: size.width*.27,
                        child: Container(
                          height:  size.height * 0.17,
                          width: size.width * 0.66,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.only(topLeft: Radius.circular(13.0),bottomLeft: Radius.circular(13.0)),
                          color: mainColor),
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(15, 5, 0, 0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children:   [
                                Text(material,style: TextStyle(fontWeight: FontWeight.w500 , color: Colors.white,fontSize: 25 ),),
                                Text(title, style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.white )),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: [
                                    Text(subTitle, style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.white )),
                                    Text("/${points}points", style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.yellow )),
                                  ],
                                ),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: [
                                    Icon(Icons.timer,color: Colors.white,),
                                    Text(time +"Mintues", style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.white )),
                                  ],
                                ),

                                //

                              ],
                            ),
                          ),
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