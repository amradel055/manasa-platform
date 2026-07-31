import 'package:flutter/material.dart';
import 'package:full_mark/constants/colors.dart';
import 'package:intl/intl.dart';
import '../constants/text_styles.dart';



Widget cardWidget (String title,String subTitle,DateTime date, Size size ,String image){
  return  Container(
    child: Column(
      children: [


        Padding(
          padding:  EdgeInsets.fromLTRB(size.width*.05, size.height*.01, size.width*.05, size.height*.005),
          child: Container(
            height: size.height*.15,
            width: size.width*.9,
            decoration:  BoxDecoration(borderRadius: BorderRadius.all(Radius.circular(15)),
              color: greyColor,
            ),


            child:Padding(
              padding: const EdgeInsets.fromLTRB(10, 10, 0, 10),
              child: Row(
                  children: [
                    Container(
                      height:  size.height * 0.12,
                      width: size.width * 0.28,
                      decoration: BoxDecoration(
                          borderRadius: const BorderRadius.all(Radius.circular(15)),
                          image: DecorationImage(
                              fit: BoxFit.cover,
                              image: NetworkImage(
                                  image)
                          )
                      ),

                    ),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(5,0,0,0),
                      child: SizedBox(width: size.width*.55,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children:   [
                            Text(title,style: TextStyle(fontWeight: FontWeight.w500 , color: Colors.black,fontSize: 20 ),),
                            Text(subTitle, style:TextStyle(fontWeight: FontWeight.w500 , color: Colors.grey )),
                            SizedBox(
                              width: size.width,
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.end,
                                children: [

                                  Text(    DateFormat('yMd').format(date),style: TextStyle(fontWeight: FontWeight.w400 , color: Colors.black,fontSize: 15 ),),
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